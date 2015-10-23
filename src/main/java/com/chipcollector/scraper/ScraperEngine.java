package com.chipcollector.scraper;

import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;

import java.io.IOException;
import java.util.List;

public interface ScraperEngine {

    List<CasinoBean> searchCasinos(String search) throws IOException;

    List<PokerChip> searchPokerChip(CasinoBean casino) throws IOException;
}
