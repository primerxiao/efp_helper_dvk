package com.efp.plugins.project.gitmr.bean;

/**
 * @author primerxiao
 */
public class AppInfo {
    private String projectName;
    private String projectId;
    private String compareStatus;


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

    public String getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(String compareStatus) {
        this.compareStatus = compareStatus;
    }
}
