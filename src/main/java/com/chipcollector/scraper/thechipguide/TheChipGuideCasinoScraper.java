package com.chipcollector.scraper.thechipguide;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Location;
import com.google.common.collect.ImmutableSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public class TheChipGuideCasinoScraper {

    public static final int TIMEOUT = 5000;
    public static final String SEARCH_URL = "http://www.themogh.org/cg_casino_search.php";

    public static final String CASINO_LIST_QUERY = "td.casinolist:not([align]) tr:has(td[class~=casinolist?]";

    public static final String USA_COUNTRY = "U.S.A.";

    public static final String POST_PARAM_KEYWORD = "keywords";
    public static final String POST_PARAM_SEARCH = "search";
    public static final String POST_PARAM_USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0";
    public static final String HREF_ATTRIBUTE_KEY = "href";


    protected List<Object> searchItems(Object query) throws Throwable {
        String name, city, state, website, country, detailsPage;

        Document doc = Jsoup.connect(SEARCH_URL)
                .data(POST_PARAM_KEYWORD, query.toString())
                .data(POST_PARAM_SEARCH, "Search")
                .userAgent(POST_PARAM_USERAGENT)
                .timeout(TIMEOUT)
                .post();

        Elements casinoHtmlTableRows = doc.select(CASINO_LIST_QUERY);

        for (Element element : casinoHtmlTableRows) {
            country = element.child(0).text();
            name = element.child(1).text();
            detailsPage = element.child(1).getElementsByAttribute(HREF_ATTRIBUTE_KEY).attr(HREF_ATTRIBUTE_KEY);
            city = element.child(2).text();
            website = ofNullable(element.child(3).getElementsByAttribute(HREF_ATTRIBUTE_KEY)).map(Elements::text).orElse("");

            if (USA_STATES.contains(country)) {
                state = country;
                country = USA_COUNTRY;
            }

            //TODO. Build location and casino
        }

        return null;
    }

    public static void main(String[] args) throws Throwable {
        new TheChipGuideCasinoScraper().searchItems("bellagio");
    }


    public static final Set<String> USA_STATES = ImmutableSet.of(
            "Delaware", "Pennsylvania", "New Jersey", "Georgia", "Connecticut", "Massachusetts", "Maryland", "South Carolina", "New Hampshire", "Virginia",
            "New York", "North Carolina", "Rhode Island", "Vermont", "Kentucky", "Tennessee", "Ohio", "Louisiana", "Indiana", "Mississippi", "Illinois",
            "Alabama", "Maine", "Missouri", "Arkansas", "Michigan", "Florida", "Texas", "Iowa", "Wisconsin", "California", "Minnesota", "Oregon", "Kansas",
            "West Virginia", "Nevada", "Nebraska", "Colorado", "North Dakota", "South Dakota", "Montana", "Washington", "Idaho", "Wyoming", "Utah", "Oklahoma",
            "New Mexico", "Arizona", "Alaska", "Hawaii");
}
