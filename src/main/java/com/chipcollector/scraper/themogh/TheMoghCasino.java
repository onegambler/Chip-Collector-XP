package com.chipcollector.scraper.themogh;

import com.chipcollector.model.dashboard.CasinoBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static java.util.Objects.requireNonNull;

public class TheMoghCasino extends CasinoBean {

    private StringProperty detailPageUrl = new SimpleStringProperty();

    private TheMoghCasino(TheMoghCasinoBuilder builder) {
        setName(builder.name);
        setDetailPageUrl(builder.detailPageUrl);
        setWebsite(builder.websiteUrl);
        setCity(builder.city);
        setState(builder.state);
        setCountryName(builder.country);
    }

    public void setDetailPageUrl(String detailPageUrl) {
        this.detailPageUrl.setValue(detailPageUrl);
    }

    public String getDetailPageUrl() {
        return detailPageUrl.get();
    }

    public static TheMoghCasinoBuilder build() {
        return new TheMoghCasinoBuilder();
    }

    public static class TheMoghCasinoBuilder {
        private String name;
        private String city;
        private String state;
        private String country;
        private String detailPageUrl;
        private String websiteUrl;

        public TheMoghCasinoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TheMoghCasinoBuilder city(String city) {
            this.city = city;
            return this;
        }

        public TheMoghCasinoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public TheMoghCasinoBuilder country(String country) {
            this.country = country;
            return this;
        }

        public TheMoghCasinoBuilder detailPageUrl(String detailPageUrl) {
            this.detailPageUrl = detailPageUrl;
            return this;
        }

        public TheMoghCasinoBuilder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public TheMoghCasino build() {
            TheMoghCasino theMoghCasino = new TheMoghCasino(this);
            requireNonNull(theMoghCasino.getName());
            requireNonNull(theMoghCasino.getDetailPageUrl());
            requireNonNull(theMoghCasino.getWebsite());
            requireNonNull(theMoghCasino.getCity());
            requireNonNull(theMoghCasino.getCountryName());
            return theMoghCasino;
        }
    }
}
