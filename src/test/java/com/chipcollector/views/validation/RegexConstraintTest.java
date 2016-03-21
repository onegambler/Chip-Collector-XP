package com.chipcollector.views.validation;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RegexConstraintTest {

    private RegexConstraint regexConstraint = new RegexConstraint();

    @Test
    public void getterAndSetterWorkCorrectly() {
        regexConstraint.setRegex("regex");
        assertThat(regexConstraint.getRegex()).isEqualTo("regex");

        regexConstraint.setValidationErrorMessageId("Error message");
        assertThat(regexConstraint.getValidationErrorMessageId())
                .isEqualTo("Error message");
    }

    @Test
    public void whenPassedValueMatchesRegexThenIsValidReturnsTrue() {
        regexConstraint.setRegex("[a-zA-Z]+");
        assertThat(regexConstraint.isValid("hello")).isTrue();
    }

    @Test
    public void whenPassedValueDoesNotMatcheRegexThenIsValidReturnsFalse() {
        regexConstraint.setRegex("[a-zA-Z]+");
        assertThat(regexConstraint.isValid("1234S")).isFalse();
    }

}