package com.chipcollector.domain;

import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class BlobImageTest {

    @Test
    public void twoObjectsWithDifferentIdAreNotEquals() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);
        BlobImage second = new BlobImage();
        second.setImage(image);
        second.setThumbnail(image);
        second.setId(2L);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void twoObjectsWithSameIdAreEquals() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);
        BlobImage second = new BlobImage();
        second.setImage(image);
        second.setThumbnail(image);
        second.setId(1L);

        assertThat(first).isEqualTo(second);
    }

    @Test
    public void instanceIsNotEqualsToNull() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);

        assertThat(first).isNotEqualTo(null);
    }

    @Test
    public void instanceItEqualsToItself() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);

        assertThat(first).isEqualTo(first);
    }

    @Test
    public void twoObjectsWithDifferentIdDoNotHaveSameHashCode() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);
        BlobImage second = new BlobImage();
        second.setImage(image);
        second.setThumbnail(image);
        second.setId(2L);

        assertThat(first.hashCode()).isNotEqualTo(second.hashCode());
    }

    @Test
    public void twoObjectsWithSameIdHaveSameHashCode() throws Exception {
        BlobImage first = new BlobImage();
        final byte[] image = new byte[5];
        first.setImage(image);
        first.setThumbnail(image);
        first.setId(1L);
        BlobImage second = new BlobImage();
        second.setImage(image);
        second.setThumbnail(image);
        second.setId(1L);

        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

}