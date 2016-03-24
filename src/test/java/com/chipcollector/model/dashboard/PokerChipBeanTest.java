package com.chipcollector.model.dashboard;

import com.chipcollector.domain.PokerChip;
import javafx.beans.property.Property;
import org.assertj.core.api.StrictAssertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.chipcollector.test.util.PokerChipTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipBeanTest {

    @Test(expected = NullPointerException.class)
    public void whenCreateBeanWithNullPokerChipThenThrowException() {
        new PokerChipBean(null);
    }

    @Test
    public void pokerChipBeanGetsConstructedCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(testPokerChip);
        verifyProperty(pokerChipBean.cancelledProperty(), TEST_POKER_CHIP_CANCELLED);
        verifyProperty(pokerChipBean.categoryProperty(), TEST_POKER_CHIP_CATEGORY);
        verifyProperty(pokerChipBean.colorProperty(), TEST_POKER_CHIP_COLOR);
        verifyProperty(pokerChipBean.conditionProperty(), TEST_POKER_CHIP_CONDITION);
        verifyProperty(pokerChipBean.denomProperty(), TEST_POKER_CHIP_DENOM);
        verifyProperty(pokerChipBean.inlayProperty(), TEST_POKER_CHIP_INLAY);
        verifyProperty(pokerChipBean.insertsProperty(), TEST_POKER_CHIP_INSERTS);
        verifyProperty(pokerChipBean.issueProperty(), TEST_POKER_CHIP_ISSUE);
        verifyProperty(pokerChipBean.moldProperty(), TEST_POKER_CHIP_MOLD);
        verifyProperty(pokerChipBean.notesProperty(), TEST_POKER_CHIP_NOTES);
        verifyProperty(pokerChipBean.obsoleteProperty(), TEST_POKER_CHIP_OBSOLETE);
        verifyProperty(pokerChipBean.amountPaidProperty(), TEST_POKER_CHIP_AMOUNT_PAID);
        verifyProperty(pokerChipBean.rarityProperty(), TEST_POKER_CHIP_RARITY);
        verifyProperty(pokerChipBean.amountValueProperty(), TEST_POKER_CHIP_AMOUNT_VALUE);
        verifyProperty(pokerChipBean.yearProperty(), TEST_POKER_CHIP_YEAR);
        verifyProperty(pokerChipBean.dateOfAcquisitionProperty(), TEST_ACQUISITION_DATE);

        assertThat(pokerChipBean.getPokerChip()).isEqualTo(testPokerChip);
        assertThat(pokerChipBean.isNew()).isEqualTo(false);
        assertThat(pokerChipBean.getCasinoBean()).isEqualTo(new CasinoBean(testPokerChip.getCasino()));
        assertThat(pokerChipBean.getCasinoName()).isEqualTo(testPokerChip.getCasino().getName());
        assertThat(pokerChipBean.backImageProperty().get()).isNotNull();
        assertThat(pokerChipBean.frontImageProperty().get()).isNotNull();
        assertThat(pokerChipBean.getBackImageThumbnailView()).isNotNull();
        assertThat(pokerChipBean.getFrontImageThumbnailView()).isNotNull();
        assertThat(pokerChipBean.tcrIdProperty().get()).isNotNull();
    }

    @Test
    public void whenPropertyValueIsChangedThenUpdatePokerChipCorrespondingValue() {
        final PokerChip testPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(testPokerChip);
        verifyPropertyUpdate(pokerChipBean.categoryProperty(), testPokerChip::getCategory, TEST_POKER_CHIP_CATEGORY);
        verifyPropertyUpdate(pokerChipBean.cancelledProperty(), testPokerChip::isCancelled, TEST_POKER_CHIP_CANCELLED);
        verifyPropertyUpdate(pokerChipBean.dateOfAcquisitionProperty(), testPokerChip::getAcquisitionDate, TEST_ACQUISITION_DATE);
        verifyPropertyUpdate(pokerChipBean.colorProperty(), testPokerChip::getColor, TEST_POKER_CHIP_COLOR);
        verifyPropertyUpdate(pokerChipBean.conditionProperty(), testPokerChip::getCondition, TEST_POKER_CHIP_CONDITION);
        verifyPropertyUpdate(pokerChipBean.denomProperty(), testPokerChip::getDenom, TEST_POKER_CHIP_DENOM);
        verifyPropertyUpdate(pokerChipBean.inlayProperty(), testPokerChip::getInlay, TEST_POKER_CHIP_INLAY);
        verifyPropertyUpdate(pokerChipBean.insertsProperty(), testPokerChip::getInserts, TEST_POKER_CHIP_INSERTS);
        verifyPropertyUpdate(pokerChipBean.issueProperty(), testPokerChip::getIssue, TEST_POKER_CHIP_ISSUE);
        verifyPropertyUpdate(pokerChipBean.moldProperty(), testPokerChip::getMold, TEST_POKER_CHIP_MOLD);
        verifyPropertyUpdate(pokerChipBean.notesProperty(), testPokerChip::getNotes, TEST_POKER_CHIP_NOTES);
        verifyPropertyUpdate(pokerChipBean.obsoleteProperty(), testPokerChip::isObsolete, TEST_POKER_CHIP_OBSOLETE);
        verifyPropertyUpdate(pokerChipBean.amountPaidProperty(), testPokerChip::getAmountPaid, TEST_POKER_CHIP_AMOUNT_PAID);
        verifyPropertyUpdate(pokerChipBean.amountValueProperty(), testPokerChip::getAmountValue, TEST_POKER_CHIP_AMOUNT_VALUE);
        verifyPropertyUpdate(pokerChipBean.yearProperty(), testPokerChip::getYear, TEST_POKER_CHIP_YEAR);
        assertThat(pokerChipBean.tcrIdProperty().get()).isNotEmpty();
    }

    @Test
    public void createCopyWorksCorrectly() {
        PokerChipBean pokerChipBean = new PokerChipBean(createTestPokerChip());
        final PokerChipBean copy = pokerChipBean.createCopy();
        assertThat(copy).isEqualTo(pokerChipBean);
        assertThat(copy).isNotSameAs(pokerChipBean);
    }

    @Test
    public void updateFromOtherWorksCorrectly() {
        PokerChipBean expected = new PokerChipBean(createTestPokerChip());
        PokerChipBean bean = new PokerChipBean();
        bean.updateFromOther(expected);
        assertThat(bean).isEqualTo(expected);
        assertThat(bean).isNotSameAs(expected);
    }

    @Test
    public void whenAPropertyChangesThenDirtyGetsUpdated() {
        PokerChipBean bean = new PokerChipBean(createTestPokerChip());
        bean.resetDirty();
        assertThat(bean.isDirty()).isFalse();
        bean.inlayProperty().setValue("new Inlay");
        assertThat(bean.isDirty()).isTrue();
        bean.resetDirty();
        assertThat(bean.isDirty()).isFalse();
    }

    @Test
    public void whenASingleImageIsPassedInSetImagesThenItGetsSetToBothFrontAndBackImage() {
        byte[] image = new byte[]{'1'};
        PokerChipBean bean = new PokerChipBean(createTestPokerChip());
        bean.setImages(Arrays.asList(image));
        assertThat(bean.frontImageProperty().get()).isEqualTo(bean.backImageProperty().get());
        assertThat(bean.frontImageProperty().get()).isNotNull();
        assertThat(bean.backImageProperty().get()).isNotNull();
    }

    @Test
    public void whenTwoImagesArePassedInSetImagesThenSetOneForFrontImageAndOneForBackImage() {
        byte[] front = new byte[]{'1'};
        byte[] back = new byte[]{'2'};
        PokerChipBean bean = new PokerChipBean(createTestPokerChip());
        bean.setImages(Arrays.asList(front, back));
        assertThat(bean.frontImageProperty().get()).isNotEqualTo(bean.backImageProperty().get());
        assertThat(bean.frontImageProperty().get()).isNotNull();
        assertThat(bean.backImageProperty().get()).isNotNull();

    }

    private <T> void verifyPropertyUpdate(Property<T> property, Supplier<T> getter, T value) {
        property.setValue(value);
        StrictAssertions.assertThat(getter.get()).isEqualTo(value);
    }

    private <T> void verifyProperty(Property<T> property, T value) {
        assertThat(property.getValue()).isEqualTo(value);
    }

    private static final String NEW_VALUE_PREFIX = "new";
}