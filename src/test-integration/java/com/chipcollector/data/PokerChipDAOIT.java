package com.chipcollector.data;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.chipcollector.DatabaseTestUtil;
import com.chipcollector.domain.*;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.chipcollector.test.util.PokerChipTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PokerChipDAOIT {

    private static final DatabaseTestUtil DATABASE_UTIL = new DatabaseTestUtil("integration-test-ebean.properties");
    private static final EbeanServer TEST_SERVER = DATABASE_UTIL.getTestServer();

    private PokerChipDAO pokerChipDAO;

    @BeforeClass
    public static void setUpSchema() throws IOException {
        DATABASE_UTIL.createSchema();
    }

    @Before
    public void setUp() {
        pokerChipDAO = new PokerChipDAO(TEST_SERVER);
    }

    @Test
    public void getPagedPokerChipsReturnsASubSetOfPokerChips() {
        final Casino testCasino = createTestCasino();
        for (int i = 0; i < 20; i++) {
            TEST_SERVER.save(createTestPokerChip(testCasino, "tcr_" + i));
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

        final BlobImage testBlobImage = createTestBlobImage(TEST_FRONT_IMAGE);
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
        Location testLocation = createTestLocation();
        final Casino first = createTestCasino("casino", testLocation);
        TEST_SERVER.save(first);
        final Casino otherCasino = createTestCasino("other", testLocation);
        TEST_SERVER.save(otherCasino);
        final List<Casino> allCasinos = pokerChipDAO.getAllCasinos();
        assertThat(allCasinos).containsOnly(otherCasino, first);
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
        final Casino testCasino = createTestCasino();
        final PokerChip testPokerChip = createTestPokerChip(testCasino, "1");
        final PokerChip testPokerChip1 = createTestPokerChip(testCasino, "2");
        final PokerChip testPokerChip2 = createTestPokerChip(testCasino, "3");
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
        final Casino testCasino = createTestCasino();
        final PokerChip testPokerChip = createTestPokerChip(testCasino, "1");
        final PokerChip testPokerChip1 = createTestPokerChip(testCasino, "2");
        final PokerChip testPokerChip2 = createTestPokerChip(testCasino, "3");
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
        final Casino testCasino = createTestCasino();
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "1"));
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "2"));
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "3"));

        int pokerChipList = pokerChipDAO.getAllPokerChipsCount();
        assertThat(pokerChipList).isEqualTo(3);
    }

    @Test
    public void locationFinderWorksCorrectly() {
        final Location testLocation1 = createTestLocation();
        TEST_SERVER.save(testLocation1);
        final Location testLocation2 = createTestLocation();
        testLocation2.setCity("filterCity");
        testLocation2.setState("filterState");
        TEST_SERVER.save(testLocation2);
        Optional<Location> found = pokerChipDAO.getLocationFinder()
                .withCity("filterCity")
                .withCountry(TEST_COUNTRY_NAME)
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
        locationToBeFound.setState("filterState");
        Casino casinoToBeFound = createTestCasino("filterName", locationToBeFound);

        TEST_SERVER.save(casinoToBeFound);
        Optional<Casino> found = pokerChipDAO.getCasinoFinder()
                .withCity("filterCity")
                .withName("filterName")
                .withCountry(TEST_COUNTRY_NAME)
                .withState("filterState")
                .findSingle();

        assertThat(found).contains(casinoToBeFound);
    }

    @Test
    public void getDistinctValueSetWorksCorrectly() {
        final Casino testCasino = createTestCasino();
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "dist_1"));
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "dist_2"));
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "dist_3"));
        pokerChipDAO.savePokerChip(createTestPokerChip(testCasino, "dist_4"));
        pokerChipDAO.savePokerChip(createTestPokerChipBuilder(testCasino, "tcrd_5").color("GREY").build());
        final List<String> colorList = pokerChipDAO.getDistinctValueSet("color", PokerChip::getColor);

        assertThat(colorList).hasSize(2).containsOnly("GREY", "BLUE");
    }

    @After
    public void tearDown() {
        DATABASE_UTIL.cleanDatabase();
    }

    @AfterClass
    public static void deleteDatabase() {
        DATABASE_UTIL.tearDownDatabase();
    }
}