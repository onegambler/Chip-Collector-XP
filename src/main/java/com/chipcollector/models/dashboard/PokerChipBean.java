package com.chipcollector.models.dashboard;

import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.core.BigDecimalProperty;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;
import static java.math.BigDecimal.ZERO;
import static java.util.Objects.*;

@Slf4j
@ToString
public class PokerChipBean {

    private CasinoBean casinoBean;
    private StringProperty cityName;
    private StringProperty denomProperty;
    private StringProperty moldProperty;
    private StringProperty colorProperty;
    private StringProperty inlayProperty;
    private StringProperty insertsProperty;
    private StringProperty yearProperty;
    private StringProperty tcrIdProperty;
    private ImageView frontImageThumbnailView;
    private ImageView backImageThumbnailView;
    private BooleanProperty obsoleteProperty;
    private BooleanProperty cancelledProperty;
    private StringProperty rarityProperty;
    private StringProperty conditionProperty;
    private StringProperty categoryProperty;
    private BigDecimalProperty valueProperty;
    private BigDecimalProperty paidProperty;
    private SimpleObjectProperty<LocalDate> dateOfAcquisition;
    private StringProperty notesProperty;
    private StringProperty issueProperty;
    private byte[] frontImage;
    private byte[] backImage;

    private String casinoFlagImageString;

    private PokerChip pokerChip;

    public PokerChipBean(PokerChip pokerChip) {
        this.pokerChip = pokerChip;
        casinoBean = new CasinoBean(pokerChip.getCasino());
        cityName = new SimpleStringProperty(pokerChip.getCasino().getCity());
        denomProperty = new SimpleStringProperty(pokerChip.getDenom());
        moldProperty = new SimpleStringProperty(pokerChip.getMold());
        colorProperty = new SimpleStringProperty(pokerChip.getColor());
        inlayProperty = new SimpleStringProperty(pokerChip.getInlay());
        insertsProperty = new SimpleStringProperty(pokerChip.getInserts());
        yearProperty = new SimpleStringProperty(pokerChip.getYear());
        tcrIdProperty = new SimpleStringProperty(pokerChip.getTcrID());
        obsoleteProperty = new SimpleBooleanProperty(pokerChip.isObsolete());
        cancelledProperty = new SimpleBooleanProperty(pokerChip.isCancelled());
        rarityProperty = new SimpleStringProperty(pokerChip.getRarity());
        conditionProperty = new SimpleStringProperty(pokerChip.getCondition());
        categoryProperty = new SimpleStringProperty(pokerChip.getCategory());
        notesProperty = new SimpleStringProperty(pokerChip.getNotes());
        dateOfAcquisition = new SimpleObjectProperty<>(pokerChip.getAcquisitionDate());
        issueProperty = new SimpleStringProperty(String.valueOf(pokerChip.getIssue()));
        frontImageThumbnailView = new ImageView();
        backImageThumbnailView = new ImageView();
        //TODO: values and paidProperty
        valueProperty = new BigDecimalProperty(ZERO);
        paidProperty = new BigDecimalProperty(ZERO);

        pokerChip.getFrontImage().ifPresent(frontBlobImage ->
        {
            frontImage = frontBlobImage.getImage();
            frontImageThumbnailView.setImage(getImageFromByteArray(frontBlobImage.getThumbnail()));
        });
        pokerChip.getBackImage().ifPresent(backBlobImage ->
        {
            backImage = backBlobImage.getImage();
            backImageThumbnailView.setImage(getImageFromByteArray(backBlobImage.getThumbnail()));
        });
        initialisePropertiesListeners();
    }

    private PokerChipBean(PokerChipBeanBuilder builder) {
        casinoBean = builder.casinoBean;
        cityName = new SimpleStringProperty(casinoBean.getCity());
        colorProperty = new SimpleStringProperty(builder.color);
        denomProperty = new SimpleStringProperty(builder.denom);
        inlayProperty = new SimpleStringProperty(builder.inlay);
        insertsProperty = new SimpleStringProperty(builder.inserts);
        moldProperty = new SimpleStringProperty(builder.mold);
        tcrIdProperty = new SimpleStringProperty(builder.tcrId);
        yearProperty = new SimpleStringProperty(builder.year);

        tcrIdProperty = new SimpleStringProperty(builder.tcrId);
        obsoleteProperty = new SimpleBooleanProperty(builder.obsolete);
        cancelledProperty = new SimpleBooleanProperty(builder.cancelled);
        conditionProperty = new SimpleStringProperty(builder.condition);
        categoryProperty = new SimpleStringProperty(builder.category);
        rarityProperty = new SimpleStringProperty(builder.rarity);
        dateOfAcquisition = new SimpleObjectProperty<>(builder.dateOfAcquisition);
        //TODO: VALUE PROPERTY
        valueProperty = new BigDecimalProperty(builder.value);
        paidProperty = new BigDecimalProperty(builder.paid);
        issueProperty = new SimpleStringProperty(builder.issue);
        notesProperty = new SimpleStringProperty(builder.notes);
        frontImageThumbnailView = new ImageView();
        backImageThumbnailView = new ImageView();
        frontImage = builder.frontImage;
        backImage = builder.backImage;

        setImageThumbnails(newArrayList(frontImage, backImage));
    }

    private void initialisePropertiesListeners() {
        addPropertyListener(cancelledProperty, pokerChip::setCancelled);
        addPropertyListener(categoryProperty, pokerChip::setCategory);
        addPropertyListener(colorProperty, pokerChip::setColor);
        addPropertyListener(denomProperty, pokerChip::setDenom);
        addPropertyListener(inlayProperty, pokerChip::setInlay);
        addPropertyListener(insertsProperty, pokerChip::setInserts);
        addPropertyListener(moldProperty, pokerChip::setMold);
        addPropertyListener(yearProperty, pokerChip::setYear);
        addPropertyListener(tcrIdProperty, pokerChip::setTcrID);
        addPropertyListener(obsoleteProperty, pokerChip::setObsolete);
        addPropertyListener(conditionProperty, pokerChip::setCondition);
        addPropertyListener(rarityProperty, pokerChip::setRarity);
        addPropertyListener(valueProperty, bigDecimal -> pokerChip.getValue().setAmount(bigDecimal));
        addPropertyListener(paidProperty, bigDecimal -> pokerChip.getAmountPaid().setAmount(bigDecimal));
        addPropertyListener(issueProperty, pokerChip::setIssue);
        addPropertyListener(notesProperty, pokerChip::setNotes);
    }

    private <T> void addPropertyListener(Property<T> property, Consumer<T> propertySetter) {
        property.addListener((observable, oldValue, newValue) -> {
            if (isValueChanged(oldValue, newValue)) {
                propertySetter.accept(newValue);
            }
        });
    }

    private <T> boolean isValueChanged(T oldValue, T newValue) {
        return oldValue != null && !oldValue.equals(newValue);
    }

    @NotNull
    private Image getImageFromByteArray(byte[] imageByteArray) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream);
    }

    public StringProperty colorProperty() {
        return colorProperty;
    }

    public StringProperty denomProperty() {
        return denomProperty;
    }

    public StringProperty inlayProperty() {
        return inlayProperty;
    }

    public StringProperty insertsProperty() {
        return insertsProperty;
    }

    @FXML
    public ImageView getBackImageThumbnailView() {
        return backImageThumbnailView;
    }

    @FXML
    public ImageView getFrontImageThumbnailView() {
        return frontImageThumbnailView;
    }

    public StringProperty moldProperty() {
        return moldProperty;
    }

    public StringProperty tcrIdPropertyProperty() {
        return tcrIdProperty;
    }

    public StringProperty yearProperty() {
        return yearProperty;
    }

    public SimpleObjectProperty<Image> getFrontImage() {
        return getImageFromByteArray(frontImage, 120, 120);
    }

    public SimpleObjectProperty<Image> getBackImage() {
        return getImageFromByteArray(backImage, 120, 120);
    }

    private SimpleObjectProperty<Image> getImageFromByteArray(byte[] imageBytes, double requestedWidth, double requestedHeight) {
        final Image image = new Image(new ByteArrayInputStream(imageBytes), requestedWidth, requestedHeight, true, true);
        return new SimpleObjectProperty<>(image);
    }

    public BooleanProperty cancelledProperty() {
        return cancelledProperty;
    }

    public BooleanProperty obsoleteProperty() {
        return obsoleteProperty;
    }

    public StringProperty rarityPropertyProperty() {
        return rarityProperty;
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
        if (nonNull(imageByteArray)) {
            imageThumbnailView.setImage(getImageFromByteArray(imageByteArray, 90, 90).getValue());
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

    public StringProperty conditionPropertyProperty() {
        return conditionProperty;
    }

    public StringProperty categoryProperty() {
        return categoryProperty;
    }

    public BigDecimal getValue() {
        return valueProperty.get();
    }

    public BigDecimal getPaid() {
        return paidProperty.get();
    }

    public SimpleObjectProperty<LocalDate> dateOfAcquisitionProperty() {
        return dateOfAcquisition;
    }

    public StringProperty notesProperty() {
        return notesProperty;
    }

    public StringProperty issuePropertyProperty() {
        return issueProperty;
    }

    public PokerChip getPokerChip() {
        return pokerChip;
    }

    public boolean isNew() {
        return isNull(this.pokerChip);
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
