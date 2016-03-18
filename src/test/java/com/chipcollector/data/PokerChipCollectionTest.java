package com.chipcollector.data;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestPokerChip;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipCollectionTest {

    @Mock
    private PokerChipDAO pokerChipDAO;

    @Mock
    private Query<PokerChip> filter;

    private PokerChipCollection underTest;

    @Before
    public void setUp() {
        when(pokerChipDAO.createPokerChipFilter()).thenReturn(filter);
        underTest = new PokerChipCollection(pokerChipDAO);
    }

    @Test
    public void whenAddPokerChipThenWillPersistIt() {
        PokerChip pokerChipTestInstance = createTestPokerChip();
        underTest.add(pokerChipTestInstance);
        verify(pokerChipDAO).savePokerChip(pokerChipTestInstance);
    }

    @Test
    public void whenListenerIsRegisteredAndPokerChipIsAddedThenListenerIsNotified() {
        PokerChip pokerChipTestInstance = createTestPokerChip();
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
    public void whenGetAllCasinosThenRetrieveCasinoListFromDatabase() {
        Casino testCasino = getCasinoTestInstance();
        CasinoBean casinoBean = new CasinoBean(testCasino);
        PokerChipDAO.CasinoFinder finder = mock(PokerChipDAO.CasinoFinder.class);
        when(pokerChipDAO.getCasinoFinder()).thenReturn(finder);
        when(finder.withCity(anyString())).thenReturn(finder);
        when(finder.withCountry(anyString())).thenReturn(finder);
        when(finder.withName(anyString())).thenReturn(finder);
        when(finder.withState(anyString())).thenReturn(finder);

        underTest.getCasinoFromCasinoBean(casinoBean);
        verify(pokerChipDAO).getCasinoFinder();
        verify(finder).withName(testCasino.getName());
        verify(finder).withCity(testCasino.getCity());
        verify(finder).withState(testCasino.getState());
        verify(finder).withCountry(testCasino.getCountryName());
        verify(finder).findSingle();
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
    public void getCountryFromNameCallsDatabase() {
        String countryName = "Italy";
        underTest.getCountryFromName(countryName);
        verify(pokerChipDAO).getCountry(countryName);
    }

    @Test
    public void getAllCasinosCount() {
        when(pokerChipDAO.getAllCasinos()).thenReturn(ImmutableList.of(getCasinoTestInstance()));
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
        final Casino casinoTestInstance = getCasinoTestInstance();
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
    public void getLocationFromCasinoBeanCallsCorrectlyTheDatabase() {
        Casino testCasino = getCasinoTestInstance();
        CasinoBean casinoBean = new CasinoBean(testCasino);
        PokerChipDAO.LocationFinder finder = mock(PokerChipDAO.LocationFinder.class);
        when(pokerChipDAO.getLocationFinder()).thenReturn(finder);
        when(finder.withCity(anyString())).thenReturn(finder);
        when(finder.withCountry(anyString())).thenReturn(finder);
        when(finder.withState(anyString())).thenReturn(finder);

        underTest.getLocationFromCasinoBean(casinoBean);

        verify(pokerChipDAO).getLocationFinder();
        verify(finder).withCity(testCasino.getCity());
        verify(finder).withState(testCasino.getState());
        verify(finder).withCountry(testCasino.getCountryName());
        verify(finder).findSingle();
    }

    @Test
    public void getCountryFromCasinoBeanCallsCorrectlyTheDatabase() {
        final String countryName = "countryName";
        CasinoBean casinoBean = new CasinoBean();
        casinoBean.setCountry(countryName);
        underTest.getCountryFromCasinoBean(casinoBean);
        verify(pokerChipDAO).getCountry(countryName);
    }

    @Test
    public void getCountryFromNameCallsCorrectlyTheDatabase() {
        final String countryName = "countryName";
        underTest.getCountryFromName(countryName);
        verify(pokerChipDAO).getCountry(countryName);
    }

    public Casino getCasinoTestInstance() {
        return Casino.builder()
                .closeDate(TEST_CASINO_CLOSE_DATE)
                .openDate(TEST_CASINO_OPEN_DATE)
                .id(TEST_CASINO_ID)
                .location(TEST_CASINO_LOCATION)
                .name(TEST_CASINO_NAME)
                .oldName(TEST_CASINO_OLD_NAME)
                .status(TEST_CASINO_STATUS)
                .type(TEST_CASINO_TYPE)
                .website(TEST_CASINO_WEBSITE)
                .build();
    }

    public static final String TEST_CASINO_CLOSE_DATE = "10/10/2010";
    public static final String TEST_CASINO_OPEN_DATE = "11/11/2011";
    public static final int TEST_CASINO_ID = 23;
    public static final Location TEST_CASINO_LOCATION = Location.builder().city("city").build();
    public static final String TEST_CASINO_NAME = "name";
    public static final String TEST_CASINO_OLD_NAME = "oldName";
    public static final String TEST_CASINO_STATUS = "status";
    public static final String TEST_CASINO_TYPE = "type";
    public static final String TEST_CASINO_WEBSITE = "website";
}