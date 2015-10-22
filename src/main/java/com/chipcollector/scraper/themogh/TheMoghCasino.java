package com.chipcollector.scraper.themogh;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TheMoghCasino {

    private String name;
    private String websiteUrl;
    private String country;
    private String state;
    private String city;

    private String detailPageUrl;
}
