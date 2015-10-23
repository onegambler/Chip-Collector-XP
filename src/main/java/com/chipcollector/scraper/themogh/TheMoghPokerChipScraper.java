package com.chipcollector.scraper.themogh;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class TheMoghPokerChipScraper {

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

    public TheMoghPokerChipScraper(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
    }

    public List<PokerChip> searchItems(CasinoBean casinoBean) throws IOException {
        checkArgument(casinoBean instanceof TheMoghCasino, "Passed casinoBean it's not of the expected instance {}", TheMoghCasino.class.getSimpleName());

        Document doc = Jsoup.connect(WEBSITE_ROOT + ((TheMoghCasino) casinoBean).getDetailPageUrl()).get();

        List<PokerChip> pokerChipList = new ArrayList<>();

        Elements pokerChipElementList = doc.select(POKER_CHIPS_QUERY);
        for (Element pokerChipElement : pokerChipElementList) {
            Casino casino = getActualCasino(casinoBean);

            PokerChip pokerChip = PokerChip.builder()
                    .casino(casino)
                            //TODO: aggiungere altre info
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

    private Casino getActualCasino(CasinoBean casino) {
        Optional<Casino> existingCasino = pokerChipDAO.getCasinoFinder()
                .withCity(casino.getCity())
                .withName(casino.getName())
                .withState(casino.getState())
                .withCountry(casino.getCountry())
                .findSingle();

        if (!existingCasino.isPresent()) {
            Optional<Location> existingLocation = pokerChipDAO.getLocationFinder()
                    .withCity(casino.getCity())
                    .withState(casino.getState())
                    .withCountry(casino.getCountry())
                    .findSingle();
            Location location;

            if (!existingLocation.isPresent()) {
                Country country = pokerChipDAO.getCountry(casino.getCountry());
                location = Location.builder()
                        .city(casino.getCity())
                        .state(casino.getState())
                        .country(country)
                        .build();

            } else {
                location = existingLocation.get();
            }

            return Casino.builder()
                    .location(location)
                    .name(casino.getName())
                    .website(casino.getWebsite())
                    .build();
        }

        return existingCasino.get();
    }
}
