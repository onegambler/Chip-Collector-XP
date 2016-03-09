package com.chipcollector.models.core;

import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

public class BigDecimalProperty extends SimpleObjectProperty<BigDecimal> {

    public BigDecimalProperty(BigDecimal initialValue) {
        super(initialValue);
    }
}
