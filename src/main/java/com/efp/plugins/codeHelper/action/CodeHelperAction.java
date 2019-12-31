package com.efp.plugins.codeHelper.action;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.CodeHelperUtils;
import com.efp.common.util.DubboXmlConfigUtils;
import com.efp.common.util.SystemUtils;
import com.efp.plugins.codeHelper.bean.*;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.*;
import com.intellij.database.psi.DbNamespaceImpl;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.util.IconUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CodeHelperAction extends AnAction {

    private static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates");

    private List<VirtualFile> vfs = new ArrayList<>();

    /**
     * 是否对原有代码进行覆盖
     */
    private boolean isOverride;

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
        int checkFlag = Messages.showCheckboxOkCancelDialog("如果已经存在代码，请确实是否需要覆盖", null, "覆盖代码", true, 0, 1, IconUtil.getEditIcon());
        if (checkFlag == 1) {
            this.isOverride = true;
        } else if (checkFlag ==0) {
            this.isOverride = false;
        } else {
            return;
        }
        startGenerate(e, generateInfo);
    }

    private void startGenerate(AnActionEvent e, final GenerateInfo generateInfo) {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    try {
                        generateDomain(generateInfo);
                        generateVo(generateInfo);
                        generateDao(generateInfo);
                        VirtualFile service = generateService(generateInfo);
                        generateServiceImpl(generateInfo);
                        generateMapper(generateInfo);
                        if (!Objects.isNull(service)) {
                            generateDubboConfig(e, generateInfo, service);
                        }
                        doOptimize(e.getProject());
                        //保存文档
                        FileDocumentManagerImpl.getInstance().saveAllDocuments();
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE, "所有文件生成完成", NotificationType.INFORMATION));
                    } catch (IOException | TemplateException e) {
                        String message = CompilerBundle.message("message.tect.package.file.io.error", e.toString());
                        Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE, message, NotificationType.ERROR));
                    }
                });
            }
        });
    }

    private void generateDubboConfig(@NotNull AnActionEvent e, GenerateInfo generateInfo, VirtualFile service) {
        PsiFile file = PsiManager.getInstance(generateInfo.getProject()).findFile(service);
        if (!Objects.isNull(file)) {
            DubboXmlConfigUtils.consumerXmlConfigSet(e, generateInfo.getServiceModule(), ((PsiJavaFile) file).getClasses()[0]);
            DubboXmlConfigUtils.poviderXmlConfigSet(e, generateInfo.getImplModule(), ((PsiJavaFile) file).getClasses()[0]);
        }
    }

    private void generateDomain(GenerateInfo generateInfo) throws IOException, TemplateException {
        StringWriter sw = getStringWriter(generateInfo, TemplateFileNameEnum.DOMAIN);
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getDomainPackagePath());
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getDomainFileName(), packageDir);
        if (virtualFile == null) {
            return;
        }
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }
    private void generateVo(GenerateInfo generateInfo) throws IOException, TemplateException {
        File voPackagePath = new File(generateInfo.getGenerateJava().getVoPackagePath());
        if (!voPackagePath.exists()) {
            FileUtils.forceMkdir(voPackagePath);
        }
        VirtualFile packageDir = VfsUtil.findFile(voPackagePath.toPath(), true);
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getVoClassName() + ".java", packageDir);
        if (virtualFile == null) {
            return;
        }
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("vo.ftl");
        template.process(covertToVoClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }

    private VirtualFile generateDao(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getDaoPackagePath());
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getDaoClassName() + ".java", packageDir);
        if (virtualFile == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("dao.ftl");
        template.process(covertToDaoClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
        return virtualFile;
    }
    private VirtualFile generateService(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getServicePackagePath());
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getServiceClassName() + ".java", packageDir);
        if (virtualFile == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("service.ftl");
        template.process(covertToServiceClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
        return virtualFile;
    }

    private void generateServiceImpl(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getServiceImplPackagePath());
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getServiceImplClassName() + ".java", packageDir);
        if (virtualFile == null) {
            return;
        }
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("service_impl.ftl");
        template.process(covertToServiceImplClassInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        reformatJavaFile(generateInfo, virtualFile);
    }

    private void generateMapper(GenerateInfo generateInfo) throws IOException, TemplateException {
        VirtualFile packageDir = VfsUtil.createDirectoryIfMissing(generateInfo.getGenerateJava().getMapperPath());
        VirtualFile virtualFile = getVirtualFile(generateInfo, generateInfo.getGenerateJava().getMapperFileNameWithoutExt() + ".xml", packageDir);
        if (virtualFile == null) {
            return;
        }
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate("mapper.ftl");
        template.process(covertToMapperInfo(generateInfo), sw);
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
    }

    private void reformatJavaFile(GenerateInfo generateInfo, VirtualFile virtualFile) {
        vfs.add(virtualFile);
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
        generateJava.setDomainFileName(generateJava.getBaseClassName()+".java");
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
        generateJava.setMapperFileNameWithoutExt(generateJava.getBaseClassName() + "Mapper");
        generateJava.setMapperFileName(generateJava.getBaseClassName()+"Mapper.xml");
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

    @Nullable
    private VirtualFile getVirtualFile(GenerateInfo generateInfo, String fileName, VirtualFile packageDir) throws IOException {
        VirtualFile virtualFile = packageDir.findChild(fileName);
        if (!Objects.isNull(virtualFile)) {
            if (!isOverride) {
                //return null;
            }
        } else {
            virtualFile = packageDir.createChildData(generateInfo.getProject(), fileName);
        }
        return virtualFile;
    }

    private StringWriter getStringWriter(GenerateInfo generateInfo,TemplateFileNameEnum templateFileNameEnum) throws IOException, TemplateException{
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate(templateFileNameEnum.getFileName());
        switch (templateFileNameEnum) {
            case VO:
                template.process(covertToVoClassInfo(generateInfo), sw);
                break;
            case DAO:
                template.process(covertToDaoClassInfo(generateInfo), sw);
                break;
            case DOMAIN:
                template.process(covertToDomainClassInfo(generateInfo), sw);
                break;
            case MAPPER:
                template.process(covertToMapperInfo(generateInfo), sw);
                break;
            case SERVICE:
                template.process(covertToServiceClassInfo(generateInfo), sw);
                break;
            case SERVICEIMPL:
                template.process(covertToServiceImplClassInfo(generateInfo), sw);
                break;
        }
        return sw;
    }
}
