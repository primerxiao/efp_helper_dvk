package com.efp.common.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DubboXmlConfigUtils {
    /**
     * 消费者配置
     * @param e
     * @param serviceModule
     * @param serviceClass
     */
    public static void consumerXmlConfigSet(@NotNull AnActionEvent e, Module serviceModule, PsiClass serviceClass) {
        if (!Objects.isNull(serviceModule)) {
            String[] split = serviceModule.getName().split("\\.");
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
                        rootTag.add(xmlTag);
                        consumerXmlFile.navigate(true);
                    });
                }

            }
        }
    }

    /**
     * 生产者配置
     * @param e
     * @param implModule
     * @param serviceClass
     */
    public static void poviderXmlConfigSet(@NotNull AnActionEvent e, Module implModule, PsiClass serviceClass) {
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

    private static boolean checkTagId(XmlTag rootTag, String idValue) {
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
}
