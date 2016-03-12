package com.chipcollector.domain;

import com.avaje.ebean.annotation.EnumValue;

public enum Rarity {
    @EnumValue("R-1")R_1("R-1"),
    @EnumValue("R-2")R_2("R-2"),
    @EnumValue("R-3")R_3("R-3"),
    @EnumValue("R-4")R_4("R-4"),
    @EnumValue("R-5")R_5("R-5"),
    @EnumValue("R-6")R_6("R-6"),
    @EnumValue("R-7")R_7("R-7"),
    @EnumValue("R-8")R_8("R-8"),
    @EnumValue("R-9")R_9("R-9"),
    @EnumValue("R-10")R_10("R-10"),
    @EnumValue("UNK")UNKNOWN("Unknown");

    private String displayName;

    Rarity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
