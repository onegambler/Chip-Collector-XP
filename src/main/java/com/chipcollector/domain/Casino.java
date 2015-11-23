package com.chipcollector.domain;

import com.chipcollector.util.MessagesHelper;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@Entity
@Builder
@Table(name = "CASINOS")
public class Casino {

    @Id
    @Setter(NONE)
    protected int id;

    @Setter(PROTECTED)
    @Column(nullable = false)
    private String name;

    @Setter(PROTECTED)
    private String type;

    @Setter(PROTECTED)
    @Column(name = "WEBSITE")
    private String website;

    @Setter(PROTECTED)
    @Column(name = "OPEN_DATE")
    private String openDate;

    @Setter(PROTECTED)
    @Column(name = "CLOSE_DATE")
    private String closeDate;

    @Setter(PROTECTED)
    private String status;

    @Transient
    @Setter(NONE)
    private boolean dirty;

    @Setter(PROTECTED)
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Location location;

    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public String getCountryName() {
        return getLocation()
                .map(Location::getCountry)
                .map(Country::getName)
                .orElse(MessagesHelper.getString(UNKNOWN_COUNTRY_KEY));
    }

    public String getState() {
        return getLocation().map(Location::getState).orElse("-");
    }


    public String getCity() {
        return getLocation().map(Location::getCity).orElse("-");
    }

    public String toString() {
        return name;
    }

    public static String UNKNOWN_COUNTRY_KEY = "domain.country.unknown";
}
