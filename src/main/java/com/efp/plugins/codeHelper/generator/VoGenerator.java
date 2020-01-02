package com.efp.plugins.codeHelper.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public class VoGenerator extends Generator {

    public VoGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        super(isOverWrite,generateInfo, tpFileName);
    }

    @Override
    public VirtualFile generate() throws IOException, TemplateException {
        //根据模板生成数据
        StringWriter sw = getSw();
        //判断包是否存在
        File voPackagePath = new File(generateInfo.getGenerateJava().getVoPackagePath());
        if (!voPackagePath.exists()) {
            FileUtils.forceMkdir(voPackagePath);
        }
        VirtualFile packageDir = VfsUtil.findFile(voPackagePath.toPath(), true);

        VirtualFile virtualFile = packageDir.findChild(generateInfo.getGenerateJava().getVoFileName());
        if (Objects.isNull(virtualFile)) {
            virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getVoFileName());
        } else {
            if (!isOverWrite) {
                return null;
            }
        }
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        return virtualFile;
    }

}
