package com.efp.plugins.frame.dubbo.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.JsonUtils;
import com.efp.plugins.frame.dubbo.service.DubboService;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

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

    private final DubboService dubboService = DubboService.getInstance();

    /**
     * 构造函数
     *
     * @param project   项目
     * @param psiClass  接口类对象
     * @param psiMethod 接口函数对象
     */
    public DubboGenericCallUi(@Nullable Project project, @NotNull PsiClass psiClass, @NotNull PsiMethod psiMethod) {
        super(project);
        this.psiClass = psiClass;
        this.psiMethod = psiMethod;
        interfaceClass.setText(psiClass.getQualifiedName());
        method.setText(psiMethod.getName());
        registerAddr.setText(getRegisterAddr());
        group.setText(PluginContants.DubboConfig.DEFAULT_DUBBO_GROUP);
        version.setText(PluginContants.DubboConfig.DEFAULT_DUBBO_VERSION);
        param.setText(getParamText(psiMethod));
        setOKButtonText("发起请求");
        setCancelButtonText("关闭");
        init();
    }

    private String getParamText(PsiMethod psiMethod) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        if (parameterList.isEmpty()) {
            JsonUtils.prettyformat("{}");
        }
        PsiParameter[] parameters = parameterList.getParameters();
        if (parameters.length <= 0) {
            JsonUtils.prettyformat("{}");
        }
        for (PsiParameter parameter : parameters) {
            String name = parameter.getName();
            PsiType type = parameter.getType();
        }
        return JsonUtils.prettyformat("{}");
    }

    private String getRegisterAddr() {
        //获取注册中心的持久化配置
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        if (Objects.isNull(state) || StringUtils.isEmpty(state.dubboRegistryAddress)) {
            return PluginContants.DubboConfig.DEFAULT_DUBBO_REGISTER;
        }
        return state.dubboRegistryAddress;
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        return root;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
