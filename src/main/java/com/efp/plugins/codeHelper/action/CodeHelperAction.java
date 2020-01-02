package com.efp.plugins.codeHelper.action;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.CodeHelperUtils;
import com.efp.common.util.DubboXmlConfigUtils;
import com.efp.plugins.codeHelper.bean.ClassField;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.bean.GenerateJava;
import com.efp.plugins.codeHelper.generator.*;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.*;
import com.intellij.database.psi.DbNamespaceImpl;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.formatter.xml.XmlCodeStyleSettings;
import com.intellij.psi.xml.XmlFile;
import com.intellij.sql.dataFlow.instructions.SqlPseudoValueSource;
import com.intellij.util.IconUtil;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CodeHelperAction extends AnAction {

    private List<VirtualFile> vfs = new ArrayList<>();

    /**
     * 是否对原有代码进行覆盖
     */
    private boolean isOverride;

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
        vfs.clear();
        //获取数据库配置
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof DasTable)) {
            Messages.showErrorDialog("请选择一个数据库表对象进行操作", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        final GenerateInfo generateInfo = getGenerateInfo(e, (DasTable) psiElement);
        int checkFlag = Messages.showCheckboxOkCancelDialog("如果已经存在代码，请确实是否需要覆盖", null,
                "覆盖代码", true, 0, 1, IconUtil.getEditIcon());
        if (checkFlag == 1) {
            this.isOverride = true;
        } else if (checkFlag == 0) {
            this.isOverride = false;
        } else {
            return;
        }
        startGenerate(e, generateInfo);
    }

    private void startGenerate(AnActionEvent e, final GenerateInfo generateInfo) {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    try {
                        VirtualFile domain = new DomainGenerator(isOverride, generateInfo, TemplateFileNameEnum.DOMAIN).generate();
                        VirtualFile vo = new VoGenerator(isOverride, generateInfo, TemplateFileNameEnum.VO).generate();
                        VirtualFile dao = new DaoGenerator(isOverride, generateInfo, TemplateFileNameEnum.DAO).generate();
                        VirtualFile service = new ServiceGenerator(isOverride, generateInfo, TemplateFileNameEnum.SERVICE).generate();
                        VirtualFile serviceImpl = new ServiceImplGenerator(isOverride, generateInfo, TemplateFileNameEnum.SERVICEIMPL).generate();
                        VirtualFile generate = new MapperGenerator(isOverride, generateInfo, TemplateFileNameEnum.MAPPER).generate();
                        if (!Objects.isNull(service)) {
                            generateDubboConfig(e, generateInfo, service);
                        }
                        addVfs(domain, vo, dao, service, serviceImpl,generate);
                        doOptimize(e.getProject());
                        //保存文档
                        FileDocumentManagerImpl.getInstance().saveAllDocuments();
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                "所有文件生成完成", NotificationType.INFORMATION));
                    } catch (IOException | TemplateException e) {
                        String message = CompilerBundle.message("message.tect.package.file.io.error", e.toString());
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                message, NotificationType.ERROR));
                    }
                });
            }
        });
    }

    private void addVfs(VirtualFile... files) {
        for (VirtualFile file : files) {
            if (Objects.isNull(file)) {
                continue;
            }
            if (vfs.contains(file)) {
                continue;
            }
            vfs.add(file);
        }
    }

    private void generateDubboConfig(@NotNull AnActionEvent e, GenerateInfo generateInfo, VirtualFile service) {
        PsiFile file = PsiManager.getInstance(generateInfo.getProject()).findFile(service);
        if (!Objects.isNull(file)) {
            DubboXmlConfigUtils.consumerXmlConfigSet(e, generateInfo.getServiceModule(), ((PsiJavaFile) file).getClasses()[0]);
            DubboXmlConfigUtils.poviderXmlConfigSet(e, generateInfo.getImplModule(), ((PsiJavaFile) file).getClasses()[0]);
        }
    }

    /**
     * 根据选择信息转换成类字段属性
     *
     * @param generateInfo
     */
    private List<ClassField> coverToClassFieldInfos(GenerateInfo generateInfo) {
        List<? extends DasColumn> dasColumns = generateInfo.getDasColumns().toList();
        return dasColumns.stream().map(dasColumn ->
                new ClassField(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dasColumn.getName()),
                        dasColumn.getComment(), CodeHelperUtils.getTypeClass(dasColumn.getDataType().typeName),
                        dasColumn.getName(),
                        checkPrimaryKey(dasColumn))
        ).collect(Collectors.toList());
    }

    private GenerateInfo getGenerateInfo(AnActionEvent e, DasTable dasTable) {
        DasNamespace namespace = DasUtil.getNamespace(dasTable);
        DasDataSource dataSource = ((DbNamespaceImpl) namespace).getDataSource();
        GenerateInfo generateInfo = new GenerateInfo();
        generateInfo.setDasDataSource(dataSource);
        generateInfo.setDasNamespace(namespace);
        generateInfo.setDasTable(dasTable);
        generateInfo.setDasColumns(DasUtil.getColumns(dasTable));
        generateInfo.setProject(e.getProject());
        String implModuleName = generateInfo.getDasNamespace().getName().replace("_", ".") + ".impl";
        if (implModuleName.contains("risk")) {
            implModuleName = implModuleName.replaceFirst("risk", "riskm");
        }
        generateInfo.setImplModule(ModuleManager.getInstance(e.getProject()).findModuleByName(implModuleName));
        String serviceModuleName = generateInfo.getDasNamespace().getName().replace("_", ".") + ".service";
        if (serviceModuleName.contains("risk")) {
            serviceModuleName = serviceModuleName.replaceFirst("risk", "riskm");
        }
        generateInfo.setServiceModule(ModuleManager.getInstance(e.getProject()).findModuleByName(serviceModuleName));
        generateInfo.setGenerateJava(getGenerateJava(generateInfo));
        return generateInfo;
    }

    private GenerateJava getGenerateJava(GenerateInfo generateInfo) {
        GenerateJava generateJava = new GenerateJava();
        final String[] implModuleNameArr = generateInfo.getImplModule().getName().split("\\.");
        final String[] serviceModuleNameArr = generateInfo.getServiceModule().getName().split("\\.");
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
        return generateJava;

    }

    private boolean checkPrimaryKey(DasColumn dasColumn) {
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

    private void doOptimize(Project project) {
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                for (VirtualFile virtualFile : vfs) {
                    try {
                        PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                        if (Objects.isNull(file)) {
                            continue;
                        }
                        if (file.getFileType() instanceof JavaFileType) {
                            CodeStyleManager.getInstance(project).reformat(file);
                            PsiJavaFile javaFile = (PsiJavaFile) file;
                            JavaCodeStyleManager.getInstance(project).optimizeImports(javaFile);
                            JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
                        }
                        file.navigate(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                vfs.clear();
            }
        }.execute());
    }


}
