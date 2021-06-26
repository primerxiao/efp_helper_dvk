package com.efp.plugins.project.coder.util;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.StringUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.intellij.database.model.DasColumn;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import org.apache.commons.io.FilenameUtils;

public class GenUtils {

    public static String getNameByBaseMoudleName(String baseModuleName) {
        String[] split = baseModuleName.split("-");
        return split[1];
    }

    private String getJavaTypeByDasColumn(DasColumn dasColumn) {
        return DasUtils.getJavaTypeClass(dasColumn).getSimpleName();
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
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
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
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
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
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
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
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
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
        generateInfo.setClassFields(DasUtils.getClassFields(generateInfo));
        generateInfo.setImports(DasUtils.getImports(generateInfo.getClassFields()));
    }
}
