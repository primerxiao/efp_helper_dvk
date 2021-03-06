package com.efp.plugins.project.coder.generator;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.SystemUtils;
import com.efp.plugins.project.coder.bean.*;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.DasColumn;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Generator {

    public static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates");

    /**
     * 是否支持覆盖
     */
    public boolean isOverWrite;
    /**
     * 生成信息
     */
    public GenerateInfo generateInfo;
    /**
     * 模板文件
     */
    public TemplateFileNameEnum tpFileName;

    /**
     * 新生成
     *
     * @return
     */
    abstract VirtualFile generate() throws IOException, TemplateException;

    public Generator(boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        this.isOverWrite = isOverWrite;
        this.generateInfo = generateInfo;
        this.tpFileName = tpFileName;
    }

    public Domain covertToDomainClassInfo(GenerateInfo generateInfo) {
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

    public static List<ClassField> coverToClassFieldInfos(GenerateInfo generateInfo) {
        List<? extends DasColumn> dasColumns = generateInfo.getDasColumns().toList();
        return dasColumns.stream().map(dasColumn ->
                new ClassField(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dasColumn.getName()),
                        dasColumn.getComment(), DasUtils.getJavaTypeClass(dasColumn),
                        dasColumn.getName(),
                        DasUtils.checkPrimaryKey(dasColumn))
        ).collect(Collectors.toList());
    }

    public StringWriter getSw() throws IOException, TemplateException {
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate(tpFileName.getFileName());
        switch (tpFileName) {
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
            case CONTROLLER:
                template.process(generateInfo, sw);
                break;
        }
        return sw;
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

    public static Mapper covertToMapperInfo(GenerateInfo generateInfo) {
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
     * moduleName fileName packagePath
     */
    public String[] getSimapleGenerateInfo() throws IOException, TemplateException {
        String[] arr = new String[3];
        switch (tpFileName) {
            case VO:
                arr[0] = generateInfo.getServiceModule().getName();
                arr[1] = generateInfo.getGenerateJava().getVoFileName();
                arr[2] = generateInfo.getGenerateJava().getVoPackagePath();
                break;
            case DAO:
                arr[0] = generateInfo.getImplModule().getName();
                arr[1] = generateInfo.getGenerateJava().getDaoFileName();
                arr[2] = generateInfo.getGenerateJava().getDaoPackagePath();
                break;
            case DOMAIN:
                arr[0] = generateInfo.getImplModule().getName();
                arr[1] = generateInfo.getGenerateJava().getDomainFileName();
                arr[2] = generateInfo.getGenerateJava().getDomainPackagePath();
                break;
            case MAPPER:
                arr[0] = generateInfo.getImplModule().getName();
                arr[1] = generateInfo.getGenerateJava().getMapperFileName();
                arr[2] = generateInfo.getGenerateJava().getMapperPath();
                break;
            case SERVICE:
                arr[0] = generateInfo.getServiceModule().getName();
                arr[1] = generateInfo.getGenerateJava().getServiceFileName();
                arr[2] = generateInfo.getGenerateJava().getServicePackagePath();
                break;
            case SERVICEIMPL:
                arr[0] = generateInfo.getImplModule().getName();
                arr[1] = generateInfo.getGenerateJava().getServiceImplFileName();
                arr[2] = generateInfo.getGenerateJava().getServiceImplPackagePath();
                break;
            case CONTROLLER:
                arr[0] = generateInfo.getApiModule().getName();
                arr[1] = generateInfo.getGenerateJava().getControllerFileName();
                arr[2] = generateInfo.getGenerateJava().getControllerPackagePath();
                break;
        }
        return arr;
    }

}
