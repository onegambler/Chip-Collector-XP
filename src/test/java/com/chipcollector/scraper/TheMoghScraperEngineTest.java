package com.chipcollector.scraper;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.scraper.themogh.TheMoghCasinoScraper;
import com.chipcollector.scraper.themogh.TheMoghPokerChipScraper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TheMoghScraperEngineTest {

    @Mock
    private TheMoghCasinoScraper casinoScraper;
    @Mock
    private TheMoghPokerChipScraper pokerChipScraper;

    private TheMoghScraperEngine underTest;

    @Before
    public void setUp() {
        underTest = new TheMoghScraperEngine(casinoScraper, pokerChipScraper);
    }

    @Test
    public void searchCasinoWorksCorrectly() throws IOException {
        underTest.searchCasinos("casino");
        verify(casinoScraper).searchItems("casino");
    }

    @Test
    public void whenCasinoNameIsNullThenSearchCasinoThrowsException() throws IOException {
        assertThatThrownBy(() -> underTest.searchCasinos(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Casino name cannot be null");
    }

    @Test
    public void searchPokerChipWorksCorrectly() throws IOException {
        final CasinoBean casino = new CasinoBean();
        underTest.searchPokerChip(casino);
        verify(pokerChipScraper).searchItems(casino);
    }

    @Test
    public void whenCasinoIsNullThenSearchPokerChipThrowsException() throws IOException {
        assertThatThrownBy(() -> underTest.searchPokerChip(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Casino bean cannot be null");
    }

    @Test
    public void toStringReturnsCorrectValue() {
        assertThat(underTest.toString()).isEqualTo("The Mogh");
    }
}