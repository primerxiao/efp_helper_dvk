package com.efp.plugins.project.coder.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.PluginStringUtils;
import com.efp.plugins.project.coder.bean.ClassField;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.util.GenUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasNamed;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 86134
 */
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
    private JTextField insertSingleAliasName;
    private JTextField updateByPKAliasName;

    private final GenerateInfo generateInfo;

    private final Project project;

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
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.MAPPER);
                        try {
                            generateMapperBaseColumnList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mapperInsertSingleCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.MAPPER);
                        try {
                            generateMapperInsertSingle();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mapperUpdateByPKCheckBox.isSelected()) {
                        GenUtils.setValue(generateInfo, TemplateFileNameEnum.MAPPER);
                        try {
                            generateMapperUpdateByPk();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void generateMapperUpdateByPk() throws IOException {
        String xmlIdValue = "updateByPk";
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(updateByPKAliasName.getText())) {
            xmlIdValue = updateByPKAliasName.getText();
        }
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        String resultMapperTxt = "<update id=\"" + xmlIdValue + "\" parameterType=\"com.fdb.a." + GenUtils.getNameByBaseMoudleName(generateInfo.getBaseMoudleName()) + ".infra.persistence.po." + generateInfo.getBasicClassName() + "PO\">\n" +
                "        update\n" +
                "            " + generateInfo.getDasTable().getName() + "\n" +
                "        <trim prefix=\"set\" suffixOverrides=\",\">\n"
                +
                generateInfo.getClassFields().stream().map(c ->
                        "                <if test=\"" + PluginStringUtils.initCap(c.getFieldName()) + "!=null\">\n" +
                                "                    " + c.getDasColumnName() + "=#{" + PluginStringUtils.initCap(c.getFieldName()) + "},\n" +
                                "                </if>"
                ).collect(Collectors.joining("\n"))
                +
                "        </trim>\n" +
                "        where\n" +
                generateInfo.getPkClassFields().stream().map(c -> "   " + c.getDasColumnName() + "=#{" + PluginStringUtils.initCap(c.getFieldName()) + "}\n").collect(Collectors.joining("   and")) +
                "\n" +
                "</update>";

        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(project).createTagFromText(resultMapperTxt.replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);

        for (XmlTag subTag : Objects.requireNonNull(mapperFile.getRootTag()).getSubTags()) {
            if (xmlIdValue.equals(Objects.requireNonNull(subTag.getAttribute("id")).getValue())) {
                //相等替换
                mapperFile.addBefore(tagFromText, subTag);
                subTag.delete();
            }
        }
        mapperFile.navigate(true);
    }


    private void generateMapperInsertSingle() throws IOException {
        String xmlIdValue = "insertSingle";
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(insertSingleAliasName.getText())) {
            xmlIdValue = insertSingleAliasName.getText();
        }
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        String resultMapperTxt = "<insert id=\"" + xmlIdValue + "\" parameterType=\"com.fdb.a." + GenUtils.getNameByBaseMoudleName(generateInfo.getBaseMoudleName()) + ".infra.persistence.po." + generateInfo.getBasicClassName() + "PO\">\n"
                + "insert into " + generateInfo.getDasTable().getName() + "("
                + generateInfo.getClassFields().stream().map(ClassField::getDasColumnName).collect(Collectors.joining(","))
                + ") \nvalues ("
                + generateInfo.getClassFields().stream().map(c -> "#{" + PluginStringUtils.initCap(c.getFieldName()) + "}").collect(Collectors.joining(","))
                + ")\n"
                + "</insert>";

        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(project).createTagFromText(resultMapperTxt.replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);

        for (XmlTag subTag : Objects.requireNonNull(mapperFile.getRootTag()).getSubTags()) {
            if (xmlIdValue.equals(Objects.requireNonNull(subTag.getAttribute("id")).getValue())) {
                //相等替换
                mapperFile.addBefore(tagFromText, subTag);
                subTag.delete();
            }
        }
        mapperFile.navigate(true);
    }

    private void generateMapperBaseColumnList() throws IOException {
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        String resultMapperTxt = "<sql id=\"Base_Column_List\">\n"
                + "        "
                + generateInfo.getClassFields().stream().map(ClassField::getDasColumnName).collect(Collectors.joining(","))
                + "\n</sql>";
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(project).createTagFromText(resultMapperTxt.replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);

        for (XmlTag subTag : Objects.requireNonNull(mapperFile.getRootTag()).getSubTags()) {
            if ("Base_Column_List".equals(Objects.requireNonNull(subTag.getAttribute("id")).getValue())) {
                //相等替换
                mapperFile.addBefore(tagFromText, subTag);
                subTag.delete();
            }
        }
        mapperFile.navigate(true);
    }

    private void generateMapperResultMap() throws IOException {
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName.length <= 0) {
            throw new RuntimeException("mapper file not found");
        }
        StringBuilder resultMapperTxt = new StringBuilder("<resultMap type=\"com.fdb.a." + GenUtils.getNameByBaseMoudleName(generateInfo.getCurrentModule().getName()) + ".infra.persistence.po." + generateInfo.getBasicClassName() + "PO\"" +
                "id=\"" + PluginStringUtils.initCap(generateInfo.getBasicClassName()) + "List\">\n");

        for (ClassField classField : generateInfo.getClassFields()) {
            resultMapperTxt.append("   <result property=\"").append(PluginStringUtils.initCap(classField.getFieldName())).append("\" column=\"").append(classField.getDasColumnName()).append("\"/>\n");
        }
        resultMapperTxt.append("</resultMap>");
        XmlFile mapperFile = (XmlFile) filesByName[0];
        XmlTag tagFromText = XmlElementFactory.getInstance(project).createTagFromText(resultMapperTxt.toString().replaceAll("\r\n", "\n"), XMLLanguage.INSTANCE);

        for (XmlTag subTag : Objects.requireNonNull(mapperFile.getRootTag()).getSubTags()) {
            if ((PluginStringUtils.initCap(generateInfo.getBasicClassName()) + "List").equals(Objects.requireNonNull(subTag.getAttribute("id")).getValue())) {
                //相等替换
                mapperFile.addBefore(tagFromText, subTag);
                subTag.delete();
            }
        }
        mapperFile.navigate(true);
    }

    private void generateModle() {
        PsiFile[] filesByName = FilenameIndex.getFilesByName(project, generateInfo.getFileName(), generateInfo.getCurrentModule().getModuleScope());
        if (filesByName.length <= 0) {
            throw new RuntimeException(generateInfo.getClassName() + " file not found");
        }
        PsiJavaFile psiFile = (PsiJavaFile) filesByName[0];
        PsiClass aClass = psiFile.getClasses()[0];
        for (DasColumn selectDasColumn : generateInfo.getSelectDasColumns()) {
            //字段名小写
            String fieldNameUnCat = PluginStringUtils.initCap(PluginStringUtils.underlineToCamel(selectDasColumn.getName()));
            //字段名大写
            String fieldNameUpper = PluginStringUtils.upperFirstChar(PluginStringUtils.underlineToCamel(selectDasColumn.getName()));
            //字段
            String fieldTxt = "/**\n" +
                    "* " + selectDasColumn.getComment() + "\n" +
                    "*/\n" +
                    "private " + DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName() + " " + fieldNameUnCat + ";";
            PsiField fieldFromText = PsiElementFactory.getInstance(project).createFieldFromText(fieldTxt, aClass);
            //getter setter
            String getterMethodTxt = "public " + DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName() + " get" + fieldNameUpper + "() {\n" +
                    "   return " + fieldNameUnCat + ";\n" +
                    "}";
            String setterMethodTxt = "public void set" + fieldNameUpper + "(" + DasUtils.getJavaTypeClass(selectDasColumn).getSimpleName() + " " + fieldNameUnCat + ") {\n" +
                    "   this." + fieldNameUnCat + " = " + fieldNameUnCat + ";\n" +
                    "}";
            PsiMethod getterMethod = PsiElementFactory.getInstance(project).createMethodFromText(getterMethodTxt, aClass);
            PsiMethod setterMethod = PsiElementFactory.getInstance(project).createMethodFromText(setterMethodTxt, aClass);

            addField(aClass, generateInfo, selectDasColumn, fieldFromText, getterMethod, setterMethod);
        }
        psiFile.navigate(true);
    }

    private void addField(PsiClass aClass, GenerateInfo generateInfo, DasColumn selectDasColumn, PsiField fieldFromText, PsiMethod getterMethod, PsiMethod setterMethod) {
        //判断当前字段位置 如果是首位则需要添加到后一位前面 如果非首位则添加到上一位后面
        List<String> columnNames = generateInfo.getDasColumns().stream().map(DasNamed::getName).collect(Collectors.toList());
        int i = columnNames.indexOf(selectDasColumn.getName());
        if (i == 0) {
            aClass.addBefore(fieldFromText, aClass.getFields()[0]);
            aClass.addBefore(getterMethod, aClass.getMethods()[0]);
            aClass.addAfter(setterMethod, aClass.getMethods()[0]);
            return;
        }
        PsiField psiField = Arrays.stream(aClass.getFields()).filter(field -> field.getName().equals(PluginStringUtils.underlineToCamel(columnNames.get(i - 1)))).findFirst().orElse(null);
        //如果找不到 获取最后一个吧
        if (!Objects.isNull(psiField)) {
            aClass.addAfter(fieldFromText, psiField);
            PsiMethod preSetterMethod = Arrays.stream(aClass.getMethods()).filter(m -> m.getName().equals("set" + PluginStringUtils.upperFirstChar(psiField.getName()))).findFirst().orElse(null);
            aClass.addAfter(getterMethod, preSetterMethod);
            PsiMethod curGetterMethod = Arrays.stream(aClass.getMethods()).filter(m -> m.getName().equals(getterMethod.getName())).findFirst().orElse(null);
            aClass.addAfter(setterMethod, curGetterMethod);
            return;
        }
        aClass.add(fieldFromText);
        aClass.add(getterMethod);
        aClass.add(setterMethod);
    }

}
