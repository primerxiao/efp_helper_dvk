package com.efp.common.util;

/**
 * Created by Owen on 6/18/16.
 */
public class StringUtils {
    /**
     * 将字符串首位小写
     * @param str 字符串
     * @return String
     */
    public static String initCap(String str) {
        if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
