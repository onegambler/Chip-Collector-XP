package com.chipcollector.scraper.themogh;

import com.chipcollector.model.dashboard.PokerChipBean;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TheMoghPokerChipScraperIT {

    private TheMoghPokerChipScraper scraper = new TheMoghPokerChipScraper();

    @Test
    public void testSearchItems() throws Exception {
        TheMoghCasino casino = TheMoghCasino.build()
                .name("casinoName")
                .websiteUrl("http://www.casino.com")
                .city("City")
                .country("Country")
                .detailPageUrl("cg_chip2.php?id=NVLVBE&sort=type")
                .build();
        final List<PokerChipBean> pokerChipBeanList = scraper.searchItems(casino);
        pokerChipBeanList.forEach(pokerChipBean -> {
            assertThat(pokerChipBean.getCasinoBean()).as("CasinoBean is not null").isEqualTo(casino);
            assertThat(pokerChipBean.isNew()).as("PokerChipBean is new").isTrue();
        });
    }
}