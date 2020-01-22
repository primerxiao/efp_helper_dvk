package com.efp.plugins;

public class Test {

    @org.junit.Test
    public void test1(){
        String n = "efp.val";
        final String[] split = n.split("\\.");
        System.out.println(split[0]);
    }

}
