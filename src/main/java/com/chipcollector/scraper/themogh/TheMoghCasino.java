package com.chipcollector.scraper.themogh;

import com.chipcollector.models.dashboard.CasinoBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static java.util.Objects.requireNonNull;

public class TheMoghCasino extends CasinoBean {

    private StringProperty detailPageUrl = new SimpleStringProperty();

    public void setDetailPageUrl(String detailPageUrl) {
        this.detailPageUrl.setValue(detailPageUrl);
    }

    public String getDetailPageUrl() {
        return detailPageUrl.get();
    }

    public static TheMoghCasinoBuilder moghCasinoBuilder() {
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
            requireNonNull(name);
            requireNonNull(detailPageUrl);
            requireNonNull(websiteUrl);
            requireNonNull(city);
            requireNonNull(country);

            TheMoghCasino theMoghCasino = new TheMoghCasino();
            theMoghCasino.setName(name);
            theMoghCasino.setDetailPageUrl(detailPageUrl);
            theMoghCasino.setWebsite(websiteUrl);
            theMoghCasino.setCity(city);
            theMoghCasino.setState(state);
            theMoghCasino.setCountry(country);
            return theMoghCasino;
        }
    }
}
