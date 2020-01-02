package com.efp.plugins.codeHelper.bean;

import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.JBIterable;

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
     * serviceModule
     */
    private Module serviceModule;

    /**
     * implModule
     */
    private Module implModule;

    /**
     * apiModule
     */
    private Module apiModule;

    /**
     * 创建的包、类等信息
     */
    private GenerateJava generateJava;

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

    public Module getServiceModule() {
        return serviceModule;
    }

    public void setServiceModule(Module serviceModule) {
        this.serviceModule = serviceModule;
    }

    public Module getImplModule() {
        return implModule;
    }

    public void setImplModule(Module implModule) {
        this.implModule = implModule;
    }

    public GenerateJava getGenerateJava() {
        return generateJava;
    }

    public void setGenerateJava(GenerateJava generateJava) {
        this.generateJava = generateJava;
    }

    public Module getApiModule() {
        return apiModule;
    }

    public void setApiModule(Module apiModule) {
        this.apiModule = apiModule;
    }
}
