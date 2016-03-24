package com.chipcollector.domain;

import com.chipcollector.exception.ParseException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static com.chipcollector.domain.MoneyAmount.DEFAULT_CURRENCY;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;

public class MoneyAmountTest {

    @Test
    public void defaultCurrencyIsNotNull() {
        assertThat(DEFAULT_CURRENCY).isNotNull();
    }

    @Test
    public void parseReturnsCorrectMoneyAmountInstanceWithUSLocale() {
        final MoneyAmount moneyAmount = MoneyAmount.parse("£ 5,678.22");
        assertThat(moneyAmount.getAmount()).isEqualTo(new BigDecimal("5678.22"));
        assertThat(moneyAmount.getCurrency()).isEqualTo(Currency.POUND);
    }

    @Test
    public void parseWithoutCurrencyReturnsMoneyAmountWithDefaultCurrency() {

        final MoneyAmount moneyAmount = MoneyAmount.parse("5,678.22");
        assertThat(moneyAmount.getAmount()).isEqualTo(new BigDecimal("5678.22"));
        assertThat(moneyAmount.getCurrency()).isEqualTo(Currency.DOLLAR);
    }

    @Test
    public void parseWithoutCommasReturnsCorrectValue() {

        final MoneyAmount moneyAmount = MoneyAmount.parse("£ 5678.22");
        assertThat(moneyAmount.getAmount()).isEqualTo(new BigDecimal("5678.22"));
        assertThat(moneyAmount.getCurrency()).isEqualTo(Currency.POUND);
    }

    @Test
    public void parseThrowAnExceptionIfAmountCanNotBeParsed() {
        Locale.setDefault(Locale.US);
        assertThatThrownBy(() -> MoneyAmount.parse("56A78.22"))
                .isInstanceOf(ParseException.class)
                .hasMessage("Impossible to parse amount");
    }

    @Test
    public void parseThrowAnExceptionIfAmountCanNotBeParsed_2() {
        Locale.setDefault(Locale.US);
        assertThatThrownBy(() -> MoneyAmount.parse("5678.22,"))
                .isInstanceOf(ParseException.class)
                .hasMessage("Impossible to parse amount");
    }

    @Test
    public void whenTooManySeparationMarksArePresentParseAnyway() {
        Locale.setDefault(Locale.US);
        final MoneyAmount moneyAmount = MoneyAmount.parse("£ 5,,678.22");
        assertThat(moneyAmount.getAmount()).isEqualTo(new BigDecimal("5678.22"));
        assertThat(moneyAmount.getCurrency()).isEqualTo(Currency.POUND);
    }

    @Test
    public void parseThrowAnExceptionIfCurrencyCanNotBeParsed() {
        Locale.setDefault(Locale.US);
        assertThatThrownBy(() -> MoneyAmount.parse("% 5,678.22"))
                .isInstanceOf(ParseException.class)
                .hasMessage("Impossible to parse currency");
    }

    @Test
    public void toStringReturnsCorrectValue() {
        final MoneyAmount moneyAmount = MoneyAmount.parse("£ 5678.22");
        assertThat(moneyAmount.toString()).isEqualTo("£ 5,678.22");
    }
}