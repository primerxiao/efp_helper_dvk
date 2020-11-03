package com.efp.plugins.coder.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.data.EfpCovert;
import com.efp.common.data.EfpModuleType;
import com.efp.common.util.StringUtils;
import com.efp.plugins.coder.bean.GenerateInfo;
import com.efp.plugins.coder.generator.Generator;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.StringWriter;

public class GenerateOptionSelect extends DialogWrapper {

    private JPanel jPanel;
    private JCheckBox mapper;
    private JCheckBox dao;
    private JCheckBox service;
    private JCheckBox serviceImpl;
    private JLabel jLabel;

    private AnActionEvent e;
    private GenerateInfo generateInfo;
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jPanel;
    }
    public GenerateOptionSelect(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        this.mapper.setSelected(true);
        this.dao.setSelected(true);
        this.service.setSelected(true);
        this.serviceImpl.setSelected(true);
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        try {
            generateSelect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateSelect() {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    try {
                        if (mapper.isSelected()) {
                            generateSelectMapper();
                        }
                        if (dao.isSelected()) {
                            generateSelectDao();
                        }
                        if (service.isSelected()) {
                            generateSelectService();
                        }
                        if (serviceImpl.isSelected()) {
                            generateSelectServiceImpl();
                        }
                        //保存文档
                        FileDocumentManagerImpl.getInstance().saveAllDocuments();
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                "Generate Success", NotificationType.INFORMATION));
                    } catch (Exception e) {
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                "Generate Error", NotificationType.ERROR));
                    }
                });
            }
        });
    }

    private void generateSelectService() {
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.SERVICE);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getServiceFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("service file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText("List<"+generateInfo.getGenerateJava().getVoClassName() + "> " + generateInfo.getGenerateJava().getCurrentMethodName() + "(" + generateInfo.getGenerateJava().getVoClassName() + " " + StringUtils.initCap(generateInfo.getGenerateJava().getVoClassName()) + ")  throws Exception;", aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private void generateSelectServiceImpl() throws Exception {
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getServiceImplFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("serviceImpl file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        StringWriter sw = new StringWriter();
        Template template = Generator.freemarker.getTemplate("select_serviceImpl.ftl");
        template.process(generateInfo,sw);
        PsiJavaFile ftlJavaFile = (PsiJavaFile) PsiFileFactory.getInstance(e.getProject()).createFileFromText(JavaLanguage.INSTANCE, sw.toString().replaceAll("\r\n", "\n"));
        final PsiMethod[] methods = ftlJavaFile.getClasses()[0].getMethods();
        aClass.add(methods[0]);
        psiFile.navigate(true);
    }

    private void generateSelectDao() throws Exception{
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getDaoFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("dao file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText("List<"+generateInfo.getGenerateJava().getDomainClassName() + "> " + generateInfo.getGenerateJava().getCurrentMethodName() + "(" + generateInfo.getGenerateJava().getDomainClassName() + " " + StringUtils.initCap(generateInfo.getGenerateJava().getDomainClassName()) + ");", aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private final void generateSelectMapper() throws Exception {
        //获取mapper insert or update mapper
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getMapperFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        StringWriter sw = new StringWriter();
        Template template = Generator.freemarker.getTemplate("select_mapper.ftl");
        template.process(generateInfo,sw);
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(e.getProject()).createTagFromText(sw.toString().replaceAll("\r\n","\n"), XMLLanguage.INSTANCE);
        mapperFile.getRootTag().addSubTag(tagFromText,false);
        mapperFile.navigate(true);
    }
}
