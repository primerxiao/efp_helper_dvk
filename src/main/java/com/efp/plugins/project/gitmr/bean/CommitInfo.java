package com.efp.plugins.project.gitmr.bean;

import org.gitlab4j.api.models.Commit;

import java.util.ArrayList;


public class CommitInfo {
    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目id
     */
    private String projectId;
    /**
     * 提交文件
     */
    private ArrayList<String> commitFiles;
    /**
     * 提交信息
     */
    private ArrayList<String> commitMessages;

    /**
     * gitlab  commit对象
     */
    private ArrayList<Commit> commits;

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

    public ArrayList<String> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(ArrayList<String> commitFiles) {
        this.commitFiles = commitFiles;
    }

    public ArrayList<String> getCommitMessages() {
        return commitMessages;
    }

    public void setCommitMessages(ArrayList<String> commitMessages) {
        this.commitMessages = commitMessages;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }
}
