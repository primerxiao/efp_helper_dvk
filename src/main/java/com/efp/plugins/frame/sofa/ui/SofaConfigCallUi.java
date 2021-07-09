    package com.efp.plugins.frame.sofa.ui;

    import com.efp.common.constant.PluginContants;
    import com.efp.common.util.SofaXmlConfigUtils;
    import com.intellij.openapi.command.WriteCommandAction;
    import com.intellij.openapi.module.Module;
    import com.intellij.openapi.progress.ProgressIndicator;
    import com.intellij.openapi.progress.ProgressManager;
    import com.intellij.openapi.progress.Task;
    import com.intellij.openapi.project.Project;
    import com.intellij.openapi.ui.DialogWrapper;
    import com.intellij.psi.PsiClass;
    import org.jetbrains.annotations.NotNull;
    import org.jetbrains.annotations.Nullable;

    import javax.swing.*;

    public class SofaConfigCallUi extends DialogWrapper {

        private Project project;

        private Module serviceModule;

        private Module startModule;

        private PsiClass psiClass;

        private JPanel jPanel;
        private JCheckBox sofaConsumerCheckBox;
        private JCheckBox sofaProviderXmlCheckBox;

        public SofaConfigCallUi(Project project, Module serviceModule, Module startModule, PsiClass psiClass) {
            super(project);
            this.project = project;
            this.serviceModule = serviceModule;
            this.startModule = startModule;
            this.psiClass = psiClass;
            setOKActionEnabled(true);
            setOKButtonText("生成");
            setCancelButtonText("取消");
            setTitle(PluginContants.GENERATOR_UI_TITLE );
            init();
            sofaConsumerCheckBox.setSelected(true);
            sofaProviderXmlCheckBox.setSelected(true);
        }

        @Override
        protected @Nullable JComponent createCenterPanel() {
            return this.jPanel;
        }

        @Override
        protected void doOKAction() {
            super.doOKAction();
            ProgressManager.getInstance().run(new Task.Backgroundable(project, "生成文件") {

                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        String name = startModule.getName();
                        String[] split = name.split("-");
                        if (sofaProviderXmlCheckBox.isSelected()) {
                            SofaXmlConfigUtils.poviderXmlConfigSet(project, startModule, psiClass, split[0] + "-" + split[1]);
                        }
                        if (sofaConsumerCheckBox.isSelected()) {
                            SofaXmlConfigUtils.consumerXmlConfigSet(project, serviceModule, psiClass, split[0] + "-" + split[1]);
                        }
                    });
                }
            });
            super.close(0, true);
        }
    }
