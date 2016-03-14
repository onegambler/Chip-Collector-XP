package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.domain.BlobImage;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.util.DatabaseUtil;
import com.google.common.io.Resources;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.chipcollector.test.util.PokerChipTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipDAOTest {

    private static EbeanServer testServer;
    private final PokerChipDAO pokerChipDAO = new PokerChipDAO(testServer);

    @BeforeClass
    public static void setUpDatabase() throws IOException {
        ServerConfig config = new ServerConfig();
        config.setName("sqlite");

        Properties properties = new Properties();
        properties.load(Resources.getResource("test-ebean.properties").openStream());

        config.loadFromProperties(properties);
        config.setDefaultServer(true);
        config.setRegister(true);

        testServer = EbeanServerFactory.create(config);

        new DatabaseUtil(testServer).tryDatabaseUpdate();
    }

    @Test
    public void getPagedPokerChipsReturnsASubSetOfPokerChips() {
        for (int i = 0; i < 20; i++) {
            testServer.save(createTestPokerChipBuilder(createTestCasino(), "tcr_" + i).build());
        }
        int pageSize = 5;
        List<PokerChip> retrieved = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            List<PokerChip> chips = pokerChipDAO.getPagedPokerChips(testServer.createQuery(PokerChip.class), i, pageSize);
            assertThat(chips).hasSize(pageSize);
            assertThat(retrieved).doesNotContain(chips.toArray(new PokerChip[chips.size()]));
            retrieved.addAll(chips);
        }
    }

    @Test
    public void savePokerChipAndSavesAllFieldsCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        pokerChipDAO.savePokerChip(testPokerChip);
        final PokerChip pokerChipList = testServer.find(PokerChip.class).findUnique();
        assertThat(pokerChipList).isEqualTo(testPokerChip);

        final Casino casino = testServer.find(Casino.class).findUnique();
        assertThat(casino).isEqualTo(testPokerChip.getCasino());
    }

    @Test
    public void updatePokerChipSavesNewFieldsCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        testServer.save(testPokerChip);
        testPokerChip.setColor("new Color");
        pokerChipDAO.updatePokerChip(testPokerChip);
        final PokerChip pokerChipList = testServer.find(PokerChip.class).findUnique();
        assertThat(pokerChipList).isEqualTo(testPokerChip);
    }

    @Test
    public void deletePokerChipDeletesCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        testServer.save(testPokerChip);

        pokerChipDAO.deletePokerChip(testPokerChip);

        PokerChip pokerChipList = testServer.find(PokerChip.class).findUnique();
        assertThat(pokerChipList).isNull();
    }

    @Test
    public void whenUpdateFrontImageOnlyThenBackImageWillMaintainItsImage() {
        final PokerChip testPokerChip = createTestPokerChip();

        pokerChipDAO.savePokerChip(testPokerChip);
        pokerChipDAO.updatePokerChip(testPokerChip);

        final BlobImage blobImage = new BlobImage();
        final byte[] image = {'3', '3', '3'};
        final byte[] thumbnail = {'2', '2', '2'};
        blobImage.setImage(image);
        blobImage.setThumbnail(thumbnail);
        testPokerChip.setFrontImage(blobImage);

        pokerChipDAO.updatePokerChip(testPokerChip);

        PokerChip result = testServer.find(PokerChip.class).findUnique();

        assertThat(result).isNotNull();

        assertThat(result.getFrontImage()).contains(blobImage);
        assertThat(result.getBackImage()).isEqualTo(testPokerChip.getBackImage());

        final List<BlobImage> images = testServer.find(BlobImage.class).findList();
        assertThat(images).hasSize(2);
    }

    @Test
    public void whenUpdateBothFrontAndBackImageThenOldImagesAreDeleted() {
        final PokerChip testPokerChip = createTestPokerChip();
        testServer.save(testPokerChip);

        BlobImage frontBlobImage = new BlobImage();
        byte[] image = {'3', '3', '3'};
        byte[] thumbnail = {'2', '2', '2'};
        frontBlobImage.setImage(image);
        frontBlobImage.setThumbnail(thumbnail);
        testPokerChip.setFrontImage(frontBlobImage);

        BlobImage backBlobImage = new BlobImage();
        image = new byte[]{'4', '4', '4'};
        thumbnail = new byte[]{'5', '5', '5'};
        backBlobImage.setImage(image);
        backBlobImage.setThumbnail(thumbnail);
        testPokerChip.setBackImage(backBlobImage);

        pokerChipDAO.updatePokerChip(testPokerChip);

        PokerChip result = testServer.find(PokerChip.class).findUnique();

        assertThat(result).isNotNull();

        assertThat(result.getFrontImage()).contains(frontBlobImage);
        assertThat(result.getBackImage()).contains(backBlobImage);

        final List<BlobImage> images = testServer.find(BlobImage.class).findList();
        assertThat(images).hasSize(2);
    }

    @Test
    public void whenFrontAndBackImageAreTheSameThenSaveOnlyOnceAndReuseValue() {
        final PokerChip testPokerChip = createTestPokerChip();
        testServer.save(testPokerChip);

        final BlobImage blobImage = new BlobImage();
        final byte[] image = {'3', '3', '3'};
        final byte[] thumbnail = {'2', '2', '2'};
        blobImage.setImage(image);
        blobImage.setThumbnail(thumbnail);
        testPokerChip.setFrontImage(blobImage);
        testPokerChip.setBackImage(blobImage);

        pokerChipDAO.updatePokerChip(testPokerChip);
        PokerChip result = testServer.find(PokerChip.class).findUnique();
        assertThat(result).isNotNull();
        assertThat(result.getFrontImage()).contains(blobImage);
        assertThat(result.getBackImage()).contains(blobImage);

        final List<BlobImage> images = testServer.find(BlobImage.class).findList();
        assertThat(images).containsExactly(blobImage);
    }

    @Test
    public void getAllCasinosReturnsCorrectValues() {
        final PokerChip testPokerChip = createTestPokerChip();
        testServer.save(testPokerChip);
        final Casino otherCasino = createTestCasino("other");
        testServer.save(otherCasino);
        final List<Casino> allCasinos = pokerChipDAO.getAllCasinos();
        assertThat(allCasinos).containsOnly(otherCasino, testPokerChip.getCasino());
    }

    @Test
    public void getCountryReturnsCorrectValue() {
        final String countryName = "Italy";
        final Optional<Country> country = pokerChipDAO.getCountry(countryName);
        assertThat(country).isPresent();
        assertThat(country.get().getName()).isEqualTo(countryName);
        assertThat(country.get().getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    public void whenCountryDoesNotExistsThenReturnEmptyOptionalValue() {
        final String countryName = "Not Valid Country";
        final Optional<Country> country = pokerChipDAO.getCountry(countryName);
        assertThat(country).isEmpty();
    }

    @After
    public void tearDown() {
        try {
            testServer.beginTransaction();
            testServer.find(BlobImage.class).findEach(testServer::deletePermanent);
            testServer.find(PokerChip.class).findEach(testServer::deletePermanent);
            testServer.find(Casino.class).findEach(testServer::deletePermanent);
            testServer.commitTransaction();
        } finally {
            testServer.endTransaction();
        }
    }

    @AfterClass
    public static void tearDownDatabase() {
        testServer.shutdown(true, true);
        final boolean delete = new File("integration-test.db").delete();
        assertThat(delete).isTrue();
    }
}