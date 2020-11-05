package com.efp.plugins.project.coder.bean;

import java.util.List;

/**
 * @author primerxiao
 */
public class Mapper {
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * Base_Column_List
     */
    private String baseColumnList;

    /**
     * domain类全名
     */
    private String domainQuaName;

    /**
     * dao全名
     */
    private String daoQuaName;

    /**
     * 基础类名
     */
    private String baseClassName;

    /**
     * 类字段信息集合
     */
    private List<ClassField> classFields;

    public Mapper() {
    }


    public String getBaseColumnList() {
        return baseColumnList;
    }

    public void setBaseColumnList(String baseColumnList) {
        this.baseColumnList = baseColumnList;
    }

    public String getDomainQuaName() {
        return domainQuaName;
    }

    public void setDomainQuaName(String domainQuaName) {
        this.domainQuaName = domainQuaName;
    }

    public String getBaseClassName() {
        return baseClassName;
    }

    public void setBaseClassName(String baseClassName) {
        this.baseClassName = baseClassName;
    }

    public List<ClassField> getClassFields() {
        return classFields;
    }

    public void setClassFields(List<ClassField> classFields) {
        this.classFields = classFields;
    }

    public String getDaoQuaName() {
        return daoQuaName;
    }

    public void setDaoQuaName(String daoQuaName) {
        this.daoQuaName = daoQuaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
