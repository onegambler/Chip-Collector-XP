package com.chipcollector.data.configuration;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.chipcollector.domain.Property;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EbeanConfigurationTest {

    private static final String TEST_KEY = "key";
    private static final String TEST_VALUE = "value";

    @Mock
    private EbeanServer ebeanServer;
    @Mock
    private Query<Property> propertyQuery;
    @Mock
    private ExpressionList<Property> expressionList;

    private EbeanConfiguration ebeanConfiguration;

    @Before
    public void setUp() {
        ebeanConfiguration = new EbeanConfiguration(ebeanServer);
        when(ebeanServer.find(Property.class)).thenReturn(propertyQuery);
        when(ebeanServer.createQuery(Property.class)).thenReturn(propertyQuery);
        when(expressionList.setUseQueryCache(true)).thenReturn(propertyQuery);
        when(propertyQuery.setUseQueryCache(true)).thenReturn(propertyQuery);
        when(propertyQuery.where()).thenReturn(expressionList);

    }

    @Test
    public void whenPropertyExistsAddPropertyDirectUpdateExistingValue() {
        when(expressionList.eq("key", TEST_KEY)).thenReturn(expressionList);
        final Property expectedProperty = new Property(TEST_KEY);
        when(propertyQuery.findUnique()).thenReturn(expectedProperty);

        ebeanConfiguration.addPropertyDirect(TEST_KEY, TEST_VALUE);

        assertThat(expectedProperty.getValue()).isEqualTo(TEST_VALUE);
        verify(ebeanServer).save(expectedProperty);
    }

    @Test
    public void whenPropertyIsNewAddPropertyDirectCreatesNewProperty() {
        when(expressionList.eq("key", TEST_KEY)).thenReturn(expressionList);

        ebeanConfiguration.addPropertyDirect(TEST_KEY, TEST_VALUE);

        ArgumentCaptor<Property> propertyArgumentCaptor = ArgumentCaptor.forClass(Property.class);
        verify(ebeanServer).save(propertyArgumentCaptor.capture());
        assertThat(propertyArgumentCaptor.getValue().getValue()).isEqualTo(TEST_VALUE);
        assertThat(propertyArgumentCaptor.getValue().getKey()).isEqualTo(TEST_KEY);
    }

    @Test
    public void whenThereAreNoPropertiesThenIsEmptyReturnsTrue() {
        when(propertyQuery.findRowCount()).thenReturn(0);
        assertThat(ebeanConfiguration.isEmpty()).isTrue();
    }

    @Test
    public void whenThereArePropertiesThenIsEmptyReturnsFalse() {
        when(propertyQuery.findRowCount()).thenReturn(1);
        assertThat(ebeanConfiguration.isEmpty()).isFalse();
    }

    @Test
    public void whenThePropertyIsPresentThenContainsKeyReturnsTrue() {
        when(expressionList.eq("key", TEST_KEY)).thenReturn(expressionList);
        when(propertyQuery.findRowCount()).thenReturn(1);
        assertThat(ebeanConfiguration.containsKey(TEST_KEY)).isTrue();
    }

    @Test
    public void whenThePropertyIsNotPresentThenContainsKeyReturnsFalse() {
        when(expressionList.eq("key", TEST_KEY)).thenReturn(expressionList);
        when(propertyQuery.findRowCount()).thenReturn(0);
        assertThat(ebeanConfiguration.containsKey(TEST_KEY)).isFalse();
    }

    @Test
    public void whenGetPropertyExistsReturnIt() {
        when(expressionList.eq("key", TEST_KEY)).thenReturn(expressionList);
        final Property expectedProperty = new Property(TEST_KEY);
        expectedProperty.setValue(TEST_VALUE);
        when(propertyQuery.findUnique()).thenReturn(expectedProperty);

        final Object property = ebeanConfiguration.getProperty(TEST_KEY);

        assertThat(property).isEqualTo(TEST_VALUE);
    }

    @Test
    public void getKeysWorksAsExpected() {
        Property p = new Property(TEST_KEY);
        when(propertyQuery.findList()).thenReturn(ImmutableList.of(p));
        assertThat(ebeanConfiguration.getKeys()).containsOnly(TEST_KEY);
    }
}