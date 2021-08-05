package com.efp.plugins.project.uitest.bean;

import com.efp.common.project.data.FdbProjectInfoEnum;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;

import java.io.Serializable;
import java.util.List;

/**
 * @author 86134
 */
public class FdbUnitTestCache implements Serializable {

    /**
     * 类型全名
     */
    private String paramClassQuliaName;

    /**
     * fdb项目信息
     */
    private FdbProjectInfoEnum projectInfoEnum;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 字段信息
     */
    private List<FdbUnitTestColumnCache> fdbUnitTestColumnCache;

    @Override
    public String toString() {
        return "FdbUnitTestCache{" +
                "paramClassQuliaName='" + paramClassQuliaName + '\'' +
                ", projectInfoEnum=" + projectInfoEnum +
                ", tableName='" + tableName + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", fdbUnitTestColumnCache=" + fdbUnitTestColumnCache +
                '}';
    }

    public String getParamClassQuliaName() {
        return paramClassQuliaName;
    }

    public void setParamClassQuliaName(String paramClassQuliaName) {
        this.paramClassQuliaName = paramClassQuliaName;
    }

    public FdbProjectInfoEnum getProjectInfoEnum() {
        return projectInfoEnum;
    }

    public void setProjectInfoEnum(FdbProjectInfoEnum projectInfoEnum) {
        this.projectInfoEnum = projectInfoEnum;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<FdbUnitTestColumnCache> getFdbUnitTestColumnCache() {
        return fdbUnitTestColumnCache;
    }

    public void setFdbUnitTestColumnCache(List<FdbUnitTestColumnCache> fdbUnitTestColumnCache) {
        this.fdbUnitTestColumnCache = fdbUnitTestColumnCache;
    }
}
