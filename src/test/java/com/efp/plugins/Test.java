package com.efp.plugins;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

public class Test {

    @org.junit.Test
    public void test1(){
        String n = "efp.val";
        final String[] split = n.split("\\.");
        System.out.println(split[0]);
    }

}
