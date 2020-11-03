package com.efp.plugins.dubbo.action;

import com.efp.common.util.NotifyUtils;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.net.telnet.TelnetClient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 直接发起dubbo接口调试
 * 通过telnet发送invote命令的方式调用dubbo接口
 * @author primerxiao
 */
public class DubboServiceCall extends PsiElementBaseIntentionAction {

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
                psiMethod = superMethods[0];
                //获取当前操作的类对象
                psiClass = psiMethod.getContainingClass();
            }
            //获取包名
            String qualifiedName = Objects.requireNonNull(psiClass).getQualifiedName();
            //获取注册中心的持久化配置
            EfpSettingsState state = EfpSettingsState.getInstance().getState();
            //zk客户端操作
            ZkClient zkClient = new ZkClient(Objects.requireNonNull(state).dubboRegistryAddress, 5000);
            //获取注册中心上面的该类的提供者列表
            List<String> providers = zkClient.getChildren("/dubbo/" + qualifiedName + "/providers");
            if (providers == null) {
                throw new Exception("从注册中心" + state.dubboRegistryAddress + "获取到" + qualifiedName + "提供者数据为空");
            }
            //提供者的ip和port集合
            ArrayList<String> providerIpPorts = new ArrayList<>();
            providers.forEach(s -> {
                try {
                    providerIpPorts.add(URLDecoder.decode(s, "utf-8").split("//")[1].split("/")[0]);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    NotifyUtils.notifyError(e.getMessage());
                }
            });
            //弹出提供者列表进行选择调用
            PsiMethod finalPsiMethod = psiMethod;
            JBPopupFactory.getInstance()
                    .createPopupChooserBuilder(providerIpPorts)
                    .setTitle("Select Provider")
                    .setItemChosenCallback((value) -> {
                        try {
                            callDubboService(value, editor, finalPsiMethod);
                        } catch (Exception e) {
                            e.printStackTrace();
                            NotifyUtils.notifyError(e.getMessage());
                        }
                    })
                    .createPopup()
                    .showInBestPositionFor(editor);
        } catch (Exception e) {
            e.printStackTrace();
            NotifyUtils.notifyError(e.getMessage());
        }
    }

    private void callDubboService(String value, Editor editor, PsiMethod psiMethod) {
        TelnetClient telnetClient = null;
        PrintStream pStream = null;
        try {
            String[] providerConfigArr = value.split(":");
            telnetClient = new TelnetClient("vt200");
            telnetClient.setDefaultTimeout(5000);
            telnetClient.connect(providerConfigArr[0], Integer.parseInt(providerConfigArr[1]));
            pStream = new PrintStream(telnetClient.getOutputStream());
            pStream.println("invoke " + Objects.requireNonNull(psiMethod.getContainingClass()).getQualifiedName() + "." + psiMethod.getName() + getMethodParameters(psiMethod));
            pStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (pStream != null) {
                    pStream.close();
                }
            } catch (Exception e) { e.printStackTrace(); }
            try {
                if (telnetClient != null) {
                    telnetClient.disconnect();
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        final PsiElement parent = psiElement.getParent();
        if (!(parent instanceof PsiMethod)) return false;
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

    public String getMethodParameters(PsiMethod method) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                if (PsiType.INT.equals(parameters[i].getType())) {
                    sb.append("0");
                } else if (PsiType.LONG.equals(parameters[i].getType())) {
                    sb.append("0");
                } else if (PsiType.FLOAT.equals(parameters[i].getType())) {
                    sb.append("0f");
                } else if (PsiType.DOUBLE.equals(parameters[i].getType())) {
                    sb.append("0.0");
                } else if (PsiType.BOOLEAN.equals(parameters[i].getType())) {
                    sb.append("false");
                } else if (PsiType.CHAR.equals(parameters[i].getType())) {
                    sb.append("''");
                } else if ("java.lang.String".equals(parameters[i].getType())) {
                    sb.append("\"\"");
                } else {
                    sb.append("{}");
                }
                if (i < parameters.length - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }


}
