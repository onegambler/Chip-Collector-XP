package com.chipcollector.models.dashboard;

import com.chipcollector.domain.Casino;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.time.LocalDate;

public class CasinoBean {

    private StringProperty name;
    private StringProperty city;
    private StringProperty state;
    private StringProperty country;
    private StringProperty website;
    private SimpleObjectProperty<LocalDate> openDate;
    private SimpleObjectProperty<LocalDate> closeDate;
    private Image countryFlag;

    private String infoPageUrl;

    public CasinoBean() {
        this.name = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.state = new SimpleStringProperty();
        this.country = new SimpleStringProperty();
        this.website = new SimpleStringProperty();
        this.openDate = new SimpleObjectProperty<>();
        this.closeDate = new SimpleObjectProperty<>();
    }

    public CasinoBean(Casino casino) {
        this.name = new SimpleStringProperty(casino.getName());
        this.city = new SimpleStringProperty(casino.getCity());
        this.state = new SimpleStringProperty(casino.getState());
        this.country = new SimpleStringProperty(casino.getCountryName());
        this.website = new SimpleStringProperty(casino.getWebsite());
        this.openDate = new SimpleObjectProperty<>(casino.getOpenDate());
        this.closeDate = new SimpleObjectProperty<>(casino.getCloseDate());
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
