package com.chipcollector.scraper.themogh;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.models.dashboard.PokerChipBean.PokerChipBeanBuilder;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chipcollector.util.StringUtils.toCamelCase;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.DOTALL;
import static java.util.stream.Collectors.toList;

public class TheMoghPokerChipScraper {

    private static final String IMAGE_QUERY = "td.chippics img";
    private static final String WEBSITE_ROOT = "http://www.themogh.org/";
    private static final String POKER_CHIPS_QUERY = "table.chips tr:has(td.chipinfo[valign=top])";
    private static final String BR_HTML_TAG = "\\s*<br>\\s*";

    private final Pattern DENOM_PATTERN = Pattern.compile("\\s*Denom\\s*:\\s+");
    private final Pattern ISSUED_PATTERN = Pattern.compile("\\s*Issued\\s*:\\s+");
    private final Pattern COLOR_PATTERN = Pattern.compile("\\s*Color\\s*:\\s+");
    private final Pattern TCR_PATTERN = Pattern.compile(".*TCR\\s?#\\s*");
    private final Pattern INLAY_PATTERN = Pattern.compile("\\s*Inlay\\s*:\\s+");
    private final Pattern MOLD_PATTERN = Pattern.compile("\\s*Mold\\s*:\\s+");
    private final Pattern INSERTS_PATTERN = Pattern.compile("\\s*Inserts\\s*:\\s+");
    private final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>", DOTALL);

    private static final String IMG_SRC_ATTRIBUTE = "src";
    private static final String TD_CHIPINFO = "td.chipinfo";

    private final Executor executor = Executors.newSingleThreadExecutor();

    public List<PokerChipBean> searchItems(CasinoBean casinoBean) throws IOException {
        checkArgument(casinoBean instanceof TheMoghCasino, "Passed casinoBean it's not of the expected instance {}", TheMoghCasino.class.getSimpleName());

        String detailPageUrl = WEBSITE_ROOT + ((TheMoghCasino) casinoBean).getDetailPageUrl();
        Elements pokerChipElementList = Jsoup.connect(detailPageUrl).get().select(POKER_CHIPS_QUERY);

        Elements casinoInfoElement = pokerChipElementList.get(0).select(TD_CHIPINFO);


        //TODO: add casino details

        List<Entry<PokerChipBean, List<String>>> pokerChipBeanWithPictureUrlsSet = new ArrayList<>();

        for (int i = 1; i < pokerChipElementList.size(); i++) {

            Element pokerChipElement = pokerChipElementList.get(i);
            List<String> pictureUrls = pokerChipElement.select(IMAGE_QUERY).stream()
                    .map(element -> element.attr(IMG_SRC_ATTRIBUTE))
                    .collect(toList());

            PokerChipBeanBuilder pokerChipBuilder =
                    PokerChipBean.builder()
                            .casino(casinoBean);

            for (String token : pokerChipElement.html().split(BR_HTML_TAG)) {
                if (!token.isEmpty()) {
                    String info = HTML_TAG_PATTERN.matcher(token).replaceAll("");

                    Matcher matcher = DENOM_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.denom(matcher.replaceAll(""));
                        continue;
                    }

                    matcher = ISSUED_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.year(matcher.replaceAll(""));
                        continue;
                    }

                    matcher = COLOR_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.color(convertToCamelCase(matcher));
                        continue;
                    }

                    matcher = INSERTS_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.inserts(convertToCamelCase(matcher));
                        continue;
                    }

                    matcher = MOLD_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.mold(convertToCamelCase(matcher));
                        continue;
                    }

                    matcher = INLAY_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.inlay(convertToCamelCase(matcher));
                        continue;
                    }

                    matcher = TCR_PATTERN.matcher(info);
                    if (matcher.find()) {
                        pokerChipBuilder.tcrId(matcher.replaceAll(""));
                    }
                }
            }

            pokerChipBeanWithPictureUrlsSet.add(new SimpleEntry<>(pokerChipBuilder.build(), pictureUrls));
        }

        executor.execute(() -> {
            for (Entry<PokerChipBean, List<String>> entry : pokerChipBeanWithPictureUrlsSet) {
                final List<Image> images = entry.getValue().stream().map(this::getImageFromUrl).collect(toList());
                entry.getKey().setImages(images);
            }
        });

        return pokerChipBeanWithPictureUrlsSet.stream().map(Entry::getKey).collect(toList());
    }

    private String convertToCamelCase(Matcher matcher) {
        return toCamelCase(matcher.replaceAll(""), ' ', '-');
    }

    private Image getImageFromUrl(String url) {
        requireNonNull(url, "Image url cannot be null");
        if (!url.startsWith(WEBSITE_ROOT)) {
            url = WEBSITE_ROOT + url;
        }
        //TODO: improve image resize
        return new Image(url, 90, 90, true, true);
    }
}
