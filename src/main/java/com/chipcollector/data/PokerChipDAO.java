package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.event.BeanPersistListener;
import com.chipcollector.domain.*;

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

    public void deletePokerChip(PokerChip pokerChip) {
        ebeanServer.beginTransaction();
        try {
            ebeanServer.delete(pokerChip);
            pokerChip.getBackImage().ifPresent(this::deleteImage);
            pokerChip.getFrontImage().ifPresent(this::deleteImage);
            ebeanServer.commitTransaction();
        } finally {
            ebeanServer.endTransaction();

        }
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
}
