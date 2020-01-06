package com.efp.common.data;

/**
 * 模块转换对象
 */
public class ModuleCovertBean {
    private String moduleName;
    private String nameSpaceName;
    private EfpModuleType efpModuleType;

    public ModuleCovertBean(String moduleName, String nameSpaceName, EfpModuleType efpModuleType) {
        this.moduleName = moduleName;
        this.nameSpaceName = nameSpaceName;
        this.efpModuleType = efpModuleType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getNameSpaceName() {
        return nameSpaceName;
    }

    public void setNameSpaceName(String nameSpaceName) {
        this.nameSpaceName = nameSpaceName;
    }

    public EfpModuleType getEfpModuleType() {
        return efpModuleType;
    }

    public void setEfpModuleType(EfpModuleType efpModuleType) {
        this.efpModuleType = efpModuleType;
    }
}