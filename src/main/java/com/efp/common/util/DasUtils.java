package com.efp.common.util;

import com.efp.common.data.EfpCovert;
import com.efp.common.data.EfpModuleType;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.bean.GenerateJava;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.*;
import com.intellij.database.psi.DbNamespaceImpl;
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
     *
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

    /**
     * 根据数据库表对象获取有关代码生成的信息封装对象
     *
     * @param e
     * @param dasTable
     * @return
     */
    public static GenerateInfo getGenerateInfo(AnActionEvent e, DasTable dasTable) {
        DasNamespace namespace = DasUtil.getNamespace(dasTable);
        DasDataSource dataSource = ((DbNamespaceImpl) namespace).getDataSource();
        GenerateInfo generateInfo = new GenerateInfo();
        generateInfo.setDasDataSource(dataSource);
        generateInfo.setDasNamespace(namespace);
        generateInfo.setDasTable(dasTable);
        generateInfo.setDasColumns(DasUtil.getColumns(dasTable));
        generateInfo.setProject(e.getProject());
        generateInfo.setImplModule(EfpCovert.getModule(e.getProject(), namespace, EfpModuleType.IMPL));
        generateInfo.setServiceModule(EfpCovert.getModule(e.getProject(), namespace, EfpModuleType.SERVICE));
        generateInfo.setApiModule(EfpCovert.getModule(e.getProject(), namespace, EfpModuleType.API));
        generateInfo.setGenerateJava(getGenerateJava(generateInfo));
        return generateInfo;
    }

    /**
     * 根据有关代码生成的信息封装对象获取java相关的封装对象
     *
     * @param generateInfo
     * @return
     */
    public static GenerateJava getGenerateJava(GenerateInfo generateInfo) {
        GenerateJava generateJava = new GenerateJava();
        final String[] implModuleNameArr = EfpCovert.getModuleNameArr(generateInfo.getImplModule());
        final String[] serviceModuleNameArr = EfpCovert.getModuleNameArr(generateInfo.getServiceModule());

        //判断路径是否存在
        //base
        generateJava.setBaseClassName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, generateInfo.getDasTable().getName()));
        //domain
        generateJava.setDomainClassName(generateJava.getBaseClassName());
        generateJava.setDomainPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.domain");
        generateJava.setDomainPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/domain/");
        generateJava.setDomainFileName(generateJava.getBaseClassName() + ".java");
        //vo
        generateJava.setVoClassName(generateJava.getBaseClassName() + "VO");
        generateJava.setVoPackageName("com.irdstudio." + serviceModuleNameArr[0] + "." + serviceModuleNameArr[1] + ".service.vo");
        generateJava.setVoPackagePath(generateInfo.getServiceModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + serviceModuleNameArr[0] + "/" + serviceModuleNameArr[1] + "/service/vo/");
        generateJava.setVoFileName(generateJava.getBaseClassName() + "VO.java");
        //dao
        generateJava.setDaoClassName(generateJava.getBaseClassName() + "Dao");
        generateJava.setDaoPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.dao");
        generateJava.setDaoPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/dao/");
        generateJava.setDaoFileName(generateJava.getBaseClassName() + "Dao.java");
        //service
        generateJava.setServiceClassName(generateJava.getBaseClassName() + "Service");
        generateJava.setServicePackageName("com.irdstudio." + serviceModuleNameArr[0] + "." + serviceModuleNameArr[1] + ".service.facade");
        generateJava.setServicePackagePath(generateInfo.getServiceModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + serviceModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/facade/");
        generateJava.setServiceFileName(generateJava.getBaseClassName() + "Service.java");
        //serviceImpl
        generateJava.setServiceImplClassName(generateJava.getBaseClassName() + "ServiceImpl");
        generateJava.setServiceImplPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.impl");
        generateJava.setServiceImplPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/impl/");
        generateJava.setServiceImplFileName(generateJava.getBaseClassName() + "ServiceImpl.java");
        //mapper
        generateJava.setMapperPath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/resources/mybatis/mapper/");
        generateJava.setMapperFileNameWithoutExt(generateJava.getBaseClassName() + "Mapper");
        generateJava.setMapperFileName(generateJava.getBaseClassName() + "Mapper.xml");
        //controller
        if (generateInfo.getApiModule() != null) {
            //api有可能为空
            final String[] apiModuleNameArr = EfpCovert.getModuleNameArr(generateInfo.getApiModule());
            generateJava.setControllerClassName(generateJava.getBaseClassName() + "Controller");
            generateJava.setControllerPathName("com.irdstudio." + apiModuleNameArr[0] + "." + apiModuleNameArr[1] + ".api.rest");
            generateJava.setControllerPackagePath(generateInfo.getApiModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + apiModuleNameArr[0] + "/" + apiModuleNameArr[1] + "/api/rest/");
            generateJava.setControllerFileName(generateJava.getBaseClassName() + "Controller.java");
        }
        return generateJava;
    }

}
