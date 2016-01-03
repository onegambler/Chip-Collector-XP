package com.chipcollector.models.dashboard;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.core.BigDecimalProperty;
import com.chipcollector.util.ImageConverter;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.requireNonNull;


@Slf4j
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
    private ImageView frontImageThumbnailView;
    private ImageView backImageThumbnailView;
    private BooleanProperty obsolete;
    private BooleanProperty cancelled;
    private StringProperty rarity;
    private StringProperty condition;
    private StringProperty category;
    private BigDecimalProperty value;
    private BigDecimalProperty paid;
    private SimpleObjectProperty<LocalDate> dateOfAcquisition;
    private StringProperty notes;
    private StringProperty issue;
    private byte[] frontImage;
    private byte[] backImage;

    private String casinoFlagImageString;

    private PokerChip pokerChip;

    public PokerChipBean(PokerChip pokerChip) {
        this.pokerChip = pokerChip;

        this.casinoBean = new CasinoBean(pokerChip.getCasino());

        this.cityName = new SimpleStringProperty(pokerChip.getCasino().getCity());
        this.denom = new SimpleStringProperty(pokerChip.getDenom());
        this.mold = new SimpleStringProperty(pokerChip.getMold());
        this.color = new SimpleStringProperty(pokerChip.getColor());
        this.inlay = new SimpleStringProperty(pokerChip.getInlay());
        this.inserts = new SimpleStringProperty(pokerChip.getInserts());
        this.year = new SimpleStringProperty(pokerChip.getYear());
        this.tcrId = new SimpleStringProperty(pokerChip.getTcrID());
        this.frontImage = pokerChip.getFrontImage().map(BlobImage::getImage).orElse(new byte[0]);
        this.backImage = pokerChip.getBackImage().map(BlobImage::getImage).orElse(new byte[0]);
        this.obsolete = new SimpleBooleanProperty(pokerChip.isObsolete());
        this.cancelled = new SimpleBooleanProperty(pokerChip.isCancelled());
        this.rarity = new SimpleStringProperty(pokerChip.getRarity());
        this.condition = new SimpleStringProperty(pokerChip.getCondition());
        this.category = new SimpleStringProperty(pokerChip.getCategory());
        //TODO: values and paid
        this.value = new BigDecimalProperty(BigDecimal.ZERO);
        this.paid = new BigDecimalProperty(BigDecimal.ZERO);
        this.notes = new SimpleStringProperty(pokerChip.getNotes());
        this.dateOfAcquisition = new SimpleObjectProperty<>(pokerChip.getAcquisitionDate());
        this.issue = new SimpleStringProperty(String.valueOf(pokerChip.getIssue()));
        this.frontImageThumbnailView = new ImageView();
        this.backImageThumbnailView = new ImageView();
        pokerChip.getFrontImage().ifPresent(blobImage -> {
            try {
                frontImageThumbnailView.setImage(ImageConverter.rawBytesToImage(blobImage.getThumbnail()));
            } catch (IOException e) {
                log.error("Impossible to load image", e);
            }
        });
        pokerChip.getBackImage().ifPresent(blobImage -> {
            try {
                backImageThumbnailView.setImage(ImageConverter.rawBytesToImage(blobImage.getThumbnail()));
            } catch (IOException e) {
                log.error("Impossible to load image", e);
            }
        });
    }

    private PokerChipBean(PokerChipBeanBuilder builder) {
        this.casinoBean = builder.casinoBean;
        this.cityName = new SimpleStringProperty(casinoBean.getCity());
        this.color = new SimpleStringProperty(builder.color);
        this.denom = new SimpleStringProperty(builder.denom);
        this.inlay = new SimpleStringProperty(builder.inlay);
        this.inserts = new SimpleStringProperty(builder.inserts);
        this.mold = new SimpleStringProperty(builder.mold);
        this.tcrId = new SimpleStringProperty(builder.tcrId);
        this.year = new SimpleStringProperty(builder.year);
        this.tcrId = new SimpleStringProperty(builder.tcrId);
        this.obsolete = new SimpleBooleanProperty(builder.obsolete);
        this.cancelled = new SimpleBooleanProperty(builder.cancelled);
        this.condition = new SimpleStringProperty(builder.condition);
        this.category = new SimpleStringProperty(builder.category);
        this.rarity = new SimpleStringProperty(builder.rarity);
        this.dateOfAcquisition = new SimpleObjectProperty<>(builder.dateOfAcquisition);
        this.value = new BigDecimalProperty(builder.value);
        this.paid = new BigDecimalProperty(builder.paid);
        this.issue = new SimpleStringProperty(builder.issue);
        this.notes = new SimpleStringProperty(builder.notes);
        this.frontImage = builder.frontImage;
        this.backImage = builder.backImage;
        this.frontImageThumbnailView = new ImageView();
        this.backImageThumbnailView = new ImageView();
        setImageThumbnails(newArrayList(frontImage, backImage));
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

    public ImageView getFrontImageThumbnailView() {
        return frontImageThumbnailView;
    }

    public ImageView getBackImageThumbnailView() {
        return backImageThumbnailView;
    }

    public Image getFrontImage() {
        return getImageFromByteArray(frontImage, 120, 120);
    }

    public Image getBackImage() {
        return getImageFromByteArray(backImage, 120, 120);
    }

    @NotNull
    private Image getImageFromByteArray(byte[] image, double requestedWidth, double requestedHeight) {
        return new Image(new ByteArrayInputStream(image), requestedWidth, requestedHeight, true, true);
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

    public void setImageThumbnails(List<byte[]> pictures) {
        requireNonNull(pictures);
        if (pictures.size() >= 1) {
            frontImage = pictures.get(0);
            setImageThumbnailIfNotNull(frontImage, frontImageThumbnailView);

            if (pictures.size() >= 2) {
                backImage = pictures.get(1);
            } else {
                backImage = frontImage;
            }
            setImageThumbnailIfNotNull(backImage, backImageThumbnailView);
        }
    }

    private void setImageThumbnailIfNotNull(byte[] imageByteArray, ImageView imageThumbnailView) {
        if (imageByteArray != null) {
            imageThumbnailView.setImage(getImageFromByteArray(imageByteArray, 90, 90));
        }
    }

    public static PokerChipBeanBuilder builder() {
        return new PokerChipBeanBuilder();
    }

    public CasinoBean getCasinoBean() {
        return casinoBean;
    }

    public String getCasinoName() {
        return casinoBean.getName();
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
        private byte[] frontImage;
        private byte[] backImage;
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

        public PokerChipBeanBuilder frontImage(byte[] frontImage) {
            this.frontImage = frontImage;
            return this;
        }

        public PokerChipBeanBuilder backImage(byte[] backImage) {
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
            return new PokerChipBean(this);
        }
    }
}
