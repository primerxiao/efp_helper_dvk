package com.efp.plugins.project.coder.bean;

import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.JBIterable;

import java.util.List;

/**
 * 选择的信息
 */
public class GenerateInfo {
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
    private JBIterable<? extends DasColumn> dasColumns;
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
     * 当前文件路径
     */

    private String filePath;
    /**
     * 当前包路径
     */
    private String packagepath;

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

    public JBIterable<? extends DasColumn> getDasColumns() {
        return dasColumns;
    }

    public void setDasColumns(JBIterable<? extends DasColumn> dasColumns) {
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPackagepath() {
        return packagepath;
    }

    public void setPackagepath(String packagepath) {
        this.packagepath = packagepath;
    }
}
