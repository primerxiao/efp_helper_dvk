package com.efp.plugins.project.coder.generator;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DasUtils;
import com.efp.common.util.SystemUtils;
import com.efp.plugins.project.coder.bean.*;
import com.efp.plugins.project.coder.template.method.SimpleBaseModuleNameMethod;
import com.efp.plugins.project.coder.util.GenUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
        //设置参数
        GenUtils.setValue(generateInfo,tpFileName);
    }

    public StringWriter getSw() throws IOException, TemplateException {
        StringWriter sw = new StringWriter();

        HashMap<String, Object> root = new HashMap<>();

        root.put("simpleBaseModuleNameMethod", new SimpleBaseModuleNameMethod());
        root.put("generateInfo", generateInfo);

        Template template = freemarker.getTemplate(tpFileName.getFileName());
        template.process(root, sw);
        return sw;
    }


}
