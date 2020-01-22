package com.efp.common.data;

/**
 * 模块转换对象
 *
 * @author gcb
 */
public class ModuleCovertBean {

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 数据库名称
     */
    private String nameSpaceName;

    /**
     * 模块类型
     */
    private EfpModuleType efpModuleType;

    public ModuleCovertBean(String moduleName, String nameSpaceName, EfpModuleType efpModuleType) {
        this.moduleName = moduleName;
        this.nameSpaceName = nameSpaceName;
        this.efpModuleType = efpModuleType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getNameSpaceName() {
        return nameSpaceName;
    }

    public EfpModuleType getEfpModuleType() {
        return efpModuleType;
    }

}