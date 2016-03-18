package com.chipcollector.scraper.themogh;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TheMoghCasinoScraperIT {

    public static final String TEST_CASINO_NAME = "bellagio";
    private TheMoghCasinoScraper underTest = new TheMoghCasinoScraper();

    @Test
    public void searchCasinosWorksCorrectly() throws IOException {

        final List<TheMoghCasino> bellagioCasinosList = underTest.searchItems(TEST_CASINO_NAME).stream()
                .map(casino -> (TheMoghCasino) casino)
                .collect(Collectors.toList());
        assertThat(bellagioCasinosList).isNotEmpty();
        bellagioCasinosList.stream().forEach(casinoBean ->
        {
            assertThat(casinoBean.getName()).containsIgnoringCase(TEST_CASINO_NAME);
            assertThat(casinoBean.getCountry()).isNotNull();
            System.out.println("casinoBean = " + casinoBean.getDetailPageUrl());
            assertThat(casinoBean.getDetailPageUrl()).isNotNull();
        });
    }
}