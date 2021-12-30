package com.efp.common.util;

import com.efp.common.constant.PluginContants;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SofaXmlConfigUtils {
    /**
     * 消费者配置
     *
     * @param e            事件对象
     * @param facaModule   service模块
     * @param serviceClass service类
     */
    public static void consumerXmlConfigSet(@NotNull AnActionEvent e, Module facaModule, PsiClass serviceClass, String baseMoudleName) {
        consumerXmlConfigSet(e.getProject(), facaModule,
                serviceClass,
                baseMoudleName);
    }

    public static void consumerXmlConfigSet(@NotNull Project project, Module facaModule, PsiClass serviceClass, String baseMoudleName) {
        if (!Objects.isNull(facaModule)) {
            String consumerXmlFileName = "sofa-consumer-" + baseMoudleName + ".xml";
            PsiFile[] consumerXmlFileArr = FilenameIndex.getFilesByName(project, consumerXmlFileName, facaModule.getModuleScope());
            if (consumerXmlFileArr.length > 0) {
                XmlFile consumerXmlFile = (XmlFile) consumerXmlFileArr[0];
                XmlTag rootTag = consumerXmlFile.getRootTag();
                //判断是否已经存在id
                String idValue = PluginStringUtils.initCap(serviceClass.getName());
                //判断是否存在该id的tag
                if (!checkTagExist(rootTag, idValue, "id")) {
                    //生成配置
                    final XmlTag xmlTag = rootTag.createChildTag("sofa:reference", rootTag.getNamespace(), null, false);
                    xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                    xmlTag.setAttribute("id", idValue);
                    xmlTag.setAttribute("unique-id", "1.0.0");
                    XmlTag sofaBindingBolt = xmlTag.createChildTag("sofa:binding.bolt", xmlTag.getNamespace(), null, false);
                    xmlTag.add(sofaBindingBolt);
                    rootTag.addSubTag(xmlTag, false);
                    consumerXmlFile.navigate(true);
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "生成配置到sofa-consumer-xml文件中成功", NotificationType.INFORMATION));
                }else {
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "生成配置到sofa-consumer-xml文件中失败，已经存在该配置信息", NotificationType.ERROR));
                }
            }
        }
    }

    /**
     * 生产者配置
     *
     * @param e
     * @param startModule
     * @param serviceClass
     */
    public static void poviderXmlConfigSet(@NotNull AnActionEvent e, Module startModule, PsiClass serviceClass, String baseMoudleName) {
        poviderXmlConfigSet(e.getProject(), startModule, serviceClass, baseMoudleName);
    }

    public static void poviderXmlConfigSet(Project project, Module startModule, PsiClass serviceClass, String baseMoudleName) {
        if (!Objects.isNull(startModule)) {
            String providerXmlFileName = "sofa-provider-" + baseMoudleName + ".xml";
            PsiFile[] providerXmlFileArr = FilenameIndex.getFilesByName(project, providerXmlFileName, startModule.getModuleScope());
            if (providerXmlFileArr.length > 0) {
                XmlFile providerXmlFile = (XmlFile) providerXmlFileArr[0];
                XmlTag rootTag = providerXmlFile.getRootTag();
                //判断是否已经存在id
                String refValue = PluginStringUtils.initCap(serviceClass.getName());
                //判断是否存在该id的tag
                if (!checkTagExist(rootTag, refValue, "ref")) {
                    //生成配置
                    final XmlTag xmlTag = rootTag.createChildTag("sofa:service", rootTag.getNamespace(), null, false);
                    xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                    xmlTag.setAttribute("ref", refValue);
                    xmlTag.setAttribute("unique-id", "1.0.0");
                    XmlTag sofaBindingBolt = xmlTag.createChildTag("sofa:binding.bolt", xmlTag.getNamespace(), null, false);
                    xmlTag.add(sofaBindingBolt);
                    rootTag.add(xmlTag);
                    providerXmlFile.navigate(true);
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "生成配置到sofa-provider-xml文件中成功", NotificationType.INFORMATION));
                } else {
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "生成配置到sofa-provider-xml文件中失败，已经存在该配置信息", NotificationType.ERROR));
                }
            }
        }
    }

    /**
     * @param rootTag
     * @param attrValue
     * @param attrName
     * @return
     */
    private static boolean checkTagExist(XmlTag rootTag, String attrValue, String attrName) {
        XmlTag[] subTags = rootTag.getSubTags();
        for (XmlTag subTag : subTags) {
            final XmlAttribute id = subTag.getAttribute(attrName);
            if (Objects.isNull(id)) {
                continue;
            }
            if (attrValue.equals(id.getValue())) {
                return true;
            }
        }
        return false;
    }

    private static void addNoinspectionComment(Project project, XmlTag anchor) throws IncorrectOperationException {
        final XmlComment newComment = createComment(project, "noinspection ");
        PsiElement parent = anchor.getParentTag();
        if (parent == null) {
            parent = PsiTreeUtil.getPrevSiblingOfType(anchor, XmlProlog.class);
            if (parent != null) {
                CodeStyleManager.getInstance(PsiManager.getInstance(project).getProject()).reformat(parent.add(newComment));
            }
        } else {
            CodeStyleManager.getInstance(PsiManager.getInstance(project).getProject()).reformat(parent.addBefore(newComment, anchor));
        }
    }

    @NotNull
    private static XmlComment createComment(Project project, String s) throws IncorrectOperationException {
        final XmlTag element = XmlElementFactory.getInstance(project).createTagFromText("<foo><!-- " + s + " --></foo>", XMLLanguage.INSTANCE);
        final XmlComment newComment = PsiTreeUtil.getChildOfType(element, XmlComment.class);
        assert newComment != null;
        return newComment;
    }
}
