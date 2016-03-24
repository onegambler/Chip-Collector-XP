package com.chipcollector.views.node;

import com.chipcollector.util.MessagesHelper;
import com.chipcollector.views.validation.Constraint;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class ValidatableTextField extends TextField {

    @FXML
    private List<Constraint<String>> constraints;

    private final PopOver popOver = new PopOver();
    private final Label errorMessageLabel = new Label();

    public ValidatableTextField() {
        super();
        errorMessageLabel.setTextFill(Color.BLACK);
        errorMessageLabel.setPadding(new Insets(0, 10, 0, 10));
        popOver.setContentNode(errorMessageLabel);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setArrowSize(5);
        popOver.setArrowIndent(2);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    }

    public List<Constraint<String>> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint<String>> constraints) {
        this.constraints = constraints;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        popOver.hide();
        if (nonNull(constraints)) {
            Optional<Constraint<String>> notValidConstraint = constraints.stream()
                    .filter(constraint -> !constraint.isValid(text))
                    .findFirst();

            if (notValidConstraint.isPresent()) {
                errorMessageLabel.setText(MessagesHelper.getString(notValidConstraint.get().getValidationErrorMessageId()));
                popOver.show(this);
                return false;
            }
        }

        return true;
    }
}