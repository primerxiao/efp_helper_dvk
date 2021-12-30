package com.efp.common.util;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 86134
 */
public class PluginStringUtils {

    public static final char UNDERLINE='_';

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

    /**
     * 下划线 转 驼峰
     */
    public static String underlineToCamel(String param, boolean initCapFirst) {
        String s = underlineToCamel(param);
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (initCapFirst) {
            return PluginStringUtils.initCap(s);
        }
        return s;
    }

    /**
     * 驼峰转下划线
     * @param camelStr
     * @return
     */
    public static String camelToUnderLine(String camelStr) {
        return CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(camelStr.toLowerCase());
    }

    public static String package2Path(String packageName) {
        //com.fdb.a.smcpi.infra.repository.impl
        return packageName.replaceAll("\\.", "/");
    }

}
