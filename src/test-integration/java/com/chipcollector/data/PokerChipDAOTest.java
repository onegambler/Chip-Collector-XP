package com.chipcollector.data;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.domain.*;
import com.chipcollector.domain.PokerChip.PokerChipBuilder;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.chipcollector.domain.Currency.DOLLAR;
import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class PokerChipDAOTest {

    private static final EbeanServer defaultServer = Ebean.getDefaultServer();

    private final PokerChipDAO pokerChipDAO = new PokerChipDAO(defaultServer);

    private int tcrIdSequence = 0;

    @Test
    public void testSavePokerChip() {
        final Country country = new Country("Name");
        final Location location = Location.builder()
                .city("city")
                .country(country)
                .state("state")
                .build();
        final Casino casino = Casino.builder()
                .closeDate(LocalDate.now().toString())
                .location(location)
                .name("name")
                .openDate(LocalDate.now().toString())
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

        allPokerChips = pokerChipDAO.getAllPokerChips();

        assertThat(allPokerChips).hasSize(1).containsOnly(chip_2);

        assertThat(allPokerChips.get(0).getCasino()).isEqualTo(casino);

        pokerChipDAO.deletePokerChip(chip_2);

        allPokerChips = pokerChipDAO.getAllPokerChips();

        assertThat(allPokerChips).isEmpty();
    }

    @Test
    public void testSavePokerChipWithImage() throws Exception {
        BlobImage image = new BlobImage();
        image.setImage(Files.readAllBytes(Paths.get(Resources.getResource("images/java_logo.png").toURI())));
        PokerChip chip = getTestPokerChipBuilder()
                .frontImage(image)
                .backImage(image)
                .build();

        pokerChipDAO.savePokerChip(chip);

        PokerChip savedPokerChip = Ebean.find(PokerChip.class).findUnique();
        assertThat(savedPokerChip).isNotNull();

        BlobImage savedImage = Ebean.find(BlobImage.class).findUnique();
        assertThat(savedImage).isEqualTo(image);

        pokerChipDAO.deletePokerChip(savedPokerChip);

        savedImage = Ebean.find(BlobImage.class).findUnique();

        assertThat(savedImage).isNull();
    }

    @Test
    public void testPokerChipUpdate() throws Exception {
        BlobImage image_1 = new BlobImage();
        image_1.setImage(Files.readAllBytes(Paths.get(Resources.getResource("images/java_logo.png").toURI())));
        PokerChip chip = getTestPokerChipBuilder()
                .frontImage(image_1)
                .backImage(image_1)
                .build();

        pokerChipDAO.savePokerChip(chip);

        BlobImage image_2 = new BlobImage();

        chip.setBackImage(image_2);

        pokerChipDAO.updatePokerChip(chip);

        List<BlobImage> savedImageList = Ebean.find(BlobImage.class).findList();
        assertThat(savedImageList).hasSize(2);

        assertThat(chip.getFrontImage()).isPresent()
                .contains(image_1);

        assertThat(chip.getBackImage()).isPresent()
                .contains(image_2);

        chip.setFrontImage(image_2);

        pokerChipDAO.updatePokerChip(chip);

        BlobImage savedImage = Ebean.find(BlobImage.class).findUnique();
        assertThat(savedImage).isNotNull();

        pokerChipDAO.deletePokerChip(chip);

        assertThat(pokerChipDAO.getAllPokerChips()).isEmpty();

        savedImage = Ebean.find(BlobImage.class).findUnique();
        assertThat(savedImage).isNull();

    }

    private PokerChipBuilder getTestPokerChipBuilder() {
        return getTestPokerChipBuilder(null);
    }

    private PokerChipBuilder getTestPokerChipBuilder(Casino inputCasino) {
        Casino casino = Optional.ofNullable(inputCasino)
                .orElse(Casino.builder().name("casino").build());

        return PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .amountPaid(new MoneyAmount(DOLLAR, ONE))
                .tcrID("tcr_" + tcrIdSequence++)
                .casino(casino);
    }

    @After
    public void afterTest() {
        try {
            defaultServer.beginTransaction();

            defaultServer.createQuery(PokerChip.class).delete();
            defaultServer.createQuery(BlobImage.class).delete();
            defaultServer.createQuery(Casino.class).delete();
            defaultServer.commitTransaction();
        } finally {
            defaultServer.endTransaction();
        }
    }

}