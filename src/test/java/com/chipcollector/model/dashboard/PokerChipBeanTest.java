package com.chipcollector.model.dashboard;

import com.chipcollector.domain.MoneyAmount;
import com.chipcollector.domain.PokerChip;
import javafx.beans.property.Property;
import org.assertj.core.api.StrictAssertions;
import org.junit.Test;

import java.util.function.Supplier;

import static com.chipcollector.domain.Currency.EURO;
import static com.chipcollector.domain.Currency.POUND;
import static com.chipcollector.test.util.PokerChipTestUtil.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.MAX;
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
        verifyProperty(pokerChipBean.paidProperty(), TEST_POKER_CHIP_AMOUNT_PAID);
        verifyProperty(pokerChipBean.rarityProperty(), TEST_POKER_CHIP_RARITY);
        verifyProperty(pokerChipBean.valueProperty(), TEST_POKER_CHIP_AMOUNT_VALUE);
        verifyProperty(pokerChipBean.yearProperty(), TEST_POKER_CHIP_YEAR);
        verifyProperty(pokerChipBean.dateOfAcquisitionProperty(), TEST_ACQUISITION_DATE);

        assertThat(pokerChipBean.getPokerChip()).isEqualTo(testPokerChip);
        assertThat(pokerChipBean.isNew()).isEqualTo(false);
        assertThat(pokerChipBean.getCasinoBean()).isEqualTo(new CasinoBean(testPokerChip.getCasino()));
        assertThat(pokerChipBean.getCasinoName()).isEqualTo(testPokerChip.getCasino().getName());
        assertThat(pokerChipBean.getBackImageProperty().get()).isNotNull();
        assertThat(pokerChipBean.getFrontImageProperty().get()).isNotNull();
        assertThat(pokerChipBean.getBackImageThumbnailView()).isNotNull();
        assertThat(pokerChipBean.getFrontImageThumbnailView()).isNotNull();
        assertThat(pokerChipBean.tcrIdProperty().get()).isNotNull();
    }

    @Test
    public void whenPropertyValueIsChangedThenUpdatePokerChipCorrespondingValue() {
        final PokerChip testPokerChip = createTestPokerChip();
        PokerChipBean pokerChipBean = new PokerChipBean(testPokerChip);
        verifyPropertyUpdate(pokerChipBean.categoryProperty(), testPokerChip::getCategory, "new Value");
        verifyPropertyUpdate(pokerChipBean.cancelledProperty(), testPokerChip::isCancelled, false);
        verifyPropertyUpdate(pokerChipBean.dateOfAcquisitionProperty(), testPokerChip::getAcquisitionDate, MAX);
        verifyPropertyUpdate(pokerChipBean.colorProperty(), testPokerChip::getColor, "new Value");
        verifyPropertyUpdate(pokerChipBean.conditionProperty(), testPokerChip::getCondition, "new Value");
        verifyPropertyUpdate(pokerChipBean.denomProperty(), testPokerChip::getDenom, "new Value");
        verifyPropertyUpdate(pokerChipBean.inlayProperty(), testPokerChip::getInlay, "new Value");
        verifyPropertyUpdate(pokerChipBean.insertsProperty(), testPokerChip::getInserts, "new Value");
        verifyPropertyUpdate(pokerChipBean.issueProperty(), testPokerChip::getIssue, "new Value");
        verifyPropertyUpdate(pokerChipBean.moldProperty(), testPokerChip::getMold, "new Value");
        verifyPropertyUpdate(pokerChipBean.notesProperty(), testPokerChip::getNotes, "new Value");
        verifyPropertyUpdate(pokerChipBean.obsoleteProperty(), testPokerChip::isObsolete, true);
        verifyPropertyUpdate(pokerChipBean.paidProperty(), testPokerChip::getAmountPaid, new MoneyAmount(EURO, ONE));
        verifyPropertyUpdate(pokerChipBean.valueProperty(), testPokerChip::getAmountValue, new MoneyAmount(POUND, TEN));
        verifyPropertyUpdate(pokerChipBean.yearProperty(), testPokerChip::getYear, "new Value");
        verifyPropertyUpdate(pokerChipBean.tcrIdProperty(), testPokerChip::getTcrID, "new Value");
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