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
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Component
@Order(ScraperEngine.DEFAULT_ENGINE)
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
        requireNonNull(search, "Casino name cannot be null");
        return casinoScraper.searchItems(search);
    }

    @Override
    public List<PokerChipBean> searchPokerChip(CasinoBean casino) throws IOException {
        requireNonNull(casino, "Casino bean cannot be null");
        return pokerChipScraper.searchItems(casino);
    }

    @Override
    public String toString() {
        return "The Mogh";
    }
}
