package com.chipcollector.data;

import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipCollectionTest {

    public static final String TEST_CASINO_CLOSE_DATE = "10/10/2010";
    public static final String TEST_CASINO_OPEN_DATE = "11/11/2011";
    public static final String TEST_ID = "id";
    public static final int TEST_CASINO_ID = 23;
    public static final Location TEST_CASINO_LOCATION = Location.builder().city("city").build();
    public static final String TEST_CASINO_NAME = "name";
    public static final String TEST_CASINO_OLD_NAME = "oldName";
    public static final String TEST_CASINO_STATUS = "status";
    public static final String TEST_CASINO_TYPE = "type";
    public static final String TEST_CASINO_WEBSITE = "website";
    @Mock
    private PokerChipDAO pokerChipDAO;

    private PokerChipCollection underTest;

    @Before
    public void setUp() {
        underTest = new PokerChipCollection(pokerChipDAO);
    }

    @Test
    public void whenAddPokerChipThenWillPersistIt() {
        PokerChip pokerChipTestInstance = getPokerChipTestInstance();
        underTest.add(pokerChipTestInstance);
        verify(pokerChipDAO).savePokerChip(pokerChipTestInstance);
    }

    @Test
    public void whenListenerIsRegisteredAndPokerChipIsAddedThenListenerIsNotified() {
        PokerChip pokerChipTestInstance = getPokerChipTestInstance();
        Listener listener = mock(Listener.class);
        underTest.addUpdateListener(listener);
        underTest.add(pokerChipTestInstance);
        verify(listener).action();
    }

    @Test
    public void whenListenerIsRegisteredAndPokerChipIsUpdatedThenListenerIsNotified() {
        PokerChip pokerChipTestInstance = getPokerChipTestInstance();
        Listener listener = mock(Listener.class);
        underTest.addUpdateListener(listener);
        underTest.update(pokerChipTestInstance);
        verify(listener).action();
    }

    @Test
    public void getAllCasinosThenRetrievesCasinoListFromDatabase() {
        List<Casino> expected = ImmutableList.of(Casino.builder().build());
        when(pokerChipDAO.getAllCasinos()).thenReturn(expected);
        assertThat(underTest.getAllCasinos()).isEqualTo(expected);
    }

    @Test
    public void getgetAllPokerChipsCountRetrievesValueFromDatabase() {
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
    public void getColorAutocompleteValuesRetrieveListFromDatabase()  {
        List<String> expected = ImmutableList.of("blue", "yellow");
        when(pokerChipDAO.getDistinctValueSet(eq("color"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getColorAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getDenomAutocompleteValuesRetrieveListFromDatabase()  {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("denom"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getDenomAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getInlayAutocompleteValuesRetrieveListFromDatabase()  {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("inlay"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getInlayAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    @Test
    public void getMoldAutocompleteValuesRetrieveListFromDatabase()  {
        List<String> expected = ImmutableList.of("one", "two");
        when(pokerChipDAO.getDistinctValueSet(eq("mold"), any()))
                .thenReturn(expected);
        List<String> resultValues = underTest.getMoldAutocompleteValues();
        assertThat(resultValues).isEqualTo(expected);
    }

    public PokerChip getPokerChipTestInstance() {
        return PokerChip.builder().build();
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

}