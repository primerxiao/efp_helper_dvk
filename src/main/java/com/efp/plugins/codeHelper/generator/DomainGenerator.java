package com.efp.plugins.codeHelper.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public class DomainGenerator extends GeneratorService {

    public DomainGenerator(Boolean isOverWrite,GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        super(isOverWrite,generateInfo, tpFileName);
    }

    @Override
    public void generate() throws IOException, TemplateException {
        //根据模板生成数据
        StringWriter sw = new StringWriter();
        Template template = freemarker.getTemplate(tpFileName.getFileName());
        template.process(covertToDomainClassInfo(generateInfo), sw);
        //判断包是否存在
        File voPackagePath = new File(generateInfo.getGenerateJava().getDomainPackagePath());
        if (!voPackagePath.exists()) {
            FileUtils.forceMkdir(voPackagePath);
        }
        VirtualFile packageDir = VfsUtil.findFile(voPackagePath.toPath(), true);

        VirtualFile virtualFile = packageDir.findChild(generateInfo.getGenerateJava().getDomainFileName());
        if (Objects.isNull(virtualFile)) {
            virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getDomainFileName());
        } else {
            if (!isOverWrite) {
                return;
            }
        }
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
    }

}
