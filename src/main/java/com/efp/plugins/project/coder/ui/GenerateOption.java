package com.efp.plugins.project.coder.ui;

import com.alibaba.fastjson.JSON;
import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.PluginStringUtils;
import com.efp.common.util.SofaXmlConfigUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.generator.*;
import com.efp.plugins.project.coder.util.GenUtils;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import freemarker.template.TemplateException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateOption extends DialogWrapper {
    private JPanel jpanel;
    private JCheckBox repositoryCheckBox;
    private JCheckBox mapperCheckBox;
    private JCheckBox facadeCheckBox;
    private JCheckBox controllerCheckBox;
    private JCheckBox openGenerateFileCheckBox;
    private JCheckBox doCheckBox;
    private JCheckBox inputCheckBox;
    private JCheckBox outputCheckBox;
    private JCheckBox poCheckBox;
    private JCheckBox producerCheckBox;
    private JCheckBox consumerCheckBox;
    private JCheckBox daoCheckBox;
    private JCheckBox isOverideCheckBox;
    private JCheckBox repositoryImplCheckBox;
    private JCheckBox facadeImplCheckBox;

    private AnActionEvent e;

    private GenerateInfo generateInfo;

    private List<VirtualFile> vfs = new ArrayList<>();

    private String generate_checkbox_cache = "generate_checkbox_cache";
    ;

    public GenerateOption(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
        getCache(generateInfo);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        repositoryCheckBox = new JCheckBox();
        mapperCheckBox = new JCheckBox();
        facadeCheckBox = new JCheckBox();
        controllerCheckBox = new JCheckBox();
        openGenerateFileCheckBox = new JCheckBox();
        doCheckBox = new JCheckBox();
        inputCheckBox = new JCheckBox();
        outputCheckBox = new JCheckBox();
        poCheckBox = new JCheckBox();
        producerCheckBox = new JCheckBox();
        consumerCheckBox = new JCheckBox();
        daoCheckBox = new JCheckBox();
        isOverideCheckBox = new JCheckBox();
        repositoryImplCheckBox = new JCheckBox();
        facadeImplCheckBox = new JCheckBox();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    @Override
    protected void doOKAction() {
        setCache();
        super.doOKAction();
        vfs.clear();
        startGenerate(e, generateInfo);
    }

    /**
     * 开始创建
     *
     * @param e            事件
     * @param generateInfo 生成信息
     */
    private void startGenerate(AnActionEvent e, final GenerateInfo generateInfo) {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    VirtualFile daoFile = null;
                    if (daoCheckBox.isSelected()) {
                        try {
                            daoFile = new ModleGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.DAO).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成dao文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile repositoryFile = null;
                    if (repositoryCheckBox.isSelected()) {
                        try {
                            repositoryFile = new RepositoryGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.REPOSITORY).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成repository文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile repositoryImplFile = null;
                    if (repositoryImplCheckBox.isSelected()) {
                        try {
                            repositoryImplFile = new RepositoryGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.REPOSITORYIMP).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成repositoryImpl文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile mapperFile = null;
                    if (mapperCheckBox.isSelected()) {
                        try {
                            mapperFile = new MapperGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.MAPPER).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成mapper-xml文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile facadeFile = null;
                    if (facadeCheckBox.isSelected()) {
                        try {
                            facadeFile = new FacadeGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.FACADE).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成facade文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile facadeImplFile = null;
                    if (facadeImplCheckBox.isSelected()) {
                        try {
                            facadeImplFile = new FacadeGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.FACADEIMPL).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成facadeImpl文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile controllerFile = null;
                    if (controllerCheckBox.isSelected()) {

                    }
                    VirtualFile doFile = null;
                    if (doCheckBox.isSelected()) {
                        try {
                            doFile = new ModleGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.DO).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成do文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile inputFile = null;
                    if (inputCheckBox.isSelected()) {
                        try {
                            inputFile = new ModleGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.INPUT).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成input文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile outputFile = null;
                    if (outputCheckBox.isSelected()) {
                        try {
                            outputFile = new ModleGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.OUTPUT).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                    "生成output文件失败，信息为：" + ioException.getMessage(), NotificationType.ERROR));
                        }
                    }
                    VirtualFile poFile = null;
                    if (poCheckBox.isSelected()) {
                        try {
                            poFile = new ModleGenerator(isOverideCheckBox.isSelected(), generateInfo, TemplateFileNameEnum.PO).generate();
                        } catch (IOException | TemplateException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    VirtualFile producerFile = null;
                    if (producerCheckBox.isSelected()) {
                        generateDubboConfig(e, generateInfo, true);
                    }
                    VirtualFile consumerFile = null;
                    if (consumerCheckBox.isSelected()) {
                        generateDubboConfig(e, generateInfo, false);
                    }
                    addVfs(daoFile, repositoryFile, repositoryImplFile, mapperFile, facadeFile, facadeImplFile, controllerFile, doFile, inputFile, outputFile, poFile, producerFile, consumerFile);
                    doOptimize(e.getProject());
                    //保存文档
                    FileDocumentManagerImpl.getInstance().saveAllDocuments();
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "所有文件生成完成", NotificationType.INFORMATION));
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

    private void generateDubboConfig(@NotNull AnActionEvent e, GenerateInfo generateInfo, boolean provider) {
        //获取service类名
        Module serviceMoudle = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-facade");
        Module startMoudle = ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getBaseMoudleName() + "-start");
        String serviceClassFilePath = FilenameUtils.getFullPath(serviceMoudle.getModuleFilePath())
                + "src/main/java/com/fdb/a/"
                + GenUtils.getNameByBaseMoudleName(generateInfo.getBaseMoudleName())
                + "/facade/";
        String serviceClassName =
                PluginStringUtils.upperFirstChar(PluginStringUtils.underlineToCamel(generateInfo.getDasTable().getName())) + "Service.java";

        VirtualFile fileByIoFile = VfsUtil.findFileByIoFile(new File(serviceClassFilePath + serviceClassName), true);
        if (fileByIoFile == null) {
            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                    "找不到接口类:" + serviceClassName, NotificationType.ERROR));
        }
        PsiFile file = PsiManager.getInstance(generateInfo.getProject()).findFile(fileByIoFile);
        if (!Objects.isNull(file)) {
            if (provider) {
                SofaXmlConfigUtils.poviderXmlConfigSet(e, startMoudle, ((PsiJavaFile) file).getClasses()[0], generateInfo.getBaseMoudleName());
            } else {
                SofaXmlConfigUtils.consumerXmlConfigSet(e, serviceMoudle, ((PsiJavaFile) file).getClasses()[0], generateInfo.getBaseMoudleName());
            }
        }
    }


    private void doOptimize(Project project) {
        ApplicationManager.getApplication().runWriteAction(() -> WriteCommandAction.runWriteCommandAction(project, "格式化文件", null, () -> {
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
                    if (openGenerateFileCheckBox.isSelected()) {
                        file.navigate(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            vfs.clear();
        }));
    }

    private void getCache(GenerateInfo generateInfo) {
        String value = PropertiesComponent.getInstance(generateInfo.getProject()).getValue(generate_checkbox_cache);
        if (StringUtils.isEmpty(value)) {
            return;
        }
        List<Boolean> booleans = JSON.parseArray(value, boolean.class);
        try {
            repositoryCheckBox.setSelected(booleans.get(0));
            mapperCheckBox.setSelected(booleans.get(1));
            facadeCheckBox.setSelected(booleans.get(2));
            controllerCheckBox.setSelected(booleans.get(3));
            openGenerateFileCheckBox.setSelected(booleans.get(4));
            doCheckBox.setSelected(booleans.get(5));
            inputCheckBox.setSelected(booleans.get(6));
            outputCheckBox.setSelected(booleans.get(7));
            poCheckBox.setSelected(booleans.get(8));
            producerCheckBox.setSelected(booleans.get(9));
            consumerCheckBox.setSelected(booleans.get(10));
            daoCheckBox.setSelected(booleans.get(11));
            isOverideCheckBox.setSelected(booleans.get(12));
            repositoryImplCheckBox.setSelected(booleans.get(13));
            facadeImplCheckBox.setSelected(booleans.get(14));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setCache() {
        boolean[] booleans = {
                repositoryCheckBox.isSelected(),
                mapperCheckBox.isSelected(),
                facadeCheckBox.isSelected(),
                controllerCheckBox.isSelected(),
                openGenerateFileCheckBox.isSelected(),
                doCheckBox.isSelected(),
                inputCheckBox.isSelected(),
                outputCheckBox.isSelected(),
                poCheckBox.isSelected(),
                producerCheckBox.isSelected(),
                consumerCheckBox.isSelected(),
                daoCheckBox.isSelected(),
                isOverideCheckBox.isSelected(),
                repositoryImplCheckBox.isSelected(),
                facadeImplCheckBox.isSelected()
        };
        String o = JSON.toJSONString(booleans);
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue(generate_checkbox_cache, o);
    }
}
