package com.chipcollector.domain;

import com.google.common.base.Throwables;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "POKER_CHIP_IMAGES")
public class BlobImage {

    @Id
    @Getter
    private long id;

    @Lob
    @Getter @Setter
    @Basic(optional = false, fetch = LAZY)
    private byte[] image;

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
