package com.chipcollector.views.node;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;

import static org.controlsfx.control.PopOver.ArrowLocation.TOP_CENTER;

public class ValidationPopOver extends PopOver {

    private final Label errorMessageLabel = new Label();

    public ValidationPopOver() {
        super();
        errorMessageLabel.setTextFill(Color.BLACK);
        errorMessageLabel.setPadding(new Insets(0, 10, 0, 10));
        setContentNode(errorMessageLabel);
        setAutoHide(true);
        setHideOnEscape(true);
        setArrowSize(5);
        setArrowIndent(2);
        setArrowLocation(TOP_CENTER);
    }

    public void setValidationMessage(String message) {
        errorMessageLabel.setText(message);
    }
}
