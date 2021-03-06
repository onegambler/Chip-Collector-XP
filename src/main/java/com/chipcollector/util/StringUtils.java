package com.chipcollector.util;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;
import static java.util.Objects.isNull;

public class StringUtils {

    @Nullable
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
            character = isUppercase ? toUpperCase(character) : toLowerCase(character);
            camelCaseString.append(character);
            isUppercase = delimiters.contains(character);
        }

        return camelCaseString.toString();
    }
}
