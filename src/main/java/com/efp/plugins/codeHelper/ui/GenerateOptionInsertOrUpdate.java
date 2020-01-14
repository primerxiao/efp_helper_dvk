package com.efp.plugins.codeHelper.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.data.EfpCovert;
import com.efp.common.data.EfpModuleType;
import com.efp.common.util.StringUtils;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.efp.plugins.codeHelper.generator.*;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.ImportStatementElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.xmlbeans.XmlLanguage;
import org.apache.xmlbeans.impl.values.XmlLanguageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public class GenerateOptionInsertOrUpdate extends DialogWrapper {

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
    public GenerateOptionInsertOrUpdate(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        this.mapper.setSelected(true);
        this.dao.setSelected(true);
        this.service.setSelected(true);
        this.serviceImpl.setSelected(true);
        setOKButtonText("确定");
        setCancelButtonText("取消");
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        try {
            generateInsertOrUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateInsertOrUpdate() {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    try {
                        if (mapper.isSelected()) {
                            generateInsertOrUpdateMapper();
                        }
                        if (dao.isSelected()) {
                            generateInsertOrUpdateDao();
                        }
                        if (service.isSelected()) {
                            generateInsertOrUpdateService();
                        }
                        if (serviceImpl.isSelected()) {
                            generateInsertOrUpdateServiceImpl();
                        }
                        //保存文档
                        FileDocumentManagerImpl.getInstance().saveAllDocuments();
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                "所有文件生成完成", NotificationType.INFORMATION));
                    } catch (Exception e) {
                        String message = CompilerBundle.message("message.tect.package.file.io.error", e.toString());
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                                message, NotificationType.ERROR));
                    }
                });
            }
        });
    }

    private void generateInsertOrUpdateService() {
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.SERVICE);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getServiceFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("获取不到dao文件");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText("int insertOrUpdate(List<"+generateInfo.getGenerateJava().getVoClassName()+"> list);",aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private void generateInsertOrUpdateServiceImpl() throws Exception {
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getServiceImplFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("获取不到dao文件");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];

        StringWriter sw = new StringWriter();
        Template template = Generator.freemarker.getTemplate("insert_or_update_serviceImpl.ftl");
        template.process(generateInfo,sw);
        PsiJavaFile ftlJavaFile = (PsiJavaFile) PsiFileFactory.getInstance(e.getProject()).createFileFromText(JavaLanguage.INSTANCE, sw.toString().replaceAll("\r\n", "\n"));
        final PsiMethod[] methods = ftlJavaFile.getClasses()[0].getMethods();
        aClass.add(methods[0]);
        psiFile.navigate(true);
    }

    private void generateInsertOrUpdateDao() throws Exception{
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getDaoFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("获取不到dao文件");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText("int insertOrUpdate(List<"+generateInfo.getGenerateJava().getDomainClassName()+"> list);",aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private final void generateInsertOrUpdateMapper() throws Exception {
        //获取mapper insert or update mapper
        final Module module = EfpCovert.getModule(e.getProject(), generateInfo.getDasNamespace(), EfpModuleType.IMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getGenerateJava().getMapperFileName(), module.getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("获取不到maper文件");
        }
        StringWriter sw = new StringWriter();
        Template template = Generator.freemarker.getTemplate("insert_or_update_mapper.ftl");
        template.process(Generator.covertToMapperInfo(generateInfo),sw);
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(e.getProject()).createTagFromText(sw.toString().replaceAll("\r\n","\n"), XMLLanguage.INSTANCE);
        mapperFile.getRootTag().addSubTag(tagFromText,false);
        mapperFile.navigate(true);
    }
}
