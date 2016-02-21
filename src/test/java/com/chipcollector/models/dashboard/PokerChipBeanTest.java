package com.chipcollector.models.dashboard;

import org.junit.Test;

public class PokerChipBeanTest {

    @Test(expected = NullPointerException.class)
    public void whenCreateBeanWithNullPokerChipThenThrowException() {
        new PokerChipBean(null);
    }
}