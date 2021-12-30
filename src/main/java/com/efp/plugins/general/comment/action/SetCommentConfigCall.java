package com.efp.plugins.general.comment.action;

import com.efp.common.util.PluginStringUtils;
import com.efp.plugins.general.comment.bean.TestAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 86134
 */
public class SetCommentConfigCall extends PsiElementBaseIntentionAction {
    @Override
    public @IntentionName @NotNull String getText() {
        return getFamilyName();
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "自动补充注释";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        TestAction testAction = new TestAction();
        testAction.setTest("");
        if (psiElement instanceof PsiIdentifier) {
            String text = ((PsiIdentifier) psiElement).getText();
            if (StringUtils.isNotEmpty(text) && text.length() > 3) {
                if (text.startsWith("set")) {
                    //set函数
                    PsiMethodCallExpression parentOfType = PsiTreeUtil.getParentOfType(psiElement, PsiMethodCallExpression.class);
                    assert parentOfType != null;
                    String setMethodName = parentOfType.getMethodExpression().getReferenceName();
                    assert setMethodName != null;
                    //类名
                    PsiClassReferenceType type = (PsiClassReferenceType) (parentOfType.getMethodExpression().getQualifierExpression().getType());
                    assert type != null;
                    String className = type.getClassName();
                    @NotNull PsiFile[] filesByName = FilenameIndex.getFilesByName(project, className + ".java", GlobalSearchScope.projectScope(project));
                    if (filesByName.length < 1) {
                        return;
                    }
                    PsiJavaFile javaFile = (PsiJavaFile) filesByName[0];
                    PsiClass[] classes = javaFile.getClasses();
                    PsiClass psiClass = Arrays.stream(classes).filter(c -> Objects.equals(c.getName(), className)).findFirst().orElse(null);
                    assert psiClass != null;
                    PsiField[] fields = psiClass.getFields();
                    PsiField psiField = Arrays.stream(fields).filter(f -> setMethodName.equals("set" + PluginStringUtils.upperFirstChar(f.getName()))).findFirst().orElse(null);
                    assert psiField != null;
                    PsiDocComment docComment = psiField.getDocComment();
                    assert docComment != null;
                    PsiElement[] descriptionElements = docComment.getDescriptionElements();
                    String collect = Arrays.stream(descriptionElements)
                            .filter(d -> StringUtils.isNotEmpty(d.getText()))
                            .map(e -> e.getText().trim())
                            .collect(Collectors.joining(" "));
                    PsiComment commentFromText = PsiElementFactory.getInstance(project)
                            .createCommentFromText(StringUtils.join("//", collect.trim()), parentOfType);
                    parentOfType.getParent().addBefore(commentFromText, parentOfType);
                }
            }
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        if (psiElement instanceof PsiMethodCallExpression) {
            return true;
        }
        if (psiElement instanceof PsiIdentifier) {
            String text = ((PsiIdentifier) psiElement).getText();
            if (StringUtils.isNotEmpty(text) && text.length() > 3) {
                return text.startsWith("set");
            }
        }
        return false;
    }

}
