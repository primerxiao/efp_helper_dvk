package com.efp.plugins.project.coder.action;

import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.EditorUtils;
import com.efp.common.util.PluginStringUtils;
import com.efp.plugins.project.coder.util.GenUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.FilenameIndex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 从数据库的字段remark信息同步都数据库
 *
 * @author HIFeng
 */
public class RewriteRemarkAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DasUtils.refreshDas(e);
        EditorUtils.closeAllEditor(e);
        //获取数据库配置
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof DasTable)) {
            Messages.showErrorDialog("请选择一个数据库表对象进行操作", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        //弹出基础module选择
        IPopupChooserBuilder<String> stringIPopupChooserBuilder = JBPopupFactory.getInstance().createPopupChooserBuilder(PluginContants.CHOOSE_MODULE_NAMES).setItemChosenCallback(b -> {
            process(e.getProject(), (DasTable) psiElement, b);
        });
        stringIPopupChooserBuilder.setAdText("选择存储代码的应用");
        stringIPopupChooserBuilder.createPopup().showCenteredInCurrentWindow(e.getProject());
    }

    public void process(Project project, DasTable dasTable, String baseModuleName) {
        String tableName = dasTable.getName();
        String baseClassName = PluginStringUtils.upperFirstChar(PluginStringUtils.underlineToCamel(tableName));
        List<? extends DasColumn> dasColumns = DasUtil.getColumns(dasTable).toList();
        Map<String, String> commentMap = dasColumns.stream().collect(Collectors.toMap(
                dasColumn -> PluginStringUtils.underlineToCamel(dasColumn.getName()),
                dasColumn -> org.apache.commons.lang3.StringUtils.isEmpty(dasColumn.getComment()) ? "" : dasColumn.getComment()));
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                WriteCommandAction.runWriteCommandAction(super.getProject(), () -> {
                    indicator.setText("开始处理po模型");
                    indicator.setFraction(20);
                    //po
                    processModle("PO", TemplateFileNameEnum.PO);
                    //do
                    indicator.setText("开始处理do模型");
                    indicator.setFraction(40);
                    processModle("DO", TemplateFileNameEnum.DO);
                    //input
                    indicator.setText("开始处理input模型");
                    indicator.setFraction(80);
                    processModle("Input", TemplateFileNameEnum.INPUT);
                    //out
                    indicator.setText("开始处理output模型");
                    indicator.setFraction(100);
                    processModle("Output", TemplateFileNameEnum.OUTPUT);
                });
            }

            private void processModle(String suffix, TemplateFileNameEnum templateFileNameEnum) {
                Module poModule = GenUtils.getModule(project, baseModuleName, templateFileNameEnum);
                if (poModule == null) {
                    return;
                }
                @NotNull PsiFile[] filesByName = FilenameIndex.getFilesByName(project, baseClassName + suffix + ".java", poModule.getModuleContentScope());
                if (filesByName.length > 0) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) filesByName[0];
                    PsiClass[] classes = psiJavaFile.getClasses();
                    for (PsiClass aClass : classes) {
                        if (!aClass.getName().equals(baseClassName + suffix)) {
                            continue;
                        }
                        PsiField[] fields = aClass.getFields();
                        for (PsiField field : fields) {
                            if (commentMap.containsKey(field.getName())) {
                                //字段匹配 --修改comment
                                PsiDocComment docComment = field.getDocComment();
                                if (docComment != null) {
                                    docComment.delete();
                                }
                                //根据新的信息创建文档主席
                                String fiedlTargetRemark = commentMap.get(field.getName());
                                if (org.apache.commons.lang3.StringUtils.isEmpty(fiedlTargetRemark)) {
                                    continue;
                                }
                                fiedlTargetRemark = fiedlTargetRemark.replaceAll("\r\n", "\n");
                                String[] split = fiedlTargetRemark.split("\n");
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("/**");
                                for (String value : split) {
                                    stringBuilder.append("\n* ");
                                    stringBuilder.append(value);
                                }
                                stringBuilder.append("\n");
                                stringBuilder.append("*/");
                                PsiDocComment docCommentFromText = PsiElementFactory.getInstance(project).createDocCommentFromText(stringBuilder.toString(), field);
                                field.addBefore(docCommentFromText, field);
                            }
                        }
                    }
                    psiJavaFile.navigate(true);
                }
            }
        });
    }


}
