package com.chipcollector.domain;

import com.chipcollector.exception.ParseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chipcollector.domain.Currency.DOLLAR;

@Data
@AllArgsConstructor
@Embeddable
public class MoneyAmount {

    static final Currency DEFAULT_CURRENCY = Currency.getDefault();

    private Currency currency;
    private BigDecimal amount;

    @NotNull
    public static MoneyAmount parse(String stringAmount) {

        Matcher matcher = CURRENCY_PATTERN.matcher(stringAmount);

        Currency currency = DOLLAR;
        int currencyIdx = 0;
        if (matcher.find()) {
            currencyIdx = matcher.end();
            currency = Currency.getCurrencyFromSymbol(matcher.group().trim())
                    .orElseThrow(() -> new ParseException("Impossible to parse currency"));
        }

        ParsePosition parsePosition = new ParsePosition(0);
        final String amountString = stringAmount.substring(currencyIdx);
        BigDecimal amount = (BigDecimal) FORMATTER.parse(amountString, parsePosition);
        if (parsePosition.getIndex() < amountString.length()) {
            throw new ParseException("Impossible to parse amount");
        }

        return new MoneyAmount(currency, amount);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.getCurrency().getSymbol(), FORMATTER.format(this.getAmount()));
    }

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("^\\W\\s*");
    private static final DecimalFormat FORMATTER = new DecimalFormat("###,###.###", DecimalFormatSymbols.getInstance());
    static {
        FORMATTER.setParseBigDecimal(true);
    }
}