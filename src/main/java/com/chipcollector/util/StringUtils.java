package com.chipcollector.util;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static java.util.Objects.isNull;

public class StringUtils {

    public static String toCamelCase(final String string, Character... camelCaseDelimiter) {
        if (isNull(string)) {
            return null;
        }
        boolean isUppercase = true;

        Set<Character> delimiters;
        if (camelCaseDelimiter.length == 0) {
            delimiters = ImmutableSet.of(' ');
        } else {
            delimiters = ImmutableSet.copyOf(camelCaseDelimiter);
        }

        final StringBuilder camelCaseString = new StringBuilder(string.length());

        for (Character character : string.toCharArray()) {
            if (isUppercase) {
                character = Character.toUpperCase(character);
            } else {
                character = Character.toLowerCase(character);
            }
            camelCaseString.append(character);

            isUppercase = delimiters.contains(character);
        }

        return camelCaseString.toString();
    }
}
