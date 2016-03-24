package com.chipcollector.model.dashboard;

import com.chipcollector.domain.MoneyAmount;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.domain.Rarity;
import com.chipcollector.model.core.MoneyAmountProperty;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.chipcollector.util.ImageConverter.getImageFromByteArray;
import static java.util.Objects.*;

@Slf4j
@ToString
public class PokerChipBean {

    private CasinoBean casinoBean;
    private StringProperty cityNameProperty;
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
    private MoneyAmountProperty amountValueProperty;
    private MoneyAmountProperty amountPaidProperty;
    private SimpleObjectProperty<LocalDate> dateOfAcquisitionProperty;
    private StringProperty notesProperty;
    private StringProperty issueProperty;
    private SimpleObjectProperty<Image> frontImageProperty;
    private SimpleObjectProperty<Image> backImageProperty;

    private PokerChip pokerChip;
    private boolean dirty;
    private boolean frontImageChanged;
    private boolean backImageChanged;

    private PokerChipBean(CasinoBean casinoBean) {
        cityNameProperty = initialiseProperty(new SimpleStringProperty());
        colorProperty = initialiseProperty(new SimpleStringProperty());
        denomProperty = initialiseProperty(new SimpleStringProperty());
        inlayProperty = initialiseProperty(new SimpleStringProperty());
        insertsProperty = initialiseProperty(new SimpleStringProperty());
        moldProperty = initialiseProperty(new SimpleStringProperty());
        tcrIdProperty = initialiseProperty(new SimpleStringProperty());
        yearProperty = initialiseProperty(new SimpleStringProperty());
        dateOfAcquisitionProperty = initialiseProperty(new SimpleObjectProperty<>());
        obsoleteProperty = initialiseProperty(new SimpleBooleanProperty());
        cancelledProperty = initialiseProperty(new SimpleBooleanProperty());
        conditionProperty = initialiseProperty(new SimpleStringProperty());
        categoryProperty = initialiseProperty(new SimpleStringProperty());
        rarityProperty = initialiseProperty(new SimpleObjectProperty<>());
        amountValueProperty = initialiseProperty(new MoneyAmountProperty());
        amountPaidProperty = initialiseProperty(new MoneyAmountProperty());
        issueProperty = initialiseProperty(new SimpleStringProperty());
        notesProperty = initialiseProperty(new SimpleStringProperty());
        frontImageProperty = initialiseProperty(new SimpleObjectProperty<>());
        backImageProperty = initialiseProperty(new SimpleObjectProperty<>());
        frontImageThumbnailView = new ImageView();
        backImageThumbnailView = new ImageView();

        this.casinoBean = casinoBean;
    }

    public PokerChipBean() {
        this(new CasinoBean());
    }

    public PokerChipBean(PokerChip pokerChip) {
        this(new CasinoBean(pokerChip.getCasino()));

        this.pokerChip = pokerChip;

        cityNameProperty.set(casinoBean.getCity());
        denomProperty.set(pokerChip.getDenom());
        moldProperty.set(pokerChip.getMold());
        colorProperty.set(pokerChip.getColor());
        inlayProperty.set(pokerChip.getInlay());
        insertsProperty.set(pokerChip.getInserts());
        yearProperty.set(pokerChip.getYear());
        tcrIdProperty.set(pokerChip.getTcrID());
        obsoleteProperty.set(pokerChip.isObsolete());
        cancelledProperty.set(pokerChip.isCancelled());
        rarityProperty.set(pokerChip.getRarity());
        conditionProperty.set(pokerChip.getCondition());
        categoryProperty.set(pokerChip.getCategory());
        notesProperty.set(pokerChip.getNotes());
        dateOfAcquisitionProperty.set(pokerChip.getAcquisitionDate());
        issueProperty.set(String.valueOf(pokerChip.getIssue()));
        amountValueProperty.set(pokerChip.getAmountValue());
        amountPaidProperty.set(pokerChip.getAmountPaid());
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
    }

    private PokerChipBean(PokerChipBeanBuilder builder) {
        this(builder.casinoBean);
        cityNameProperty.set(casinoBean.getCity());
        colorProperty.set(builder.color);
        denomProperty.set(builder.denom);
        inlayProperty.set(builder.inlay);
        insertsProperty.set(builder.inserts);
        moldProperty.set(builder.mold);
        tcrIdProperty.set(builder.tcrId);
        yearProperty.set(builder.year);
        dateOfAcquisitionProperty.set(builder.acquisitionDate);
    }

    public void updateFromOther(PokerChipBean other) {
        updateProperty(cancelledProperty, other.cancelledProperty);
        updateProperty(categoryProperty, other.categoryProperty);
        updateProperty(colorProperty, other.colorProperty);
        updateProperty(denomProperty, other.denomProperty);
        updateProperty(inlayProperty, other.inlayProperty);
        updateProperty(insertsProperty, other.insertsProperty);
        updateProperty(moldProperty, other.moldProperty);
        updateProperty(yearProperty, other.yearProperty);
        updateProperty(tcrIdProperty, other.tcrIdProperty);
        updateProperty(obsoleteProperty, other.obsoleteProperty);
        updateProperty(conditionProperty, other.conditionProperty);
        updateProperty(rarityProperty, other.rarityProperty);
        updateProperty(amountValueProperty, other.amountValueProperty);
        updateProperty(amountPaidProperty, other.amountPaidProperty);
        updateProperty(issueProperty, other.issueProperty);
        updateProperty(notesProperty, other.notesProperty);
        updateProperty(dateOfAcquisitionProperty, other.dateOfAcquisitionProperty);
        updateProperty(frontImageProperty, other.frontImageProperty);
        updateProperty(backImageProperty, other.backImageProperty);
        updateProperty(cityNameProperty, other.cityNameProperty);
        pokerChip = other.pokerChip;
        casinoBean = other.casinoBean;
    }

    private <T extends Property<Q>, Q> T initialiseProperty(T property) {
        property.addListener((observable, oldValue, newValue) -> dirty |= isValueChanged(oldValue, newValue));
        return property;
    }

    private <T> void updateProperty(Property<T> property, Property<T> other) {
        property.setValue(other.getValue());
    }

    private <T> boolean isValueChanged(T oldValue, T newValue) {
        return !Objects.equals(oldValue, newValue);
    }

    public void setColor(String value) {
        colorProperty.setValue(value);
    }

    public String getColor() {
        return colorProperty.getValue();
    }

    public StringProperty colorProperty() {
        return colorProperty;
    }

    public void setDenom(String value) {
        denomProperty.setValue(value);
    }

    public String getDenom() {
        return denomProperty.getValue();
    }

    public StringProperty denomProperty() {
        return denomProperty;
    }

    public void setInlay(String value) {
        inlayProperty.setValue(value);
    }

    public String getInlay() {
        return inlayProperty.getValue();
    }

    public StringProperty inlayProperty() {
        return inlayProperty;
    }

    public void setInserts(String value) {
        insertsProperty.setValue(value);
    }

    public String getInserts() {
        return insertsProperty.getValue();
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

    public void setMold(String value) {
        moldProperty.setValue(value);
    }

    public String getMold() {
        return moldProperty.getValue();
    }

    public StringProperty moldProperty() {
        return moldProperty;
    }

    public void setTcrId(String value) {
        tcrIdProperty.setValue(value);
    }

    public String getTcrId() {
        return tcrIdProperty.getValue();
    }

    public StringProperty tcrIdProperty() {
        return tcrIdProperty;
    }

    public void setYear(String value) {
        yearProperty.setValue(value);
    }

    public String getYear() {
        return yearProperty.getValue();
    }

    public StringProperty yearProperty() {
        return yearProperty;
    }

    public void setFrontImage(Image value) {
        frontImageChanged = true;
        frontImageProperty.setValue(value);
    }

    public Image getFrontImage() {
        return frontImageProperty.getValue();
    }

    public SimpleObjectProperty<Image> frontImageProperty() {
        return frontImageProperty;
    }

    public void setBackImage(Image value) {
        backImageChanged = true;
        backImageProperty.setValue(value);
    }

    public Image getBackImage() {
        return backImageProperty.getValue();
    }

    public SimpleObjectProperty<Image> backImageProperty() {
        return backImageProperty;
    }

    public void setCancelled(boolean value) {
        cancelledProperty.setValue(value);
    }

    public boolean isCancelled() {
        return cancelledProperty.getValue();
    }

    public BooleanProperty cancelledProperty() {
        return cancelledProperty;
    }

    public void setObsolete(boolean value) {
        obsoleteProperty.setValue(value);
    }

    public boolean isObsolete() {
        return obsoleteProperty.getValue();
    }

    public BooleanProperty obsoleteProperty() {
        return obsoleteProperty;
    }

    public void setRarity(Rarity value) {
        rarityProperty.setValue(value);
    }

    public Rarity getRarity() {
        return rarityProperty.getValue();
    }

    public SimpleObjectProperty<Rarity> rarityProperty() {
        return rarityProperty;
    }

    public void setCondition(String value) {
        conditionProperty.setValue(value);
    }

    public String getCondition() {
        return conditionProperty.getValue();
    }

    public StringProperty conditionProperty() {
        return conditionProperty;
    }

    public void setCategory(String value) {
        categoryProperty.setValue(value);
    }

    public String getCategory() {
        return categoryProperty.getValue();
    }

    public StringProperty categoryProperty() {
        return categoryProperty;
    }

    public void setAmountValue(MoneyAmount value) {
        amountValueProperty.setValue(value);
    }

    public MoneyAmount getValue() {
        return amountValueProperty.getValue();
    }

    public MoneyAmountProperty amountValueProperty() {
        return amountValueProperty;
    }

    public void setAmountPaid(MoneyAmount value) {
        amountPaidProperty.setValue(value);
    }

    public MoneyAmount getAmountPaid() {
        return amountPaidProperty.getValue();
    }

    public MoneyAmountProperty amountPaidProperty() {
        return amountPaidProperty;
    }

    public void setDateOfAcquisition(LocalDate value) {
        dateOfAcquisitionProperty.setValue(value);
    }

    public LocalDate getDateOfAcquisition() {
        return dateOfAcquisitionProperty.getValue();
    }

    public SimpleObjectProperty<LocalDate> dateOfAcquisitionProperty() {
        return dateOfAcquisitionProperty;
    }

    public void setNotes(String value) {
        notesProperty.setValue(value);
    }

    public String getNotes() {
        return notesProperty.getValue();
    }

    public StringProperty notesProperty() {
        return notesProperty;
    }

    public void setIssue(String value) {
        issueProperty.setValue(value);
    }

    public String getIssue() {
        return issueProperty.getValue();
    }

    public StringProperty issueProperty() {
        return issueProperty;
    }

    public PokerChip getPokerChip() {
        return pokerChip;
    }

    public void setImages(List<byte[]> pictures) { //TODO? refactor!
        requireNonNull(pictures);
        if (!pictures.isEmpty()) {
            final byte[] frontImageByteArray;
            final byte[] backImageByteArray;
            if (pictures.size() >= 2) {
                frontImageByteArray = pictures.get(0);
                backImageByteArray = pictures.get(1);
                frontImageProperty.set(getImageFromByteArray(frontImageByteArray));
                backImageProperty.set(getImageFromByteArray(backImageByteArray));
                setImageThumbnailIfNotNull(frontImageByteArray, frontImageThumbnailView);
                setImageThumbnailIfNotNull(backImageByteArray, backImageThumbnailView);
            } else {
                frontImageByteArray = pictures.get(0);
                frontImageProperty.set(getImageFromByteArray(frontImageByteArray));
                backImageProperty.set(frontImageProperty.get());
                setImageThumbnailIfNotNull(frontImageByteArray, frontImageThumbnailView);
                backImageThumbnailView.setImage(frontImageThumbnailView.getImage());
            }
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

    @NotNull
    public CasinoBean getCasinoBean() {
        return casinoBean;
    }

    @FXML
    public String getCasinoName() {
        return casinoBean.getName();
    }

    public boolean isNew() {
        return isNull(this.pokerChip);
    }

    public PokerChipBean createCopy() {
        return new PokerChipBean(getPokerChip());
    }

    public boolean isDirty() {
        return dirty;
    }

    public void resetDirty() {
        this.dirty = false;
    }

    public boolean isFrontImageChanged() {
        return frontImageChanged;
    }

    public boolean isBackImageChanged() {
        return backImageChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokerChipBean that = (PokerChipBean) o;
        return Objects.equals(casinoBean, that.casinoBean) &&
                Objects.equals(cityNameProperty.get(), that.cityNameProperty.get()) &&
                Objects.equals(denomProperty.get(), that.denomProperty.get()) &&
                Objects.equals(moldProperty.get(), that.moldProperty.get()) &&
                Objects.equals(colorProperty.get(), that.colorProperty.get()) &&
                Objects.equals(inlayProperty.get(), that.inlayProperty.get()) &&
                Objects.equals(insertsProperty.get(), that.insertsProperty.get()) &&
                Objects.equals(yearProperty.get(), that.yearProperty.get()) &&
                Objects.equals(tcrIdProperty.get(), that.tcrIdProperty.get()) &&
                Objects.equals(obsoleteProperty.get(), that.obsoleteProperty.get()) &&
                Objects.equals(cancelledProperty.get(), that.cancelledProperty.get()) &&
                Objects.equals(rarityProperty.get(), that.rarityProperty.get()) &&
                Objects.equals(conditionProperty.get(), that.conditionProperty.get()) &&
                Objects.equals(categoryProperty.get(), that.categoryProperty.get()) &&
                Objects.equals(amountValueProperty.get(), that.amountValueProperty.get()) &&
                Objects.equals(amountPaidProperty.get(), that.amountPaidProperty.get()) &&
                Objects.equals(dateOfAcquisitionProperty.get(), that.dateOfAcquisitionProperty.get()) &&
                Objects.equals(notesProperty.get(), that.notesProperty.get()) &&
                Objects.equals(issueProperty.get(), that.issueProperty.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(casinoBean, cityNameProperty.get(), denomProperty.get(), moldProperty.get(),
                colorProperty.get(), inlayProperty.get(), insertsProperty.get(), yearProperty.get(),
                tcrIdProperty.get(), obsoleteProperty.get(), cancelledProperty.get(), rarityProperty.get(),
                conditionProperty.get(), categoryProperty.get(), amountValueProperty.get(), amountPaidProperty.get(),
                dateOfAcquisitionProperty.get(), notesProperty.get(), issueProperty.get());
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
