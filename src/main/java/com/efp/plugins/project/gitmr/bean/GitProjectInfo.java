package com.efp.plugins.project.gitmr.bean;

/**
 * @author primerxiao
 */
public class GitProjectInfo {
    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目id
     */
    private String projectId;

    private String s1Suffix;

    private String uatSuffix;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getS1Suffix() {
        return s1Suffix;
    }

    public void setS1Suffix(String s1Suffix) {
        this.s1Suffix = s1Suffix;
    }

    public String getUatSuffix() {
        return uatSuffix;
    }

    public void setUatSuffix(String uatSuffix) {
        this.uatSuffix = uatSuffix;
    }
}
