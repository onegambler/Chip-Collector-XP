package com.chipcollector.domain;

import com.google.common.base.Throwables;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Entity
@Table(name = "POKER_CHIP_IMAGES")
public class BlobImage {

    @Id
    @Getter
    private long id;

    @Lob
    private byte[] imageByteArray;

    @Getter
    private int usages;

    public void setImage(BufferedImage image, String imageFormat) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, imageFormat, outputStream);
            outputStream.flush();
            imageByteArray = outputStream.toByteArray();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public BufferedImage getImage() {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageByteArray)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void decreaseUsage() {
        usages--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlobImage blobImage = (BlobImage) o;

        return id == blobImage.id;

    }
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
