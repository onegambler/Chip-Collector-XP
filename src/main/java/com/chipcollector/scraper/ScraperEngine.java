package com.chipcollector.scraper;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;

import java.io.IOException;
import java.util.List;

public interface ScraperEngine {

    int DEFAULT_ENGINE = 0;

    List<CasinoBean> searchCasinos(String search) throws IOException;

    List<PokerChipBean> searchPokerChip(CasinoBean casino) throws IOException;
}
