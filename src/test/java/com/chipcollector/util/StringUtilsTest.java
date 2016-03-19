package com.chipcollector.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {

    @Test
    public void whenInputIsLowerCaseStringThenToCamelCaseConvertsItToCamelCase() {
        String loweCase = "lower case string";
        final String s = StringUtils.toCamelCase(loweCase, ' ');
        assertThat(s).isEqualTo("Lower Case String");
    }

    @Test
    public void whenInputIsUpperCaseStringThenToCamelCaseConvertsItToCamelCase() {
        String loweCase = "UPPER CASE STRING";
        final String s = StringUtils.toCamelCase(loweCase, ' ');
        assertThat(s).isEqualTo("Upper Case String");
    }

    @Test
    public void wheDelimiterIsNotSpecifiedThenToCamelCaseUsesDefaultSpace() {
        String loweCase = "UPPER CASE STRING";
        final String s = StringUtils.toCamelCase(loweCase);
        assertThat(s).isEqualTo("Upper Case String");
    }

    @Test
    public void whenInputIsNullThentoCamelCaseReturnsNull() {
        final String s = StringUtils.toCamelCase(null, ' ');
        assertThat(s).isNull();
    }
}