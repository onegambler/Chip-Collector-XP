package com.chipcollector.scraper.themogh;

import com.chipcollector.models.dashboard.CasinoBean;
import com.google.common.collect.ImmutableSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class TheMoghCasinoScraper {

    public List<CasinoBean> searchItems(String query) throws IOException {
        String nameString, cityString, stateString, websiteString, countryString, detailsPageUrl;

        Document doc = Jsoup.connect(SEARCH_URL)
                .data(POST_PARAM_KEYWORD, query)
                .data(POST_PARAM_SEARCH, "Search")
                .userAgent(POST_PARAM_USER_AGENT)
                .timeout(TIMEOUT)
                .post();

        Elements casinoHtmlTableRows = doc.select(CASINO_LIST_QUERY);

        List<CasinoBean> queryResult = new ArrayList<>();
        for (Element element : casinoHtmlTableRows) {
            countryString = CasinoElement.COUNTRY.getTextFrom(element);
            nameString = CasinoElement.NAME.getTextFrom(element);
            detailsPageUrl = CasinoElement.NAME.getAttributeTextFrom(element, HREF_ATTRIBUTE_KEY);
            cityString = CasinoElement.CITY.getTextFrom(element);
            websiteString = CasinoElement.WEBSITE.getAttributeTextFrom(element, HREF_ATTRIBUTE_KEY);

            if (isUsaState(countryString)) {
                stateString = countryString;
                countryString = USA_COUNTRY;
            } else {
                stateString = null;
            }

            TheMoghCasino casino = TheMoghCasino.build()
                    .name(nameString)
                    .detailPageUrl(detailsPageUrl)
                    .city(cityString)
                    .country(countryString)
                    .state(stateString)
                    .websiteUrl(websiteString)
                    .build();

            queryResult.add(casino);
        }
        return queryResult;
    }

    private boolean isUsaState(String state) {
        return USA_STATES.contains(state);
    }

    private enum CasinoElement {
        COUNTRY(0),
        NAME(1),
        CITY(2),
        WEBSITE(3);

        private int index;

        CasinoElement(int index) {
            this.index = index;
        }

        public String getTextFrom(Element element) {
            return element.child(index).text();
        }

        public String getAttributeTextFrom(Element element, String attribute) {
            return element.child(index).getElementsByAttribute(attribute).attr(attribute);
        }
    }
    public static final String POST_PARAM_KEYWORD = "keywords";
    public static final String POST_PARAM_SEARCH = "search";
    public static final String POST_PARAM_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0";
    public static final String HREF_ATTRIBUTE_KEY = "href";
    public static final String USA_COUNTRY = "United States";

    public static final int TIMEOUT = 5000;
    public static final String SEARCH_URL = "http://www.themogh.org/cg_casino_search.php";
    public static final String CASINO_LIST_QUERY = "td.casinolist:not([align]) tr:has(td[class~=casinolist?]";

    public static final Set<String> USA_STATES = ImmutableSet.of(
            "Delaware", "Pennsylvania", "New Jersey", "Georgia", "Connecticut", "Massachusetts", "Maryland", "South Carolina", "New Hampshire", "Virginia",
            "New York", "North Carolina", "Rhode Island", "Vermont", "Kentucky", "Tennessee", "Ohio", "Louisiana", "Indiana", "Mississippi", "Illinois",
            "Alabama", "Maine", "Missouri", "Arkansas", "Michigan", "Florida", "Texas", "Iowa", "Wisconsin", "California", "Minnesota", "Oregon", "Kansas",
            "West Virginia", "Nevada", "Nebraska", "Colorado", "North Dakota", "South Dakota", "Montana", "Washington", "Idaho", "Wyoming", "Utah", "Oklahoma",
            "New Mexico", "Arizona", "Alaska", "Hawaii");
}
