package com.efp.plugins.project.coder.bean;

import java.util.List;

public class Dao implements ImportListOperator {

    /**
     * 导入包列表
     */
    private List<String> imports;

    /**
     * 包名
     */
    private String packageName;
    /**
     * 类名
     */
    private String className;
    /**
     * 基础类名
     */
    private String baseClassName;
    /**
     * 注释
     */
    private String comment;
    /**
     * 作者
     */
    private String author;
    /**
     * 日期
     */
    private String dateStr;

    /**
     * 主键
     */
    private ClassField primarkClassField;

    public Dao() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public ClassField getPrimarkClassField() {
        return primarkClassField;
    }

    public void setPrimarkClassField(ClassField primarkClassField) {
        this.primarkClassField = primarkClassField;
    }

    public String getBaseClassName() {
        return baseClassName;
    }

    public void setBaseClassName(String baseClassName) {
        this.baseClassName = baseClassName;
    }
}
