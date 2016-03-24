package com.chipcollector.domain;

import org.junit.Test;

import static com.chipcollector.domain.Currency.*;
import static org.assertj.core.api.Assertions.assertThat;


public class CurrencyTest {

    @Test
    public void getCurrencyFromSymbolWorksCorrectly() {
        assertThat(Currency.getCurrencyFromSymbol("$")).contains(DOLLAR);
        assertThat(Currency.getCurrencyFromSymbol("£")).contains(POUND);
        assertThat(Currency.getCurrencyFromSymbol("€")).contains(EURO);
    }

    @Test
    public void getCurrencyFromCodeWorksCorrectly() {
        assertThat(Currency.getCurrencyFromCode("USD")).contains(DOLLAR);
        assertThat(Currency.getCurrencyFromCode("GBP")).contains(POUND);
        assertThat(Currency.getCurrencyFromCode("EUR")).contains(EURO);
    }

    @Test
    public void geSymbolWorksCorrectly() {
        assertThat(DOLLAR.getSymbol()).isEqualTo("$");
        assertThat(POUND.getSymbol()).isEqualTo("£");
        assertThat(EURO.getSymbol()).isEqualTo("€");
    }
}