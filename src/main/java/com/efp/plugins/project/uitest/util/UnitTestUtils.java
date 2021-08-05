package com.efp.plugins.project.uitest.util;

public class UnitTestUtils {
    public static boolean checkBaseName(String srcName, String tarName) {
        if (srcName.endsWith("DO")) {
            srcName = srcName.replace("DO", "");
        }
        if (srcName.endsWith("Input")) {
            srcName = srcName.replace("Input", "");
        }
        if (tarName.endsWith("DO")) {
            tarName = tarName.replace("DO", "");
        }
        if (tarName.endsWith("Input")) {
            tarName = tarName.replace("Input", "");
        }
        return srcName.equalsIgnoreCase(tarName);
    }
}
