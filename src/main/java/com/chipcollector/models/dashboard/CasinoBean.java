package com.chipcollector.models.dashboard;

import com.chipcollector.domain.Casino;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CasinoBean {

    private StringProperty name;
    private StringProperty city;
    private StringProperty state;
    private StringProperty country;
    private StringProperty website;
    private StringProperty oldName;
    private StringProperty status;
    private StringProperty type;

    private SimpleStringProperty openDate;
    private SimpleStringProperty closeDate;

    private String infoPageUrl;

    public CasinoBean() {
        name = new SimpleStringProperty();
        city = new SimpleStringProperty();
        state = new SimpleStringProperty();
        country = new SimpleStringProperty();
        website = new SimpleStringProperty();
        openDate = new SimpleStringProperty();
        closeDate = new SimpleStringProperty();
        oldName = new SimpleStringProperty();
        type = new SimpleStringProperty();
        status = new SimpleStringProperty();
    }

    public CasinoBean(Casino casino) {
        name = new SimpleStringProperty(casino.getName());
        city = new SimpleStringProperty(casino.getCity());
        state = new SimpleStringProperty(casino.getState());
        country = new SimpleStringProperty(casino.getCountryName());
        website = new SimpleStringProperty(casino.getWebsite());
        openDate = new SimpleStringProperty(casino.getOpenDate());
        closeDate = new SimpleStringProperty(casino.getCloseDate());
        status = new SimpleStringProperty(casino.getStatus());
        oldName = new SimpleStringProperty(casino.getOldName());
        type = new SimpleStringProperty(casino.getType());
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

    public String getState() {
        return state.get();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name.get(), city.get());
    }

    public void setOldName(String oldName) {
        this.oldName.setValue(oldName);
    }

    public String getOldName() {
        return oldName.get();
    }

    public void setOpenDate(String openDate) {
        this.openDate.setValue(openDate);
    }

    public String getOpenDate() {
        return openDate.get();
    }

    public void setClosedDate(String closedDate) {
        this.closeDate.set(closedDate);
    }

    public String getClosedDate() {
        return closeDate.get();
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public String getType() {
        return type.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }
}
