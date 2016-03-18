package com.chipcollector.models.core;

import com.chipcollector.domain.MoneyAmount;
import javafx.beans.property.SimpleObjectProperty;

public class MoneyAmountProperty extends SimpleObjectProperty<MoneyAmount> {

    public MoneyAmountProperty(MoneyAmount initialValue) {
        super(initialValue);
    }
}
