package com.chipcollector.data;

import com.avaje.ebean.Query;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
public class Collection {

    private final PokerChipDAO pokerChipDAO;

    private Query<PokerChip> currentFilter;

    @Autowired
    public Collection(PokerChipDAO pokerChipDAO) {
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

    public void load() {

    }
}
