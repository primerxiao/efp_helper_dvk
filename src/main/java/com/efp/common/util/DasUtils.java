package com.efp.common.util;

import com.efp.plugins.project.coder.bean.ClassField;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.*;
import com.intellij.database.psi.DbNamespaceImpl;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.containers.JBIterable;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DasUtils {
    /**
     * @param dasColumn 字段
     * @return boolean
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
     * @param e 事件
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
     * @return Class<? extends Object>
     */
    public static Class<?> getJavaTypeClass(DasColumn dasColumn) {

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

            return Integer.class;
        }
        if ("smallint".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Integer.class;
        }
        if ("int".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Integer.class;
        }
        if ("integer".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Integer.class;
        }
        if ("bigint".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Long.class;
        }
        if ("real".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Float.class;
        }
        if ("float".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Double.class;
        }
        if ("double".equalsIgnoreCase(dasColumn.getDataType().typeName)) {

            return Double.class;
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
     *
     * @param dasColumn 数据表字段对象
     * @return String
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

    /**
     * 根据数据库表对象获取有关代码生成的信息封装对象
     *
     * @param e 事件对象
     * @param dasTable 数据库表
     * @return GenerateInfo
     */
    public static GenerateInfo getGenerateInfo(AnActionEvent e, DasTable dasTable) {
        DasNamespace namespace = DasUtil.getNamespace(dasTable);
        DasDataSource dataSource = ((DbNamespaceImpl) namespace).getDataSource();
        GenerateInfo generateInfo = new GenerateInfo();
        generateInfo.setDasDataSource(dataSource);
        generateInfo.setDasNamespace(namespace);
        generateInfo.setDasTable(dasTable);

        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dasTable);
        List<? extends DasColumn> dasColumns = columns.toList();
        generateInfo.setDasColumns(dasColumns);
        generateInfo.setProject(e.getProject());
        return generateInfo;
    }

    public static List<ClassField> getClassFields(GenerateInfo generateInfo) {
        List<? extends DasColumn> dasColumns = generateInfo.getDasColumns();
        return dasColumns.stream().map(dasColumn ->
                new ClassField(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dasColumn.getName()),
                        dasColumn.getComment(), DasUtils.getJavaTypeClass(dasColumn),
                        dasColumn.getName(),
                        DasUtils.checkPrimaryKey(dasColumn))
        ).collect(Collectors.toList());
    }


    public static List<String> getImports(List<ClassField> classFields) {
        ArrayList<String> imports = new ArrayList<>();
        for (ClassField classField : classFields) {
            if (org.apache.commons.lang3.StringUtils.isEmpty(classField.getJavaTypeClass().getSimpleName())) {
                continue;
            }
            if (!classField.getJavaTypeClass().isPrimitive() && !classField.getJavaTypeClass().getName().startsWith("java.lang")) {
                if (imports.contains(classField.getJavaTypeClass().getName())) {
                    continue;
                }
                imports.add(classField.getJavaTypeClass().getName());
            }
        }
        return imports;
    }

}
