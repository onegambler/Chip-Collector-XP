package com.chipcollector.model.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

import static java.util.Objects.nonNull;

public class CasinoBean {

    private StringProperty name;
    private StringProperty city;
    private StringProperty state;
    private StringProperty countryName;
    private SimpleObjectProperty<Country> country;
    private StringProperty website;
    private StringProperty oldName;
    private StringProperty status;
    private StringProperty type;
    private StringProperty theme;

    private StringProperty openDate;
    private StringProperty closeDate;

    private boolean dirty;
    private Casino casino;

    public CasinoBean() {
        name = initialiseProperty(new SimpleStringProperty());
        city = initialiseProperty(new SimpleStringProperty());
        state = initialiseProperty(new SimpleStringProperty());
        countryName = initialiseProperty(new SimpleStringProperty());
        website = initialiseProperty(new SimpleStringProperty());
        openDate = initialiseProperty(new SimpleStringProperty());
        closeDate = initialiseProperty(new SimpleStringProperty());
        oldName = initialiseProperty(new SimpleStringProperty());
        type = initialiseProperty(new SimpleStringProperty());
        status = initialiseProperty(new SimpleStringProperty());
        theme = initialiseProperty(new SimpleStringProperty());
        country = initialiseProperty(new SimpleObjectProperty<>());
    }

    private <T extends Property<Q>, Q> T initialiseProperty(T property) {
        property.addListener((observable, oldValue, newValue) -> dirty = !Objects.equals(oldValue, newValue));
        return property;
    }

    public CasinoBean(Casino casino) {
        this();
        this.casino = casino;

        if (nonNull(casino)) {
            name.set(casino.getName());
            city.set(casino.getCity());
            state.set(casino.getState());
            countryName.set(casino.getCountryName());
            website.set(casino.getWebsite());
            openDate.set(casino.getOpenDate());
            closeDate.set(casino.getCloseDate());
            status.set(casino.getStatus());
            oldName.set(casino.getOldName());
            type.set(casino.getType());
            theme.set(casino.getTheme());
            country.set(casino.getCountry());
            dirty = false;
        }
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getCountryName() {
        if (nonNull(country.get())) {
            return country.get().getName();
        }
        return countryName.get();
    }

    public void setCountryName(String countryName) {
        this.countryName.set(countryName);
    }

    public Country getCountry() {
        return country.get();
    }

    public void setCountry(Country country) {
        this.country.set(country);
    }

    public String getWebsite() {
        return website.get();
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public String getState() {
        return state.get();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (name.isEmpty().get()) {
            stringBuilder.append("N/A");
        } else {
            stringBuilder.append(name.get());
        }

        if (!city.isEmpty().get()) {
            stringBuilder.append(" ").append("(").append(city.get()).append(")");
        }
        return stringBuilder.toString();
    }

    public void setOldName(String oldName) {
        this.oldName.setValue(oldName);
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

    public String getTheme() {
        return theme.get();
    }

    public void setTheme(String theme) {
        this.theme.set(theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CasinoBean that = (CasinoBean) o;
        return Objects.equals(name.get(), that.name.get()) &&
                Objects.equals(city.get(), that.city.get()) &&
                Objects.equals(state.get(), that.state.get()) &&
                Objects.equals(country.get(), that.country.get()) &&
                Objects.equals(website.get(), that.website.get()) &&
                Objects.equals(oldName.get(), that.oldName.get()) &&
                Objects.equals(status.get(), that.status.get()) &&
                Objects.equals(type.get(), that.type.get()) &&
                Objects.equals(openDate.get(), that.openDate.get()) &&
                Objects.equals(closeDate.get(), that.closeDate.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name.get(),
                city.get(),
                state.get(),
                country.get(),
                website.get(),
                oldName.get(),
                status.get(),
                type.get(),
                openDate.get(),
                closeDate.get());
    }

    public boolean isDirty() {
        return dirty;
    }

    public Casino getCasino() {
        return casino;
    }
}
