package com.efp.common.constant;

public enum CronExpTypeEnum {
    SECOND("SECOND"),
    MIMUTE("MIMUTE"),
    HOUR("HOUR"),
    DAY("DAY"),
    MONTH("MONTH"),
    WEEK("WEEK"),
    YEAR("YEAR");
    private String value;

    CronExpTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
