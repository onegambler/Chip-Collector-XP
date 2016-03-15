package com.chipcollector.test.util;

import com.chipcollector.domain.*;
import com.google.common.base.Throwables;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Random;

import static com.chipcollector.domain.Currency.DOLLAR;
import static com.chipcollector.domain.Currency.EURO;
import static com.chipcollector.domain.Rarity.R_1;
import static com.google.common.io.Resources.getResource;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.nio.file.Files.readAllBytes;

public class PokerChipTestUtil {

    public static Country createTestCountry() {
        return new Country(TEST_COUNTRY_NAME);
    }

    public static Location createTestLocation() {
        return Location.builder()
                .city(TEST_CITY)
                .country(createTestCountry())
                .state(TEST_STATE)
                .build();
    }

    public static Casino createTestCasino(String casinoName) {
        return createTestCasino(casinoName, createTestLocation());
    }

    public static Casino createTestCasino(String casinoName, Location location) {
        return Casino.builder()
                .closeDate(LocalDate.now().toString())
                .location(location)
                .name(casinoName)
                .openDate(TEST_CASINO_OPEN_DATE.toString())
                .type(TEST_CASINO_TYPE)
                .website(TEST_CASINO_WEBSITE)
                .build();
    }

    public static Casino createTestCasino() {
        return createTestCasino(TEST_CASINO_NAME);
    }

    public static PokerChip.PokerChipBuilder createTestPokerChipBuilder(Casino casino, String tcrId) {

        return PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .amountPaid(TEST_POKER_CHIP_AMOUNT_PAID)
                .amountValue(TEST_POKER_CHIP_AMOUNT_VALUE)
                .backImage(createTestBlobImage())
                .frontImage(createTestBlobImage())
                .cancelled(TEST_POKER_CHIP_CANCELLED)
                .category(TEST_POKER_CHIP_CATEGORY)
                .color(TEST_POKER_CHIP_COLOR)
                .condition(TEST_POKER_CHIP_CONDITION)
                .denom(TEST_POKER_CHIP_DENOM)
                .inlay(TEST_POKER_CHIP_INLAY)
                .inserts(TEST_POKER_CHIP_INSERTS)
                .issue(TEST_POKER_CHIP_ISSUE)
                .mold(TEST_POKER_CHIP_MOLD)
                .notes(TEST_POKER_CHIP_NOTES)
                .obsolete(TEST_POKER_CHIP_OBSOLETE)
                .rarity(TEST_POKER_CHIP_RARITY)
                .year(TEST_POKER_CHIP_YEAR)
                .tcrID(tcrId)
                .casino(casino);
    }

    public static PokerChip createTestPokerChip() {
        return createTestPokerChipBuilder(createTestCasino(), String.valueOf(new Random().nextInt(100)))
                .build();
    }

    public static BlobImage createTestBlobImage() {
        BlobImage blobImage = new BlobImage();
        try {
            blobImage.setImage(readAllBytes(Paths.get(getResource(TEST_IMAGE).toURI())));
            blobImage.setThumbnail(readAllBytes(Paths.get(getResource(TEST_IMAGE).toURI())));
        } catch (Exception e) {
            Throwables.propagate(e);
        }

        return blobImage;
    }

    public static final String TEST_CITY = "Las Vegas";
    public static final String TEST_STATE = "Nevada";
    public static final String TEST_COUNTRY_NAME = "USA";

    public static final String TEST_CASINO_NAME = "Bellagio";
    public static final LocalDate TEST_CASINO_OPEN_DATE = LocalDate.now();
    public static final String TEST_CASINO_TYPE = "Land Based";
    public static final String TEST_CASINO_WEBSITE = "http://www.bellagio.com";

    public static final MoneyAmount TEST_POKER_CHIP_AMOUNT_PAID = new MoneyAmount(DOLLAR, ONE);
    public static final MoneyAmount TEST_POKER_CHIP_AMOUNT_VALUE = new MoneyAmount(EURO, TEN);
    public static final boolean TEST_POKER_CHIP_CANCELLED = true;
    public static final String TEST_POKER_CHIP_CATEGORY = "category";

    public static final String TEST_POKER_CHIP_COLOR = "BLUE";
    public static final String TEST_POKER_CHIP_CONDITION = "New";
    public static final String TEST_POKER_CHIP_DENOM = "$ 1,00";
    public static final String TEST_POKER_CHIP_INLAY = "inlay";
    public static final String TEST_POKER_CHIP_INSERTS = "inserts";
    public static final String TEST_POKER_CHIP_ISSUE = "issue";
    public static final String TEST_POKER_CHIP_MOLD = "mold";
    public static final String TEST_POKER_CHIP_NOTES = "notes";
    public static final boolean TEST_POKER_CHIP_OBSOLETE = true;
    public static final Rarity TEST_POKER_CHIP_RARITY = R_1;
    public static final String TEST_POKER_CHIP_YEAR = "1900";

    public static final String TEST_IMAGE = "images/java_logo.png";
}
