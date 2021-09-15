package com.efp.plugins.project.gitmr.constant;

import com.efp.plugins.project.gitmr.bean.GitProjectInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class GitMrConstant {

    public static String GIT_LAB_APIURL = "http://10.139.6.26:7077/";

    public static ArrayList<GitProjectInfo> GIT_PROJECT_INFOS;

    public static HashMap<String, String> ASSIGN_MAPS;

    static {
        GIT_PROJECT_INFOS = new ArrayList<>();
        //初始化项目数据

        ASSIGN_MAPS = new HashMap<>();
        //初始化assign数据
        ASSIGN_MAPS.put("高伟才", "");
        ASSIGN_MAPS.put("肖均辉", "");


    }

}
