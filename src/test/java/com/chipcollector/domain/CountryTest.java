package com.chipcollector.domain;

import javafx.scene.image.Image;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {

    @Test
    public void getFlagImageReturnsImageCorrectly() {
        Country country = Country.builder().flagImageName("AD.png").build();
        final Image flagImage = country.getFlagImage();
        assertThat(flagImage).isNotNull();

    }

    @Test(expected = IllegalArgumentException.class)
    public void getFlagImageThrowsExceptionIfResourceIsNotPresent() {
        Country country = Country.builder().flagImageName("notThere").build();
        country.getFlagImage();
    }
}