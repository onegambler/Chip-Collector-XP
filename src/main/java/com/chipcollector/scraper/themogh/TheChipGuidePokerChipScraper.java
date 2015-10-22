package com.chipcollector.scraper.themogh;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class TheChipGuidePokerChipScraper {

    private static final String WEBSITE_ROOT = "http://www.themogh.org/";
    private static final String IMG_TAG = "img";
    private static final String IMG_SRC_ATTRIBUTE = "src";
    private static final String POKER_CHIPS_QUERY = "table.chips tr:has(td.chipinfo[valign=top])";
    private static final String CASINO_INFO_QUERY = "table.chips tr td:has(table)";
    private static final String CASINO_CLOSED = "closed: ";
    private static final String HASH_SIGN = "#";
    private static final String TABLE_CHIPS_ATTRIBUTE = "table.chips";
    private static final String REGEX_EXP_CASINO = "<(td|b|[/]b)(.*?)>";
    private static final String CASINO_OPENED = "opened: ";
    private static final String CASINO_TYPE = "type: ";
    private static final String REGEX_EXP_CHIP = "<(.*?)>";
    private static final String LINE_BREAK_HTML_TAG = "<br />";
    private static final String TD_CHIPINFO = "td.chipinfo";

    private final PokerChipDAO pokerChipDAO;

    public TheChipGuidePokerChipScraper(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
    }

    protected List<PokerChip> searchItems(TheMoghCasino theMoghCasino) throws Throwable {

        Document doc = Jsoup.connect(WEBSITE_ROOT + theMoghCasino.getDetailPageUrl()).get();

        List<PokerChip> pokerChipList = new ArrayList<>();

        Elements pokerChipElementList = doc.select(POKER_CHIPS_QUERY);
        for (Element pokerChipElement : pokerChipElementList) {
            Casino casino = getCasino(theMoghCasino);

            PokerChip pokerChip = PokerChip.builder()
                    .casino(casino)
                    .build();

            pokerChipList.add(pokerChip);
        }

        return pokerChipList;
    }


    private BufferedImage getImageFromUrl(String url) throws IOException {
        requireNonNull(url, "Image url cannot be null");
        if (!url.startsWith(WEBSITE_ROOT)) {
            url = WEBSITE_ROOT + url;
        }
        return ImageIO.read(new URL(url));
    }

    public static void main(String[] args) throws Throwable {
        TheMoghCasino theMoghCasino = TheMoghCasino.builder().build();
        theMoghCasino.setDetailPageUrl("cg_chip2.php?id=lkcoc1&sort=type");
        new TheChipGuidePokerChipScraper(null).searchItems(theMoghCasino);
    }

    public Casino getCasino(TheMoghCasino theChipGuideCasino) {
        Optional<Casino> existingCasino = pokerChipDAO.getCasinoFinder()
                .withCity(theChipGuideCasino.getCity())
                .withName(theChipGuideCasino.getName())
                .withState(theChipGuideCasino.getState())
                .withCountry(theChipGuideCasino.getCountry())
                .findSingle();

        if (!existingCasino.isPresent()) {
            Optional<Location> existingLocation = pokerChipDAO.getLocationFinder()
                    .withCity(theChipGuideCasino.getCity())
                    .withState(theChipGuideCasino.getState())
                    .withCountry(theChipGuideCasino.getCountry())
                    .findSingle();
            Location location;

            if (!existingLocation.isPresent()) {
                Country country = pokerChipDAO.getCountry(theChipGuideCasino.getCountry());
                location = Location.builder()
                        .city(theChipGuideCasino.getCity())
                        .state(theChipGuideCasino.getState())
                        .country(country)
                        .build();

            } else {
                location = existingLocation.get();
            }

            return Casino.builder()
                    .location(location)
                    .name(theChipGuideCasino.getName())
                    .website(theChipGuideCasino.getWebsiteUrl())
                    .build();
        }

        return existingCasino.get();
    }
}
