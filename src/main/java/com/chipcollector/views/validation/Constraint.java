package com.chipcollector.views.validation;

public abstract class Constraint<T> {

    private String validationErrorMessageId;

    public String getValidationErrorMessageId() {
        return validationErrorMessageId;
    }

    public void setValidationErrorMessageId(String validationErrorMessageId) {
        this.validationErrorMessageId = validationErrorMessageId;
    }

    public abstract boolean isValid(T value);
}
