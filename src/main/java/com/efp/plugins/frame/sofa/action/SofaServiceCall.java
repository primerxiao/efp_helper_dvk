package com.efp.plugins.frame.sofa.action;


import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 直接发起dubbo接口调试
 * 通过telnet发送invote命令的方式调用dubbo接口
 *
 * @author primerxiao
 */
public class SofaServiceCall extends PsiElementBaseIntentionAction {

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
                //获取当前操作的类对象
                psiClass = psiMethod.getContainingClass();
            }

            PsiMethod finalPsiMethod = psiMethod;
            new Task.Backgroundable(project, "Calling Dubbo Service...") {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    callSofaService(editor, finalPsiMethod);
                }
            }.queue();

        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "错误信息");
        }
    }

    private void callSofaService(Editor editor, PsiMethod psiMethod) {
        // sofa服务提供者-sofa直连地址,12200默认端口
        String sofaUrl = "bolt://127.0.0.1:2181";
        // sofa服务提供者-接口类路径
        String className = psiMethod.getContainingClass().getQualifiedName();
        // sofa服务提供者-接口方法名
        String methodName = psiMethod.getName();
        //获取参数
        PsiParameterList parameterList = psiMethod.getParameterList();
        for (PsiParameter parameter : parameterList.getParameters()) {

        }
//        // 引用一个服务
//        ConsumerConfig<GenericService> consumerConfig = new ConsumerConfig<GenericService>().setInterfaceId(className)
//                .setGeneric(true).setDirectUrl(sofaUrl);
//        // 拿到代理类
//        GenericService genericService = consumerConfig.refer();
//        // 发起调用 todo:需要处理参数
//        Object resultObj = genericService.$genericInvoke(methodName, new String[]{""},
//                new Object[]{"{}"});
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        final PsiElement parent = psiElement.getParent();
        if (!(parent instanceof PsiMethod)) {
            return false;
        }
        return true;
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
