package com.efp.plugins.frame.dubbo.ui;

import com.alibaba.fastjson.JSONObject;
import com.efp.common.constant.PluginContants;
import com.efp.common.notifier.NotificationHelper;
import com.efp.common.util.JsonUtils;
import com.efp.plugins.frame.dubbo.bean.DubboMethodParam;
import com.efp.plugins.frame.dubbo.bean.DubboParamTableModel;
import com.efp.plugins.frame.dubbo.service.DubboCallParam;
import com.efp.plugins.frame.dubbo.service.DubboService;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
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

    private final Project project;

    /**
     * 构造函数
     *
     * @param project   项目
     * @param psiClass  接口类对象
     * @param psiMethod 接口函数对象
     */
    public DubboGenericCallUi(@Nullable Project project, @NotNull PsiClass psiClass, @NotNull PsiMethod psiMethod) {
        super(project);
        this.project = project;
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
        ProgressManager
                .getInstance()
                .run(new Task.Backgroundable(project, "Dubbo: remote calling...") {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        indicator.setFraction(0.1);
                        DubboParamTableModel model = (DubboParamTableModel) paramTable.getModel();
                        List<DubboMethodParam> dubboMethodParams = model.getDubboMethodParams();
                        String[] types = new String[dubboMethodParams.size()];
                        Object[] values = new Object[dubboMethodParams.size()];
                        for (int i = 0; i < dubboMethodParams.size(); i++) {
                            types[i] = dubboMethodParams.get(i).getType();
                            values[i] = dubboMethodParams.get(i).getValue();
                        }
                        indicator.setFraction(0.3);
                        try {
                            DubboCallParam dubboCallParam = DubboCallParam
                                    .Builder
                                    .aDubboCallParam()
                                    .withRegistryAddress(registerAddr.getText())
                                    .withReferenceGeneric(true)
                                    .withInvokeMethod(method.getText())
                                    .withInvokeMethodParamType(types)
                                    .withInvokeMethodParam(values)
                                    .withReferenceGroup(group.getText())
                                    .withReferenceInterface(interfaceClass.getText())
                                    .withReferenceVersion(version.getText())
                                    .build();
                            indicator.setFraction(0.5);
                            Object callResult = dubboService.remoteCall(dubboCallParam);
                            NotificationHelper.getInstance().notifyInfo(StringUtils.join(
                                    "调用接口",
                                    interfaceClass.getText(),
                                    ":", method.getText(),
                                    "返回结果:\n", JSONObject.toJSONString(callResult)), project);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            NotificationHelper.getInstance().notifyError(StringUtils.join(
                                    "调用接口",
                                    interfaceClass.getText(),
                                    ":", method.getText(),
                                    "发生异常:\n",
                                    exception.getLocalizedMessage()), project);
                        }
                        indicator.setFraction(1.0);
                    }
                });
    }
}
