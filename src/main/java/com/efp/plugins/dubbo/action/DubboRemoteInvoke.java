package com.efp.plugins.dubbo.action;

import com.intellij.lang.properties.PropertiesReferenceManager;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.PrintStream;

/**
 * 需求编号:2019D0519
 * 问题编号:
 * 开发人员: caoxin
 * 创建日期:2019/12/25
 * 功能描述:
 * 修改日期:2019/12/25
 * 修改描述:
 */
public class DubboRemoteInvoke extends AnAction {
    public static final String IP = "127.0.0.1";
    public static final String PROPERTIES_NAME = "dubbo.protocol.port";
    public static final String PROPERTIES_FILE_NAME = "application";
    public static final String INVOKE = "invoke ";
    public static final String POINT = ".";
    public static final String STRING = "java.lang.String";
    public static String PORT = null;
    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取当前操作的项目
        Project project = e.getData(PlatformDataKeys.PROJECT);
        //获取当前事件触发时，光标所在的元素
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        //如果光标选择的不是类，弹出对话框提醒
        if (psiElement == null || !(psiElement instanceof PsiMethod)) {
            Messages.showMessageDialog(project, "Please focus on a method", "Generate Failed", null);
            return;
        }
        // 获取当前操作的类文件
        PsiFile Classfile = e.getData(CommonDataKeys.PSI_FILE);

        if (Classfile.getVirtualFile().getNameWithoutExtension().endsWith("Impl")) {
            final String impl = Classfile.getVirtualFile().getNameWithoutExtension().replace("Impl", "");
            Classfile = FilenameIndex.getFilesByName(e.getProject(), impl+".java", GlobalSearchScope.allScope(e.getProject()))[0];
        }
        Module implModule = getImplModule(ModuleUtil.findModuleForFile(Classfile));
        PropertiesFile propertiesFile = PropertiesReferenceManager.getInstance(e.getProject()).findPropertiesFiles(implModule, PROPERTIES_FILE_NAME).get(0);
        // 调用IP 端口
        PORT = propertiesFile.findPropertyByKey(PROPERTIES_NAME).getValue();
        // 类全名 + 方法
        PsiMethod method = (PsiMethod) psiElement;
        String methodName = method.getName();
        PsiJavaFile javaFile = (PsiJavaFile) Classfile;
        String invokeMethod = INVOKE + javaFile.getPackageName() + POINT + Classfile.getVirtualFile().getNameWithoutExtension() + POINT + methodName + getMethodParameters(method);
        // telnet调用
        try {
            TelnetClient telnetClient = new TelnetClient("vt200");  //指明Telnet终端类型，否则会返回来的数据中文会乱码
            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
            telnetClient.connect(IP,Integer.valueOf(PORT));  //建立一个连接,默认端口是23
            PrintStream pStream = new PrintStream(telnetClient.getOutputStream());  //写命令的流
            pStream.println(invokeMethod); //写命令
            pStream.flush(); //将命令发送到telnet Server
            if(null != pStream) {
                pStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
                if (PsiType.INT.equals(parameters[i].getType())) sb.append("0");
                else if (PsiType.LONG.equals(parameters[i].getType())) sb.append("0");
                else if (PsiType.FLOAT.equals(parameters[i].getType())) sb.append("0f");
                else if (PsiType.DOUBLE.equals(parameters[i].getType())) sb.append("0.0");
                else if (PsiType.BOOLEAN.equals(parameters[i].getType())) sb.append("false");
                else if (PsiType.CHAR.equals(parameters[i].getType())) sb.append("''");
                else if (STRING.equals(parameters[i].getType())) sb.append("\"\"");
                else sb.append("{}");
                if (i < parameters.length - 1) sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
