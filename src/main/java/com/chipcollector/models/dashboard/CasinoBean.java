package com.chipcollector.models.dashboard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class CasinoBean {

    private StringProperty name;
    private StringProperty city;
    private StringProperty state;
    private StringProperty country;
    private StringProperty website;
    private StringProperty year;

    private Image countryFlag;

    private String infoPageUrl;

    public CasinoBean() {
        this.name = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.state = new SimpleStringProperty();
        this.country = new SimpleStringProperty();
        this.website = new SimpleStringProperty();
        this.year = new SimpleStringProperty();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }


    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getWebsite() {
        return website.get();
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public String getYear() {
        return year.get();
    }

    public StringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getInfoPageUrl() {
        return infoPageUrl;
    }

    public void setInfoPageUrl(String infoPageUrl) {
        this.infoPageUrl = infoPageUrl;
    }

    public Image getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(Image countryFlag) {
        this.countryFlag = countryFlag;
    }

    public StringProperty getState() {
        return state;
    }
}
