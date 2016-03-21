package com.chipcollector.util;

import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class MessagesHelperTest {

    @Test
    public void getStringWorksCorrectly() {
        final String string = MessagesHelper.getString("menu.edit.casino");
        assertThat(string).isEqualTo("Casino");
    }

    @Test
    public void getStringWithParametersWorksCorrectly() {
        final String string = MessagesHelper.getString("menu.edit.casino");
        assertThat(string).isEqualTo("Casino");
    }

}