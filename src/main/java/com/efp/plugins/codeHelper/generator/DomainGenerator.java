package com.efp.plugins.codeHelper.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.codeHelper.bean.GenerateInfo;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public class DomainGenerator extends Generator {

    public DomainGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        super(isOverWrite, generateInfo, tpFileName);
    }

    @Override
    public VirtualFile generate() throws IOException, TemplateException {
        VirtualFile virtualFile = null;
        String packageName = null;
        //查找文件看是否存在
        PsiFile[] filesByName = FilenameIndex.getFilesByName(generateInfo.getProject(),
                generateInfo.getGenerateJava().getDomainFileName(), generateInfo.getImplModule().getModuleScope());
        if (filesByName != null && filesByName.length > 0) {
            virtualFile = filesByName[0].getVirtualFile();
            packageName = ((PsiJavaFile) PsiManager.getInstance(generateInfo.getProject()).findFile(virtualFile))
                    .getPackageStatement().getPackageName();
        } else {
            File voPackagePath = new File(generateInfo.getGenerateJava().getDomainPackagePath());
            if (!voPackagePath.exists()) {
                FileUtils.forceMkdir(voPackagePath);
            }
            virtualFile = VfsUtil.findFile(voPackagePath.toPath(), true)
                    .createChildData(generateInfo.getProject(), generateInfo.getGenerateJava().getDomainFileName());
        }
        virtualFile.setBinaryContent(getSw().toString().getBytes(Charset.forName("utf-8")));
        //重新设置包名
        if (StringUtils.isNotEmpty(packageName)) {
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
            PsiJavaFile psiFile = (PsiJavaFile) (FilenameIndex.getFilesByName(generateInfo.getProject(),
                    generateInfo.getGenerateJava().getDomainFileName(), generateInfo.getImplModule().getModuleScope()))[0];
            psiFile.setPackageName(packageName);
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
        }
        return virtualFile;
    }

}
