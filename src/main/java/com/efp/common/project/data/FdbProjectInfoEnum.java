package com.efp.common.project.data;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author 86134
 */
public enum FdbProjectInfoEnum {
    /**
     * a_smcai 模块
     */
    A_SMCAI_MOUDLE("a-smcai", "smcai", "ob_smcrloan_dev"),
    /**
     * a_smcbi 模块
     */
    A_SMCBI_MOUDLE("a-smcbi", "smcbi", "ob_smcrbiz_dev"),
    /**
     * a_smcbs 模块
     */
    A_SMCBS_MOUDLE("a-smcbs", "smcbs", ""),
    /**
     * a_smcfc 模块
     */
    A_SMCFC_MOUDLE("a-smcfc", "smcfc", "ob_smcrmiddle_dev"),
    /**
     * a_smcia 模块
     */
    A_SMCIA_MOUDLE("a-smcia", "smcia", ""),
    /**
     * a_smcii 模块
     */
    A_SMCII_MOUDLE("a-smcii", "smcii", "ob_smcrloan_dev"),
    /**
     * a_smcpi 模块
     */
    A_SMCPI_MOUDLE("a-smcpi", "smcpi", "ob_smcrloan_dev"),
    /**
     * a_smcqc 模块
     */
    A_SMCQC_MOUDLE("a-smcqc", "smcqc", "ob_smcrmiddle_dev"),
    /**
     * a_smcrc 模块
     */
    A_SMCRC_MOUDLE("a-smcrc", "smcrc", "ob_smcrmiddle_dev"),
    /**
     * a_smcrp 模块
     */
    A_SMCRP_MOUDLE("a-smcrp", "smcrp", "ob_smcrloan_dev"),
    /**
     * a_smcti 模块
     */
    A_SMCTI_MOUDLE("a-smcti", "smcti", "");

    /**
     * 模块全名称
     */
    String moduleName;
    /**
     * 模块简单名称
     */
    String moduleSimpleName;
    /**
     * 连接数据库名
     */
    String dbName;

    FdbProjectInfoEnum(String moduleName, String moduleSimpleName, String dbName) {
        this.moduleName = moduleName;
        this.moduleSimpleName = moduleSimpleName;
        this.dbName = dbName;
    }

    public static FdbProjectInfoEnum findByModuleName(String moduleName) {
        return Arrays.stream(FdbProjectInfoEnum.values())
                .filter(f -> moduleName.startsWith(f.getModuleName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "FdbProjectInfoEnum{" +
                "moduleName='" + moduleName + '\'' +
                ", moduleSimpleName='" + moduleSimpleName + '\'' +
                ", dbName='" + dbName + '\'' +
                '}';
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleSimpleName() {
        return moduleSimpleName;
    }

    public void setModuleSimpleName(String moduleSimpleName) {
        this.moduleSimpleName = moduleSimpleName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
