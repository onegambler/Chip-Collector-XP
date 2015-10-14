package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.BlobImage;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageDAO {

    private final EbeanServer ebeanServer;

    public ImageDAO(EbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public BufferedImage getImage(String pokerChipId) {
        List<BlobImage> blobImageList = ebeanServer.find(BlobImage.class)
                .where()
                .findList();
        return blobImageList.toArray(blobImageList.size())


    }
}
