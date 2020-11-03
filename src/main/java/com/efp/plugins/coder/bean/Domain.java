package com.efp.plugins.coder.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Domain implements ImportListOperator {

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
     * 类字段信息集合
     */
    private List<ClassField> classFields;

    public Domain() {
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

    public List<ClassField> getClassFields() {
        return classFields;
    }

    public void setClassFieldInfos(List<ClassField> classFields) {
        this.classFields = classFields;
    }

    @Override
    public List<String> getImports() {
        imports = new ArrayList<>();
        for (ClassField classField : classFields) {
            if (StringUtils.isEmpty(classField.getJavaTypeClass().getSimpleName())) {
                continue;
            }
            if (!classField.getJavaTypeClass().isPrimitive() && !classField.getJavaTypeClass().getName().startsWith("java.lang")) {
                if (imports.contains(classField.getJavaTypeClass().getName())) {
                    continue;
                }
                imports.add(classField.getJavaTypeClass().getName());
            }
        }
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

}
