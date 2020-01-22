package com.efp.plugins.dubbo.action;

import com.efp.common.data.EfpCovert;
import com.efp.common.data.EfpModuleType;
import com.efp.common.util.StringUtils;
import com.intellij.codeInspection.DefaultXmlSuppressionProvider;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.properties.PropertiesReferenceManager;
import com.intellij.lang.properties.psi.PropertiesFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.XmlElementFactory;
import com.intellij.psi.impl.source.xml.XmlElementChangeUtil;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlComment;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlTagUtil;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * dubbo生产者以及消费者配置动作
 */
public class DubboXmlConfigAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            PsiFile editPsiFile = e.getData(LangDataKeys.PSI_FILE);
            final List<PropertiesFile> propertiesFiles = PropertiesReferenceManager.getInstance(e.getProject()).findPropertiesFiles(ModuleUtil.findModuleForFile(editPsiFile), "application.properties");
            final List<PropertiesFile> propertiesFile = PropertiesReferenceManager.getInstance(e.getProject()).findPropertiesFiles(ModuleUtil.findModuleForFile(editPsiFile), "application");
            if (Objects.isNull(editPsiFile)) {
                throw new RuntimeException("获取不到编辑中的文件");
            }
            FileType fileType = editPsiFile.getFileType();
            if (fileType instanceof JavaFileType) {
                //获取当前编辑的module
                Module editModule = ModuleUtil.findModuleForFile(editPsiFile);
                if (!editModule.getName().endsWith(".impl") && !editModule.getName().endsWith(".service")) {
                    throw new RuntimeException("当前模块暂不支持dubbo配置");
                }
                //serviceMoudle
                Module serviceModule = EfpCovert.getModule(editModule, EfpModuleType.SERVICE);
                //implMoudle
                Module implModule = EfpCovert.getModule(editModule, EfpModuleType.IMPL);
                //获取service类
                PsiClass serviceClass = getServiceClass(editPsiFile);
                consumerXmlConfigSet(e, serviceModule, implModule, serviceClass);
                poviderXmlConfigSet(e, implModule, serviceClass);
            } else {
                Messages.showInfoMessage("非java文件，无法添加dubbo配置", "提示信息");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(ex.getLocalizedMessage(), "错误提示：");
        }
    }

    /**
     * 消费者配置
     *
     * @param e
     * @param serviceModule
     * @param implModule
     * @param serviceClass
     */
    private void consumerXmlConfigSet(@NotNull AnActionEvent e, Module serviceModule, Module implModule, PsiClass serviceClass) {
        if (!Objects.isNull(serviceModule)) {
            String[] split = implModule.getName().split("\\.");
            String consumerXmlFileName = "dubbo-consumer-" + split[0] + "-" + split[1] + ".xml";
            PsiFile[] consumerXmlFileArr = FilenameIndex.getFilesByName(e.getProject(), consumerXmlFileName, serviceModule.getModuleScope());
            if (consumerXmlFileArr.length > 0) {
                XmlFile consumerXmlFile = (XmlFile) consumerXmlFileArr[0];
                XmlTag rootTag = consumerXmlFile.getRootTag();
                //判断是否已经存在id
                String idValue = StringUtils.initCap(serviceClass.getName());
                //判断是否存在该id的tag
                if (checkTagId(rootTag, idValue)) {
                    Messages.showErrorDialog("已经存在消费者配置，不进行消费者配置生产", "提示信息");
                } else {
                    //生成配置
                    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                        final XmlTag xmlTag = rootTag.createChildTag("dubbo:reference", rootTag.getNamespace(), null, false);
                        xmlTag.setAttribute("id", StringUtils.initCap(serviceClass.getName()));
                        xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                        xmlTag.setAttribute("version", "1.0.0");

                        rootTag.addSubTag(xmlTag, false);
                        consumerXmlFile.navigate(true);
                    });
                }

            }
        }
    }

    /**
     * 生产者配置
     *
     * @param e
     * @param implModule
     * @param serviceClass
     */
    private void poviderXmlConfigSet(@NotNull AnActionEvent e, Module implModule, PsiClass serviceClass) {
        if (!Objects.isNull(implModule)) {
            String[] split = implModule.getName().split("\\.");
            String providerXmlFileName = "dubbo-provider-" + split[0] + "-" + split[1] + ".xml";
            PsiFile[] providerXmlFileArr = FilenameIndex.getFilesByName(e.getProject(), providerXmlFileName, implModule.getModuleScope());
            if (providerXmlFileArr.length > 0) {
                XmlFile providerXmlFile = (XmlFile) providerXmlFileArr[0];
                XmlTag rootTag = providerXmlFile.getRootTag();
                //判断是否已经存在id
                String idValue = StringUtils.initCap(serviceClass.getName()) + "Provider";
                //判断是否存在该id的tag
                if (checkTagId(rootTag, idValue)) {
                    Messages.showErrorDialog("已经存在生产者配置，不进行生产者配置生产", "提示信息");
                } else {
                    //生成配置
                    WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                        final XmlTag xmlTag = rootTag.createChildTag("dubbo:service", rootTag.getNamespace(), null, false);
                        xmlTag.setAttribute("id", idValue);
                        xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                        xmlTag.setAttribute("ref", StringUtils.initCap(serviceClass.getName()));
                        xmlTag.setAttribute("version", "1.0.0");
                        xmlTag.setAttribute("retries", "0");
                        xmlTag.setAttribute("cluster", "failover");
                        xmlTag.setAttribute("timeout", "150000");
                        rootTag.add(xmlTag);
                        providerXmlFile.navigate(true);
                    });
                }

            }
        }
    }

    private boolean checkTagId(XmlTag rootTag, String idValue) {
        XmlTag[] subTags = rootTag.getSubTags();
        for (XmlTag subTag : subTags) {
            final XmlAttribute id = subTag.getAttribute("id");
            if (Objects.isNull(id)) {
                continue;
            }
            if (idValue.equals(id.getValue())) {
                return true;
            }
        }
        return false;
    }

    public PsiClass getServiceClass(PsiFile editPsiFile) {
        PsiJavaFile editJavaFile = (PsiJavaFile) editPsiFile;
        PsiClass editClass = editJavaFile.getClasses()[0];
        if (editClass.isInterface()) {
            return editClass;
        }
        PsiClass[] supers = editClass.getSupers();
        for (PsiClass superClass : supers) {
            //判断超类的名字时候被实现类包含
            if (editClass.getName().contains(superClass.getName())) {
                return superClass;
            }
        }
        throw new RuntimeException("获取service出错");
    }
}
