package com.chipcollector.data;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestCasino;
import static com.chipcollector.test.util.PokerChipTestUtil.createTestPokerChip;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipCollectionTest {

    @Mock
    private PokerChipDAO pokerChipDAO;

    @Mock
    private PokerChipHandler pokerChipCreator;

    @Mock
    private Query<PokerChip> filter;

    private PokerChipCollection underTest;

    @Before
    public void setUp() {
        when(pokerChipDAO.createPokerChipFilter()).thenReturn(filter);
        underTest = new PokerChipCollection(pokerChipDAO, pokerChipCreator);
    }

    @Test
    public void whenAddPokerChipThenWillPersistIt() throws IOException {
        final PokerChip testPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBeanTestInstance = new PokerChipBean();
        when(pokerChipCreator.getNewPokerChip(pokerChipBeanTestInstance)).thenReturn(testPokerChip);
        underTest.add(pokerChipBeanTestInstance);
        verify(pokerChipDAO).savePokerChip(testPokerChip);
    }

    @Test
    public void whenListenerIsRegisteredAndPokerChipIsAddedThenListenerIsNotified() throws IOException {
        PokerChipBean pokerChipTestInstance = new PokerChipBean();
        Listener listener = mock(Listener.class);
        underTest.addUpdateListener(listener);
        underTest.add(pokerChipTestInstance);
        verify(listener).action();
    }

    @Test
    public void whenListenerIsRegisteredAndPokerChipIsUpdatedThenListenerIsNotified() {
        PokerChip pokerChipTestInstance = createTestPokerChip();
        Listener listener = mock(Listener.class);
        underTest.addUpdateListener(listener);
        underTest.update(new PokerChipBean(pokerChipTestInstance));
        verify(listener).action();
    }

    @Test
    public void getAllCasinosThenRetrievesCasinoListFromDatabase() {
        List<Casino> expected = ImmutableList.of(Casino.builder().build());
        when(pokerChipDAO.getAllCasinos()).thenReturn(expected);
        assertThat(underTest.getAllCasinos()).isEqualTo(expected);
    }

    @Test
    public void getAllPokerChipsCountRetrievesValueFromDatabase() {
        when(pokerChipDAO.getAllPokerChipsCount()).thenReturn(5);
        assertThat(underTest.getAllPokerChipsCount()).isEqualTo(5);
    }

    @Test
    public void getColorAutocompleteValuesRetrieveListFromDatabase() {
        List<String> expected = ImmutableList.of("blue", "yellow");
        when(pokerChipDAO.getDistinctValueSet(eq("color"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getColorAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getDenomAutocompleteValuesRetrieveListFromDatabase() {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("denom"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getDenomAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getInlayAutocompleteValuesRetrieveListFromDatabase() {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("inlay"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getInlayAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getMoldAutocompleteValuesRetrieveListFromDatabase() {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("mold"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getMoldAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }


    @Test
    public void getAllCasinosCount() {
        when(pokerChipDAO.getAllCasinos()).thenReturn(ImmutableList.of(createTestCasino()));
        assertThat(underTest.getAllCasinosCount()).isEqualTo(1);
    }

    @Test
    public void getPagedPokerChipsCallsCorrectlyTheDatabase() {
        int pageIndex = 1;
        int pageSize = 2;
        underTest.getPagedPokerChips(pageIndex, pageSize);
        verify(pokerChipDAO).getPagedPokerChips(filter, pageIndex, pageSize);
    }

    @Test
    public void getPokerChipsListCallsCorrectlyTheDatabase() {
        underTest.getPokerChipList();
        verify(pokerChipDAO).getPokerChipList(filter);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getPokerChipCountForLast7DaysCallsCorrectlyTheDatabase() {
        ExpressionList<PokerChip> expressionList = mock(ExpressionList.class);
        when(filter.where()).thenReturn(expressionList);
        when(expressionList.gt(anyString(), any())).thenReturn(expressionList);
        underTest.getPokerChipCountForLast7Days();
        verify(pokerChipDAO).getPokerChipCount(any());
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(expressionList).gt(eq("acquisitionDate"), captor.capture());
        assertThat(captor.getValue()).isEqualToIgnoringHours(now().minusDays(7));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getPokerChipCountForLastMonthCallsCorrectlyTheDatabase() {
        ExpressionList<PokerChip> expressionList = mock(ExpressionList.class);
        when(filter.where()).thenReturn(expressionList);
        when(expressionList.gt(anyString(), any())).thenReturn(expressionList);
        underTest.getPokerChipCountForLastMonth();
        verify(pokerChipDAO).getPokerChipCount(any());
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(expressionList).gt(eq("acquisitionDate"), captor.capture());
        assertThat(captor.getValue()).isEqualToIgnoringHours(now().minusMonths(1));
    }

    @Test
    public void getFilteredPokerChipCountCallsCorrectlyTheDatabase() {
        final int expectedCount = 5;
        when(pokerChipDAO.getPokerChipCount(filter)).thenReturn(expectedCount);
        int result = underTest.getFilteredPokerChipCount();
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setCasinoFilterCorrectlySetFilter() {
        final Casino casinoTestInstance = createTestCasino();
        ExpressionList expressionList = mock(ExpressionList.class);
        when(filter.where()).thenReturn(expressionList);
        when(expressionList.eq(anyString(), any())).thenReturn(expressionList);
        underTest.setCasinoFilter(casinoTestInstance);
        verify(pokerChipDAO, times(2)).createPokerChipFilter();
        verify(expressionList).eq("casino.id", casinoTestInstance.getId());
        verify(expressionList).query();
        verifyNoMoreInteractions(expressionList);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void resetCasinoFilterCorrectlyResetFilter() {
        underTest.setCurrentFilter(filter);
        when(pokerChipDAO.createPokerChipFilter()).thenReturn(mock(Query.class));
        underTest.resetCasinoFilter();
        verify(pokerChipDAO, times(2)).createPokerChipFilter();
        assertThat(filter).isNotEqualTo(underTest.getCurrentFilter());
    }

    @Test
    public void getAllCountriesWorksCorrectly() {
        underTest.getAllCountries();
        verify(pokerChipDAO).getAllCountries();
    }

    @Test
    public void deleteWorksCorrectly() {
        PokerChip pokerChip = createTestPokerChip();
        underTest.deletePokerChip(pokerChip);
        verify(pokerChipDAO).deletePokerChip(pokerChip);
    }

}