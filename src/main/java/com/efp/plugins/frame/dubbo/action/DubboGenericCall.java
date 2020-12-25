package com.efp.plugins.frame.dubbo.action;

import com.efp.plugins.frame.dubbo.ui.DubboGenericCallUi;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.twelvemonkeys.imageio.metadata.psd.PSD;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.net.telnet.TelnetClient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * dubbo泛化调用
 * 通过telnet发送invote命令的方式调用dubbo接口
 *
 * @author primerxiao
 */
public class DubboGenericCall extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        try {
            //获取当前操作的函数对象
            PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
            //获取当前操作的类对象
            PsiClass psiClass = Objects.requireNonNull(psiMethod).getContainingClass();
            if (!Objects.requireNonNull(psiClass).isInterface()) {
                //不是接口
                PsiMethod[] superMethods = psiMethod.findSuperMethods();
                if (superMethods.length == 0) {
                    throw new Exception("函数不合法");
                }
                psiMethod = superMethods[0];
                psiClass = psiMethod.getContainingClass();
            }
            //打开界面
            new DubboGenericCallUi(editor.getProject(), psiClass, psiMethod).show();

        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "错误信息");
        }
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        final PsiElement parent = psiElement.getParent();
        return parent instanceof PsiMethod;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Invote dubbo service method";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getText() {
        return this.getFamilyName();
    }

}
