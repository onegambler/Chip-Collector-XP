package com.chipcollector.data;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipDAOTest {

    private static final EbeanServer defaultServer = Ebean.getDefaultServer();

    private final PokerChipDAO pokerChipDAO = new PokerChipDAO(defaultServer);

    @BeforeClass
    public static void setUp() {
        Ebean.register(defaultServer, true);
    }

    @Test
    public void testSavePokerChip() {
        final Country country = new Country("Name");
        final Location location = Location.builder()
                .city("city")
                .country(country)
                .state("state")
                .build();
        final Casino casino = Casino.builder()
                .closeDate(LocalDate.now())
                .location(location)
                .name("name")
                .openDate(LocalDate.now())
                .theme("theme")
                .type("type")
                .website("website")
                .build();

        PokerChip chip_1 = PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .amountPaid(new MoneyAmount(MoneyAmount.Currency.DOLLAR, 3d))
                .tcrID("id_1")
                .casino(casino)
                .build();
        pokerChipDAO.savePokerChip(chip_1);

        PokerChip chip_2 = PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .amountPaid(new MoneyAmount(MoneyAmount.Currency.DOLLAR, 3d))
                .tcrID("id_2")
                .casino(casino)
                .build();
        pokerChipDAO.savePokerChip(chip_2);

        List<PokerChip> allPokerChips = pokerChipDAO.getAllPokerChips();

        assertThat(allPokerChips).hasSize(2)
                .containsExactly(chip_1, chip_2);

        assertThat(allPokerChips.get(0).getCasino())
                .isEqualTo(allPokerChips.get(1).getCasino())
                .isEqualTo(casino);

        assertThat(casino.getId()).isNotNull();
    }

}