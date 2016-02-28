package com.chipcollector.domain;

import com.chipcollector.util.MessagesHelper;
import org.junit.Test;

import static com.chipcollector.domain.Casino.UNKNOWN_COUNTRY_KEY;
import static org.assertj.core.api.Assertions.assertThat;

public class CasinoTest {

    @Test
    public void getLocationReturnOptionalWithLocationFieldWhenPresent() throws Exception {
        final Location location = Location.builder().city("city").build();
        final Casino casino = Casino.builder().location(location).build();
        assertThat(casino.getLocation())
                .isPresent()
                .contains(location);
    }

    @Test
    public void getLocationReturnOptionalEmptyWhenLocationFieldIfMissing() throws Exception {
        final Casino casino = Casino.builder().build();
        assertThat(casino.getLocation()).isEmpty();
    }

    @Test
    public void getCountryNameReturnsUnknownCountryNameWhenNotPresent() throws Exception {
        final Location location = Location.builder().city("city").build();
        final Casino casino = Casino.builder().location(location).build();

        assertThat(casino.getCountryName()).isEqualTo(MessagesHelper.getString(UNKNOWN_COUNTRY_KEY));
    }

    @Test
    public void getCountryNameReturnsLocationCountryNameWhenPresent() throws Exception {
        final String expectedCountryName = "country name";
        Country country = Country.builder().name(expectedCountryName).build();
        final Location location = Location.builder().country(country).city("city").build();
        final Casino casino = Casino.builder().location(location).build();

        assertThat(casino.getCountryName()).isEqualTo(expectedCountryName);
    }
}