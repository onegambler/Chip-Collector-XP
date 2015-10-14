package com.chipcollector.domain;

import com.google.common.base.Throwables;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Entity
@Table(name = "IMAGES")
public class BlobImage {

    @Id
    private long id;

    @Lob
    private byte[] imageByteArray;

    @Transient
    private BufferedImage image;

    private void setImage(BufferedImage image) {
        this.image = image;
    }

    private BufferedImage getImage() {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByteArray)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
