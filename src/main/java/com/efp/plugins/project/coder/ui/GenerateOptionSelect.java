package com.efp.plugins.project.coder.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.StringUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.generator.Generator;
import com.efp.plugins.project.coder.template.method.SimpleBaseModuleNameMethod;
import com.efp.plugins.project.coder.util.GenUtils;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
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
import java.util.HashMap;
import java.util.Map;

public class GenerateOptionSelect extends DialogWrapper {

    private JPanel jPanel;
    private JCheckBox mapper;
    private JCheckBox dao;
    private JCheckBox service;
    private JCheckBox serviceImpl;
    private JLabel jLabel;
    private JCheckBox repository;
    private JCheckBox repositoryImpl;

    private AnActionEvent e;
    private GenerateInfo generateInfo;
    private String methodName;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jPanel;
    }

    public GenerateOptionSelect(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo, String methodName) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        this.mapper.setSelected(true);
        this.dao.setSelected(true);
        this.service.setSelected(true);
        this.serviceImpl.setSelected(true);
        this.methodName = methodName;
        this.repository.setSelected(true);
        this.repositoryImpl.setSelected(true);
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
                        if (repository.isSelected()) {
                            generateSelectRepository();
                        }
                        if (repositoryImpl.isSelected()) {
                            generateSelectRepositoryImpl();
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

    private void generateSelectServiceImpl() {
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.FACADEIMPL);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("facadeImpl file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        String methodText = "@Override\n" +
                "    public List<" + generateInfo.getBasicClassName() + "Output> " + methodName + "(" + generateInfo.getBasicClassName() + "Input input) {\n" +
                "        List<" + generateInfo.getBasicClassName() + "DO> " + StringUtils.initCap(generateInfo.getBasicClassName()) + "DOS = " + StringUtils.initCap(generateInfo.getBasicClassName()) + "Repository.queryList(mapperFacade.map(input, " + generateInfo.getBasicClassName() + "DO.class));\n" +
                "        return mapperFacade.mapAsList(" + StringUtils.initCap(generateInfo.getBasicClassName()) + "DOS, " + generateInfo.getBasicClassName() + "Output.class);\n" +
                "    }";
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText(methodText, aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private void generateSelectService() {
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.FACADE);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("facade file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];

        StringBuilder methodTextBuilder = new StringBuilder();
        //CrdtApplInfoDO queryByPk(CrdtApplInfoDO crdtApplInfoDO);
        methodTextBuilder.append("List<");
        methodTextBuilder.append(generateInfo.getBasicClassName());
        methodTextBuilder.append("Output>");
        methodTextBuilder.append(" ");
        methodTextBuilder.append(methodName + "(");
        methodTextBuilder.append(generateInfo.getBasicClassName() + "Input");
        methodTextBuilder.append(" ");
        methodTextBuilder.append("input");
        methodTextBuilder.append(");");
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText(methodTextBuilder.toString(), aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private void generateSelectRepositoryImpl() throws Exception {
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.REPOSITORYIMP);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("serviceImpl file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];


        String methodText = "@Override\n" +
                "    public List<" + generateInfo.getBasicClassName() + "DO> " + methodName + "(" + generateInfo.getBasicClassName() + "DO " + StringUtils.initCap(generateInfo.getBasicClassName()) + "DO) {\n" +
                "        List<" + generateInfo.getBasicClassName() + "PO> " + StringUtils.initCap(generateInfo.getBasicClassName()) + "s = " + StringUtils.initCap(generateInfo.getBasicClassName()) + "Mapper.queryList(\n" +
                "                mapperFacade.map(" + StringUtils.initCap(generateInfo.getBasicClassName()) + "DO, " + generateInfo.getBasicClassName() + "PO.class)\n" +
                "        );\n" +
                "        return mapperFacade.mapAsList(" + StringUtils.initCap(generateInfo.getBasicClassName()) + "s, " + generateInfo.getBasicClassName() + "DO.class);\n" +
                "    }";
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText(methodText, aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private void generateSelectRepository() {
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.REPOSITORY);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("repository file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];

        StringBuilder methodTextBuilder = new StringBuilder();
        //CrdtApplInfoDO queryByPk(CrdtApplInfoDO crdtApplInfoDO);
        methodTextBuilder.append("List<");
        methodTextBuilder.append(generateInfo.getBasicClassName());
        methodTextBuilder.append("DO>");
        methodTextBuilder.append(" ");
        methodTextBuilder.append(methodName + "(");
        methodTextBuilder.append(generateInfo.getBasicClassName() + "DO");
        methodTextBuilder.append(" ");
        methodTextBuilder.append(StringUtils.initCap(generateInfo.getBasicClassName()) + "DO");
        methodTextBuilder.append(");");
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText(methodTextBuilder.toString(), aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }


    private void generateSelectDao() throws Exception {
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.DAO);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("dao file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];

        StringBuilder methodTextBuilder = new StringBuilder();
        //List<BdLoanpayPO> queryByPk(BdLoanpayPO bdLoanpayPO);
        methodTextBuilder.append("List<");
        methodTextBuilder.append(generateInfo.getBasicClassName());
        methodTextBuilder.append("PO>");
        methodTextBuilder.append(" ");
        methodTextBuilder.append(methodName + "(");
        methodTextBuilder.append(generateInfo.getBasicClassName() + "PO");
        methodTextBuilder.append(" ");
        methodTextBuilder.append(StringUtils.initCap(generateInfo.getBasicClassName()) + "PO");
        methodTextBuilder.append(");");
        PsiMethod method = PsiElementFactory.getInstance(e.getProject()).createMethodFromText(methodTextBuilder.toString(), aClass);
        aClass.add(method);
        psiFile.navigate(true);
    }

    private final void generateSelectMapper() throws Exception {
        //获取mapper
        GenUtils.setValue(generateInfo, TemplateFileNameEnum.MAPPER);
        PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        StringWriter sw = new StringWriter();
        Template template = Generator.freemarker.getTemplate("select_mapper.ftl");
        template.process(processValue(), sw);
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(e.getProject()).createTagFromText(sw.toString().replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);
        mapperFile.getRootTag().addSubTag(tagFromText, false);
        mapperFile.navigate(true);
    }

    public Map<String, Object> processValue() {
        HashMap<String, Object> root = new HashMap<>();
        root.put("generateInfo", generateInfo);
        root.put("simpleBaseModuleNameMethod", new SimpleBaseModuleNameMethod());
        root.put("methodName", methodName);
        return root;
    }
}
