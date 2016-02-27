package com.chipcollector.views.validation;

import static com.google.common.base.Strings.isNullOrEmpty;

public class RegexConstraint extends Constraint<String> {

    private String regex;

    @Override
    public boolean isValid(String value) {
        return isNullOrEmpty(value) || value.matches(regex);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
