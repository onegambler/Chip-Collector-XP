package com.chipcollector.scraper;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.themogh.TheMoghCasinoScraper;
import com.chipcollector.scraper.themogh.TheMoghPokerChipScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Order(0)
public class TheMoghScraperEngine implements ScraperEngine {

    private TheMoghCasinoScraper casinoScraper;
    private TheMoghPokerChipScraper pokerChipScraper;

    @Autowired
    public TheMoghScraperEngine(TheMoghCasinoScraper casinoScraper, TheMoghPokerChipScraper pokerChipScraper) {
        this.casinoScraper = casinoScraper;
        this.pokerChipScraper = pokerChipScraper;
    }

    @Override
    public List<CasinoBean> searchCasinos(String search) throws IOException {
        return casinoScraper.searchItems(search);
    }

    @Override
    public List<PokerChipBean> searchPokerChip(CasinoBean casino) throws IOException {
        return pokerChipScraper.searchItems(casino);
    }

    @Override
    public String toString() {
        return "The Mogh";
    }
}
