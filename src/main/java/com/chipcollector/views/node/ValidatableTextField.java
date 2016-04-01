package com.chipcollector.views.node;

import com.chipcollector.util.MessagesHelper;
import com.chipcollector.views.validation.Constraint;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class ValidatableTextField extends TextField {

    @FXML
    private List<Constraint<String>> constraints;

    private final ValidationPopOver popOver = new ValidationPopOver();

    public ValidatableTextField() {
        super();
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
                popOver.setValidationMessage(MessagesHelper.getString(notValidConstraint.get().getValidationErrorMessageId()));
                popOver.show(this);
                return false;
            }
        }

        return true;
    }
}