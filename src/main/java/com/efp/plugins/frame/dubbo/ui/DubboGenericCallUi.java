package com.efp.plugins.frame.dubbo.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.util.JsonUtils;
import com.efp.plugins.frame.dubbo.bean.DubboParamTableModel;
import com.efp.plugins.frame.dubbo.service.DubboService;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class DubboGenericCallUi extends DialogWrapper {
    private JPanel root;
    private JTextField registerAddr;
    private JTextField interfaceClass;
    private JTextField method;
    private JTextField group;
    private JTextField version;
    private JTable paramTable;

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
        setOKButtonText("发起请求");
        setCancelButtonText("关闭");
        paramTable.setModel(new DubboParamTableModel(dubboService.getDubboMethodParams(psiMethod)));
        paramTable.setEnabled(true);
        init();
    }

//    i
//    int
//
//    integer
//    java.lang.Integer
//
//    string
//    java.lang.String
//
//    doubleParam
//    java.lang.Double
//
//    bigDecimal
//    java.math.BigDecimal
//
//    map
//    java.util.Map<java.lang.String,java.lang.String>
//
//    accloan
//    com.irdstudio.basic.framework.core.bean.SedSynAccLoanVO


    private String getRegisterAddr() {
        //获取注册中心的持久化配置
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        if (Objects.isNull(state) || StringUtils.isEmpty(state.dubboRegistryAddress)) {
            return PluginContants.DubboConfig.DEFAULT_DUBBO_REGISTER;
        }
        return state.dubboRegistryAddress;
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
