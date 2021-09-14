package com.efp.plugins.project.tbtransfor.ui;

import com.efp.common.constant.PluginContants;
import com.efp.common.notifier.NotificationHelper;
import com.efp.common.util.PluginStringUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author primerxiao
 */
public class DeleteCodeByTableUI extends DialogWrapper {

    private JPanel Jpanel;

    private JComboBox<String> baseModuleComboBox;

    private JTextField tableNameTextField;

    private Project project;


    public DeleteCodeByTableUI(@Nullable Project project) {
        super(project);
        this.project = project;
        this.setOKActionEnabled(true);
        this.setOKButtonText("执行");
        this.setCancelButtonText("取消");
        PluginContants.CHOOSE_MODULE_NAMES.forEach(s -> {
            baseModuleComboBox.addItem(s);
        });
        init();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "开始删除相关文件") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    indicator.setFraction(0);
                    try {
                        NotificationHelper.getInstance().notifyInfo("开始删除文件", project);
                        deleteFiles(indicator);
                    } catch (Exception e) {
                        e.printStackTrace();
                        NotificationHelper.getInstance().notifyError(e.getMessage(), project);
                    } finally {
                        indicator.setFraction(100);
                    }
                });
            }
        });
    }

    private void deleteFiles(@NotNull ProgressIndicator indicator) {
        if (StringUtils.isEmpty((CharSequence) baseModuleComboBox.getSelectedItem())) {
            NotificationHelper.getInstance().notifyError("必须选择一个应用", project);
            return;
        }
        if (StringUtils.isEmpty(tableNameTextField.getText())) {
            NotificationHelper.getInstance().notifyError("必须设置一个表名", project);
            return;
        }
        String baseClassName = PluginStringUtils.upperFirstChar(PluginStringUtils.underlineToCamel(tableNameTextField.getText()));
        String baseMduleName = (String) baseModuleComboBox.getSelectedItem();
        Module start = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-start");
        Module domain = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-domain");
        Module facade = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-facade");
        Module application = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-application");
        Module infracl = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-infracl");
        Module infrastructure = ModuleManager.getInstance(project).findModuleByName(baseMduleName + "-infrastructure");
        //sofa config
        NotificationHelper.getInstance().notifyInfo("开始删除sofa-provider配置", project);
        if (start != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "sofa-provider-" + baseMduleName + ".xml", start.getModuleScope());
            if (psiFiles.length > 0) {
                XmlFile psiFile = (XmlFile) psiFiles[0];

                XmlTag rootTag = psiFile.getRootTag();
                XmlTag[] subTags = rootTag.getSubTags();
                for (XmlTag subTag : subTags) {
                    //interface="com.fdb.a.smcpi.facade.CrdtApplInfoService" ref="crdtApplInfoService"
                    XmlAttribute anInterface = subTag.getAttribute("interface");
                    XmlAttribute ref = subTag.getAttribute("ref");
                    if (anInterface == null) {
                        continue;
                    }
                    if (ref == null) {
                        continue;
                    }
                    if (("com.fdb.a." + baseMduleName.split("-")[1] + ".facade." + baseClassName + "Service").equals(anInterface.getValue())
                            && (PluginStringUtils.initCap(baseClassName) + "Service").equals(ref.getValue())
                    ) {
                        subTag.delete();
                    }
                }
            }
        }
        NotificationHelper.getInstance().notifyInfo("开始删除sofa-consumer配置", project);
        if (facade != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, "sofa-consumer-" + baseMduleName + ".xml", facade.getModuleScope());
            if (psiFiles.length > 0) {
                XmlFile psiFile = (XmlFile) psiFiles[0];

                XmlTag rootTag = psiFile.getRootTag();
                XmlTag[] subTags = rootTag.getSubTags();
                for (XmlTag subTag : subTags) {
                    //interface="com.fdb.a.smcpi.facade.CrdtApplInfoService" ref="crdtApplInfoService"
                    XmlAttribute id = subTag.getAttribute("id");

                    if (id == null) {
                        continue;
                    }
                    if ((PluginStringUtils.initCap(baseClassName) + "Service").equals(id.getValue())
                    ) {
                        subTag.delete();
                    }
                }

            }
        }
        //do
        NotificationHelper.getInstance().notifyInfo("开始删除do类", project);
        if (domain != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "DO.java", domain.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //po
        NotificationHelper.getInstance().notifyInfo("开始删除po类", project);
        if (infrastructure != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "PO.java", infrastructure.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //input
        NotificationHelper.getInstance().notifyInfo("开始删除input类", project);
        if (facade != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "Input.java", facade.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //output
        NotificationHelper.getInstance().notifyInfo("开始删除output类", project);
        if (facade != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "Output.java", facade.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //service
        NotificationHelper.getInstance().notifyInfo("开始删除service类", project);
        if (facade != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "Service.java", facade.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //serviceImpl
        NotificationHelper.getInstance().notifyInfo("开始删除serviceImpl类", project);
        if (application != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "ServiceImpl.java", application.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //repository
        NotificationHelper.getInstance().notifyInfo("开始删除repository类", project);
        if (infracl != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "Repository.java", infracl.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //reposityryImpl
        NotificationHelper.getInstance().notifyInfo("开始删除reposityryImpl类", project);
        if (infrastructure != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "RepositoryImpl.java", infrastructure.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //dao
        NotificationHelper.getInstance().notifyInfo("开始删除dao类", project);
        if (infrastructure != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "Mapper.java", infrastructure.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
        //xml
        NotificationHelper.getInstance().notifyInfo("开始删除mybatis xml 文件", project);
        if (infrastructure != null) {
            @NotNull PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, baseClassName + "MapperImpl.xml", infrastructure.getModuleScope());
            if (psiFiles.length > 0) {
                PsiFile psiFile = psiFiles[0];
                psiFile.delete();
            }
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return Jpanel;
    }
}
