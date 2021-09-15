package com.efp.plugins.project.gitmr.constant;

import com.efp.plugins.project.gitmr.bean.GitProjectInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class GitMrConstant {

    public static String GIT_LAB_APIURL = "http://188.100.26.38/";

    public static ArrayList<GitProjectInfo> GIT_PROJECT_INFOS;

    public static HashMap<String, String> ASSIGN_MAPS;

    static {
        GIT_PROJECT_INFOS = new ArrayList<>();
        //初始化项目数据
        GitProjectInfo i180 = new GitProjectInfo();
        i180.setProjectName("A-SMCRP");
        i180.setProjectId("180");
        i180.setS1Suffix("-s1");
        i180.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i180);

        GitProjectInfo i179 = new GitProjectInfo();
        i179.setProjectName("A-SMCAI");
        i179.setProjectId("179");
        i179.setS1Suffix("-s1");
        i179.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i179);

        GitProjectInfo i178 = new GitProjectInfo();
        i178.setProjectName("A-SMCII");
        i178.setProjectId("178");
        i178.setS1Suffix("-s1");
        i178.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i178);

        GitProjectInfo i177 = new GitProjectInfo();
        i177.setProjectName("A-SMCPI");
        i177.setProjectId("177");
        i177.setS1Suffix("-s1");
        i177.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i177);

        GitProjectInfo i165 = new GitProjectInfo();
        i165.setProjectName("A-SMCMS");
        i165.setProjectId("165");
        i165.setS1Suffix("-s1");
        i165.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i165);

        GitProjectInfo i164 = new GitProjectInfo();
        i164.setProjectName("A-SMCBS");
        i164.setProjectId("164");
        i164.setS1Suffix("-s1");
        i164.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i164);

        GitProjectInfo i163 = new GitProjectInfo();
        i163.setProjectName("A-SMCTS");
        i163.setProjectId("163");
        i163.setS1Suffix("-s1");
        i163.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i163);

        GitProjectInfo i162 = new GitProjectInfo();
        i162.setProjectName("A-SMCBP");
        i162.setProjectId("162");
        i162.setS1Suffix("-s1");
        i162.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i162);

        GitProjectInfo i161 = new GitProjectInfo();
        i161.setProjectName("A-SMCFC");
        i161.setProjectId("161");
        i161.setS1Suffix("-s1");
        i161.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i161);

        GitProjectInfo i160 = new GitProjectInfo();
        i160.setProjectName("A-SMCYP");
        i160.setProjectId("160");
        i160.setS1Suffix("-s1");
        i160.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i160);

        GitProjectInfo i159 = new GitProjectInfo();
        i159.setProjectName("A-SMCQC");
        i159.setProjectId("159");
        i159.setS1Suffix("-s1");
        i159.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i159);

        GitProjectInfo i158 = new GitProjectInfo();
        i158.setProjectName("A-SMCRC");
        i158.setProjectId("158");
        i158.setS1Suffix("-s1");
        i158.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i158);

        GitProjectInfo i157 = new GitProjectInfo();
        i157.setProjectName("A-SMCIA");
        i157.setProjectId("157");
        i157.setS1Suffix("-s1");
        i157.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i157);

        GitProjectInfo i156 = new GitProjectInfo();
        i156.setProjectName("A-SMCBI");
        i156.setProjectId("156");
        i156.setS1Suffix("-s1");
        i156.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i156);

        GitProjectInfo i155 = new GitProjectInfo();
        i155.setProjectName("A-SMCTI");
        i155.setProjectId("155");
        i155.setS1Suffix("-s1");
        i155.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i155);

        GitProjectInfo i154 = new GitProjectInfo();
        i154.setProjectName("A-SMCIG");
        i154.setProjectId("154");
        i154.setS1Suffix("-s1");
        i154.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i154);

        GitProjectInfo i152 = new GitProjectInfo();
        i152.setProjectName("A-SMCRM");
        i152.setProjectId("152");
        i152.setS1Suffix("-s1");
        i152.setUatSuffix("-uat");
        GIT_PROJECT_INFOS.add(i152);


        ASSIGN_MAPS = new HashMap<>();
        //初始化assign数据
        ASSIGN_MAPS.put("高伟才", "13");
        ASSIGN_MAPS.put("肖均辉", "458");

    }

}
