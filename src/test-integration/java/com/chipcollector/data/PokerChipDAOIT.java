package com.chipcollector.data;

import com.avaje.ebean.Query;
import com.chipcollector.DatabaseIntegrationTest;
import com.chipcollector.domain.*;
import com.chipcollector.util.DatabaseUtil;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.chipcollector.test.util.PokerChipTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipDAOIT extends DatabaseIntegrationTest {

    private final PokerChipDAO pokerChipDAO = new PokerChipDAO(TEST_SERVER);

    @BeforeClass
    public static void setUpSchema() throws IOException {
        new DatabaseUtil(TEST_SERVER).tryDatabaseUpdate();
    }

    @Test
    public void getPagedPokerChipsReturnsASubSetOfPokerChips() {
        for (int i = 0; i < 20; i++) {
            TEST_SERVER.save(createTestPokerChipBuilder(createTestCasino(), "tcr_" + i).build());
        }
        int pageSize = 5;
        List<PokerChip> retrieved = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            List<PokerChip> chips = pokerChipDAO.getPagedPokerChips(TEST_SERVER.createQuery(PokerChip.class), i, pageSize);
            assertThat(chips).hasSize(pageSize);
            assertThat(retrieved).doesNotContain(chips.toArray(new PokerChip[chips.size()]));
            retrieved.addAll(chips);
        }
    }

    @Test
    public void savePokerChipAndSavesAllFieldsCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        pokerChipDAO.savePokerChip(testPokerChip);
        final PokerChip pokerChipList = TEST_SERVER.find(PokerChip.class).findUnique();
        assertThat(pokerChipList).isEqualTo(testPokerChip);

        final Casino casino = TEST_SERVER.find(Casino.class).findUnique();
        assertThat(casino).isEqualTo(testPokerChip.getCasino());
    }

    @Test
    public void updatePokerChipSavesNewFieldsCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);
        testPokerChip.setColor("new Color");
        pokerChipDAO.updatePokerChip(testPokerChip);
        final PokerChip pokerChipList = TEST_SERVER.find(PokerChip.class).findUnique();
        assertThat(pokerChipList).isEqualTo(testPokerChip);
    }

    @Test
    public void whenImageChangedIsFalseThenUpdatePokerChipDoesNotUpdateImages() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);

        final BlobImage testBlobImage = createTestBlobImage();
        testBlobImage.setImage(new byte[]{'9'});
        testPokerChip.setImagesChanged(false);
        testPokerChip.setFrontImage(testBlobImage);
        pokerChipDAO.updatePokerChip(testPokerChip);
        final PokerChip pokerChip = TEST_SERVER.find(PokerChip.class).findUnique();
        assertThat(pokerChip).isNotNull();
        assertThat(pokerChip.getFrontImage()).isNotEqualTo(testBlobImage);
    }

    @Test
    public void deletePokerChipDeletesCorrectly() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);

        pokerChipDAO.deletePokerChip(testPokerChip);

        PokerChip pokerChipList = TEST_SERVER.find(PokerChip.class).findUnique();
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

        PokerChip result = TEST_SERVER.find(PokerChip.class).findUnique();

        assertThat(result).isNotNull();

        assertThat(result.getFrontImage()).contains(blobImage);
        assertThat(result.getBackImage()).isEqualTo(testPokerChip.getBackImage());

        final List<BlobImage> images = TEST_SERVER.find(BlobImage.class).findList();
        assertThat(images).hasSize(2);
    }

    @Test
    public void whenUpdateBothFrontAndBackImageThenOldImagesAreDeleted() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);

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

        PokerChip result = TEST_SERVER.find(PokerChip.class).findUnique();

        assertThat(result).isNotNull();

        assertThat(result.getFrontImage()).contains(frontBlobImage);
        assertThat(result.getBackImage()).contains(backBlobImage);

        final List<BlobImage> images = TEST_SERVER.find(BlobImage.class).findList();
        assertThat(images).hasSize(2);
    }

    @Test
    public void whenFrontAndBackImageAreTheSameThenSaveOnlyOnceAndReuseValue() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);

        final BlobImage blobImage = new BlobImage();
        final byte[] image = {'3', '3', '3'};
        final byte[] thumbnail = {'2', '2', '2'};
        blobImage.setImage(image);
        blobImage.setThumbnail(thumbnail);
        testPokerChip.setFrontImage(blobImage);
        testPokerChip.setBackImage(blobImage);

        pokerChipDAO.updatePokerChip(testPokerChip);
        PokerChip result = TEST_SERVER.find(PokerChip.class).findUnique();
        assertThat(result).isNotNull();
        assertThat(result.getFrontImage()).contains(blobImage);
        assertThat(result.getBackImage()).contains(blobImage);

        final List<BlobImage> images = TEST_SERVER.find(BlobImage.class).findList();
        assertThat(images).containsExactly(blobImage);
    }

    @Test
    public void getAllCasinosReturnsCorrectValues() {
        final PokerChip testPokerChip = createTestPokerChip();
        TEST_SERVER.save(testPokerChip);
        final Casino otherCasino = createTestCasino("other");
        TEST_SERVER.save(otherCasino);
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

    @Test
    public void getPokerChipReturnsCorrectValue() {
        PokerChip pokerChip = createTestPokerChip();
        pokerChipDAO.savePokerChip(pokerChip);

        assertThat(pokerChip.getId()).isGreaterThan(0);
        Optional<PokerChip> other = pokerChipDAO.getPokerChip(pokerChip.getId());

        assertThat(other).contains(pokerChip);
    }

    @Test
    public void whenPokerChipDoesNotExistsThenGetPokerChipReturnsNull() {
        Optional<PokerChip> other = pokerChipDAO.getPokerChip(10L);
        assertThat(other).isEmpty();
    }

    @Test
    public void getPokerChipListWithQueryReturnCorrectValues() {
        final PokerChip testPokerChip = createTestPokerChipBuilder(createTestCasino(), "1").build();
        final PokerChip testPokerChip1 = createTestPokerChipBuilder(createTestCasino(), "2").build();
        final PokerChip testPokerChip2 = createTestPokerChipBuilder(createTestCasino(), "3").build();
        testPokerChip.setColor("blue");
        testPokerChip1.setColor("blue");
        pokerChipDAO.savePokerChip(testPokerChip);
        pokerChipDAO.savePokerChip(testPokerChip1);
        pokerChipDAO.savePokerChip(testPokerChip2);

        final Query<PokerChip> query = TEST_SERVER.createQuery(PokerChip.class).where("color = 'blue'");
        List<PokerChip> pokerChipList = pokerChipDAO.getPokerChipList(query);
        assertThat(pokerChipList).containsOnly(testPokerChip, testPokerChip1);
    }

    @Test
    public void getPokerChipCountWithQueryReturnCorrectValue() {
        final PokerChip testPokerChip = createTestPokerChipBuilder(createTestCasino(), "1").build();
        final PokerChip testPokerChip1 = createTestPokerChipBuilder(createTestCasino(), "2").build();
        final PokerChip testPokerChip2 = createTestPokerChipBuilder(createTestCasino(), "3").build();
        testPokerChip.setColor("blue");
        testPokerChip1.setColor("blue");
        pokerChipDAO.savePokerChip(testPokerChip);
        pokerChipDAO.savePokerChip(testPokerChip1);
        pokerChipDAO.savePokerChip(testPokerChip2);

        final Query<PokerChip> query = TEST_SERVER.createQuery(PokerChip.class).where("color = 'blue'");
        int pokerChipCount = pokerChipDAO.getPokerChipCount(query);
        assertThat(pokerChipCount).isEqualTo(2);
    }

    @Test
    public void getAllPokerChipCountReturnsCorrectCount() {
        pokerChipDAO.savePokerChip(createTestPokerChipBuilder(createTestCasino(), "1").build());
        pokerChipDAO.savePokerChip(createTestPokerChipBuilder(createTestCasino(), "2").build());
        pokerChipDAO.savePokerChip(createTestPokerChipBuilder(createTestCasino(), "3").build());

        int pokerChipList = pokerChipDAO.getAllPokerChipsCount();
        assertThat(pokerChipList).isEqualTo(3);
    }

    @Test
    public void locationFinderWorksCorrectly() {
        final Location testLocation1 = createTestLocation();
        TEST_SERVER.save(testLocation1);
        final Location testLocation2 = createTestLocation();
        testLocation2.setCity("filterCity");
        testLocation2.setCountry(new Country("filterCountry"));
        testLocation2.setState("filterState");
        TEST_SERVER.save(testLocation2);
        Optional<Location> found = pokerChipDAO.getLocationFinder()
                .withCity("filterCity")
                .withCountry("filterCountry")
                .withState("filterState")
                .findSingle();

        assertThat(found).contains(testLocation2);
    }

    @Test
    public void casinoFinderWorksCorrectly() {
        final Casino casinoNotToBeFound = createTestCasino();
        TEST_SERVER.save(casinoNotToBeFound);

        Location locationToBeFound = createTestLocation();
        locationToBeFound.setCity("filterCity");
        locationToBeFound.setCountry(new Country("filterCountry"));
        locationToBeFound.setState("filterState");
        Casino casinoToBeFound = createTestCasino("filterName", locationToBeFound);

        TEST_SERVER.save(casinoToBeFound);
        Optional<Casino> found = pokerChipDAO.getCasinoFinder()
                .withCity("filterCity")
                .withName("filterName")
                .withCountry("filterCountry")
                .withState("filterState")
                .findSingle();

        assertThat(found).contains(casinoToBeFound);
    }

    @Test
    public void getDistinctValueSetWorksCorrectly() {
        pokerChipDAO.savePokerChip(createTestPokerChip());
        pokerChipDAO.savePokerChip(createTestPokerChip());
        pokerChipDAO.savePokerChip(createTestPokerChip());
        pokerChipDAO.savePokerChip(createTestPokerChip());
        pokerChipDAO.savePokerChip(createTestPokerChipBuilder(createTestCasino(), "tcrd").color("GREY").build());
        final List<String> colorList = pokerChipDAO.getDistinctValueSet("color", PokerChip::getColor);

        assertThat(colorList).hasSize(2).containsOnly("GREY", "BLUE");


    }

    @After
    public void tearDown() {
        try {
            TEST_SERVER.beginTransaction();
            TEST_SERVER.find(BlobImage.class).findEach(TEST_SERVER::deletePermanent);
            TEST_SERVER.find(PokerChip.class).findEach(TEST_SERVER::deletePermanent);
            TEST_SERVER.find(Casino.class).findEach(TEST_SERVER::deletePermanent);
            TEST_SERVER.commitTransaction();
        } finally {
            TEST_SERVER.endTransaction();
        }
    }
}