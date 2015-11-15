package com.chipcollector.models.core;

import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

public class BigDecimalProperty extends SimpleObjectProperty<BigDecimal> {

    public BigDecimalProperty(BigDecimal initialValue) {
        super(initialValue);
    }

    public BigDecimalProperty(Object bean, String name, BigDecimal initialValue) {
        super(bean, name, initialValue);
    }

    public BigDecimalProperty(Object bean, String name) {
        super(bean, name);
    }
}
