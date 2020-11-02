package com.efp.plugins.dubbo.action;

import com.efp.common.util.StringUtils;
import com.efp.plugins.settings.EfpSettingsState;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 添加dubbo配置
 * @author HIFeng
 */
public class DubboConfigCall extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        //获取当前操作的类对象
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        if (psiClass == null) {
            return;
        }
        //获取当前操作的文件对象
        PsiFile psiFile = psiClass.getContainingFile();
        //获取当前操作的文件类型
        FileType fileType = psiFile.getFileType();
        if (fileType instanceof JavaFileType) {
            //判断当前类是接口还是实现类
            Module serviceModule = null;
            Module implModule = null;
            if (psiClass.isInterface()) {
                //service
                serviceModule = ModuleUtil.findModuleForFile(psiFile);
                PsiFile[] filesByName = FilenameIndex.getFilesByName(project, psiClass.getName() + "Impl.java", GlobalSearchScope.projectScope(project));
                implModule = ModuleUtil.findModuleForFile(filesByName[0]);
            } else {
                //impl
                psiClass = psiClass.getSuperClass();
                serviceModule = ModuleUtil.findModuleForFile(Objects.requireNonNull(psiClass).getContainingFile());
                implModule = ModuleUtil.findModuleForFile(psiFile);
            }
            //获取配置
            EfpSettingsState state = EfpSettingsState.getInstance().getState();
            if (Objects.requireNonNull(state).comsumerCheckBox) {
                consumerXmlConfigSet(project, serviceModule, implModule, psiClass);
            }
            if (state.providerCheckBox) {
                poviderXmlConfigSet(project, implModule, psiClass);
            }
        } else {
            Messages.showInfoMessage("非java文件，无法添加dubbo配置", "提示信息");
        }

    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        final PsiElement parent = psiElement.getParent();
        if (!(parent instanceof PsiClass)) return false;
        return true;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "Generate dubbo config";
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

    /**
     * 消费者配置
     *
     * @param project
     * @param serviceModule
     * @param implModule
     * @param serviceClass
     */
    private void consumerXmlConfigSet(@NotNull Project project, Module serviceModule, Module implModule, PsiClass serviceClass) {
        if (!Objects.isNull(serviceModule)) {
            String[] split = implModule.getName().split("\\.");
            String consumerXmlFileName = "dubbo-consumer-" + split[0] + "-" + split[1] + ".xml";
            PsiFile[] consumerXmlFileArr = FilenameIndex.getFilesByName(project, consumerXmlFileName, serviceModule.getModuleScope());
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
                    WriteCommandAction.runWriteCommandAction(project, () -> {
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
     * @param project
     * @param implModule
     * @param serviceClass
     */
    private void poviderXmlConfigSet(@NotNull Project project, Module implModule, PsiClass serviceClass) {
        if (!Objects.isNull(implModule)) {
            String[] split = implModule.getName().split("\\.");
            String providerXmlFileName = "dubbo-provider-" + split[0] + "-" + split[1] + ".xml";
            PsiFile[] providerXmlFileArr = FilenameIndex.getFilesByName(project, providerXmlFileName, implModule.getModuleScope());
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
                    WriteCommandAction.runWriteCommandAction(project, () -> {
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
