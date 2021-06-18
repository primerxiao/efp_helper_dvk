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

    public static FreemarkerConfiguration freemarker = new FreemarkerConfiguration("/templates/fdb");

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
        template.process(generateInfo, sw);
        return sw;
    }


}
