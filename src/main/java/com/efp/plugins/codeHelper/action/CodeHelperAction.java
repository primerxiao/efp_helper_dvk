package com.efp.plugins.codeHelper.action;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.PluginContants;
import com.efp.common.util.CodeHelperUtils;
import com.efp.common.util.DubboXmlConfigUtils;
import com.efp.common.util.SystemUtils;
import com.efp.plugins.codeHelper.bean.*;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.*;
import com.intellij.database.psi.DbNamespaceImpl;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CodeHelperAction extends AnAction {

    private static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates");

    private List<VirtualFile> vfs = new ArrayList<>();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        vfs.clear();
        //获取数据库配置
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof DasTable)) {
            Messages.showErrorDialog("请选择一个数据库表对象进行操作", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        final GenerateInfo generateInfo = getGenerateInfo(e, (DasTable) psiElement);
        //判断主键数量 暂时不支持联合主键
        if (getPrimaryKeyNum(generateInfo) > 1) {
            Messages.showErrorDialog("暂时不支持联合主键的生成", PluginContants.GENERATOR_UI_TITLE);
            return;
        }
        try {
            generateDomain(generateInfo);
            generateVo(generateInfo);
            generateDao(generateInfo);
            VirtualFile service = generateService(generateInfo);
            generateServiceImpl(generateInfo);
            generateMapper(generateInfo);
            //生成dubbo配置
            generateDubboConfig(e, generateInfo, service);
            doOptimize(e.getProject());
            //保存文档
            FileDocumentManagerImpl.getInstance().saveAllDocuments();
            Messages.showInfoMessage("代码生成成功", PluginContants.GENERATOR_UI_TITLE);
        } catch (IOException | TemplateException ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(ex.getLocalizedMessage(), PluginContants.GENERATOR_UI_TITLE);
        }
    }

    private void generateDubboConfig(@NotNull AnActionEvent e, GenerateInfo generateInfo, VirtualFile service) {
        PsiFile file = PsiManager.getInstance(generateInfo.getProject()).findFile(service);
        if (!Objects.isNull(file)) {
            DubboXmlConfigUtils.consumerXmlConfigSet(e, generateInfo.getServiceModule(), ((PsiJavaFile) file).getClasses()[0]);
            DubboXmlConfigUtils.poviderXmlConfigSet(e, generateInfo.getImplModule(), ((PsiJavaFile) file).getClasses()[0]);
        }
    }

    private void generateDomain( GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getDomainPackagePath());
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getDomainClassName() + ".java");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("domain.ftl");
        template.process(covertToDomainClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }

    private void generateVo(GenerateInfo generateInfo) throws IOException, TemplateException {
        File voPackagePath = new File(generateInfo.getGenerateJava().getVoPackagePath());
        if (!voPackagePath.exists()) {
            FileUtils.forceMkdir(voPackagePath);
        }
        VirtualFile packageDir = VfsUtil.findFile(voPackagePath.toPath(), true);
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getVoClassName() + ".java");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("vo.ftl");
        template.process(covertToVoClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }

    private VirtualFile generateDao(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getDaoPackagePath());
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getDaoClassName() + ".java");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("dao.ftl");
        template.process(covertToDaoClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
        return virtualFile;
    }

    private VirtualFile generateService(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getServicePackagePath());
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getServiceClassName() + ".java");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("service.ftl");
        template.process(covertToServiceClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
        return virtualFile;
    }

    private void generateServiceImpl(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getServiceImplPackagePath());
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getServiceImplClassName() + ".java");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("service_impl.ftl");
        template.process(covertToServiceImplClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }

    private void generateMapper(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getMapperPath());
        VirtualFile virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getMapperFileName() + ".xml");
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("mapper.ftl");
        template.process(covertToMapperInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
    }

    private void reformatJavaFile(GenerateInfo generateInfo, VirtualFile virtualFile) {
        vfs.add(virtualFile);
        if (true) {
            return;
        }
        CommandProcessor.getInstance().executeCommand(generateInfo.getProject(), () -> {
            PsiJavaFile javaFile = (PsiJavaFile) PsiManager.getInstance(generateInfo.getProject()).findFile(virtualFile);
            if (javaFile != null) {
                CodeStyleManager.getInstance(generateInfo.getProject()).reformat(javaFile);
                JavaCodeStyleManager.getInstance(generateInfo.getProject()).optimizeImports(javaFile);
                JavaCodeStyleManager.getInstance(generateInfo.getProject()).removeRedundantImports(javaFile);
                JavaCodeStyleManager.getInstance(generateInfo.getProject()).shortenClassReferences(javaFile);
                javaFile.navigate(true);
            }
        }, null, null);
    }

    private Domain covertToDomainClassInfo(GenerateInfo generateInfo) {
        Domain doMain = new Domain();
        doMain.setClassName(generateInfo.getGenerateJava().getDomainClassName());
        doMain.setComment(generateInfo.getDasTable().getComment());
        doMain.setAuthor(SystemUtils.getPcName());
        doMain.setDateStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        doMain.setPackageName(generateInfo.getGenerateJava().getDomainPackageName());
        doMain.setClassFieldInfos(coverToClassFieldInfos(generateInfo));
        doMain.getImports();
        return doMain;
    }

    private Domain covertToVoClassInfo(GenerateInfo generateInfo) {
        Domain doMain = new Domain();
        doMain.setClassName(generateInfo.getGenerateJava().getVoClassName());
        doMain.setComment(generateInfo.getDasTable().getComment());
        doMain.setAuthor(SystemUtils.getPcName());
        doMain.setDateStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        doMain.setPackageName(generateInfo.getGenerateJava().getVoPackageName());
        doMain.setClassFieldInfos(coverToClassFieldInfos(generateInfo));
        doMain.getImports();
        return doMain;
    }

    private Dao covertToDaoClassInfo(GenerateInfo generateInfo) {
        Dao dao = new Dao();
        dao.setClassName(generateInfo.getGenerateJava().getDaoClassName());
        dao.setComment(generateInfo.getDasTable().getComment());
        dao.setAuthor(SystemUtils.getPcName());
        dao.setDateStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dao.setPackageName(generateInfo.getGenerateJava().getDaoPackageName());
        dao.setBaseClassName(generateInfo.getGenerateJava().getBaseClassName());
        List<String> imports = new ArrayList<>();
        //domain import
        imports.add(generateInfo.getGenerateJava().getDomainPackageName() + "." + generateInfo.getGenerateJava().getDomainClassName());
        dao.setImports(imports);
        return dao;
    }

    private Service covertToServiceClassInfo(GenerateInfo generateInfo) {
        Service service = new Service();
        service.setClassName(generateInfo.getGenerateJava().getServiceClassName());
        service.setBaseClassName(generateInfo.getGenerateJava().getBaseClassName());
        service.setComment(generateInfo.getDasTable().getComment());
        service.setAuthor(SystemUtils.getPcName());
        service.setDateStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        service.setPackageName(generateInfo.getGenerateJava().getServicePackageName());
        List<String> imports = new ArrayList<>();
        //vo import
        imports.add(generateInfo.getGenerateJava().getVoPackageName() + "." + generateInfo.getGenerateJava().getVoClassName());
        service.setImports(imports);
        return service;
    }

    private ServiceImpl covertToServiceImplClassInfo(GenerateInfo generateInfo) {
        ServiceImpl serviceImpl = new ServiceImpl();
        serviceImpl.setClassName(generateInfo.getGenerateJava().getServiceImplClassName());
        serviceImpl.setBaseClassName(generateInfo.getGenerateJava().getBaseClassName());
        serviceImpl.setComment(generateInfo.getDasTable().getComment());
        serviceImpl.setAuthor(SystemUtils.getPcName());
        serviceImpl.setDateStr(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        serviceImpl.setPackageName(generateInfo.getGenerateJava().getServiceImplPackageName());
        List<String> imports = new ArrayList<>();
        //import
        imports.add("java.util.List");
        imports.add("java.util.Objects");
        imports.add("org.slf4j.Logger");
        imports.add("org.slf4j.LoggerFactory");
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add("org.springframework.stereotype.Service");
        imports.add("com.irdstudio.basic.framework.core.base.FrameworkService");
        //service dao domain vo
        imports.add(generateInfo.getGenerateJava().getServicePackageName() + "." + generateInfo.getGenerateJava().getServiceClassName());
        imports.add(generateInfo.getGenerateJava().getDaoPackageName() + "." + generateInfo.getGenerateJava().getDaoClassName());
        imports.add(generateInfo.getGenerateJava().getDomainPackageName() + "." + generateInfo.getGenerateJava().getDomainClassName());
        imports.add(generateInfo.getGenerateJava().getVoPackageName() + "." + generateInfo.getGenerateJava().getVoClassName());
        serviceImpl.setImports(imports);
        return serviceImpl;
    }

    private Mapper covertToMapperInfo(GenerateInfo generateInfo) {
        Mapper mapper = new Mapper();
        mapper.setTableName(generateInfo.getDasTable().getName());
        mapper.setBaseClassName(generateInfo.getGenerateJava().getBaseClassName());
        mapper.setClassFields(coverToClassFieldInfos(generateInfo));
        mapper.setBaseColumnList(mapper.getClassFields().stream().map(f -> f.getDasColumnName()).collect(Collectors.joining(",")));
        mapper.setDomainQuaName(generateInfo.getGenerateJava().getDomainPackageName() + "." + generateInfo.getGenerateJava().getDomainClassName());
        mapper.setDaoQuaName(generateInfo.getGenerateJava().getDaoPackageName() + "." + generateInfo.getGenerateJava().getDaoClassName());
        return mapper;
    }

    /**
     * 根据选择信息转换成类字段属性
     *
     * @param generateInfo
     */
    private List<ClassField> coverToClassFieldInfos(GenerateInfo generateInfo) {
        List<? extends DasColumn> dasColumns = generateInfo.getDasColumns().toList();
        return dasColumns.stream().map(dasColumn ->
                new ClassField(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dasColumn.getName()),
                        dasColumn.getComment(), CodeHelperUtils.getTypeClass(dasColumn.getDataType().typeName),
                        dasColumn.getName(),
                        checkPrimaryKey(dasColumn))
        ).collect(Collectors.toList());
    }

    private GenerateInfo getGenerateInfo(AnActionEvent e, DasTable dasTable) {
        DasNamespace namespace = DasUtil.getNamespace(dasTable);
        DasDataSource dataSource = ((DbNamespaceImpl) namespace).getDataSource();
        GenerateInfo generateInfo = new GenerateInfo();
        generateInfo.setDasDataSource(dataSource);
        generateInfo.setDasNamespace(namespace);
        generateInfo.setDasTable(dasTable);
        generateInfo.setDasColumns(DasUtil.getColumns(dasTable));
        generateInfo.setProject(e.getProject());
        String implModuleName = generateInfo.getDasNamespace().getName().replace("_", ".") + ".impl";
        if (implModuleName.contains("risk")) {
            implModuleName = implModuleName.replaceFirst("risk", "riskm");
        }
        generateInfo.setImplModule(ModuleManager.getInstance(e.getProject()).findModuleByName(implModuleName));
        String serviceModuleName = generateInfo.getDasNamespace().getName().replace("_", ".") + ".service";
        if (serviceModuleName.contains("risk")) {
            serviceModuleName = serviceModuleName.replaceFirst("risk", "riskm");
        }
        generateInfo.setServiceModule(ModuleManager.getInstance(e.getProject()).findModuleByName(serviceModuleName));
        generateInfo.setGenerateJava(getGenerateJava(generateInfo));
        return generateInfo;
    }

    private GenerateJava getGenerateJava(GenerateInfo generateInfo) {
        GenerateJava generateJava = new GenerateJava();
        final String[] implModuleNameArr = generateInfo.getImplModule().getName().split("\\.");
        final String[] serviceModuleNameArr = generateInfo.getServiceModule().getName().split("\\.");
        //判断路径是否存在
        //base
        generateJava.setBaseClassName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, generateInfo.getDasTable().getName()));
        //domain
        generateJava.setDomainClassName(generateJava.getBaseClassName());
        generateJava.setDomainPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.domain");
        generateJava.setDomainPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/domain/");
        //vo
        generateJava.setVoClassName(generateJava.getBaseClassName() + "VO");
        generateJava.setVoPackageName("com.irdstudio." + serviceModuleNameArr[0] + "." + serviceModuleNameArr[1] + ".service.vo");
        generateJava.setVoPackagePath(generateInfo.getServiceModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + serviceModuleNameArr[0] + "/" + serviceModuleNameArr[1] + "/service/vo/");
        //dao
        generateJava.setDaoClassName(generateJava.getBaseClassName() + "Dao");
        generateJava.setDaoPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.dao");
        generateJava.setDaoPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/dao/");
        //service
        generateJava.setServiceClassName(generateJava.getBaseClassName() + "Service");
        generateJava.setServicePackageName("com.irdstudio." + serviceModuleNameArr[0] + "." + serviceModuleNameArr[1] + ".service.facade");
        generateJava.setServicePackagePath(generateInfo.getServiceModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + serviceModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/facade/");
        //serviceImpl
        generateJava.setServiceImplClassName(generateJava.getBaseClassName() + "ServiceImpl");
        generateJava.setServiceImplPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.impl");
        generateJava.setServiceImplPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/impl/");
        //mapper
        generateJava.setMapperPath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/resources/mybatis/mapper/");
        generateJava.setMapperFileName(generateJava.getBaseClassName() + "Mapper");
        return generateJava;

    }

    private boolean checkPrimaryKey(DasColumn dasColumn) {
        DasTableKey primaryKey = DasUtil.getPrimaryKey(dasColumn.getTable());
        if (Objects.isNull(primaryKey)) {
            return false;
        }
        MultiRef<? extends DasTypedObject> columnsRef = primaryKey.getColumnsRef();
        if (Objects.isNull(columnsRef)) {
            return false;
        }
        Iterable<String> names = columnsRef.names();
        for (String name : names) {
            if (name.equalsIgnoreCase(dasColumn.getName())) {
                return true;
            }
        }
        return false;
    }

    private int getPrimaryKeyNum(GenerateInfo generateInfo) {
        DasTableKey primaryKey = DasUtil.getPrimaryKey(generateInfo.getDasTable());
        if (Objects.isNull(primaryKey)) {
            return 0;
        }
        MultiRef<? extends DasTypedObject> columnsRef = primaryKey.getColumnsRef();
        if (Objects.isNull(columnsRef)) {
            return 0;
        }
        Iterable<String> names = columnsRef.names();
        int num = 0;
        for (String name : names) {
            num++;
        }
        return num;
    }

    private void doOptimize(Project project) {
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                for (VirtualFile virtualFile : vfs) {
                    try {
                        PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                        if (Objects.isNull(file)) {
                            continue;
                        }
                        if (!(file.getFileType() instanceof JavaFileType)) {
                            continue;
                        }
                        PsiJavaFile javaFile = (PsiJavaFile) file;
                        CodeStyleManager.getInstance(project).reformat(javaFile);
                        JavaCodeStyleManager.getInstance(project).optimizeImports(javaFile);
                        JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                vfs.clear();
            }
        }.execute());
    }

}
