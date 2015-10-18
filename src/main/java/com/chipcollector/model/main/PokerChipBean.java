package com.chipcollector.model.main;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.PokerChip;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class PokerChipBean {

    private StringProperty casinoName;
    private StringProperty cityName;
    private StringProperty denom;
    private StringProperty mold;
    private StringProperty color;
    private StringProperty inlay;
    private StringProperty inserts;
    private StringProperty year;
    private StringProperty tcrId;
    private ImageView frontImage;
    private ImageView backImage;

    public PokerChipBean(PokerChip pokerChip) {
        this.casinoName = new SimpleStringProperty(pokerChip.getCasino().getName());
        this.cityName = new SimpleStringProperty(pokerChip.getCasino().getCity());
        this.denom = new SimpleStringProperty(pokerChip.getDenom());
        this.mold = new SimpleStringProperty(pokerChip.getMold());
        this.color = new SimpleStringProperty(pokerChip.getColor());
        this.inlay = new SimpleStringProperty(pokerChip.getInlay());
        this.inserts = new SimpleStringProperty(pokerChip.getInserts());
        this.year = new SimpleStringProperty(pokerChip.getYear());
        this.tcrId = new SimpleStringProperty(pokerChip.getTcrID());
        if (pokerChip.getFrontImage().isPresent()) {
            Image image = getImageFromByteArray(pokerChip.getFrontImage().map(BlobImage::getImage).get());
            this.frontImage = new ImageView(image);
        }

        if (pokerChip.getBackImage().isPresent()) {
            Image image = getImageFromByteArray(pokerChip.getBackImage().map(BlobImage::getImage).get());
            this.backImage = new ImageView(image);
        }
    }

    @NotNull
    private Image getImageFromByteArray(byte[] imageByteArray) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream, IMAGE_THUMBNAIL_SIZE, IMAGE_THUMBNAIL_SIZE, true, true);
    }

    public String getCasinoName() {
        return casinoName.get();
    }

    public String getCityName() {
        return cityName.get();
    }

    public String getColor() {
        return color.get();
    }

    public String getDenom() {
        return denom.get();
    }

    public String getInlay() {
        return inlay.get();
    }

    public String getInserts() {
        return inserts.get();
    }

    public String getMold() {
        return mold.get();
    }

    public String getTcrId() {
        return tcrId.get();
    }

    public String getYear() {
        return year.get();
    }

    public ImageView getFrontImage() {
        return frontImage;
    }

    public ImageView getBackImage() {
        return backImage;
    }

    public static final int IMAGE_THUMBNAIL_SIZE = 75;
}
