package com.efp.plugins.coder.generator;

import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.plugins.coder.bean.GenerateInfo;
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

public class DaoGenerator extends Generator {

    public DaoGenerator(Boolean isOverWrite, GenerateInfo generateInfo, TemplateFileNameEnum tpFileName) {
        super(isOverWrite,generateInfo, tpFileName);
    }

    @Override
    public VirtualFile generate() throws IOException, TemplateException {
        final String[] simapleGenerateInfo = getSimapleGenerateInfo();
        VirtualFile virtualFile = null;
        String packageName = null;
        //查找文件看是否存在
        PsiFile[] filesByName = FilenameIndex.getFilesByName(
                generateInfo.getProject(),
                simapleGenerateInfo[1],
                ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(simapleGenerateInfo[0]).getModuleScope()
        );
        if (filesByName != null && filesByName.length > 0) {
            if (!isOverWrite) {
                return null;
            }
            virtualFile = filesByName[0].getVirtualFile();
            packageName = ((PsiJavaFile) PsiManager.getInstance(generateInfo.getProject()).findFile(virtualFile))
                    .getPackageStatement().getPackageName();
        } else {
            File packagePath = new File(simapleGenerateInfo[2]);
            if (!packagePath.exists()) {
                FileUtils.forceMkdir(packagePath);
            }
            virtualFile = VfsUtil.findFile(packagePath.toPath(), true)
                    .createChildData(generateInfo.getProject(), simapleGenerateInfo[1]);
        }
        virtualFile.setBinaryContent(getSw().toString().getBytes(Charset.forName("utf-8")));
        //重新设置包名
        if (StringUtils.isNotEmpty(packageName)) {
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
            PsiJavaFile psiFile = (PsiJavaFile) (
                    FilenameIndex.getFilesByName(
                            generateInfo.getProject(),
                            simapleGenerateInfo[1],
                            ModuleManager.getInstance(generateInfo.getProject()).findModuleByName(simapleGenerateInfo[0]).getModuleScope()
                    )
            )[0];
            psiFile.setPackageName(packageName);
            PsiDocumentManager.getInstance(generateInfo.getProject()).commitAllDocuments();
            return psiFile.getVirtualFile();
        }
        return virtualFile;
    }

}
