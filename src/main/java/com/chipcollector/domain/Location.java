package com.chipcollector.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.NONE;

@Data
@Entity
@Builder
@Table(name = "LOCATIONS")
public class Location {

    @Id
    @Setter(NONE)
    private long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @ManyToOne(fetch = LAZY, cascade = ALL, optional = false)
    private Country country;

}