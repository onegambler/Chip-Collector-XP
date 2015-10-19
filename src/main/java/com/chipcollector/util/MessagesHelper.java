package com.chipcollector.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessagesHelper {
    private static final String BUNDLE_NAME = "com/chipcollector/resources/DisplayStrings";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private MessagesHelper() {
    }

    public static String getString(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    public static String getString(String key, Object... params) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params);
    }
}