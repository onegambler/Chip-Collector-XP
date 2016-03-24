package com.chipcollector.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.NONE;

@Data
@Entity
@Builder
@Table(name = "LOCATIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"city", "state", "country_id"}))
public class Location {

    @Id
    @Setter(NONE)
    private long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @ManyToOne(fetch = LAZY, optional = false)
    @Column(name = "country_id")
    private Country country;

}