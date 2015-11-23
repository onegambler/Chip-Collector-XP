package com.chipcollector.data;

import com.avaje.ebean.Query;
import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Repository
public class PokerChipCollection {

    private final PokerChipDAO pokerChipDAO;

    private Query<PokerChip> currentFilter;

    private List<Listener> updateListenersList = new ArrayList<>();

    @Autowired
    public PokerChipCollection(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
        this.currentFilter = pokerChipDAO.createPokerChipFilter();
    }

    public List<Casino> getAllCasinos() {
        return this.pokerChipDAO.getAllCasinos();
    }

    public List<PokerChip> getPokerChipList() {
        return this.pokerChipDAO.getPokerChipList(currentFilter);
    }

    public int getPokerChipCount() {
        return pokerChipDAO.getPokerChipCount(currentFilter);
    }

    public int getAllPokerChipsCount() {
        return pokerChipDAO.getAllPokerChipsCount();
    }

    public int getPokerChipCountForLast7Days() {
        return pokerChipDAO.getPokerChipCount(pokerChipDAO.createPokerChipFilter().where().gt("acquisitionDate", now().minusDays(7)).query());
    }

    public int getPokerChipCountForLastMonth() {
        return pokerChipDAO.getPokerChipCount(pokerChipDAO.createPokerChipFilter().where().gt("acquisitionDate", now().minusMonths(1)).query());
    }

    public int getNumDifferentCasinos() {
        return pokerChipDAO.getAllCasinos().size();
    }

    public List<PokerChip> getPagedPokerChips(int pageIndex, int pageSize) {
        return pokerChipDAO.getPagedPokerChips(currentFilter, pageIndex, pageSize);
    }

    public void setCasinoFilter(Casino casino) {
        currentFilter = pokerChipDAO.createPokerChipFilter().where().eq("casino.id", casino.getId()).query();
    }

    public void add(PokerChip pokerChip) {
        pokerChipDAO.savePokerChip(pokerChip);
        updateListenersList.forEach(Listener::action);
    }

    public Optional<Casino> getCasinoFromCasinoBean(CasinoBean casinoBean) {
        return pokerChipDAO.getCasinoFinder()
                .withName(casinoBean.getName())
                .withCity(casinoBean.getCity())
                .withState(casinoBean.getState())
                .withCountry(casinoBean.getCountry())
                .findSingle();
    }

    public Optional<Location> getLocationFromCasinoBean(CasinoBean casinoBean) {
        return pokerChipDAO.getLocationFinder()
                .withCity(casinoBean.getCity())
                .withState(casinoBean.getState())
                .withCountry(casinoBean.getCountry())
                .findSingle();
    }

    public Country getCountryFromCasinoBean(CasinoBean casinoBean) {
        return getCountryFromName(casinoBean.getCountry());
    }

    public Country getCountryFromName(String name) {
        return pokerChipDAO.getCountry(name);
    }

    public void addUpdateListener(Listener listener) {
        updateListenersList.add(listener);
    }
}
