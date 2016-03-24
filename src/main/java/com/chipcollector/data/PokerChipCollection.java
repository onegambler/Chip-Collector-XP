package com.chipcollector.data;

import com.avaje.ebean.Query;
import com.chipcollector.data.listeners.Listener;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.MainProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@Component
@MainProfile
public class PokerChipCollection {

    private final PokerChipDAO pokerChipDAO;
    private PokerChipHandler pokerChipHandler;
    private Query<PokerChip> currentFilter;
    private List<Listener> updateListenersList = new ArrayList<>();

    @Autowired
    public PokerChipCollection(PokerChipDAO pokerChipDAO, PokerChipHandler pokerChipHandler) {
        this.pokerChipHandler = pokerChipHandler;
        this.pokerChipDAO = pokerChipDAO;
        this.currentFilter = pokerChipDAO.createPokerChipFilter();
    }

    public List<Casino> getAllCasinos() {
        return this.pokerChipDAO.getAllCasinos();
    }

    public List<Country> getAllCountries() {
        return this.pokerChipDAO.getAllCountries();
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

    public void add(PokerChipBean pokerChipBean) throws IOException {
        PokerChip pokerChip = pokerChipHandler.getNewPokerChip(pokerChipBean);
        pokerChipDAO.savePokerChip(pokerChip);
        updateListenersList.forEach(Listener::action);
    }

    public void update(PokerChipBean pokerChipBean) {
        final PokerChip updatedPokerChip = pokerChipHandler.getUpdatedPokerChip(pokerChipBean);
        pokerChipDAO.updatePokerChip(updatedPokerChip);
        updateListenersList.forEach(Listener::action);
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

    public void addUpdateListener(Listener listener) {
        updateListenersList.add(listener);
    }

    public void deletePokerChip(PokerChip pokerChip) {
        pokerChipDAO.deletePokerChip(pokerChip);
    }
}
