package com.chipcollector.scraper.themogh;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.models.dashboard.PokerChipBean.PokerChipBeanBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

@Component
public class TheMoghPokerChipScraper {

    private final Executor executor = Executors.newSingleThreadExecutor();

    public List<PokerChipBean> searchItems(CasinoBean casinoBean) throws IOException {
        checkArgument(casinoBean instanceof TheMoghCasino, "CasinoBean it's not of the expected instance {}", TheMoghCasino.class.getSimpleName());

        String casinoDetailsPageURL = WEBSITE_HOST + ((TheMoghCasino) casinoBean).getDetailPageUrl();

        Elements pokerChipElementList = Jsoup.connect(casinoDetailsPageURL).get().select(POKER_CHIPS_QUERY);

        Elements casinoInfoElement = pokerChipElementList.get(0).select(CASINO_INFO_QUERY);
        updateCasinoInformation(casinoBean, casinoInfoElement);

        List<Entry<PokerChipBean, List<String>>> pokerChipBeanWithPictureUrlSet = new ArrayList<>();

        for (int i = 1; i < pokerChipElementList.size(); i++) {

            Element pokerChipElement = pokerChipElementList.get(i);
            List<String> pictureUrls = pokerChipElement
                    .select(IMAGE_QUERY).stream()
                    .map(element -> element.attr(IMG_SRC_ATTRIBUTE))
                    .collect(toList());

            PokerChipBeanBuilder pokerChipBuilder =
                    PokerChipBean.builder().casino(casinoBean);

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

            pokerChipBeanWithPictureUrlSet.add(new SimpleEntry<>(pokerChipBuilder.build(), pictureUrls));
        }

        loadImagesInASeparateThread(pokerChipBeanWithPictureUrlSet);

        return pokerChipBeanWithPictureUrlSet.stream().map(Entry::getKey).collect(toList());
    }

    private void loadImagesInASeparateThread(List<Entry<PokerChipBean, List<String>>> pokerChipBeanWithPictureUrlsSet) {
        executor.execute(() -> {
            for (Entry<PokerChipBean, List<String>> entry : pokerChipBeanWithPictureUrlsSet) {
                final List<byte[]> images = entry.getValue().stream().map(this::getImageFromUrl).collect(toList());
                entry.getKey().setImages(images);
            }
        });
    }

    private void updateCasinoInformation(CasinoBean casinoBean, Elements casinoInfoElement) {

        String webSite = casinoInfoElement.select(CASINO_WEBSITE_QUERY).attr(HREF_ATTRIBUTE);
        casinoBean.setWebsite(webSite);

        for (String casinoInfoToken : casinoInfoElement.html().split(BR_HTML_TAG + "|,")) {
            if (!casinoInfoToken.isEmpty()) {
                String info = HTML_TAG_PATTERN.matcher(casinoInfoToken).replaceAll("");

                Matcher matcher = CASINO_WAS_PATTERN.matcher(info);
                if (matcher.find()) {
                    casinoBean.setOldName(matcher.replaceAll(""));
                    continue;
                }

                matcher = CASINO_OPENED_PATTERN.matcher(info);
                if (matcher.find()) {
                    final String openDate = matcher.replaceAll("");
                    casinoBean.setOpenDate(openDate);
                    continue;
                }

                matcher = CASINO_CLOSED_PATTERN.matcher(info);
                if (matcher.find()) {
                    final String closedDate = matcher.replaceAll("");
                    casinoBean.setClosedDate(closedDate);
                    continue;
                }

                matcher = CASINO_TYPE_PATTERN.matcher(info);
                if (matcher.find()) {
                    casinoBean.setType(matcher.replaceAll(""));
                }

                matcher = CASINO_STATUS_PATTERN.matcher(info);
                if (matcher.find()) {
                    casinoBean.setStatus(matcher.replaceAll(""));
                }
            }
        }
    }

    private LocalDate parseDate(String stringDate) {
        requireNonNull(stringDate, "Date cannot be null");
        if (stringDate.matches(MM_DD_YYYY_DATE_MATCHING_REGEX)) {
            return LocalDate.parse(stringDate, DATE_FORMAT);
        }

        return null;
    }

    private String convertToCamelCase(Matcher matcher) {
        return toCamelCase(matcher.replaceAll(""), ' ', '-');
    }

    private byte[] getImageFromUrl(String url) {
        requireNonNull(url, "Image url cannot be null");
        if (!url.startsWith(WEBSITE_HOST)) {
            url = WEBSITE_HOST + url;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = new URL(url).openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            return null;
        }
        return outputStream.toByteArray();
    }

    private final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>", DOTALL);

    private final Pattern DENOM_PATTERN = Pattern.compile("\\s*Denom\\s*:\\s+");
    private final Pattern ISSUED_PATTERN = Pattern.compile("\\s*Issued\\s*:\\s+");
    private final Pattern COLOR_PATTERN = Pattern.compile("\\s*Color\\s*:\\s+");
    private final Pattern TCR_PATTERN = Pattern.compile(".*TCR\\s?#\\s*");
    private final Pattern INLAY_PATTERN = Pattern.compile("\\s*Inlay\\s*:\\s+");
    private final Pattern MOLD_PATTERN = Pattern.compile("\\s*Mold\\s*:\\s+");
    private final Pattern INSERTS_PATTERN = Pattern.compile("\\s*Inserts\\s*:\\s+");

    private final Pattern CASINO_TYPE_PATTERN = Pattern.compile("\\s*Type\\s*:\\s+");
    private final Pattern CASINO_OPENED_PATTERN = Pattern.compile("\\s*Open\\s*:\\s+");
    private final Pattern CASINO_CLOSED_PATTERN = Pattern.compile("\\s*Close\\s*:\\s+");
    private final Pattern CASINO_WAS_PATTERN = Pattern.compile("\\s*Was\\s*:\\s+");
    private final Pattern CASINO_STATUS_PATTERN = Pattern.compile("\\s*Status\\s*:\\s+");

    private static final String IMAGE_QUERY = "td.chippics img";
    private static final String WEBSITE_HOST = "http://www.themogh.org/";
    private static final String POKER_CHIPS_QUERY = "table.chips tr:has(td.chipinfo[valign=top])";
    private static final String CASINO_INFO_QUERY = "td.chipinfo";
    private static final String CASINO_WEBSITE_QUERY = "a[title=Link to Website]";
    private static final String BR_HTML_TAG = "\\s*<br>\\s*";
    private static final String IMG_SRC_ATTRIBUTE = "src";
    private static final String HREF_ATTRIBUTE = "href";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final String MM_DD_YYYY_DATE_MATCHING_REGEX = "^(0?[1-9]|1[012])[-/.](0?[1-9]|[12][0-9]|3[01])[-/.]\\d{4}$";
}
