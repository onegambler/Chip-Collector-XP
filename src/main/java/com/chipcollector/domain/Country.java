package com.chipcollector.domain;

import com.avaje.ebean.annotation.CacheStrategy;
import com.chipcollector.util.ImageConverter;
import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "COUNTRIES")
@CacheStrategy(readOnly = true, warmingQuery = "order by name")
public final class Country {

    @Id
    @Setter(NONE)
    private long id;

    @Column(length = 50)
    private String name;

    @Lob
    @Getter(NONE)
    @Column(name = "FLAG_IMAGE")
    private byte[] flagImageByteArray;

    @Transient
    private Optional<BufferedImage> flagImage;

    public Country(String name) {
        this.name = name;
    }

    public Optional<BufferedImage> getFlagImage() {

        if (flagImage == null) {
            flagImage = Optional.ofNullable(flagImageByteArray).map(bytes -> {
                try {
                    return ImageConverter.rawBytesToBufferedImage(bytes);
                } catch (IOException e) {
                    throw Throwables.propagate(e);
                }
            });
        }

        return flagImage;

    }
}
