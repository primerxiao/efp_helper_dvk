package com.efp.common.service;

import com.efp.plugins.mybatisjump.bean.IdDomElement;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yanglin
 * @update itar
 * @function 主要是运行后台服务的一个service类
 */
public class JavaService {

    private Project project;

    private JavaPsiFacade javaPsiFacade;

    public JavaService(Project project) {
        this.project = project;
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    }

    public static JavaService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, JavaService.class);
    }

    public Optional<PsiClass> getReferenceClazzOfPsiField(@NotNull PsiElement field) {
        if (!(field instanceof PsiField)) {
            return Optional.empty();
        }
        PsiType type = ((PsiField) field).getType();
        return type instanceof PsiClassReferenceType ? Optional.ofNullable(((PsiClassReferenceType) type).resolve())
            : Optional.<PsiClass>empty();
    }

    public void   process(@NotNull PsiMethod psiMethod, @NotNull Processor<DomElement> processor) {
        PsiClass psiClass = psiMethod.getContainingClass();
        if (null == psiClass) {
            return;
        }
        //id为 全限定名 + 方法名称
        String id = psiClass.getQualifiedName() + "." + psiMethod.getName();
        //找出所有的mapper xml文件
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        List<DomFileElement<DomElement>> elements = DomService.getInstance().getFileElements(DomElement.class, project, scope);
        //讲domFileElement转化为domElement
        Collections2.transform(elements, (Function<DomFileElement<DomElement>, DomElement>) input -> input.getRootElement());
        for (DomElement mapper : elements) {

                //如果在mapper中找到的id签名和这个方法一致，就加入processor的list里面
            processor.process(mapper);
        }
    }

    /**
     * 根据domElement找到method
     * @param domElement
     * @param processor
     */
    public void ProcessDaoMethod(@NotNull DomElement domElement,@NotNull Processor<PsiMethod> processor){

    }

    /**
     * 根据条件找到合适的mapper放在processor中待使用
     * @param target
     * @param processor
     */
    @SuppressWarnings("unchecked")
    public void process(@NotNull PsiElement target, @NotNull Processor processor) {
        if (target instanceof PsiMethod) {
            process((PsiMethod) target, processor);
        } else if (target instanceof PsiClass) {
            process((PsiClass) target, processor);
        }
    }
}

