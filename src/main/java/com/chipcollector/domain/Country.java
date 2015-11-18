package com.chipcollector.domain;

import com.avaje.ebean.annotation.CacheStrategy;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.NONE;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COUNTRIES")
@CacheStrategy(readOnly = true, warmingQuery = "order by name")
public final class Country {

    @Id
    @Setter(NONE)
    private long id;

    @Column
    private String name;

    @Column(name = "FLAG_IMAGE")
    private String flagImageName;

    public Country(String name) {
        this.name = name;
    }
}
