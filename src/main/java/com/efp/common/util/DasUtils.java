package com.efp.common.util;

import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTableKey;
import com.intellij.database.model.DasTypedObject;
import com.intellij.database.model.MultiRef;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Objects;

public class DasUtils {
    /**
     * @param dasColumn
     * @return
     */
    public static boolean checkPrimaryKey(DasColumn dasColumn) {
        DasTableKey primaryKey = DasUtil.getPrimaryKey(dasColumn.getTable());
        if (Objects.isNull(primaryKey)) {
            return false;
        }
        MultiRef<? extends DasTypedObject> columnsRef = primaryKey.getColumnsRef();
        if (Objects.isNull(columnsRef)) {
            return false;
        }
        Iterable<String> names = columnsRef.names();
        for (String name : names) {
            if (name.equalsIgnoreCase(dasColumn.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新数据库信息
     *
     * @param e
     */
    public static void refreshDas(AnActionEvent e) {
        AnAction action = ActionManager.getInstance().getAction("DatabaseView.Refresh");
        if (action != null) {
            action.actionPerformed(e);
        }
    }

    /**
     * 获取数据库字段对应的java类型
     *
     * @param dasColumn 数据表字段对象
     * @return
     */
    public static Class<? extends Object> getJavaTypeClass(DasColumn dasColumn) {

        if ("char".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return String.class;
        }
        if ("varchar".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return String.class;
        }
        if ("longvarchar".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return String.class;
        }
        if ("numeric".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return BigDecimal.class;
        }
        if ("decimal".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return BigDecimal.class;
        }
        if ("bit".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return boolean.class;
        }
        if ("boolean".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return boolean.class;
        }
        if ("tinyint".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return byte.class;
        }
        if ("smallint".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return short.class;
        }
        if ("int".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Integer.class;
        }
        if ("integer".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Integer.class;
        }
        if ("bigint".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return long.class;
        }
        if ("real".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return float.class;
        }
        if ("float".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return double.class;
        }
        if ("double".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return double.class;
        }
        if ("binary".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return byte[].class;
        }
        if ("varbinary".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return byte[].class;
        }
        if ("longvarbinary".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return byte[].class;
        }
        if ("datetime".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Date.class;
        }
        if ("date".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Date.class;
        }
        if ("time".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Time.class;
        }
        if ("timestamp".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Timestamp.class;
        }
        if ("clob".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Clob.class;
        }
        if ("blob".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Blob.class;
        }
        if ("array".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Array.class;
        }
        if ("struct".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Struct.class;
        }
        if ("ref".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Ref.class;
        }
        if ("datalink".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return URL.class;
        }
        return Object.class;
    }

    /**
     * 根据数据库字段获取需要import的数据
     * @param dasColumn 数据表字段对象
     * @return
     */
    public static String getJavaImport(DasColumn dasColumn) {
        Class<?> typeClass = getJavaTypeClass(dasColumn);
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
}
