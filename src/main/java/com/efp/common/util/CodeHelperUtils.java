package com.efp.common.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Objects;

public class CodeHelperUtils {
    /**
     * 根据dascolumntypeName获取导包数据
     * @param dasColumnTypeName
     * @return
     */
    public static String getImportByDasColumnTypeName(String dasColumnTypeName) {
        Class<?> typeClass = getTypeClass(dasColumnTypeName);
        if (Objects.isNull(typeClass)) {
            return null;
        }
        if (StringUtils.isEmpty(typeClass.getSimpleName())) {
            return null;
        }
        if (!typeClass.isPrimitive() && !typeClass.getName().startsWith("java.lang")) {

            return typeClass.getName();
        }
        return null;
    }

    /**
     * 根据dasColumanTypeName获取java类型
     *
     * @param dasColumnTypeName das字段类型名
     * @return
     */
    public static Class<? extends Object> getTypeClass(String dasColumnTypeName) {
        if ("char".equalsIgnoreCase(dasColumnTypeName)) {

            return String.class;
        }
        if ("varchar".equalsIgnoreCase(dasColumnTypeName)) {

            return String.class;
        }
        if ("longvarchar".equalsIgnoreCase(dasColumnTypeName)) {

            return String.class;
        }
        if ("numeric".equalsIgnoreCase(dasColumnTypeName)) {

            return BigDecimal.class;
        }
        if ("decimal".equalsIgnoreCase(dasColumnTypeName)) {

            return BigDecimal.class;
        }
        if ("bit".equalsIgnoreCase(dasColumnTypeName)) {

            return boolean.class;
        }
        if ("boolean".equalsIgnoreCase(dasColumnTypeName)) {

            return boolean.class;
        }
        if ("tinyint".equalsIgnoreCase(dasColumnTypeName)) {

            return byte.class;
        }
        if ("smallint".equalsIgnoreCase(dasColumnTypeName)) {

            return short.class;
        }
        if ("int".equalsIgnoreCase(dasColumnTypeName)) {

            return Integer.class;
        }
        if ("integer".equalsIgnoreCase(dasColumnTypeName)) {

            return Integer.class;
        }
        if ("bigint".equalsIgnoreCase(dasColumnTypeName)) {

            return long.class;
        }
        if ("real".equalsIgnoreCase(dasColumnTypeName)) {

            return float.class;
        }
        if ("float".equalsIgnoreCase(dasColumnTypeName)) {

            return double.class;
        }
        if ("double".equalsIgnoreCase(dasColumnTypeName)) {

            return double.class;
        }
        if ("binary".equalsIgnoreCase(dasColumnTypeName)) {

            return byte[].class;
        }
        if ("varbinary".equalsIgnoreCase(dasColumnTypeName)) {

            return byte[].class;
        }
        if ("longvarbinary".equalsIgnoreCase(dasColumnTypeName)) {

            return byte[].class;
        }
        if ("datetime".equalsIgnoreCase(dasColumnTypeName)) {

            return Date.class;
        }
        if ("date".equalsIgnoreCase(dasColumnTypeName)) {

            return Date.class;
        }
        if ("time".equalsIgnoreCase(dasColumnTypeName)) {

            return Time.class;
        }
        if ("timestamp".equalsIgnoreCase(dasColumnTypeName)) {

            return Timestamp.class;
        }
        if ("clob".equalsIgnoreCase(dasColumnTypeName)) {

            return Clob.class;
        }
        if ("blob".equalsIgnoreCase(dasColumnTypeName)) {

            return Blob.class;
        }
        if ("array".equalsIgnoreCase(dasColumnTypeName)) {

            return Array.class;
        }
        if ("struct".equalsIgnoreCase(dasColumnTypeName)) {

            return Struct.class;
        }
        if ("ref".equalsIgnoreCase(dasColumnTypeName)) {

            return Ref.class;
        }
        if ("datalink".equalsIgnoreCase(dasColumnTypeName)) {

            return URL.class;
        }
        return Object.class;
    }
}
