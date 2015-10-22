package com.chipcollector.domain;

import com.chipcollector.util.MessagesHelper;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Builder
@Table(name = "CASINOS")
public class Casino {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(name = "WEB_SITE")
    private String website;

    @Column(name = "OPEN_DATE")
    private LocalDate openDate;

    @Column(name = "CLOSE_DATE")
    private LocalDate closeDate;

    private String theme;

    @Transient
    private boolean dirty;

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

    public static String UNKNOWN_COUNTRY_KEY = "country.unknown";
}
