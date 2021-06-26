package com.efp.common.util;

import com.intellij.lang.xml.XMLLanguage;
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

public class DubboXmlConfigUtils {
    /**
     * 消费者配置
     *
     * @param e            事件对象
     * @param facaModule   service模块
     * @param serviceClass service类
     */
    public static void consumerXmlConfigSet(@NotNull AnActionEvent e, Module facaModule, PsiClass serviceClass, String baseMoudleName) {
        if (!Objects.isNull(facaModule)) {
            String consumerXmlFileName = "sofa-consumer-" + baseMoudleName + ".xml";
            PsiFile[] consumerXmlFileArr = FilenameIndex.getFilesByName(e.getProject(), consumerXmlFileName, facaModule.getModuleScope());
            if (consumerXmlFileArr.length > 0) {
                XmlFile consumerXmlFile = (XmlFile) consumerXmlFileArr[0];
                XmlTag rootTag = consumerXmlFile.getRootTag();
                //判断是否已经存在id
                String idValue = StringUtils.initCap(serviceClass.getName());
                //判断是否存在该id的tag
                if (!checkTagExist(rootTag, idValue, "id")) {
                    //<sofa:reference  interface="com.fdb.a.smcpi.facade.CrdtApplInfoService" id="crdtApplInfoService" unique-id="1.0.0"  ><sofa:binding.bolt/>
                    //</sofa:reference>
                    //生成配置
                    final XmlTag xmlTag = rootTag.createChildTag("sofa:reference", rootTag.getNamespace(), null, false);
                    xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                    xmlTag.setAttribute("id", idValue);
                    xmlTag.setAttribute("unique-id", "1.0.0");
                    rootTag.addSubTag(xmlTag, false);
                    consumerXmlFile.navigate(true);
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
        if (!Objects.isNull(startModule)) {
            String providerXmlFileName = "sofa-provider-" + baseMoudleName + ".xml";
            PsiFile[] providerXmlFileArr = FilenameIndex.getFilesByName(e.getProject(), providerXmlFileName, startModule.getModuleScope());
            if (providerXmlFileArr.length > 0) {
                XmlFile providerXmlFile = (XmlFile) providerXmlFileArr[0];
                XmlTag rootTag = providerXmlFile.getRootTag();
                //判断是否已经存在id
                String refValue = StringUtils.initCap(serviceClass.getName());
                //判断是否存在该id的tag
                if (!checkTagExist(rootTag, refValue, "ref")) {
/*                      <!-- 授信申请信息服务 -->
                        <sofa:service interface="com.fdb.a.smcpi.facade.CrdtApplInfoService" ref="crdtApplInfoService" unique-id="1.0.0">
                            <sofa:binding.bolt/>
                        </sofa:service>*/
                    //生成配置
                    final XmlTag xmlTag = rootTag.createChildTag("sofa:service", rootTag.getNamespace(), null, false);
                    xmlTag.setAttribute("interface", serviceClass.getQualifiedName());
                    xmlTag.setAttribute("ref", refValue);
                    xmlTag.setAttribute("unique", "1.0.0");
                    XmlTag sofaBindingBolt = xmlTag.createChildTag("sofa:binding.bolt", xmlTag.getNamespace(), null, false);
                    xmlTag.add(sofaBindingBolt);
                    rootTag.add(xmlTag);
                    providerXmlFile.navigate(true);
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
