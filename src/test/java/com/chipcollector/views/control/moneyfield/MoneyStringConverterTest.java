package com.chipcollector.views.control.moneyfield;

import com.chipcollector.domain.MoneyAmount;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static com.chipcollector.domain.Currency.DOLLAR;
import static com.chipcollector.domain.Currency.POUND;
import static org.assertj.core.api.Assertions.assertThat;

public class MoneyStringConverterTest {

    private MoneyStringConverter converter = new MoneyStringConverter();

    @BeforeClass
    public void setUp() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void fromStringReturnsCorrectMoneyAmountObject() {
        MoneyAmount moneyAmount = converter.fromString("$ 100,000.56");
        assertThat(moneyAmount.getAmount()).isEqualByComparingTo("100000.56");
        assertThat(moneyAmount.getCurrency()).isEqualTo(DOLLAR);
    }

    @Test
    public void whenStringIsEmptyThenReturnsNull() {
        MoneyAmount moneyAmount = converter.fromString(null);
        assertThat(moneyAmount).isNull();
    }

    @Test
    public void toStringReturnsCorrectMoneyAmountAsString() {
        MoneyAmount moneyAmount = new MoneyAmount(POUND, new BigDecimal("567890.22"));
        final String result = converter.toString(moneyAmount);
        assertThat(result).isEqualTo("£ 567,890.22");
    }

    @Test
    public void whenMoneyAmountIsNullThenReturnsEmptyString() {
        final String result = converter.toString(null);
        assertThat(result).isEmpty();
    }

}