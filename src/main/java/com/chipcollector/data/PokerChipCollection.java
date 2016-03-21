package com.chipcollector.data;

import com.avaje.ebean.Query;
import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.CasinoBean;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.MainProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@Component
@MainProfile
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

    public int getFilteredPokerChipCount() {
        return pokerChipDAO.getPokerChipCount(currentFilter);
    }

    public int getAllPokerChipsCount() {
        return pokerChipDAO.getAllPokerChipsCount();
    }

    public int getPokerChipCountForLast7Days() {
        final Query<PokerChip> last7DaysPokerChipFilter = pokerChipDAO.createPokerChipFilter()
                .where().gt("acquisitionDate", now().minusDays(7))
                .query();
        return pokerChipDAO.getPokerChipCount(last7DaysPokerChipFilter);
    }

    public int getPokerChipCountForLastMonth() {
        final Query<PokerChip> lastMonthPokerChipFilter = pokerChipDAO.createPokerChipFilter()
                .where().gt("acquisitionDate", now().minusMonths(1))
                .query();
        return pokerChipDAO.getPokerChipCount(lastMonthPokerChipFilter);
    }

    public int getAllCasinosCount() {
        return pokerChipDAO.getAllCasinos().size();
    }

    public Stream<PokerChipBean> getPagedPokerChips(int pageIndex, int pageSize) {
        return pokerChipDAO.getPagedPokerChips(currentFilter, pageIndex, pageSize).
                stream().map(PokerChipBean::new);
    }

    public void setCasinoFilter(Casino casino) {
        currentFilter = pokerChipDAO.createPokerChipFilter().where().eq("casino.id", casino.getId()).query();
    }

    public void resetCasinoFilter() {
        currentFilter = pokerChipDAO.createPokerChipFilter();
    }

    public void add(PokerChip pokerChip) {
        pokerChipDAO.savePokerChip(pokerChip);
        updateListenersList.forEach(Listener::action);
    }

    public void update(PokerChipBean pokerChipBean) {
        pokerChipDAO.updatePokerChip(pokerChipBean.getPokerChip());
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

    public Optional<Country> getCountryFromCasinoBean(CasinoBean casinoBean) {
        return getCountryFromName(casinoBean.getCountry());
    }

    public List<String> getDenomAutocompleteValues() {
        return pokerChipDAO.getDistinctValueSet("denom", PokerChip::getDenom);
    }

    public List<String> getInlayAutocompleteValues() {
        return pokerChipDAO.getDistinctValueSet("inlay", PokerChip::getInlay);
    }

    public List<String> getColorAutocompleteValues() {
        return pokerChipDAO.getDistinctValueSet("color", PokerChip::getColor);
    }

    public List<String> getMoldAutocompleteValues() {
        return pokerChipDAO.getDistinctValueSet("mold", PokerChip::getMold);
    }

    /**
     * Method created for test purposes
     *
     * @return the current filter
     */
    Query<PokerChip> getCurrentFilter() {
        return currentFilter;
    }

    /**
     * Method created for test purposes
     *
     * @param currentFilter the filter to set
     */
    void setCurrentFilter(Query<PokerChip> currentFilter) {
        this.currentFilter = currentFilter;
    }

    public Optional<Country> getCountryFromName(String name) {
        return pokerChipDAO.getCountry(name);
    }

    public void addUpdateListener(Listener listener) {
        updateListenersList.add(listener);
    }

}
