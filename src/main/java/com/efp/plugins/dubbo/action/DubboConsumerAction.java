package com.efp.plugins.dubbo.action;

import com.efp.common.data.EfpData;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * dubbo生产者配置动作
 */
@Deprecated
public class DubboConsumerAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //判断当前编辑文件是否java文件
        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (Objects.isNull(psiFile)) {
            return;
        }
        FileType fileType = psiFile.getFileType();
        if (fileType instanceof JavaFileType) {
            Module moduleForFile = ModuleUtil.findModuleForFile(psiFile);
            if (!moduleForFile.getName().endsWith(".impl") && !moduleForFile.getName().endsWith(".service")) {
                Messages.showErrorDialog("找不到相应的配置文件","错误信息：");
                return;
            }
            Module operaModule = null;
            if (moduleForFile.getName().endsWith(".impl")) {
                operaModule = getServiceModule(moduleForFile);
            } else {
                operaModule = moduleForFile;
            }
            String[] split = operaModule.getName().split("\\.");
            String xmlName = "dubbo-consumer-"+split[0]+"-"+split[1]+".xml";
            EfpData efpData = new EfpData(operaModule.getName(),xmlName,2);
            PsiJavaFile javaFile = (PsiJavaFile) psiFile;
            PsiClass psiClass = javaFile.getClasses()[0];
            String className = "";
            if (psiClass.isInterface()) {
                className = psiClass.getName();
            } else {
                className = getSuperClass(psiClass).getName();
            }
            if (StringUtils.isEmpty(className)) {
                Messages.showErrorDialog("获取到类名为空", "错误信息");
                return;
            }
            PsiFile[] filesByName = FilenameIndex.getFilesByName(e.getProject(), efpData.getXmlFileName(), operaModule.getModuleScope());
            if (filesByName.length > 0) {
                XmlFile xmlFile = (XmlFile) filesByName[0];
                XmlTag rootTag = xmlFile.getRootTag();
                XmlTag[] subTags = rootTag.getSubTags();
                for (XmlTag subTag : subTags) {
                    XmlAttribute id = subTag.getAttribute("id");
                    if (Objects.isNull(id)) {
                        continue;
                    }
                    if (initCap(className).equals(id.getValue())) {
                        Messages.showErrorDialog("xml文件已经存在该配置", "错误信息");
                        return;
                    }
                }
                String finalClassName = className;
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    final XmlTag xmlTag = rootTag.createChildTag("dubbo:reference", rootTag.getNamespace(), null, false);
                    xmlTag.setAttribute("id", initCap(finalClassName));
                    xmlTag.setAttribute("interface", psiClass.isInterface() ? psiClass.getQualifiedName() : getSuperClass(psiClass).getQualifiedName());
                    xmlTag.setAttribute("version", "1.0.0");
                    rootTag.add(xmlTag);
                    xmlFile.getVirtualFile().refresh(true, true);
                    xmlFile.navigate(true);
                    //PsiNavigateUtil.navigate(xmlTag.getNavigationElement());
                });
            }
        }
    }

    public String initCap(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public PsiClass getSuperClass(PsiClass psiClass) {
        PsiClass[] supers = psiClass.getSupers();
        for (PsiClass aSuper : supers) {
            if (psiClass.getName().startsWith(aSuper.getName())) {
                return aSuper;
            }
        }
        return null;
    }
    public Module getImplModule(Module module) {
        Module[] modules = ModuleManager.getInstance(module.getProject()).getModules();
        for (Module module1 : modules) {
            if (module1.getName().equals(module.getName().replace(".service", ".impl"))) {
                return module1;
            }
        }
        return null;
    }
    public Module getServiceModule(Module module) {
        Module[] modules = ModuleManager.getInstance(module.getProject()).getModules();
        for (Module module1 : modules) {
            if (module1.getName().equals(module.getName().replace(".impl", ".service"))) {
                return module1;
            }
        }
        return null;
    }

}
