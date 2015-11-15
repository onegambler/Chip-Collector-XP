package com.chipcollector.models.dashboard;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.core.BigDecimalProperty;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
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
    private StringProperty rarity;
    private StringProperty condition;
    private StringProperty category;
    private BigDecimalProperty value;
    private BigDecimalProperty paid;
    private SimpleObjectProperty<LocalDate> dateOfAcquisition;
    private StringProperty notes;
    private StringProperty issue;

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

    private PokerChipBean(CasinoBean casinoBean, StringProperty cityName, StringProperty denom, StringProperty mold,
                          StringProperty color, StringProperty inlay, StringProperty inserts, StringProperty year,
                          StringProperty tcrId, ImageView frontImage, ImageView backImage, BooleanProperty obsolete, BooleanProperty cancelled,
                          BigDecimalProperty value, BigDecimalProperty paid, StringProperty condition, StringProperty rarity,
                          SimpleObjectProperty<LocalDate> dateOfAcquisition, StringProperty category, StringProperty issue,
                          StringProperty notes) {
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
        this.value = value;
        this.paid = paid;
        this.condition = condition;
        this.rarity = rarity;
        this.dateOfAcquisition = dateOfAcquisition;
        this.category = category;
        this.issue = issue;
        this.notes = notes;
    }

    private PokerChipBean(CasinoBean casinoBean, String color, String denom, String inlay, String inserts, String mold,
                          String tcrId, String year, Image frontImage, Image backImage, boolean obsolete, boolean cancelled,
                          BigDecimal value, BigDecimal paid, String condition, String rarity, LocalDate dateOfAcquisition,
                          String category, String issue, String notes) {
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
        this.condition = new SimpleStringProperty(condition);
        this.category = new SimpleStringProperty(category);
        this.rarity = new SimpleStringProperty(rarity);
        this.dateOfAcquisition = new SimpleObjectProperty<>(dateOfAcquisition);
        this.value = new BigDecimalProperty(value);
        this.paid = new BigDecimalProperty(paid);
        this.issue = new SimpleStringProperty(issue);
        this.notes = new SimpleStringProperty(notes);
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

    public boolean isCancelled() {
        return this.cancelled.get();
    }

    public boolean isObsolete() {
        return this.obsolete.get();
    }

    public String getRarity() {
        return this.rarity.get();
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

    public String getCondition() {
        return condition.get();
    }

    public String getCategory() {
        return category.get();
    }

    public BigDecimal getValue() {
        return value.get();
    }

    public BigDecimal getPaid() {
        return paid.get();
    }

    public LocalDate getDateOfAcquisition() {
        return dateOfAcquisition.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public String getIssue() {
        return issue.get();
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
        private String category;
        private String condition;
        private String rarity;
        private Image frontImage;
        private Image backImage;
        private boolean obsolete;
        private boolean cancelled;
        private BigDecimal value;
        private BigDecimal paid;
        private LocalDate dateOfAcquisition;
        private String issue;
        private String notes;

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

        public PokerChipBeanBuilder category(String category) {
            this.category = category;
            return this;
        }

        public PokerChipBeanBuilder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public PokerChipBeanBuilder rarity(String rarity) {
            this.rarity = rarity;
            return this;
        }

        public PokerChipBeanBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public PokerChipBeanBuilder issue(String issue) {
            this.issue = issue;
            return this;
        }

        public PokerChipBeanBuilder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public PokerChipBeanBuilder paid(BigDecimal paid) {
            this.paid = paid;
            return this;
        }

        public PokerChipBeanBuilder dateOfAcquisition(LocalDate dateOfAcquisition) {
            this.dateOfAcquisition = dateOfAcquisition;
            return this;
        }

        public PokerChipBean build() {
            return new PokerChipBean(casinoBean, color, denom, inlay, inserts, mold, tcrId, year, frontImage, backImage,
                    obsolete, cancelled, value, paid, condition, rarity, dateOfAcquisition, category, issue, notes);
        }
    }
}
