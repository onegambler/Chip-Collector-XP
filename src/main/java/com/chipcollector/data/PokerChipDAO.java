package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Transaction;
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
        ebeanServer.delete(BlobImage.class, image.getId());
    }

    public PokerChip getPokerChip(long chipId) {
        return ebeanServer.find(PokerChip.class, chipId);
    }
}
