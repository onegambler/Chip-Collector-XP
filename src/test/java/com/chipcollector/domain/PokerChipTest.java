package com.chipcollector.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Currency;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipTest {

    @Test
    public void whenResetDirtyIsInvokedThenDirtyIsSetToFalse() {
        PokerChip pokerChip = PokerChip.builder().dirty(true).build();
        pokerChip.resetDirty();
        assertThat(pokerChip.isDirty()).isFalse();
    }

    @Test
    public void whenSetAcquisitionDateThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setAcquisitionDate(TEST_ACQISITION_DATE);
        assertThat(pokerChip.getAcquisitionDate()).isEqualTo(TEST_ACQISITION_DATE);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetAmountPaidThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setAmountPaid(TEST_AMOUNT_PAID);
        assertThat(pokerChip.getAmountPaid()).isEqualTo(TEST_AMOUNT_PAID);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetBackImageThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        assertThat(pokerChip.isImagesChanged()).isFalse();
        pokerChip.setBackImage(TEST_BACK_IMAGE);
        assertThat(pokerChip.getBackImage())
                .isPresent()
                .contains(TEST_BACK_IMAGE);
        assertThat(pokerChip.isDirty()).isTrue();
        assertThat(pokerChip.isImagesChanged()).isTrue();
    }

    @Test
    public void whenSetCancelledThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setCancelled(TEST_CANCELLED);
        assertThat(pokerChip.isCancelled()).isEqualTo(TEST_CANCELLED);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetCasinoThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setCasino(TEST_CASINO);
        assertThat(pokerChip.getCasino()).isEqualTo(TEST_CASINO);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetCategoryThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setCategory(TEST_CATEGORY);
        assertThat(pokerChip.getCategory()).isEqualTo(TEST_CATEGORY);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetColorThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setColor(TEST_COLOR);
        assertThat(pokerChip.getColor()).isEqualTo(TEST_COLOR);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetConditionThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setCondition(TEST_CONDITION);
        assertThat(pokerChip.getCondition()).isEqualTo(TEST_CONDITION);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetDenomThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setDenom(TEST_DENOM);
        assertThat(pokerChip.getDenom()).isEqualTo(TEST_DENOM);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetFrontImageThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setFrontImage(TEST_FRONT_IMAGE);
        assertThat(pokerChip.getFrontImage()).isPresent()
                .contains(TEST_FRONT_IMAGE);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetIdThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setId(TEST_ID);
        assertThat(pokerChip.getId()).isEqualTo(TEST_ID);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetInlayThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setInlay(TEST_INLAY);
        assertThat(pokerChip.getInlay()).isEqualTo(TEST_INLAY);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetInsertsThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setInserts(TEST_INSERTS);
        assertThat(pokerChip.getInserts()).isEqualTo(TEST_INSERTS);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetIssueThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setIssue(TEST_ISSUE);
        assertThat(pokerChip.getIssue()).isEqualTo(TEST_ISSUE);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetMoldThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setMold(TEST_MOLD);
        assertThat(pokerChip.getMold()).isEqualTo(TEST_MOLD);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetNotesThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setNotes(TEST_NOTES);
        assertThat(pokerChip.getNotes()).isEqualTo(TEST_NOTES);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetObsoleteThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setObsolete(TEST_OBSOLETE);
        assertThat(pokerChip.isObsolete()).isEqualTo(TEST_OBSOLETE);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetRarityThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setRarity(TEST_RARITY);
        assertThat(pokerChip.getRarity()).isEqualTo(TEST_RARITY);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetTcrIdThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setTcrID(TEST_TCRID);
        assertThat(pokerChip.getTcrID()).isEqualTo(TEST_TCRID);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetValueThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setValue(TEST_VALUE);
        assertThat(pokerChip.getValue()).isEqualTo(TEST_VALUE);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    @Test
    public void whenSetYearThenUpdateValueAndSetDirty() {
        PokerChip pokerChip = PokerChip.builder().build();
        assertThat(pokerChip.isDirty()).isFalse();
        pokerChip.setYear(TEST_YEAR);
        assertThat(pokerChip.getYear()).isEqualTo(TEST_YEAR);
        assertThat(pokerChip.isDirty()).isTrue();
    }

    private static final LocalDate TEST_ACQISITION_DATE = LocalDate.now();
    private static final MoneyAmount TEST_AMOUNT_PAID = new MoneyAmount(MoneyAmount.Currency.DOLLAR, ONE);
    private static final MoneyAmount TEST_VALUE = new MoneyAmount(MoneyAmount.Currency.EURO, TEN);
    private static final BlobImage TEST_FRONT_IMAGE = new BlobImage();
    private static final BlobImage TEST_BACK_IMAGE = new BlobImage();
    private static final boolean TEST_CANCELLED = false;
    private static final boolean TEST_OBSOLETE = false;
    private static final Casino TEST_CASINO = Casino.builder().build();
    private static final String TEST_CATEGORY = "category";
    private static final String TEST_COLOR = "color";
    private static final String TEST_CONDITION = "condition";
    private static final String TEST_DENOM = "denom";
    public static final long TEST_ID = 10L;
    public static final String TEST_INLAY = "inlay";
    public static final String TEST_INSERTS = "inserts";
    public static final String TEST_ISSUE = "issue";
    public static final String TEST_MOLD = "mold";
    public static final String TEST_NOTES = "notes";
    public static final String TEST_RARITY = "rarity";
    public static final String TEST_TCRID = "tcrid";
    public static final String TEST_YEAR = "1990";

    static {
        TEST_FRONT_IMAGE.setImage(new byte[0]);
        TEST_BACK_IMAGE.setImage(new byte[]{'6'});
    }
}