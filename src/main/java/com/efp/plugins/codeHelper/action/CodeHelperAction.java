package com.efp.plugins.codeHelper.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.data.EfpCovert;
import com.efp.common.data.EfpModuleType;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.bean.GenerateJava;
import com.efp.plugins.codeHelper.ui.GenerateOption;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DbNamespaceImpl;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CodeHelperAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AnAction action = ActionManager.getInstance().getAction("DatabaseView.Refresh");
        if (action != null) {
            action.actionPerformed(e);
        }
        FileEditor[] allEditors = FileEditorManager.getInstance(e.getProject()).getAllEditors();
        if (allEditors != null && allEditors.length > 0) {
            AnAction xCloseAllEditors = ActionManager.getInstance().getAction("CloseAllEditors");
            xCloseAllEditors.actionPerformed(e);
        }
        //获取数据库配置
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof DasTable)) {
            Messages.showErrorDialog("请选择一个数据库表对象进行操作", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        final GenerateInfo generateInfo = getGenerateInfo(e, (DasTable) psiElement);
        GenerateOption generateOption = new GenerateOption(true, e, generateInfo);
        generateOption.show();

    }

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
