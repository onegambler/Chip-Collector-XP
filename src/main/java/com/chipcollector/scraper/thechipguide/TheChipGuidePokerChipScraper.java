package com.chipcollector.scraper.thechipguide;

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

import static java.util.Objects.requireNonNull;

public class TheChipGuidePokerChipScraper {

    private static final String WEBSITE_ROOT = "http://www.themogh.org/";
    private static final String IMG_TAG = "img";
    private static final String IMG_SRC_ATTRIBUTE = "src";
    private static final String POKER_CHIPS_QUERY = "table.chips tr:has(td:not([class~=chipheader?])";
    private static final String CASINO_CLOSED = "closed: ";
    private static final String HASH_SIGN = "#";
    private static final String TABLE_CHIPS_ATTRIBUTE = "table.chips";
    private static final String REGEX_EXP_CASINO = "<(td|b|[/]b)(.*?)>";
    private static final String CASINO_OPENED = "opened: ";
    private static final String CASINO_TYPE = "type: ";
    private static final String REGEX_EXP_CHIP = "<(.*?)>";
    private static final String LINE_BREAK_HTML_TAG = "<br />";
    private static final String TD_CHIPINFO = "td.chipinfo";

    protected List<PokerChip> searchItems(String casinoUrl) throws Throwable {

        Document doc = Jsoup.connect(WEBSITE_ROOT + casinoUrl).get();

        List<PokerChip> chips = new ArrayList<>();

        Elements pokerChipElementList = doc.select(POKER_CHIPS_QUERY);
        for(Element pokerChipElement : pokerChipElementList) {

        }


        return chips;
    }


    public static BufferedImage getImageFromUrl(String url) throws IOException {
        requireNonNull(url, "Image url cannot be null");
        if (!url.startsWith(WEBSITE_ROOT)) {
            url = WEBSITE_ROOT + url;
        }
        return ImageIO.read(new URL(url));

    }

    public static void main(String[] args) throws Throwable {
        new TheChipGuidePokerChipScraper().searchItems("cg_chip2.php?id=lkcoc1&sort=type");
    }
}
