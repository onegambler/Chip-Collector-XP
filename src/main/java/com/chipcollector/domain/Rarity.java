package com.chipcollector.domain;
//TODO
public enum Rarity {
    R_1("R-1"),
    R_2("R-2"),
    R_3("R-3"),
    R_4("R-4"),
    R_5("R-5"),
    R_6("R-6"),
    R_7("R-7"),
    R_8("R-8"),
    R_9("R-9"),
    R_10("R-10"),
    UNKNOWN("Unknown");

    private String displayName;

    Rarity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
