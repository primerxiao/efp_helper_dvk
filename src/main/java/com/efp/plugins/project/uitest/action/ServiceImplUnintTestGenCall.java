package com.efp.plugins.project.uitest.action;

import com.efp.common.util.NotifyUtils;
import com.efp.common.util.PluginStringUtils;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 添加sofa生产者和消费者配置
 *
 * @author primerxiao
 */
public class ServiceImplUnintTestGenCall extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        try {
            //获取当前操作的class
            PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
            Module applicationModule = ModuleUtil.findModuleForPsiElement(psiClass);
            //开始生成代码
            @NotNull PsiFile[] testPsiFileArr = FilenameIndex.getFilesByName(project, psiClass.getName() + "Test" + ".java", applicationModule.getModuleScope());
            //类已经存在
            if (testPsiFileArr.length > 0) {
                return;
            }
            //类不存在
            ApplicationManager.getApplication().runWriteAction(
                    () -> WriteCommandAction.runWriteCommandAction(project, "", null, () -> {
                        try {
                            PsiJavaFile containingJava = PsiTreeUtil.getParentOfType(psiClass, PsiJavaFile.class);
                            assert containingJava != null;
                            File packagePath = new File(
                                    org.apache.commons.lang3.StringUtils.join(
                                            FilenameUtils.getFullPath(applicationModule.getModuleFilePath()),
                                            "src/test/java/",
                                            PluginStringUtils.package2Path(containingJava.getPackageName())));
                            if (!packagePath.exists()) {
                                FileUtils.forceMkdir(packagePath);
                            }
                            VirtualFile childData = VfsUtil.findFile(packagePath.toPath(), true).createChildData(project, containingJava.getName().replace("Impl", "ImplTest"));
                            childData.setBinaryContent(getInitServiceImplTestStr(psiClass, applicationModule).getBytes(StandardCharsets.UTF_8));
                            PsiDocumentManager.getInstance(project).commitAllDocuments();
                            PsiManager.getInstance(project).findFile(childData).navigate(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));

        } catch (
                Exception e) {
            NotifyUtils.notifyError("错误：" + e.getMessage());
        }

    }

    private String getInitServiceImplTestStr(PsiClass psiClass, Module applicationModule) {
        String[] split = applicationModule.getName().split("-");
        String simpleModuleName = split[1];
        String baseModleName = psiClass.getName().replace("ServiceImpl", "");


        return "package com.fdb.a." + simpleModuleName + ".application.service.impl;\n" +
                "\n" +
                "import com.fdb.a." + simpleModuleName + ".acl.repository." + baseModleName + "Repository;\n" +
                "import com.fdb.a." + simpleModuleName + ".domain.entity." + baseModleName + "DO;\n" +
                "import com.fdb.a." + simpleModuleName + ".facade.dto." + baseModleName + "Input;\n" +
                "import com.fdb.a." + simpleModuleName + ".facade.dto." + baseModleName + "Output;\n" +
                "import com.fdb.smcts.core.vo.IsrvRspInfoOutput;\n" +
                "import ma.glasnost.orika.MapperFacade;\n" +
                "import ma.glasnost.orika.impl.DefaultMapperFactory;\n" +
                "import org.junit.After;\n" +
                "import org.junit.Before;\n" +
                "import org.junit.Test;\n" +
                "import org.junit.runner.RunWith;\n" +
                "import org.mockito.ArgumentMatchers;\n" +
                "import org.mockito.InjectMocks;\n" +
                "import org.mockito.Mock;\n" +
                "import org.powermock.api.mockito.PowerMockito;\n" +
                "import org.powermock.core.classloader.annotations.PrepareForTest;\n" +
                "import org.powermock.modules.junit4.PowerMockRunner;\n" +
                "\n" +
                "@RunWith(PowerMockRunner.class)\n" +
                "@PrepareForTest({" + baseModleName + "ServiceImpl.class})\n" +
                "public class " + baseModleName + "ServiceImplTest {\n" +
                "\n" +
                "    @InjectMocks\n" +
                "    private " + baseModleName + "ServiceImpl " + PluginStringUtils.initCap(baseModleName) + "Service = new " + baseModleName + "ServiceImpl();\n" +
                "\n" +
                "    @Mock\n" +
                "    private " + baseModleName + "Repository " + PluginStringUtils.initCap(baseModleName) + "Repository;\n" +
                "\n" +
                "    private MapperFacade mapperFacade;\n" +
                "\n" +
                "    @Before\n" +
                "    public void setUp() {\n" +
                "        System.out.println(\"before test method invoke\");\n" +
                "        mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();\n" +
                "        " + PluginStringUtils.initCap(baseModleName) + "Service.mapperFacade = mapperFacade;\n" +
                "    }\n" +
                "\n" +
                "    @After\n" +
                "    public void tearDown() {\n" +
                "        System.out.println(\"afater test method invoke\");\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void insert() {\n" +
                "        " + baseModleName + "Input input = new " + baseModleName + "Input();\n" +
                "        PowerMockito.when(" + PluginStringUtils.initCap(baseModleName) + "Repository.insertSingle(ArgumentMatchers.any(" + baseModleName + "DO.class))).thenReturn(1);\n" +
                "        IsrvRspInfoOutput isrvRspInfoOutput = " + PluginStringUtils.initCap(baseModleName) + "Service.insertSingle(input);\n" +
                "        assert isrvRspInfoOutput.getRspCnt() > 0;\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void queryByPK() {\n" +
                "        " + baseModleName + "Input input = new " + baseModleName + "Input();\n" +
                "        //赋值\n" +
                "        " + baseModleName + "DO pkDO = new " + baseModleName + "DO();\n" +
                "        //赋值\n" +
                "        PowerMockito.when(" + PluginStringUtils.initCap(baseModleName) + "Repository.queryByPk(ArgumentMatchers.any(" + baseModleName + "DO.class))).thenReturn(pkDO);\n" +
                "        " + baseModleName + "Output crdtApplInfoOutput = " + PluginStringUtils.initCap(baseModleName) + "Service.queryByPk(input);\n" +
                "        assert crdtApplInfoOutput != null;\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void updateByPk() {\n" +
                "        " + baseModleName + "Input input = new " + baseModleName + "Input();\n" +
                "        //赋值\n" +
                "        " + baseModleName + "DO pkDO = new " + baseModleName + "DO();\n" +
                "        //赋值;\n" +
                "        PowerMockito.when(" + PluginStringUtils.initCap(baseModleName) + "Repository.updateByPk(ArgumentMatchers.any(" + baseModleName + "DO.class))).thenReturn(1);\n" +
                "        IsrvRspInfoOutput isrvRspInfoOutput = " + PluginStringUtils.initCap(baseModleName) + "Service.updateByPk(input);\n" +
                "        assert isrvRspInfoOutput.getRspCnt() > 0;\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void deleteByPk() {\n" +
                "        " + baseModleName + "Input input = new " + baseModleName + "Input();\n" +
                "        //赋值\n" +
                "        PowerMockito.when(" + PluginStringUtils.initCap(baseModleName) + "Repository.deleteByPk(ArgumentMatchers.any(" + baseModleName + "DO.class))).thenReturn(1);\n" +
                "        IsrvRspInfoOutput isrvRspInfoOutput = " + PluginStringUtils.initCap(baseModleName) + "Service.deleteByPk(input);\n" +
                "        assert isrvRspInfoOutput.getRspCnt() > 0;\n" +
                "    }\n" +
                "}\n";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {

        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        if (psiClass == null) {
            return false;
        }
        return Objects.requireNonNull(psiClass.getName()).endsWith("ServiceImpl");
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
        return "FDB:创建单元测试类";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getText() {
        return this.getFamilyName();
    }

}
