package com.efp.common.constant;

public enum RegCenterTypeEnum {
    ZOOKEEPER("zookeeper"),
    EUREKA("eureka"),
    NACOS("nacos"),
    ;
    private String value;

    RegCenterTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RegCenterTypeEnum getEnumBtValue(String value) {
        RegCenterTypeEnum[] values = RegCenterTypeEnum.values();
        for (RegCenterTypeEnum regCenterTypeEnum : values) {
            if (regCenterTypeEnum.getValue().equals(value)) {
                return regCenterTypeEnum;
            }
        }
        return null;
    }
}
