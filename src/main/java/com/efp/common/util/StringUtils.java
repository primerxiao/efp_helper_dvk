package com.efp.common.util;

import java.util.Locale;

/**
 * Created by Owen on 6/18/16.
 */
public class StringUtils {

    private static final char UNDERLINE='_';

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

    /**
     * 将首位字母转大写
     * @param str
     * @return
     */
    public static String upperFirstChar(String str) {
        if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 下划线 转 驼峰
     * @param param
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(param.charAt(i));
            if (c == UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
