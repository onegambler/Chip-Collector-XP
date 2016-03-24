package com.chipcollector.data;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Location;
import com.chipcollector.model.dashboard.CasinoBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestCasino;
import static com.chipcollector.test.util.PokerChipTestUtil.createTestLocation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CasinoHandlerTest {

    @Mock
    private PokerChipDAO pokerChipDAO;

    private CasinoHandler underTest;

    @Before
    public void setUp() {
        underTest = new CasinoHandler(pokerChipDAO);
    }

    @Test
    public void whenAnExistingCasinoIsInDatabaseThenGetCasinoReturnThatCasino() {
        final Casino expectedCasino = createTestCasino();
        PokerChipDAO.CasinoFinder finder = mock(PokerChipDAO.CasinoFinder.class);
        when(pokerChipDAO.getCasinoFinder()).thenReturn(finder);
        when(finder.withCity(anyString())).thenReturn(finder);
        when(finder.withCountry(anyString())).thenReturn(finder);
        when(finder.withName(expectedCasino.getName())).thenReturn(finder);
        when(finder.withState(anyString())).thenReturn(finder);
        when(finder.findSingle()).thenReturn(Optional.of(expectedCasino));
        CasinoBean casinoBean = new CasinoBean(expectedCasino);

        final Casino casino = underTest.getCasino(casinoBean);
        assertThat(casino).isEqualTo(expectedCasino);
    }

    @Test
    public void whenCasinoDoesNotExistInDatabaseThenGetCasinoReturnsNewCasinoWithExistingLocation() {
        final Casino expectedCasino = createTestCasino();
        PokerChipDAO.CasinoFinder casinoFinder = mock(PokerChipDAO.CasinoFinder.class);
        when(pokerChipDAO.getCasinoFinder()).thenReturn(casinoFinder);
        when(casinoFinder.withCity(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.withCountry(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.withName(expectedCasino.getName())).thenReturn(casinoFinder);
        when(casinoFinder.withState(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.findSingle()).thenReturn(Optional.empty());

        PokerChipDAO.LocationFinder locationFinder = mock(PokerChipDAO.LocationFinder.class);
        when(pokerChipDAO.getLocationFinder()).thenReturn(locationFinder);
        when(locationFinder.withCity(anyString())).thenReturn(locationFinder);
        when(locationFinder.withCountry(anyString())).thenReturn(locationFinder);
        when(locationFinder.withState(anyString())).thenReturn(locationFinder);
        when(locationFinder.findSingle()).thenReturn(expectedCasino.getLocation());

        CasinoBean casinoBean = new CasinoBean(expectedCasino);

        final Casino casino = underTest.getCasino(casinoBean);
        assertThat(casino).isNotSameAs(expectedCasino);
    }

    @Test
    public void whenCasinoDoesNotExistInDatabaseThenGetCasinoReturnsNewCasinoWithNewLocation() {
        final Casino expectedCasino = createTestCasino();
        PokerChipDAO.CasinoFinder casinoFinder = mock(PokerChipDAO.CasinoFinder.class);
        when(pokerChipDAO.getCasinoFinder()).thenReturn(casinoFinder);
        when(casinoFinder.withCity(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.withCountry(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.withName(expectedCasino.getName())).thenReturn(casinoFinder);
        when(casinoFinder.withState(anyString())).thenReturn(casinoFinder);
        when(casinoFinder.findSingle()).thenReturn(Optional.empty());

        PokerChipDAO.LocationFinder locationFinder = mock(PokerChipDAO.LocationFinder.class);
        when(pokerChipDAO.getLocationFinder()).thenReturn(locationFinder);
        when(locationFinder.withCity(anyString())).thenReturn(locationFinder);
        when(locationFinder.withCountry(anyString())).thenReturn(locationFinder);
        when(locationFinder.withState(anyString())).thenReturn(locationFinder);
        when(locationFinder.findSingle()).thenReturn(Optional.empty());

        when(pokerChipDAO.getCountry(any())).thenReturn(Optional.of(expectedCasino.getCountry()));
        CasinoBean casinoBean = new CasinoBean(expectedCasino);

        final Casino casino = underTest.getCasino(casinoBean);
        assertThat(casino).isNotSameAs(expectedCasino);
    }

    @Test
    public void whenCasinoBeanIsNullThenGetCasinoReturnsNull() {
        assertThat(underTest.getCasino(null)).isNull();
    }

    @Test
    public void whenCasinoBeanNameIsNullThenGetCasinoReturnsNull() {
        CasinoBean casinoBean = new CasinoBean();
        assertThat(underTest.getCasino(casinoBean)).isNull();
    }

    @Test
    public void whenGetCasinoFromCasinoBeanIsCalledThenRetrieveCasinoFromDatabase() {
        Casino testCasino = createTestCasino();
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
    public void getCountryFromNameCallsDatabase() {
        String countryName = "Italy";
        underTest.getCountryFromName(countryName);
        verify(pokerChipDAO).getCountry(countryName);
    }

    @Test
    public void getCountryFromCasinoBeanCallsCorrectlyTheDatabase() {
        final String countryName = "countryName";
        CasinoBean casinoBean = new CasinoBean();
        casinoBean.setCountryName(countryName);
        underTest.getCountryFromCasinoBean(casinoBean);
        verify(pokerChipDAO).getCountry(countryName);
    }

    @Test
    public void getCountryFromNameCallsCorrectlyTheDatabase() {
        final String countryName = "countryName";
        underTest.getCountryFromName(countryName);
        verify(pokerChipDAO).getCountry(countryName);
    }

    @Test
    public void getLocationFromCasinoBeanCallsCorrectlyTheDatabase() {
        Casino testCasino = createTestCasino();
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
    public void updateCasinoWorksCorrectly() {
        final Casino testCasino = createTestCasino();
        final Location testLocation = createTestLocation();

        PokerChipDAO.LocationFinder finder = mock(PokerChipDAO.LocationFinder.class);
        when(pokerChipDAO.getLocationFinder()).thenReturn(finder);
        when(finder.withCity(anyString())).thenReturn(finder);
        when(finder.withCountry(anyString())).thenReturn(finder);
        when(finder.withState(anyString())).thenReturn(finder);
        when(finder.findSingle()).thenReturn(Optional.of(testLocation));

        final Casino expectedCasino = createTestCasino();
        expectedCasino.setCloseDate("new CloseDate");
        expectedCasino.setLocation(testLocation);
        expectedCasino.setName("new Name");
        expectedCasino.setOpenDate("new OpenDate");
        expectedCasino.setStatus("new Status");
        expectedCasino.setTheme("new Theme");
        expectedCasino.setType("new Type");
        expectedCasino.setWebsite("new Website");

        CasinoBean casinoBean = new CasinoBean(testCasino);
        casinoBean.setCity(expectedCasino.getCity());
        casinoBean.setClosedDate(expectedCasino.getCloseDate());
        casinoBean.setCountry(expectedCasino.getCountry());
        casinoBean.setName(expectedCasino.getName());
        casinoBean.setOpenDate(expectedCasino.getOpenDate());
        casinoBean.setState(expectedCasino.getState());
        casinoBean.setStatus(expectedCasino.getStatus());
        casinoBean.setTheme(expectedCasino.getTheme());
        casinoBean.setType(expectedCasino.getType());
        casinoBean.setWebsite(expectedCasino.getWebsite());

        final Casino casino = underTest.updateCasino(casinoBean);
        assertThat(casino).isEqualTo(expectedCasino);
    }
}