package com.efp.plugins.frame.sofa.action;

import com.efp.plugins.frame.sofa.ui.SofaConfigCallUi;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 添加sofa生产者和消费者配置
 *
 * @author primerxiao
 */
public class SofaConfigCall extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        //获取当前操作的类对象
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        if (psiClass == null) {
            return;
        }
        //获取当前操作的文件对象
        PsiFile psiFile = psiClass.getContainingFile();
        //获取当前操作的文件类型
        FileType fileType = psiFile.getFileType();
        if (fileType instanceof JavaFileType) {
            //判断当前类是接口还是实现类
            Module serviceModule = null;
            if (psiClass.isInterface()) {
                //service
                serviceModule = ModuleUtil.findModuleForFile(psiFile);
            } else {
                //impl
                psiClass = psiClass.getSuperClass();
                serviceModule = ModuleUtil.findModuleForFile(Objects.requireNonNull(psiClass).getContainingFile());
            }
            String[] split = serviceModule.getName().split("-");
            Module startModule = ModuleManager.getInstance(project).findModuleByName(split[0] + "-" + split[1] + "-start");
            new SofaConfigCallUi(project, serviceModule, startModule, psiClass).show();
        } else {
            Messages.showInfoMessage("非java文件，无法添加dubbo配置", "提示信息");
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        final PsiElement parent = psiElement.getParent();
        if (!(parent instanceof PsiClass)) {
            return false;
        }
        return true;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "生产者和消费者配置";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getText() {
        return this.getFamilyName();
    }

}
