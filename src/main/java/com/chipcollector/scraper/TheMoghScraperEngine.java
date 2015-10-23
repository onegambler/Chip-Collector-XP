package com.chipcollector.scraper;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.scraper.themogh.TheMoghPokerChipScraper;
import com.chipcollector.scraper.themogh.TheMoghCasinoScraper;

import java.io.IOException;
import java.util.List;

public class TheMoghScraperEngine implements ScraperEngine {

    private TheMoghCasinoScraper casinoScraper;
    private TheMoghPokerChipScraper pokerChipScraper;

    public TheMoghScraperEngine(PokerChipDAO pokerChipDAO) {
        casinoScraper = new TheMoghCasinoScraper();
        pokerChipScraper = new TheMoghPokerChipScraper(pokerChipDAO);
    }

    @Override
    public List<CasinoBean> searchCasinos(String search) throws IOException {
        return casinoScraper.searchItems(search);
    }

    @Override
    public List<PokerChip> searchPokerChip(CasinoBean casino) throws IOException {
        return pokerChipScraper.searchItems(casino);
    }
}
