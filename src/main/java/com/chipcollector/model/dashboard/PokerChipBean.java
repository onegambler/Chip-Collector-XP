package com.chipcollector.model.dashboard;

import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.domain.Rarity;
import com.chipcollector.model.core.MoneyAmountProperty;
import com.chipcollector.util.ImageConverter;
import com.google.common.base.Throwables;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.chipcollector.util.ImageConverter.getImageFromByteArray;
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
    private SimpleObjectProperty<Rarity> rarityProperty;
    private StringProperty conditionProperty;
    private StringProperty categoryProperty;
    private MoneyAmountProperty valueProperty;
    private MoneyAmountProperty paidProperty;
    private SimpleObjectProperty<LocalDate> dateOfAcquisitionProperty;
    private StringProperty notesProperty;
    private StringProperty issueProperty;
    private SimpleObjectProperty<Image> frontImageProperty;
    private SimpleObjectProperty<Image> backImageProperty;


    private PokerChip pokerChip;

    public PokerChipBean() {
        this(PokerChipBean.builder().casino(new CasinoBean()));
    }

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
        rarityProperty = new SimpleObjectProperty<>(pokerChip.getRarity());
        conditionProperty = new SimpleStringProperty(pokerChip.getCondition());
        categoryProperty = new SimpleStringProperty(pokerChip.getCategory());
        notesProperty = new SimpleStringProperty(pokerChip.getNotes());
        dateOfAcquisitionProperty = new SimpleObjectProperty<>(pokerChip.getAcquisitionDate());
        issueProperty = new SimpleStringProperty(String.valueOf(pokerChip.getIssue()));
        frontImageThumbnailView = new ImageView();
        backImageThumbnailView = new ImageView();
        valueProperty = new MoneyAmountProperty(pokerChip.getAmountValue());
        paidProperty = new MoneyAmountProperty(pokerChip.getAmountPaid());
        frontImageProperty = new SimpleObjectProperty<>();
        backImageProperty = new SimpleObjectProperty<>();
        pokerChip.getFrontImage().ifPresent(frontBlobImage ->
        {
            frontImageProperty.set(getImageFromByteArray(frontBlobImage.getImage()));
            frontImageThumbnailView.setImage(getImageFromByteArray(frontBlobImage.getThumbnail()));
        });
        pokerChip.getBackImage().ifPresent(backBlobImage ->
        {
            backImageProperty.set(getImageFromByteArray(backBlobImage.getImage()));
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
        dateOfAcquisitionProperty = new SimpleObjectProperty<>(builder.acquisitionDate);

        obsoleteProperty = new SimpleBooleanProperty();
        cancelledProperty = new SimpleBooleanProperty();
        conditionProperty = new SimpleStringProperty();
        categoryProperty = new SimpleStringProperty();
        rarityProperty = new SimpleObjectProperty<>();
        valueProperty = new MoneyAmountProperty();
        paidProperty = new MoneyAmountProperty();
        issueProperty = new SimpleStringProperty();
        notesProperty = new SimpleStringProperty();
        frontImageProperty = new SimpleObjectProperty<>();
        backImageProperty = new SimpleObjectProperty<>();
        frontImageThumbnailView = new ImageView();
        backImageThumbnailView = new ImageView();
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
        addPropertyListener(valueProperty, pokerChip::setAmountValue);
        addPropertyListener(paidProperty, pokerChip::setAmountPaid);
        addPropertyListener(issueProperty, pokerChip::setIssue);
        addPropertyListener(notesProperty, pokerChip::setNotes);
        addPropertyListener(dateOfAcquisitionProperty, pokerChip::setAcquisitionDate);

        addPropertyListener(frontImageProperty, image ->
                pokerChip.setFrontImage(getBlobImageFromImageAndHandleException(image))
        );

        addPropertyListener(backImageProperty, image ->
                pokerChip.setBackImage(getBlobImageFromImageAndHandleException(image))
        );
    }

    private BlobImage getBlobImageFromImageAndHandleException(Image image) {
        try {
            return ImageConverter.getBlobImageFromImage(image);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private <T> void addPropertyListener(Property<T> property, Consumer<T> propertySetter) {
        property.addListener((observable, oldValue, newValue) -> {
            if (isValueChanged(oldValue, newValue)) {
                propertySetter.accept(newValue);
            }
        });
    }

    private <T> boolean isValueChanged(T oldValue, T newValue) {
        return !Objects.equals(oldValue, newValue);
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

    public StringProperty tcrIdProperty() {
        return tcrIdProperty;
    }

    public StringProperty yearProperty() {
        return yearProperty;
    }

    public SimpleObjectProperty<Image> getFrontImageProperty() {
        return frontImageProperty;
    }

    public SimpleObjectProperty<Image> getBackImageProperty() {
        return backImageProperty;
    }

    public BooleanProperty cancelledProperty() {
        return cancelledProperty;
    }

    public BooleanProperty obsoleteProperty() {
        return obsoleteProperty;
    }

    public SimpleObjectProperty<Rarity> rarityProperty() {
        return rarityProperty;
    }

    public void setImages(List<byte[]> pictures) {
        requireNonNull(pictures);
        if (!pictures.isEmpty()) {
            final byte[] frontImageByteArray;
            final byte[] backImageByteArray;
            if (pictures.size() >= 2) {
                frontImageByteArray = pictures.get(0);
                backImageByteArray = pictures.get(1);
            } else {
                backImageByteArray = pictures.get(0);
                frontImageByteArray = pictures.get(0);
            }

            frontImageProperty.set(getImageFromByteArray(frontImageByteArray));
            backImageProperty.set(getImageFromByteArray(backImageByteArray));
            setImageThumbnailIfNotNull(frontImageByteArray, frontImageThumbnailView);
            setImageThumbnailIfNotNull(backImageByteArray, backImageThumbnailView);

        }
    }

    private void setImageThumbnailIfNotNull(byte[] imageByteArray, ImageView imageThumbnailView) {
        if (nonNull(imageByteArray)) {
            imageThumbnailView.setImage(getImageFromByteArray(imageByteArray, 90, 90));
        }
    }

    public static PokerChipBeanBuilder builder() {
        return new PokerChipBeanBuilder();
    }

    public CasinoBean getCasinoBean() {
        return casinoBean;
    }

    @FXML
    public String getCasinoName() {
        return casinoBean.getName();
    }

    public StringProperty conditionProperty() {
        return conditionProperty;
    }

    public StringProperty categoryProperty() {
        return categoryProperty;
    }

    public MoneyAmountProperty valueProperty() {
        return valueProperty;
    }

    public MoneyAmountProperty paidProperty() {
        return paidProperty;
    }

    public SimpleObjectProperty<LocalDate> dateOfAcquisitionProperty() {
        return dateOfAcquisitionProperty;
    }

    public StringProperty notesProperty() {
        return notesProperty;
    }

    public StringProperty issueProperty() {
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
        private LocalDate acquisitionDate = LocalDate.now();

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

        public PokerChipBean build() {
            return new PokerChipBean(this);
        }
    }
}
