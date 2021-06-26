package com.efp.plugins.project.coder.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public class ControllerGenerator extends Generator {

    public ControllerGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        super(isOverWrite,generateInfo, tpFileName);
    }

    @Override
    public VirtualFile generate() throws IOException, TemplateException {
        //根据模板生成数据
        StringWriter sw = getSw();
        //判断包是否存在
        File packagePath = new File(generateInfo.getPackagePath());
        if (!packagePath.exists()) {
            FileUtils.forceMkdir(packagePath);
        }
        VirtualFile packageDir = VfsUtil.findFile(packagePath.toPath(), true);

        VirtualFile virtualFile = packageDir.findChild(generateInfo.getFileName());
        if (Objects.isNull(virtualFile)) {
            virtualFile = packageDir.createChildData(generateInfo.getProject(), generateInfo.getFileName());
        } else {
            if (!isOverWrite) {
                return null;
            }
        }
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        return virtualFile;
    }

}
