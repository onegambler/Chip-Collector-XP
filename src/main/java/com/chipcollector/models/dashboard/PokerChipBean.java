package com.chipcollector.models.dashboard;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.PokerChip;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@ToString
public class PokerChipBean {

    private CasinoBean casinoBean;
    private StringProperty cityName;
    private StringProperty denom;
    private StringProperty mold;
    private StringProperty color;
    private StringProperty inlay;
    private StringProperty inserts;
    private StringProperty year;
    private StringProperty tcrId;
    private BooleanProperty obsolete;
    private BooleanProperty cancelled;
    private ImageView frontImage;
    private ImageView backImage;

    private String casinoFlagImageString;

    public PokerChipBean(PokerChip pokerChip) {
        this.casinoBean = new CasinoBean(pokerChip.getCasino());
        this.cityName = new SimpleStringProperty(pokerChip.getCasino().getCity());
        this.denom = new SimpleStringProperty(pokerChip.getDenom());
        this.mold = new SimpleStringProperty(pokerChip.getMold());
        this.color = new SimpleStringProperty(pokerChip.getColor());
        this.inlay = new SimpleStringProperty(pokerChip.getInlay());
        this.inserts = new SimpleStringProperty(pokerChip.getInserts());
        this.year = new SimpleStringProperty(pokerChip.getYear());
        this.tcrId = new SimpleStringProperty(pokerChip.getTcrID());
        if (pokerChip.getFrontImage().isPresent()) {
            Image image = getImageFromByteArray(pokerChip.getFrontImage().map(BlobImage::getThumbnail).get());
            this.frontImage = new ImageView(image);
        }

        if (pokerChip.getBackImage().isPresent()) {
            Image image = getImageFromByteArray(pokerChip.getBackImage().map(BlobImage::getThumbnail).get());
            this.backImage = new ImageView(image);
        }
        this.obsolete = new SimpleBooleanProperty(pokerChip.isObsolete());
        this.cancelled = new SimpleBooleanProperty(pokerChip.isCancelled());
    }

    private PokerChipBean(CasinoBean casinoBean, StringProperty cityName, StringProperty denom, StringProperty mold, StringProperty color, StringProperty inlay, StringProperty inserts, StringProperty year, StringProperty tcrId, ImageView frontImage, ImageView backImage, BooleanProperty obsolete, BooleanProperty cancelled) {
        this.casinoBean = casinoBean;
        this.cityName = cityName;
        this.denom = denom;
        this.mold = mold;
        this.color = color;
        this.inlay = inlay;
        this.inserts = inserts;
        this.year = year;
        this.tcrId = tcrId;
        this.frontImage = frontImage;
        this.backImage = backImage;
        this.obsolete = obsolete;
        this.cancelled = cancelled;
    }

    private PokerChipBean(CasinoBean casinoBean, String color, String denom, String inlay, String inserts, String mold, String tcrId, String year, Image frontImage, Image backImage, boolean obsolete, boolean cancelled) {
        this.casinoBean = casinoBean;
        this.cityName = new SimpleStringProperty(casinoBean.getCity());
        this.color = new SimpleStringProperty(color);
        this.denom = new SimpleStringProperty(denom);
        this.inlay = new SimpleStringProperty(inlay);
        this.inserts = new SimpleStringProperty(inserts);
        this.mold = new SimpleStringProperty(mold);
        this.tcrId = new SimpleStringProperty(tcrId);
        this.year = new SimpleStringProperty(year);
        this.tcrId = new SimpleStringProperty(tcrId);
        this.frontImage = new ImageView(frontImage);
        this.backImage = new ImageView(backImage);
        this.obsolete = new SimpleBooleanProperty(obsolete);
        this.cancelled = new SimpleBooleanProperty(cancelled);
    }

    @NotNull
    private Image getImageFromByteArray(byte[] imageByteArray) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream);
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

    public void setImages(List<Image> pictures) {
        if (pictures.size() >= 1) {

            this.frontImage.setImage(pictures.get(0));

            if (pictures.size() >= 2) {
                this.backImage.setImage(pictures.get(1));
            } else {
                this.backImage.setImage(pictures.get(0));
            }
        }
    }

    public static PokerChipBeanBuilder builder() {
        return new PokerChipBeanBuilder();
    }

    public CasinoBean getCasinoBean() {
        return casinoBean;
    }


    public static class PokerChipBeanBuilder {
        private CasinoBean casinoBean;
        private String color;
        private String denom;
        private String inlay;
        private String inserts;
        private String mold;
        private String tcrId;
        private String year;
        private Image frontImage;
        private Image backImage;
        private boolean obsolete;
        private boolean cancelled;

        public PokerChipBeanBuilder casino(CasinoBean casino) {
            this.casinoBean = casino;
            return this;
        }

        public PokerChipBeanBuilder color(String color) {
            this.color = color;
            return this;
        }

        public PokerChipBeanBuilder denom(String denom) {
            this.denom = denom;
            return this;
        }

        public PokerChipBeanBuilder inlay(String inlay) {
            this.inlay = inlay;
            return this;
        }

        public PokerChipBeanBuilder inserts(String inserts) {
            this.inserts = inserts;
            return this;
        }

        public PokerChipBeanBuilder mold(String mold) {
            this.mold = mold;
            return this;
        }

        public PokerChipBeanBuilder tcrId(String tcrId) {
            this.tcrId = tcrId;
            return this;
        }

        public PokerChipBeanBuilder year(String year) {
            this.year = year;
            return this;
        }

        public PokerChipBeanBuilder frontImage(Image frontImage) {
            this.frontImage = frontImage;
            return this;
        }

        public PokerChipBeanBuilder backImage(Image backImage) {
            this.backImage = backImage;
            return this;
        }

        public PokerChipBeanBuilder obsolete(boolean obsolete) {
            this.obsolete = obsolete;
            return this;
        }

        public PokerChipBeanBuilder cancelled(boolean cancelled) {
            this.cancelled = cancelled;
            return this;
        }

        public PokerChipBean build() {
            return new PokerChipBean(casinoBean, color, denom, inlay, inserts, mold, tcrId, year, frontImage, backImage, obsolete, cancelled);
        }
    }
}
