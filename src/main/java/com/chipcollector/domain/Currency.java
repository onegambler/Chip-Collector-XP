package com.chipcollector.domain;

import com.avaje.ebean.annotation.EnumValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Currency {
    @EnumValue("EUR")EURO("€", "EUR"),
    @EnumValue("GBP")POUND("£", "GBP"),
    @EnumValue("USD")DOLLAR("$", "USD");

    private String symbol;
    private String code;

    Currency(String symbol, String code) {
        this.symbol = symbol;
        this.code = code;
    }

    public static Currency getDefault() {
        return getCurrencyFromCode(java.util.Currency.getInstance(Locale.getDefault()).getCurrencyCode())
                .orElse(DOLLAR);
    }

    public String getSymbol() {
        return symbol;
    }

    public static Optional<Currency> getCurrencyFromSymbol(String symbol) {
        return Arrays.asList(Currency.values())
                .stream()
                .filter(currency1 -> currency1.symbol.equals(symbol))
                .findFirst();
    }

    public static Optional<Currency> getCurrencyFromCode(String code) {
        return Arrays.asList(Currency.values())
                .stream()
                .filter(c -> c.code.equals(code))
                .findFirst();
    }
}
