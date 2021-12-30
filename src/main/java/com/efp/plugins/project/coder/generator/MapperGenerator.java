package com.efp.plugins.project.coder.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;

public class MapperGenerator extends Generator {

    public MapperGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
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
                //不覆盖 那么就替换更新
                //先根据模板生成一份虚拟的
                XmlFile fileFromText = (XmlFile) PsiFileFactory.getInstance(generateInfo.getProject()).createFileFromText(XMLLanguage.INSTANCE, sw.toString().replaceAll("\r\n", "\n"));
                XmlFile mapperFile = (XmlFile) PsiManager.getInstance(generateInfo.getProject()).findFile(virtualFile);
                XmlTag[] mapperFileSubTags = mapperFile.getRootTag().getSubTags();
                XmlTag[] subTags = fileFromText.getRootTag().getSubTags();
                for (XmlTag subTag : subTags) {
                    XmlAttribute id = subTag.getAttribute("id");
                    if (Objects.isNull(id)) {
                        continue;
                    }
                    String value = id.getValue();
                    XmlTag xmlTag = Arrays.stream(mapperFileSubTags).filter(
                            e -> e.getAttribute("id") != null && e.getAttribute("id").getValue().equals(value))
                            .findFirst().orElseGet(null);
                    if (Objects.isNull(xmlTag)) {
                        //直接添加
                        mapperFile.getRootTag().addSubTag(subTag, true);
                        continue;
                    }
                    //替换
                    mapperFile.getRootTag().addBefore(subTag, xmlTag);
                    xmlTag.delete();
                }
                return virtualFile;
            }
        }
        virtualFile.setBinaryContent(sw.toString().getBytes(Charset.forName("utf-8")));
        return virtualFile;
    }

}
