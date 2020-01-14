package com.efp.plugins.codeHelper.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DubboXmlConfigUtils;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.generator.*;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTableKey;
import com.intellij.database.model.DasTypedObject;
import com.intellij.database.model.MultiRef;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateOption extends DialogWrapper {
    private JPanel jpanel;
    private JCheckBox serviceImpl;
    private JCheckBox mapper;
    private JCheckBox domain;
    private JCheckBox service;
    private JCheckBox controller;
    private JCheckBox dao;
    private JCheckBox vo;
    private JCheckBox isOverWrite;
    private JCheckBox dubboConfig;
    private JCheckBox openGenerateFile;

    private AnActionEvent e;
    private GenerateInfo generateInfo;
    private List<VirtualFile> vfs = new ArrayList<>();

    public GenerateOption(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
        getCache(generateInfo);
        if (generateInfo.getApiModule() == null) {
            controller.setSelected(false);
            controller.disable();
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        serviceImpl = new JCheckBox();
        mapper = new JCheckBox();
        domain = new JCheckBox();
        service = new JCheckBox();
        controller = new JCheckBox();
        dao = new JCheckBox();
        vo = new JCheckBox();
        isOverWrite = new JCheckBox();
        dubboConfig = new JCheckBox();
        openGenerateFile = new JCheckBox();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    @Override
    protected void doOKAction() {
        boolean[] strings = new boolean[]{
                serviceImpl.isSelected(),
                mapper.isSelected(),
                domain.isSelected(),
                service.isSelected(),
                controller.isSelected(),
                dao.isSelected(),
                vo.isSelected(),
                isOverWrite.isSelected(),
                dubboConfig.isSelected()
        };
        setCache();
        super.doOKAction();
        vfs.clear();
        startGenerate(e, generateInfo);
    }

    private void startGenerate(AnActionEvent e, final GenerateInfo generateInfo) {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    try {
                        VirtualFile domainFile = null;
                        if (domain.isSelected()) {
                            domainFile = new DomainGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.DOMAIN).generate();
                        }
                        VirtualFile voFile = null;
                        if (vo.isSelected()) {
                            voFile = new VoGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.VO).generate();
                        }
                        VirtualFile daoFile = null;
                        if (dao.isSelected()) {
                            daoFile = new DaoGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.DAO).generate();
                        }
                        VirtualFile serviceFile = null;
                        if (service.isSelected()) {
                            serviceFile = new ServiceGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.SERVICE).generate();
                        }
                        VirtualFile serviceImplFile = null;
                        if (serviceImpl.isSelected()) {
                            serviceImplFile = new ServiceImplGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.SERVICEIMPL).generate();
                        }
                        VirtualFile mapperFile = null;
                        if (mapper.isSelected()) {
                            mapperFile = new MapperGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.MAPPER).generate();
                        }
                        if (dubboConfig.isSelected() && !Objects.isNull(serviceFile)) {
                            generateDubboConfig(e, generateInfo, serviceFile);
                        }
                        //Controller
                        VirtualFile controllerFile = null;
                        if (controller.isSelected()&&!Objects.isNull(generateInfo.getApiModule())) {
                            controllerFile = new ControllerGenerator(isOverWrite.isSelected(), generateInfo, TemplateFileNameEnum.CONTROLLER).generate();
                        }
                        addVfs(domainFile, voFile, daoFile, serviceFile, serviceImplFile, mapperFile, controllerFile);
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
                PsiDocumentManager.getInstance(project).commitAllDocuments();
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
                        if (openGenerateFile.isSelected()) {
                            file.navigate(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                vfs.clear();
            }
        }.execute());
    }

    private void getCache(GenerateInfo generateInfo) {
        serviceImpl.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("serviceImpl_efp_helper"));
        mapper.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("mapper_efp_helper"));
        domain.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("domain_efp_helper"));
        service.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("servic1e_efp_helper"));
        controller.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("controller_efp_helper"));
        dao.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("dao_efp_helper"));
        vo.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("vo_efp_helper"));
        isOverWrite.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("isOverWrite_efp_helper"));
        dubboConfig.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("dubboConfig_efp_helper"));
        openGenerateFile.setSelected(PropertiesComponent.getInstance(generateInfo.getProject()).getBoolean("openGenerateFile_efp_helper"));
    }

    private void setCache() {
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("serviceImpl_efp_helper", serviceImpl.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("mapper_efp_helper", mapper.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("domain_efp_helper", domain.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("servic1e_efp_helper", service.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("controller_efp_helper", controller.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("dao_efp_helper", dao.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("vo_efp_helper", vo.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("isOverWrite_efp_helper", isOverWrite.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("dubboConfig_efp_helper", dubboConfig.isSelected());
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue("openGenerateFile_efp_helper", openGenerateFile.isSelected());
    }
}
