package com.efp.plugins.project.util;

import com.efp.plugins.project.gitmr.bean.AppInfo;
import com.efp.plugins.project.gitmr.bean.GitProjectInfo;
import com.efp.plugins.project.gitmr.constant.GitMrConstant;

/**
 * @author primerxiao
 */
public class GitMrUtils {

    /**
     * 获取分支名
     * @param type 1-s1 2-uat bug-fix
     * @param gitProjectInfo 项目信息
     * @return String
     */
    public static String getBranchName(String type, GitProjectInfo gitProjectInfo) {
        if ("1".equals(type)) {
            return gitProjectInfo.getProjectName().toLowerCase() + gitProjectInfo.getS1Suffix();
        }

        if ("2".equals(type)) {
            return gitProjectInfo.getProjectName().toLowerCase() + gitProjectInfo.getUatSuffix();
        }

        if ("bug-fix".equals(type)) {
            return type;
        }

        throw new RuntimeException("类型不符合");
    }

    public static GitProjectInfo getProjectInfoById(String projectId) {
        for (GitProjectInfo gitProjectInfo : GitMrConstant.GIT_PROJECT_INFOS) {
            if (gitProjectInfo.getProjectId().equals(projectId)) {
                return gitProjectInfo;
            }
        }
        throw new RuntimeException("根据项目id:" + projectId + "获取不到项目信息");
    }

}
