package com.chipcollector.data;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.*;
import com.chipcollector.domain.PokerChip.PokerChipBuilder;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipDAOTest {

    private static final EbeanServer defaultServer = Ebean.getDefaultServer();

    private final PokerChipDAO pokerChipDAO = new PokerChipDAO(defaultServer);

    private final Random random = new SecureRandom();

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

        PokerChip chip_1 = getTestPokerChipBuilder(casino).build();
        pokerChipDAO.savePokerChip(chip_1);

        PokerChip chip_2 = getTestPokerChipBuilder(casino).build();
        pokerChipDAO.savePokerChip(chip_2);

        List<PokerChip> allPokerChips = pokerChipDAO.getAllPokerChips();

        assertThat(allPokerChips).hasSize(2)
                .containsExactly(chip_1, chip_2);

        assertThat(allPokerChips.get(0).getCasino())
                .isEqualTo(allPokerChips.get(1).getCasino())
                .isEqualTo(casino);

        assertThat(casino.getId()).isNotNull();

        pokerChipDAO.deletePokerChip(chip_1);
        pokerChipDAO.deletePokerChip(chip_2);

        assertThat(pokerChipDAO.getAllPokerChips()).isEmpty();
    }

    @Test
    public void testSavePokerChipWithImage() throws Exception {
        BlobImage image = new BlobImage();
        image.setImage(ImageIO.read(new File(Resources.getResource("images/java_logo.png").toURI())), "png");
        PokerChip chip = getTestPokerChipBuilder()
                .frontImage(image)
                .build();

        pokerChipDAO.savePokerChip(chip);

        List<PokerChip> allPokerChips = pokerChipDAO.getAllPokerChips();
        assertThat(allPokerChips)
                .hasSize(1);

        PokerChip pokerChip = allPokerChips.stream().findFirst().get();
        assertThat(pokerChip.getFrontImage()).isPresent();
        assertThat(pokerChip.getFrontImage().get().getImage()).isNotNull();

        PokerChip anotherChip = getTestPokerChipBuilder()
                .frontImage(pokerChip.getFrontImage().get())
                .build();

        pokerChipDAO.savePokerChip(anotherChip);

        pokerChipDAO.deletePokerChip(pokerChip);

        anotherChip = pokerChipDAO.getPokerChip(anotherChip.getId());

        assertThat(anotherChip.getFrontImage()).isPresent();
        assertThat(anotherChip.getFrontImage().get().getId()).isNotNull();

        pokerChipDAO.deletePokerChip(anotherChip);


    }

    private PokerChipBuilder getTestPokerChipBuilder() {
        return getTestPokerChipBuilder(null);
    }

    private PokerChipBuilder getTestPokerChipBuilder(Casino inputCasino) {
        Casino casino = Optional.ofNullable(inputCasino)
                .orElse(Casino.builder().name("casino").build());

        return PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .amountPaid(new MoneyAmount(MoneyAmount.Currency.DOLLAR, 3d))
                .tcrID("tcr_" + random.nextInt(1000))
                .casino(casino);
    }

}