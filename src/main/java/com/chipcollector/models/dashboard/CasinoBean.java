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
    private StringProperty type;
    private SimpleObjectProperty<LocalDate> openDate;
    private SimpleObjectProperty<LocalDate> closeDate;

    private String infoPageUrl;

    public CasinoBean() {
        name = new SimpleStringProperty();
        city = new SimpleStringProperty();
        state = new SimpleStringProperty();
        country = new SimpleStringProperty();
        website = new SimpleStringProperty();
        openDate = new SimpleObjectProperty<>();
        closeDate = new SimpleObjectProperty<>();
        oldName = new SimpleStringProperty();
        type = new SimpleStringProperty();
    }

    public CasinoBean(Casino casino) {
        name = new SimpleStringProperty(casino.getName());
        city = new SimpleStringProperty(casino.getCity());
        state = new SimpleStringProperty(casino.getState());
        country = new SimpleStringProperty(casino.getCountryName());
        website = new SimpleStringProperty(casino.getWebsite());
        openDate = new SimpleObjectProperty<>(casino.getOpenDate());
        closeDate = new SimpleObjectProperty<>(casino.getCloseDate());
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

    public StringProperty getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, city);
    }

    public void setOldName(String oldName) {
        this.oldName.setValue(oldName);
    }

    public String getOldName() {
        return oldName.get();
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate.setValue(openDate);
    }

    public String getOpenDate() {
        return Optional.ofNullable(openDate.get()).map(localDate -> localDate.format(DateTimeFormatter.BASIC_ISO_DATE)).orElse("");
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closeDate.set(closedDate);
    }

    public String getClosedDate() {
        return Optional.ofNullable(closeDate.get()).map(localDate -> localDate.format(DateTimeFormatter.BASIC_ISO_DATE)).orElse("");
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public String getType() {
        return type.get();
    }
}
