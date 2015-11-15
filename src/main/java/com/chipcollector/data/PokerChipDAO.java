package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.Transactional;
import com.chipcollector.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PokerChipDAO {

    private EbeanServer ebeanServer;

    @Autowired
    public PokerChipDAO(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public List<PokerChip> getAllPokerChips() {
        return ebeanServer.find(PokerChip.class).findList();
    }

    public List<PokerChip> getPagedPokerChips(Query<PokerChip> currentFilter, int pagedIndex, int pageSize) {
        return ebeanServer.
                findPagedList(currentFilter, ebeanServer.currentTransaction(), pagedIndex, pageSize)
                .getList();
    }

    public List<Casino> getAllCasinos() {
        return ebeanServer.find(Casino.class).findList();
    }

    public CasinoFinder getCasinoFinder() {
        return new CasinoFinder();
    }

    public List<Country> getAllCountries() {
        return ebeanServer.find(Country.class)
                .setUseCache(true)
                .setReadOnly(false)
                .findList();
    }

    public Country getCountry(String name) {
        return ebeanServer.find(Country.class)
                .setUseCache(true)
                .setReadOnly(true)
                .findUnique();
    }

    public void savePokerChip(PokerChip pokerChip) {
        ebeanServer.save(pokerChip);
        pokerChip.resetDirty();
    }

    public void updatePokerChip(PokerChip pokerChip) {
        long oldFrontImageId = -1;
        long oldBackImageId = -1;
        Set<Long> validIds = new HashSet<>();
        if (pokerChip.isImagesChanged()) {
            PokerChip oldPokerChip = getPokerChip(pokerChip.getId());
            oldFrontImageId = oldPokerChip.getFrontImage().map(BlobImage::getId).orElse(-1l);
            oldBackImageId = oldPokerChip.getBackImage().map(BlobImage::getId).orElse(-1l);
            pokerChip.getFrontImage().map(BlobImage::getId).ifPresent(validIds::add);
            pokerChip.getBackImage().map(BlobImage::getId).ifPresent(validIds::add);
        }
        ebeanServer.update(pokerChip);

        if (oldFrontImageId > -1 && !validIds.contains(oldFrontImageId)) {
            deleteImage(oldFrontImageId);
        }
        if (oldBackImageId > -1 && oldFrontImageId != oldBackImageId && !validIds.contains(oldBackImageId)) {
            deleteImage(oldBackImageId);
        }

        pokerChip.resetDirty();
    }

    @Transactional
    public void deletePokerChip(PokerChip pokerChip) {
        ebeanServer.delete(pokerChip);
        pokerChip.getBackImage().ifPresent(this::deleteImage);
        pokerChip.getFrontImage().ifPresent(this::deleteImage);
    }

    private void deleteImage(BlobImage image) {
        deleteImage(image.getId());
    }

    private void deleteImage(long imageId) {
        ebeanServer.delete(BlobImage.class, imageId);
    }

    public PokerChip getPokerChip(long chipId) {
        return ebeanServer.find(PokerChip.class, chipId);
    }

    public List<PokerChip> getPokerChipList(Query<PokerChip> filter) {
        return filter.findList();
    }

    public int getAllPokerChipsCount() {
        return getPokerChipCount(ebeanServer.createQuery(PokerChip.class));
    }

    public int getPokerChipCount(Query<PokerChip> pokerChipQuery) {
        return ebeanServer.findRowCount(pokerChipQuery, ebeanServer.currentTransaction());
    }

    public Query<PokerChip> createPokerChipFilter() {
        return ebeanServer.createQuery(PokerChip.class);
    }

    public void createDB() {

    }

    public LocationFinder getLocationFinder() {
        return new LocationFinder();
    }

    public class CasinoFinder {
        private final ExpressionList<Casino> query = ebeanServer.createQuery(Casino.class).where();

        public CasinoFinder withName(String name) {
            query.eq("name", name);
            return this;
        }

        public CasinoFinder withCity(String city) {
            query.eq("location.city", city);
            return this;
        }

        public CasinoFinder withCountry(String country) {
            query.eq("location.country", country);
            return this;
        }

        public CasinoFinder withState(String state) {
            query.eq("location.state", state);
            return this;
        }

        public Optional<Casino> findSingle() {
            Casino uniqueCasino = ebeanServer.findUnique(query.query(), ebeanServer.currentTransaction());
            return Optional.ofNullable(uniqueCasino);
        }

        public List<Casino> findList() {
            return ebeanServer.findList(query.query(), ebeanServer.currentTransaction());
        }
    }

    public class LocationFinder {
        private final ExpressionList<Location> query = ebeanServer.createQuery(Location.class).where();

        public LocationFinder withCity(String city) {
            query.eq("city", city);
            return this;
        }

        public LocationFinder withCountry(String country) {
            query.eq("country", country);
            return this;
        }

        public LocationFinder withState(String state) {
            query.eq("state", state);
            return this;
        }

        public Optional<Location> findSingle() {
            Location uniqueLocation = ebeanServer.findUnique(query.query(), ebeanServer.currentTransaction());
            return Optional.ofNullable(uniqueLocation);
        }
    }
}
