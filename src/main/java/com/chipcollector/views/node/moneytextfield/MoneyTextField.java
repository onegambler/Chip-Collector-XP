package com.chipcollector.views.node.moneytextfield;

import com.chipcollector.domain.MoneyAmount;
import com.chipcollector.model.core.MoneyAmountProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class MoneyTextField extends TextField {

    private static final MoneyStringConverter CONVERTER = new MoneyStringConverter();
    private MoneyAmountProperty value = new MoneyAmountProperty();

    public MoneyTextField() {
        super();
        this.setTextFormatter(new TextFormatter<>(CONVERTER, null, new MoneyFilter()));
        textProperty().bindBidirectional(value, CONVERTER);
    }

    public SimpleObjectProperty<MoneyAmount> valueProperty() {
        return value;
    }
}
