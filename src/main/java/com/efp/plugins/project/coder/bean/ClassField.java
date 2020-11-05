package com.efp.plugins.project.coder.bean;

public class ClassField {
    /**
     * 类属性名字段名
     */
    private String fieldName;
    /**
     * 字段注释
     */
    private String comment;
    /**
     * 字段java类型
     */
    private Class javaTypeClass;

    /**
     * 表字段名
     */
    private String dasColumnName;

    private Boolean isPrimaryKey;

    public ClassField(String fieldName, String comment, Class javaTypeClass,String dasColumnName,boolean isPrimaryKey) {
        this.fieldName = fieldName;
        this.comment = comment;
        this.javaTypeClass = javaTypeClass;
        this.dasColumnName = dasColumnName;
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Class getJavaTypeClass() {
        return javaTypeClass;
    }

    public void setJavaTypeClass(Class javaTypeClass) {
        this.javaTypeClass = javaTypeClass;
    }

    public String getDasColumnName() {
        return dasColumnName;
    }

    public void setDasColumnName(String dasColumnName) {
        this.dasColumnName = dasColumnName;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

}
