package com.efp.plugins.project.coder.util;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.StringUtils;
import com.efp.plugins.project.coder.bean.ClassField;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.intellij.database.model.DasColumn;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import org.apache.commons.io.FilenameUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GenUtils {

    public static String getNameByBaseMoudleName(String baseModuleName) {
        String[] split = baseModuleName.split("-");
        return split[1];
    }

    private String getJavaTypeByDasColumn(DasColumn dasColumn) {
        return DasUtils.getJavaTypeClass(dasColumn).getSimpleName();
    }

    private String getColumnListStr(List<ClassField> classFieldList, String linkChar) {
        if (classFieldList == null || classFieldList.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classFieldList.size(); i++) {
            if (i == classFieldList.size() - 1) {
                sb.append(classFieldList.get(i).getDasColumnName());
                continue;
            }
            sb.append(classFieldList.get(i).getDasColumnName()).append(linkChar);

        }
        return sb.toString();
    }

    /**
     * 设置部分信息
     *
     * @param generateInfo 信息
     * @param aDo
     */
    public void setValue(GenerateInfo generateInfo, TemplateFileNameEnum aDo) {
        //通用信息处理
        //基础类名
        generateInfo.setBasicClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())));
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        //字段信息
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        //主键字段信息
        List<ClassField> pkClassFields = generateInfo.getClassFields().stream().filter(ClassField::getPrimaryKey).collect(Collectors.toList());
        generateInfo.setPkClassFields(pkClassFields);
        //导包信息
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
        switch (aDo) {
            case DO:
                doParamPackage(generateInfo);
                break;
            case PO:
                poParamPackage(generateInfo);
                break;
            case INPUT:
                inputParamPackage(generateInfo);
                break;
            case OUTPUT:
                outputParamPackage(generateInfo);
                break;
            case DAO:
                daoParamPackage(generateInfo);
                break;
            case MAPPER:
                mapperParamPackage(generateInfo);
                break;
            case REPOSITORY:
                repositoryParamPackage(generateInfo);
                break;
            case REPOSITORYIMP:
                repositoryImplParamPackage(generateInfo);
                break;
            default:

        }
    }

    private void doParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-domain
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-domain");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/domain/entity/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".domain.entity");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "DO.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "DO");
    }

    private void poParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-infrastructure
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-infrastructure");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/infra/persistence/po/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".infra.persistence.po");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "PO.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "PO");
    }

    private void inputParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-facade
        //com.fdb.a.smcpi.facade.dto
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-facade");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/facade/dto/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".facade.dto");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Input.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Input");
    }

    private void outputParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-facade
        //com.fdb.a.smcpi.facade.dto
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-facade");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/facade/dto/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".facade.dto");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Output.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Output");
    }

    private void daoParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-infrastructure
        //com.fdb.a.smcpi.infra.persistence.mapper
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-infrastructure");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/infra/persistence/mapper/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".infra.persistence.mapper");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Mapper.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Mapper");
    }

    private void mapperParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-infrastructure
        //com.fdb.a.smcpi.infra.persistence.mapper
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-infrastructure");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设计路径 src/main/resources/mybatis/mapper/CrdtApplInfoMapperImpl.xml
        generateInfo.setPackagePath(FilenameUtils.getFullPath(moduleByName.getModuleFilePath()) + "src/main/resources/mybatis/mapper/");
        //设置po包名 com.fdb.a.smcpi.infra.persistence.po
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".infra.persistence.po");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "MapperImpl.xml");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Mapper");
    }

    private void repositoryParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-infracl
        //com.fdb.a.smcpi.acl.repository
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-infracl");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/acl/repository/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".acl.repository");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Repository.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Repository");
    }

    private void repositoryImplParamPackage(GenerateInfo generateInfo) {
        //a-smcpi-infrastructure
        //com.fdb.a.smcpi.infra.repository.impl
        Module moduleByName = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-infrastructure");
        //设置当前模块
        generateInfo.setCurrentModule(moduleByName);
        //设置包路径
        generateInfo.setPackagePath(
                FilenameUtils.getFullPath(moduleByName.getModuleFilePath())
                        + "src/main/java/com/fdb/a/"
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + "/infra/repository/impl/");
        //设置包名
        generateInfo.setPackageName(
                "com.fdb.a."
                        + getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                        + ".infra.repository.impl");
        //设置文件名
        generateInfo.setFileName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "RepositoryImpl.java");
        generateInfo.setClassName(StringUtils.upperFirstChar(StringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "RepositoryImpl");
    }
}
