package com.chipcollector.model.dashboard;

import com.chipcollector.domain.Casino;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

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

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
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
        return String.format("%s (%s)", name.get(), city.get());
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
}
