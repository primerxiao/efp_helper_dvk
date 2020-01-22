package com.efp.common.data;

/**
 * @author gcb
 */
public enum EfpModuleType {
    /**
     * front工程
     */
    FRONT(1),
    /**
     * api工程
     */
    API(2),
    /**
     * middle工程
     */
    MIDDLE(3),
    /**
     * service工程
     */
    SERVICE(4),
    /**
     * impl工程
     */
    IMPL(5),
    /**
     * common工程
     */
    COMMON(6),
    ;

    private int moduleTypeValue;

    EfpModuleType(int moduleTypeValue) {
        this.moduleTypeValue = moduleTypeValue;
    }

    public int getModuleTypeValue() {
        return moduleTypeValue;
    }
}