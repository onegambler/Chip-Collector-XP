package com.chipcollector.domain;

import com.chipcollector.util.ImageConverter;
import com.google.common.base.Throwables;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import static lombok.AccessLevel.NONE;

@Data
@Entity
@NoArgsConstructor
@Table(name = "COUNTRIES")
public class Country {

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

    public void setFlagImage(BufferedImage flagImage) {
        try {
            this.flagImageByteArray = ImageConverter.bufferedImageToRawBytes(flagImage, "png");
            this.flagImage = Optional.of(flagImage);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
