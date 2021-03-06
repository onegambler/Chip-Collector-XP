package com.chipcollector.domain;

import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PACKAGE;

@Data
@Entity
@Table(name = "POKER_CHIP_IMAGES")
public class BlobImage {

    @Id
    @Setter(PACKAGE)
    private long id;

    @Lob
    @Basic(optional = false, fetch = LAZY)
    private byte[] image;

    @Lob
    @Basic(optional = false, fetch = EAGER)
    private byte[] thumbnail;

    public BlobImage() {
    }

    BlobImage(long id) {
        this.id = id;
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
