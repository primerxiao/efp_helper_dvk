package com.efp.common.data;

public enum EfpModuleType {
    FRONT(1),
    API(2),
    MIDDLE(3),
    SERVICE(4),
    IMPL(5),
    COMMON(6);
    private int moduleTypeValue;

    EfpModuleType(int moduleTypeValue) {
        this.moduleTypeValue = moduleTypeValue;
    }

    public int getModuleTypeValue() {
        return moduleTypeValue;
    }
}