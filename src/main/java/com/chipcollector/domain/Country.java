package com.chipcollector.domain;

import com.avaje.ebean.annotation.CacheStrategy;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.scene.image.Image;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.net.URL;

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

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_symbol")
    private String currencySymbol;

    @Column(name = "FLAG_IMAGE")
    private String flagImageName;

    public Country(String name) {
        this.name = name;
    }

    public Image getFlagImage() {
        URL imageUrl = Resources.getResource(String.format(IMAGES_FLAGS_LOCATION, getFlagImageName()));
        try {
            return new Image(imageUrl.openStream());
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/%s";
}
