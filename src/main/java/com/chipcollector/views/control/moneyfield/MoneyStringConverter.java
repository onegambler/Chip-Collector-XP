package com.chipcollector.views.control.moneyfield;

import com.chipcollector.domain.MoneyAmount;
import com.google.common.base.Strings;
import javafx.util.StringConverter;

import static java.util.Objects.isNull;

public class MoneyStringConverter extends StringConverter<MoneyAmount> {

    @Override
    public String toString(MoneyAmount moneyAmount) {
        if (isNull(moneyAmount)) {
            return "";
        }

        return moneyAmount.toString();
    }

    @Override
    public MoneyAmount fromString(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return null;
        }

        return MoneyAmount.parse(string);

    }
}
