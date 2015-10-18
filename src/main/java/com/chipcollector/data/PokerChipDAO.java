package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.Transactional;
import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.PokerChip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PokerChipDAO {

    private EbeanServer ebeanServer;

    public PokerChipDAO(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public List<PokerChip> getAllPokerChips() {
        return ebeanServer.find(PokerChip.class).findList();
    }

    public List<PokerChip> getPagedPokerChips(int pagedIndex, int pageSize) {
        Query<PokerChip> query = ebeanServer.createQuery(PokerChip.class);
        return ebeanServer.
                findPagedList(query, ebeanServer.currentTransaction(), pagedIndex, pageSize)
                .getList();
    }

    public List<Casino> getAllCasinos() {
        return ebeanServer.find(Casino.class).findList();
    }

    public List<Country> getAllCountries() {
        return ebeanServer.find(Country.class).findList();
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

    public List<PokerChip> getPokerChipListFromCasino(Casino casino) {
        return ebeanServer.createQuery(PokerChip.class).where().eq("casino.id", casino.getId()).findList();
    }

    public int getPokerChipCount(Query<PokerChip> pokerChipQuery) {
        return ebeanServer.findRowCount(pokerChipQuery, ebeanServer.currentTransaction());
    }

    public int getPokerChipCount() {
        return getPokerChipCount(ebeanServer.createQuery(PokerChip.class));
    }
}
