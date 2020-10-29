package com.efp.plugins.dubbo.action;

import com.efp.common.notifier.MyEfpPluginErrorsNotifier;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.net.telnet.TelnetClient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.net.URLDecoder;
import java.util.List;

/**
 * 直接发起dubbo调用
 */
public class DubboServiceCallExe extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        //获取当前操作的文件对象
        PsiFile srcPsiFile = PsiTreeUtil.getParentOfType(psiElement, PsiFile.class);
        //获取当前操作的函数对象
        PsiMethod method = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        if (srcPsiFile == null) return;
        if (method == null) return;
        if (!(srcPsiFile instanceof PsiJavaFile)) {
            //非Java文件
            return;
        }
        PsiJavaFile javaFile = (PsiJavaFile) srcPsiFile;
        PsiClass[] classes = javaFile.getClasses();
        PsiClass aClass = classes[0];
        if (!aClass.isInterface()) {
            //不是接口
            javaFile = PsiTreeUtil.getParentOfType(aClass.getInterfaces()[0], PsiJavaFile.class);
        }
        //获取包名
        assert javaFile != null;
        String qualifiedName = javaFile.getClasses()[0].getQualifiedName();
        String methodName = method.getName();
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        assert state != null;
        try {
            ZkClient zkClient = new ZkClient(state.dubboRegistryAddress, 5000);
            List<String> list = zkClient.getChildren("/dubbo/" + qualifiedName + "/providers");
            if (list != null) {
                String[] split = URLDecoder.decode(list.get(0), "utf-8").split("//")[1].split("/")[0].split(":");
                TelnetClient telnetClient = new TelnetClient("vt200");
                telnetClient.setDefaultTimeout(5000);
                telnetClient.connect(split[0], Integer.parseInt(split[1]));
                PrintStream pStream = new PrintStream(telnetClient.getOutputStream());
                pStream.println("invoke " + qualifiedName + "." + methodName + getMethodParameters(method));
                pStream.flush();
                pStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new MyEfpPluginErrorsNotifier().notify(e.getMessage());
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
