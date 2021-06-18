package com.efp.plugins.project.coder.ui;

import com.alibaba.fastjson.JSON;
import com.efp.common.constant.PluginContants;
import com.efp.common.constant.TemplateFileNameEnum;
import com.efp.common.util.DubboXmlConfigUtils;
import com.efp.plugins.project.coder.bean.GenerateInfo;
import com.efp.plugins.project.coder.generator.*;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTableKey;
import com.intellij.database.model.DasTypedObject;
import com.intellij.database.model.MultiRef;
import com.intellij.database.util.DasUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.compiler.CompilerBundle;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareRunnable;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import freemarker.template.TemplateException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateOption extends DialogWrapper {
    private JPanel jpanel;
    private JCheckBox repositoryCheckBox;
    private JCheckBox mapperCheckBox;
    private JCheckBox facadeCheckBox;
    private JCheckBox controllerCheckBox;
    private JCheckBox openGenerateFileCheckBox;
    private JCheckBox doCheckBox;
    private JCheckBox inputCheckBox;
    private JCheckBox outputCheckBox;
    private JCheckBox poCheckBox;
    private JCheckBox producerCheckBox;
    private JCheckBox consumerCheckBox;
    private JCheckBox daoCheckBox;

    private AnActionEvent e;

    private GenerateInfo generateInfo;

    private List<VirtualFile> vfs = new ArrayList<>();

    private String generate_checkbox_cache = "generate_checkbox_cache";;

    public GenerateOption(boolean canBeParent, AnActionEvent e, GenerateInfo generateInfo) {
        super(canBeParent);
        this.e = e;
        this.generateInfo = generateInfo;
        init();
        setTitle(PluginContants.GENERATOR_UI_TITLE);
        getCache(generateInfo);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        repositoryCheckBox = new JCheckBox();
        mapperCheckBox = new JCheckBox();
        facadeCheckBox = new JCheckBox();
        controllerCheckBox = new JCheckBox();
        openGenerateFileCheckBox = new JCheckBox();
        doCheckBox = new JCheckBox();
        inputCheckBox = new JCheckBox();
        outputCheckBox = new JCheckBox();
        poCheckBox = new JCheckBox();
        producerCheckBox = new JCheckBox();
        consumerCheckBox = new JCheckBox();
        daoCheckBox = new JCheckBox();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.jpanel;
    }

    @Override
    protected void doOKAction() {
        setCache();
        super.doOKAction();
        vfs.clear();
        startGenerate(e, generateInfo);
    }

    /**
     * 开始创建
     * @param e 事件
     * @param generateInfo 生成信息
     */
    private void startGenerate(AnActionEvent e, final GenerateInfo generateInfo) {
        ProgressManager.getInstance().run(new Task.Backgroundable(generateInfo.getProject(), "生成文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.checkCanceled();
                WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
                    VirtualFile repositoryFile = null;
                    if (repositoryCheckBox.isSelected()) {
/*                         generateJava.setDomainClassName(generateJava.getBaseClassName());
                        generateJava.setDomainPackageName("com.irdstudio." + implModuleNameArr[0] + "." + implModuleNameArr[1] + ".service.domain");
                        //generateJava.setDomainPackagePath(generateInfo.getImplModule().getModuleFile().getParent().getPath() + "/src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/domain/");
                        generateJava.setDomainPackagePath(FilenameUtils.getFullPath(generateInfo.getImplModule().getModuleFilePath()) + "src/main/java/com/irdstudio/" + implModuleNameArr[0] + "/" + implModuleNameArr[1] + "/service/domain/");
                        generateJava.setDomainFileName(generateJava.getBaseClassName() + ".java");
*/
                    }
                    VirtualFile mapperFile = null;
                    if (mapperCheckBox.isSelected()) {

                    }
                    VirtualFile facadeFile = null;
                    if (facadeCheckBox.isSelected()) {

                    }
                    VirtualFile controllerFile = null;
                    if (controllerCheckBox.isSelected()) {

                    }
                    VirtualFile openGenerateFileFile = null;
                    if (openGenerateFileCheckBox.isSelected()) {

                    }
                    VirtualFile doFile = null;
                    if (doCheckBox.isSelected()) {

                    }
                    VirtualFile inputFile = null;
                    if (inputCheckBox.isSelected()) {

                    }
                    VirtualFile outputFile = null;
                    if (outputCheckBox.isSelected()) {

                    }
                    VirtualFile poFile = null;
                    if (poCheckBox.isSelected()) {

                    }
                    VirtualFile producerFile = null;
                    if (producerCheckBox.isSelected()) {

                    }
                    VirtualFile consumerFile = null;
                    if (consumerCheckBox.isSelected()) {

                    }
                    addVfs(repositoryFile, mapperFile, facadeFile, controllerFile, openGenerateFileFile, doFile, inputFile, outputFile, poFile, producerFile, consumerFile);
                    doOptimize(e.getProject());
                    //保存文档
                    FileDocumentManagerImpl.getInstance().saveAllDocuments();
                    Notifications.Bus.notify(new Notification(PluginContants.GENERATOR_UI_TITLE, PluginContants.GENERATOR_UI_TITLE,
                            "所有文件生成完成", NotificationType.INFORMATION));
                });
            }
        });
    }

    private void addVfs(VirtualFile... files) {
        for (VirtualFile file : files) {
            if (Objects.isNull(file)) {
                continue;
            }
            if (vfs.contains(file)) {
                continue;
            }
            vfs.add(file);
        }
    }

/*    private void generateDubboConfig(@NotNull AnActionEvent e, GenerateInfo generateInfo, VirtualFile service) {
        PsiFile file = PsiManager.getInstance(generateInfo.getProject()).findFile(service);
        if (!Objects.isNull(file)) {
            DubboXmlConfigUtils.consumerXmlConfigSet(e, generateInfo.getServiceModule(), ((PsiJavaFile) file).getClasses()[0]);
            DubboXmlConfigUtils.poviderXmlConfigSet(e, generateInfo.getImplModule(), ((PsiJavaFile) file).getClasses()[0]);
        }
    }*/

    private boolean checkPrimaryKey(DasColumn dasColumn) {
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

    private void doOptimize(Project project) {
        DumbService.getInstance(project).runWhenSmart((DumbAwareRunnable) () -> new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) {
                PsiDocumentManager.getInstance(project).commitAllDocuments();
                for (VirtualFile virtualFile : vfs) {
                    try {
                        PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                        if (Objects.isNull(file)) {
                            continue;
                        }
                        if (file.getFileType() instanceof JavaFileType) {
                            CodeStyleManager.getInstance(project).reformat(file);
                            PsiJavaFile javaFile = (PsiJavaFile) file;
                            JavaCodeStyleManager.getInstance(project).optimizeImports(javaFile);
                            JavaCodeStyleManager.getInstance(project).shortenClassReferences(javaFile);
                        }
                        if (openGenerateFileCheckBox.isSelected()) {
                            file.navigate(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                vfs.clear();
            }
        }.execute());
    }

    private void getCache(GenerateInfo generateInfo) {
        String value = PropertiesComponent.getInstance(generateInfo.getProject()).getValue(generate_checkbox_cache);
        if (StringUtils.isEmpty(value)) {
            return;
        }
        boolean[] parse = (boolean[]) JSON.parse(value);
        repositoryCheckBox.setSelected(parse[0]);
        mapperCheckBox.setSelected(parse[1]);
        facadeCheckBox.setSelected(parse[2]);
        controllerCheckBox.setSelected(parse[3]);
        openGenerateFileCheckBox.setSelected(parse[4]);
        doCheckBox.setSelected(parse[5]);
        inputCheckBox.setSelected(parse[6]);
        outputCheckBox.setSelected(parse[7]);
        poCheckBox.setSelected(parse[8]);
        producerCheckBox.setSelected(parse[9]);
        consumerCheckBox.setSelected(parse[10]);
        daoCheckBox.setSelected(parse[11]);
    }

    private void setCache() {
        boolean[] booleans = {
                repositoryCheckBox.isSelected(),
                mapperCheckBox.isSelected(),
                facadeCheckBox.isSelected(),
                controllerCheckBox.isSelected(),
                openGenerateFileCheckBox.isSelected(),
                doCheckBox.isSelected(),
                inputCheckBox.isSelected(),
                outputCheckBox.isSelected(),
                poCheckBox.isSelected(),
                producerCheckBox.isSelected(),
                consumerCheckBox.isSelected(),
                daoCheckBox.isSelected()
        };
        String o = JSON.toJSONString(booleans);
        PropertiesComponent.getInstance(generateInfo.getProject()).setValue(generate_checkbox_cache, o);
    }
}
