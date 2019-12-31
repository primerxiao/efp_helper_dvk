package com.efp.plugins.codeHelper.generator;

import com.efp.common.config.FreemarkerConfiguration;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.CodeHelperUtils;
import com.efp.common.util.SystemUtils;
import com.efp.plugins.codeHelper.bean.ClassField;
import com.efp.plugins.codeHelper.bean.Domain;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.google.common.base.CaseFormat;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTableKey;
import com.intellij.database.model.DasTypedObject;
import com.intellij.database.model.MultiRef;
import com.intellij.database.util.DasUtil;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class GeneratorService {

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
     * @return
     */
    abstract void generate() throws IOException, TemplateException;

    public GeneratorService(boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
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
    public List<ClassField> coverToClassFieldInfos(GenerateInfo generateInfo) {
        List<? extends DasColumn> dasColumns = generateInfo.getDasColumns().toList();
        return dasColumns.stream().map(dasColumn ->
                new ClassField(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dasColumn.getName()),
                        dasColumn.getComment(), CodeHelperUtils.getTypeClass(dasColumn.getDataType().typeName),
                        dasColumn.getName(),
                        checkPrimaryKey(dasColumn))
        ).collect(Collectors.toList());
    }
    public boolean checkPrimaryKey(DasColumn dasColumn) {
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

}
