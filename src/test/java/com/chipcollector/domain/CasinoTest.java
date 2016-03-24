package com.chipcollector.domain;

import com.chipcollector.util.MessagesHelper;
import org.junit.Test;

import static com.chipcollector.domain.Casino.UNKNOWN_COUNTRY_KEY;
import static com.chipcollector.test.util.PokerChipTestUtil.createTestLocation;
import static org.assertj.core.api.Assertions.assertThat;

public class CasinoTest {

    @Test
    public void getLocationReturnsOptionalWithLocationFieldWhenPresent() throws Exception {
        final Location location = createTestLocation();
        final Casino casino = Casino.builder().location(location).build();

        assertThat(casino.getLocation()).contains(location);
    }

    @Test
    public void getCountryReturnsCorrectlyWhenPresent() throws Exception {
        final Location location = createTestLocation();
        final Casino casino = Casino.builder().location(location).build();

        assertThat(casino.getCountry()).isEqualTo(location.getCountry());
    }

    @Test
    public void getLocationReturnsOptionalEmptyWhenLocationFieldIfMissing() throws Exception {
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

    @Test
    public void getStateReturnsCorrectValue() {
        final Location testLocation = createTestLocation();
        Casino casino = Casino.builder().location(testLocation).build();
        assertThat(casino.getState()).isEqualTo(testLocation.getState());
    }

    @Test
    public void getCityReturnsCorrectValue() {
        final Location testLocation = createTestLocation();
        Casino casino = Casino.builder().location(testLocation).build();
        assertThat(casino.getCity()).isEqualTo(testLocation.getCity());
    }
}