package com.efp.plugins.project.tbtransfor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 表变更信息
 *
 * @author primerxiao
 */
public class TbChangeInfo implements Serializable {

    /**
     * 代码属于哪个模块 如a-smcpi
     */
    private String baseModuleName;

    /**
     * 表属于哪个数据库 如：ob_smcrbiz_dev
     */
    private String schema;

    /**
     * 原表名
     */
    private String srcTableName;

    /**
     * 目标表名
     */
    private String targetTableName;

    /**
     * 变更字段集
     */
    private List<ClChangeInfo> modifyClList;

    /**
     * 新增字段集
     */
    private List<ClChangeInfo> addClList;

    /**
     * 删除字段集
     */
    private List<ClChangeInfo> deleteClList;

    public String getSrcTableName() {
        return srcTableName;
    }

    public void setSrcTableName(String srcTableName) {
        this.srcTableName = srcTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public List<ClChangeInfo> getModifyClList() {
        return modifyClList;
    }

    public void setModifyClList(List<ClChangeInfo> modifyClList) {
        this.modifyClList = modifyClList;
    }

    public List<ClChangeInfo> getAddClList() {
        return addClList;
    }

    public void setAddClList(List<ClChangeInfo> addClList) {
        this.addClList = addClList;
    }

    public List<ClChangeInfo> getDeleteClList() {
        return deleteClList;
    }

    public void setDeleteClList(List<ClChangeInfo> deleteClList) {
        this.deleteClList = deleteClList;
    }
}
