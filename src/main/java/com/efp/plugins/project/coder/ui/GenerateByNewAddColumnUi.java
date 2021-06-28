package com.efp.plugins.project.coder.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.StringUtils;
import com.efp.plugins.project.coder.bean.ClassField;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.generator.Generator;
import com.efp.plugins.project.coder.util.GenUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import freemarker.template.Template;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.io.StringWriter;

public class GenerateByNewAddColumnUi extends DialogWrapper {

    private JPanel jpanel;
    private JCheckBox poCheckBox;
    private JCheckBox doCheckBox;
    private JCheckBox inputCheckBox;
    private JCheckBox outputCheckBox;
    private JCheckBox mapperResultMapCheckBox;
    private JCheckBox mapperBaseColumnListCheckBox;
    private JCheckBox mapperInsertSingleCheckBox;
    private JCheckBox mapperUpdateByPKCheckBox;

    private GenerateInfo generateInfo;

    private Project project;

    public GenerateByNewAddColumnUi(@Nullable Project project, GenerateInfo generateInfo) {
        super(project);
        this.generateInfo = generateInfo;
        this.project = project;
        this.setTitle(PluginContants.GENERATOR_UI_TITLE);
        this.setOKActionEnabled(true);
        this.setOKButtonText("操作");
        this.setCancelButtonText("取消");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return jpanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {

            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                WriteCommandAction.runWriteCommandAction(super.getProject(), () -> {
                    if (poCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.PO);
                        generateModle();
                    }
                    if (doCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.DO);
                        generateModle();
                    }
                    if (inputCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.INPUT);
                        generateModle();
                    }
                    if (outputCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.OUTPUT);
                        generateModle();
                    }
                    if (mapperResultMapCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.MAPPER);
                        try {
                            generateMapperResultMap();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mapperBaseColumnListCheckBox.isSelected()) {

                    }
                    if (mapperInsertSingleCheckBox.isSelected()) {

                    }
                    if (mapperUpdateByPKCheckBox.isSelected()) {

                    }
                });
            }
        });
    }

    private void generateMapperResultMap() throws IOException {
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        String resultMapperTxt = "<resultMap type=\"com.fdb.a." + GenUtils.getNameByBaseMoudleName(generateInfo.getCurrentModule().getName()) + ".infra.persistence.po." + generateInfo.getBasicClassName() + "PO\"\n" +
                "               id=\"" + StringUtils.initCap(generateInfo.getBasicClassName()) + "List\">\n";

        for (ClassField classField : generateInfo.getClassFields()) {
            resultMapperTxt = resultMapperTxt + "   <result property=\""+StringUtils.initCap(classField.getFieldName())+"\" column=\""+classField.getDasColumnName()+"\"/>\n";
        }
        resultMapperTxt = resultMapperTxt + "</resultMap>";
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(project).createTagFromText(resultMapperTxt.replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);

        for (XmlTag subTag : mapperFile.getRootTag().getSubTags()) {
            if ((StringUtils.initCap(generateInfo.getBasicClassName()) + "List").equals(subTag.getAttribute("id"))) {
                //相等替换
                mapperFile.addBefore(tagFromText, subTag);
                subTag.delete();
            }
        }
        mapperFile.navigate(true);
    }

    private void generateModle() {
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName == null && filesByName.length <= 0) {
            throw new RuntimeException(generateInfo.getClassName() + " file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        for (DasColumn selectDasColumn : generateInfo.getSelectDasColumns()) {
            //字段名小写
            String fieldNameUnCat = StringUtils.initCap(StringUtils.underlineToCamel(selectDasColumn.getName()));
            //字段名大写
            String fieldNameUpper = StringUtils.upperFirstChar(StringUtils.underlineToCamel(selectDasColumn.getName()));
            //字段备注
            String fieldCommentTxt = "/**\n" +
                    "             * " + selectDasColumn.getComment() + "\n" +
                    "             */\n";
            //字段
            String fieldTxt = "private "+DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName()+" "+fieldNameUnCat+";";
            PsiField fieldFromText = PsiElementFactory.getInstance(project).createFieldFromText(fieldTxt, aClass);
            PsiComment fileCommentFromText = PsiElementFactory.getInstance(project).createDocCommentFromText(fieldCommentTxt, fieldFromText);
            //getter setter
            String getterMethodTxt = "public "+DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName()+" get" + fieldNameUpper + "() {\n" +
                    "   return " + fieldNameUnCat + ";\n" +
                    "}";
            String setterMethodTxt = "public void set" + fieldNameUpper + "("+DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName()+" " + fieldNameUnCat + ") {\n" +
                    "   this.cardNo = " + fieldNameUnCat + ";\n" +
                    "}";
            PsiMethod getterMethod = PsiElementFactory.getInstance(project).createMethodFromText(getterMethodTxt, aClass);
            PsiMethod setterMethod = PsiElementFactory.getInstance(project).createMethodFromText(setterMethodTxt, aClass);
            aClass.add(fieldFromText);
            aClass.addBefore(fileCommentFromText, fieldFromText);
            aClass.add(getterMethod);
            aClass.add(setterMethod);
        }
        psiFile.navigate(true);
    }
}
