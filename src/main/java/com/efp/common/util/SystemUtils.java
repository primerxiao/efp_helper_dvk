package com.efp.common.util;

public class SystemUtils {
    public static String getPcName() {
        return System.getenv().get("USERNAME");
    }
}
