package com.efp.plugins.dubbo.action;

import com.efp.common.constant.PluginContants;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.lang.properties.PropertiesReferenceManager;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.apache.commons.net.telnet.TelnetClient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;

/**
 * 直接发起dubbo调用
 */
public class DubboServiceCall extends PsiElementBaseIntentionAction {
    public static final String IP = "127.0.0.1";
    public static final String PROPERTIES_NAME = "dubbo.protocol.port";
    public static final String PROPERTIES_FILE_NAME = "application";
    public static final String INVOKE = "invoke ";
    public static final String POINT = ".";
    public static final String STRING = "java.lang.String";
    public static String PORT = null;

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        PsiFile srcPsiFile = PsiTreeUtil.getParentOfType(psiElement, PsiFile.class);
        if (srcPsiFile == null) return;
        // 获取当前操作的类文件
        if (srcPsiFile.getVirtualFile().getNameWithoutExtension().endsWith("Impl")) {
            final String impl = srcPsiFile.getVirtualFile().getNameWithoutExtension().replace("Impl", "");
            srcPsiFile = FilenameIndex.getFilesByName(project, impl + ".java", GlobalSearchScope.allScope(project))[0];
        }
        Module implModule = getImplModule(ModuleUtil.findModuleForFile(srcPsiFile));
        PropertiesFile propertiesFile = PropertiesReferenceManager.getInstance(project).findPropertiesFiles(implModule, PROPERTIES_FILE_NAME).get(0);
        // 调用IP 端口
        PORT = propertiesFile.findPropertyByKey(PROPERTIES_NAME).getValue();
        // 类全名 + 方法
        PsiMethod method = (PsiMethod) psiElement.getParent();
        String methodName = method.getName();
        PsiJavaFile javaFile = (PsiJavaFile) srcPsiFile;
        String invokeMethod = INVOKE + javaFile.getPackageName() + POINT + srcPsiFile.getVirtualFile().getNameWithoutExtension() + POINT + methodName + getMethodParameters(method);
        // telnet调用
        try {
            TelnetClient telnetClient = new TelnetClient("vt200");  //指明Telnet终端类型，否则会返回来的数据中文会乱码
            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
            telnetClient.connect(IP, Integer.valueOf(PORT));  //建立一个连接,默认端口是23
            PrintStream pStream = new PrintStream(telnetClient.getOutputStream());  //写命令的流
            pStream.println(invokeMethod); //写命令
            pStream.flush(); //将命令发送到telnet Server
            if (null != pStream) {
                pStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                    ex.getMessage(), NotificationType.ERROR));
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

    public Module getImplModule(Module module) {
        if (module.getName().endsWith(".impl")) {
            return module;
        }
        return ModuleManager.getInstance(module.getProject()).findModuleByName(module.getName().replace("service", "impl"));
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
                } else if (STRING.equals(parameters[i].getType())) {
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
