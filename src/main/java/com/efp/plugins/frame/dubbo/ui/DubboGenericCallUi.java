package com.efp.plugins.frame.dubbo.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DubboGenericCallUi extends DialogWrapper {
    private JPanel root;
    private JTextField registerAddr;
    private JTextField interfaceClass;
    private JTextField method;
    private JTextField group;
    private JTextField version;
    private JTextArea param;

    private final PsiClass psiClass;
    private final PsiMethod psiMethod;

    /**
     * 构造函数
     *
     * @param project   项目
     * @param psiMethod 接口类对象
     */
    public DubboGenericCallUi(@Nullable Project project, PsiClass psiClass, PsiMethod psiMethod) {
        super(project);
        this.psiClass = psiClass;
        this.psiMethod = psiMethod;

        interfaceClass.setText(psiClass.getQualifiedName());
        method.setText(psiMethod.getName());

        setOKButtonText("发起请求");
        setCancelButtonText("关闭");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return root;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
