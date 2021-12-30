package com.efp.plugins.project.coder.bean;

import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.JBIterable;

import java.util.List;

/**
 * 选择的信息
 */
public class GenerateInfo {
    /**
     * 基础模块名称  如a-smcrp
     */
    private String baseMoudleName;
    /**
     * 数据源
     */
    private DasDataSource dasDataSource;
    /**
     * 数据库
     */
    private DasNamespace dasNamespace;
    /**
     * 数据表
     */
    private DasTable dasTable;
    /**
     * 数据源
     */
    private List<? extends DasColumn> dasColumns;
    /**
     * 项目
     */
    private Project project;

    /**
     * 当前选择的字段数组
     */
    private List<DasColumn> selectDasColumns;

    /**
     * 当前适用module
     */
    private Module currentModule;

    /**
     * 当前文件名
     */
    private String fileName;

    /**
     * 当前类名
     */
    private String className;

    /**
     * 当前基础类名
     * 不带Dao、Service等后缀的基础类名
     */
    private String basicClassName;

    /**
     * 当前包名 如：com.fdb.a.smcpi.domain.entity
     */
    private String packageName;

    /**
     * 当前包路径 如：
     */
    private String packagePath;

    /**
     * 导入的包
     */
    private List<String> imports;

    /**
     * 类字段信息
     */
    private List<ClassField> classFields;

    /**
     * 主键字段信息
     */
    private List<ClassField> pkClassFields;

    @Override
    public String toString() {
        return "GenerateInfo{" +
                "baseMoudleName='" + baseMoudleName + '\'' +
                ", dasDataSource=" + dasDataSource +
                ", dasNamespace=" + dasNamespace +
                ", dasTable=" + dasTable +
                ", dasColumns=" + dasColumns +
                ", project=" + project +
                ", selectDasColumns=" + selectDasColumns +
                ", currentModule=" + currentModule +
                ", fileName='" + fileName + '\'' +
                ", packagepath='" + packagePath + '\'' +
                ", imports=" + imports +
                '}';
    }

    public String getBaseMoudleName() {
        return baseMoudleName;
    }

    public void setBaseMoudleName(String baseMoudleName) {
        this.baseMoudleName = baseMoudleName;
    }

    public DasDataSource getDasDataSource() {
        return dasDataSource;
    }

    public void setDasDataSource(DasDataSource dasDataSource) {
        this.dasDataSource = dasDataSource;
    }

    public DasNamespace getDasNamespace() {
        return dasNamespace;
    }

    public void setDasNamespace(DasNamespace dasNamespace) {
        this.dasNamespace = dasNamespace;
    }

    public DasTable getDasTable() {
        return dasTable;
    }

    public void setDasTable(DasTable dasTable) {
        this.dasTable = dasTable;
    }

    public List<? extends DasColumn> getDasColumns() {
        return dasColumns;
    }

    public void setDasColumns(List<? extends DasColumn> dasColumns) {
        this.dasColumns = dasColumns;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<DasColumn> getSelectDasColumns() {
        return selectDasColumns;
    }

    public void setSelectDasColumns(List<DasColumn> selectDasColumns) {
        this.selectDasColumns = selectDasColumns;
    }

    public Module getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
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

    public List<ClassField> getClassFields() {
        return classFields;
    }

    public void setClassFields(List<ClassField> classFields) {
        this.classFields = classFields;
    }

    public String getBasicClassName() {
        return basicClassName;
    }

    public void setBasicClassName(String basicClassName) {
        this.basicClassName = basicClassName;
    }

    public List<ClassField> getPkClassFields() {
        return pkClassFields;
    }

    public void setPkClassFields(List<ClassField> pkClassFields) {
        this.pkClassFields = pkClassFields;
    }
}

