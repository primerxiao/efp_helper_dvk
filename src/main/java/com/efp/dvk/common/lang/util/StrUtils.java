package com.efp.dvk.common.lang.util;

import com.google.common.base.CaseFormat;

public class StrUtils {

    /**
     * 驼峰字符串转下划线字符串
     *
     * @param lineStr 驼峰字符串
     * @return String
     */
    public static String convertLineToHump(String lineStr) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, lineStr).toLowerCase();
    }

    /**
     * 下划线字符串转驼峰
     *
     * @param humpStr 下划线字符串
     * @return String
     */
    public static String convertHumpToLine(String humpStr) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, humpStr.toLowerCase());
    }
}
