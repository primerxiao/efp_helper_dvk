package com.efp.plugins;

import com.efp.common.util.RuleUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.apache.commons.lang3.StringUtils;

public class Test {

    @org.junit.Test
    public void test1(){
        String n = "efp.val";
        final String[] split = n.split("\\.");
        System.out.println(split[0]);
    }
    @org.junit.Test
    public void stringJoin(){
        String n = "";
        System.out.println(String.join(",", n, "00"));
        System.out.println(StringUtils.isEmpty(""));
    }
    @org.junit.Test
    public void isIp(){
        String ip = "127.0.0.10000000000000000";
        System.out.println(RuleUtils.isIp(ip));
    }
    @org.junit.Test
    public void package2Path(){
        String ip = "127.0.0.10000000000000000";
        System.out.println(com.efp.common.util.StringUtils.package2Path(ip));
    }



}
