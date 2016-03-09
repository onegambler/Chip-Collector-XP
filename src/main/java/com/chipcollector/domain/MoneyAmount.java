package com.chipcollector.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chipcollector.domain.MoneyAmount.Currency.DOLLAR;

@Data
@AllArgsConstructor
public class MoneyAmount {

    private Currency currency;
    private BigDecimal amount;

    public static MoneyAmount parse(String stringAmount) {

        Matcher matcher = CURRENCY_PATTERN.matcher(stringAmount);

        Currency currency = DOLLAR;
        if (matcher.find()) {
            currency = Currency.getCurrencyFromSymbol(matcher.group().trim()).orElse(DOLLAR);
        }

        matcher = NUMBER_PATTERN.matcher(stringAmount);
        BigDecimal amount = BigDecimal.ZERO;
        if (matcher.find()) {
            amount = Optional.ofNullable(matcher.group())
                    .map(s -> new BigDecimal(FORMATTER.parse(s, new ParsePosition(0)).doubleValue()))
                    .orElse(BigDecimal.ZERO);
        }

        return new MoneyAmount(currency, amount);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getCurrency().getSymbol(), FORMATTER.format(this.getAmount()));
    }

    public enum Currency {
        DOLLAR("$"),
        EURO("€"),
        POUND("£");

        private String symbol;

        Currency(String symbol) {
            this.symbol = symbol;
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
    }

    private static final DecimalFormat FORMATTER = new DecimalFormat("###,###.##");
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("(\\$|€|£)\\s*");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+((,|\\.)\\d+)*((,|\\.)\\d+)?");
}