package com.chipcollector.scraper;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.themogh.TheMoghCasinoScraper;
import com.chipcollector.scraper.themogh.TheMoghPokerChipScraper;

import java.io.IOException;
import java.util.List;

public class TheMoghScraperEngine implements ScraperEngine {

    private TheMoghCasinoScraper casinoScraper;
    private TheMoghPokerChipScraper pokerChipScraper;

    public TheMoghScraperEngine() {
        casinoScraper = new TheMoghCasinoScraper();
        pokerChipScraper = new TheMoghPokerChipScraper();
    }

    @Override
    public List<CasinoBean> searchCasinos(String search) throws IOException {
        return casinoScraper.searchItems(search);
    }

    @Override
    public List<PokerChipBean> searchPokerChip(CasinoBean casino) throws IOException {
        return pokerChipScraper.searchItems(casino);
    }
}
