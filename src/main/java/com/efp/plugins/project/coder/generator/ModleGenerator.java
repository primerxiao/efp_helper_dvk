package com.efp.plugins.project.coder.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * input output do vo 的模型生成器
 */
public class ModleGenerator extends Generator {

    public ModleGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        //设置参数
        super(isOverWrite, generateInfo, tpFileName);
    }

    @Override
    public VirtualFile generate() throws IOException, TemplateException {
        VirtualFile virtualFile = null;
        String packageName = null;
        //查找文件看是否存在
        PsiFile[] filesByName = FilenameIndex.getFilesByName(
                generateInfo.getProject(),
                generateInfo.getFileName(),
                Objects.requireNonNull(
                        ModuleManager.getInstance(generateInfo.getProject()).
                                findModuleByName(generateInfo.getCurrentModule().getName())).getModuleScope()
        );
        if (filesByName != null && filesByName.length > 0) {
            virtualFile = filesByName[0].getVirtualFile();
            packageName = ((PsiJavaFile) PsiManager.getInstance(generateInfo.getProject()).findFile(virtualFile))
                    .getPackageStatement().getPackageName();
        } else {
            File packagePath = new File(generateInfo.getPackagePath());
            if (!packagePath.exists()) {
                FileUtils.forceMkdir(packagePath);
            }
            virtualFile = VfsUtil.findFile(packagePath.toPath(), true)
                    .createChildData(generateInfo.getProject(), generateInfo.getFileName());
        }
        virtualFile.setBinaryContent(getSw().toString().getBytes(Charset.forName("utf-8")));
        //重新设置包名
        if (StringUtils.isNotEmpty(packageName)) {
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
            PsiJavaFile psiFile = (PsiJavaFile) (
                    FilenameIndex.getFilesByName(
                            generateInfo.getProject(),
                            generateInfo.getFileName(),
                            ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(generateInfo.getCurrentModule().getName()).getModuleScope()
                    )
            )[0];
            psiFile.setPackageName(packageName);
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
            return psiFile.getVirtualFile();
        }
        return virtualFile;
    }

}
