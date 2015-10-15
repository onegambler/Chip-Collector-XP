package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.*;

import java.util.List;

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

    public Collection loadCollection() {
        Collection collection = new CollectionImpl();

        return collection;
    }

    public void savePokerChip(PokerChip pokerChip) {
        ebeanServer.save(pokerChip);
    }

    public void deletePokerChip(PokerChip pokerChip) {
        ebeanServer.beginTransaction();
        pokerChip.getFrontImage().ifPresent(this::deleteImage);
        pokerChip.getBackImage().ifPresent(this::deleteImage);
        ebeanServer.delete(pokerChip);
        ebeanServer.endTransaction();
    }

    private void deleteImage(BlobImage image) {
        ebeanServer.refresh(image);
        image.decreaseUsage();
        if (image.getUsages() == 0) {
            ebeanServer.delete(image);
        } else {
            ebeanServer.update(image);
        }
    }

    public PokerChip getPokerChip(long chipId) {
        return ebeanServer.find(PokerChip.class, chipId);
    }
}
