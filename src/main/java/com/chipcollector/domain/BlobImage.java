package com.chipcollector.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "POKER_CHIP_IMAGES")
public class BlobImage {

    @Id
    @Getter
    private long id;

    @Lob
    @Getter
    @Setter
    @Basic(optional = false, fetch = LAZY)
    @Column(nullable = false, name = "IMAGE")
    private byte[] image;

    @Lob
    @Getter
    @Setter
    @Column(nullable = false, name = "THUMBNAIL")
    private byte[] thumbnail;

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
