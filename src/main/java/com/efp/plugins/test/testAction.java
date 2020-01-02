package com.efp.plugins.test;

import com.efp.common.constant.PluginContants;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DataSourceManager;
import com.intellij.database.util.DasUtil;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.dashboard.actions.RunAction;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.ide.macro.MacroManager;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.progress.util.BackgroundTaskUtil;
import com.intellij.openapi.ui.CheckBoxWithDescription;
import com.intellij.openapi.ui.DescriptionLabel;
import com.intellij.openapi.ui.popup.MultiSelectionListPopupStep;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.ui.popup.list.PopupListElementRenderer;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class testAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取所有数据库管理
        //Dialog dialog = new Dialog(PluginContants.GENERATOR_UI_TITLE, e.getProject());
        final DataContext dataContext = e.getDataContext();
        MacroManager.getInstance().cacheMacrosPreview(e.getDataContext());
        final ConfigurationContext context = ConfigurationContext.getFromContext(dataContext);
        List<RunConfiguration> allConfigurationsList = RunManager.getInstance(e.getProject()).getAllConfigurationsList();
        /*for (RunConfiguration runConfiguration : allConfigurationsList) {*/
           // SettingsEditor<? extends RunConfiguration> configurationEditor = runConfiguration.getConfigurationEditor();
            ExecutionUtil.doRunConfiguration(context.getConfiguration(), Executor.EXECUTOR_EXTENSION_NAME.getExtensionList().get(0), null, null, context.getDataContext());

        /*}*/
    }

    private void getDasList(AnActionEvent e) {
        //获取所有数据库管理
        List<DasDataSource> dataSources = null;
        List<DataSourceManager<?>> managers = DataSourceManager.getManagers(e.getProject());
        for (DataSourceManager<?> manager : managers) {
            dataSources = (List<DasDataSource>) manager.getDataSources();
            if (dataSources != null && dataSources.size() > 0) {
                break;
            }
        }
        ArrayList<DasDataSource> dasDataSources = new ArrayList<>(dataSources);
        //

        final BaseListPopupStep<DasDataSource> step =
                new BaseListPopupStep<DasDataSource>("请选择数据库配置源", dasDataSources) {
                    @Override
                    public boolean isAutoSelectionEnabled() {
                        return false;
                    }

                    @Override
                    public boolean isSpeedSearchEnabled() {
                        return true;
                    }

                    @Override
                    public PopupStep onChosen(DasDataSource selectedValue, boolean finalChoice) {
                        return getFieldList(selectedValue);
                    }

                    @Override
                    public boolean hasSubstep(DasDataSource selectedValue) {
                        return true;
                    }

                    @NotNull
                    @Override
                    public String getTextFor(DasDataSource value) {
                        return ObjectUtils.assertNotNull(value.getConnectionConfig().getName());
                    }

                    @Override
                    public Icon getIconFor(DasDataSource aValue) {
                        return aValue.getIcon(0);
                    }
                };

        ListPopupImpl popup = new ListPopupImpl(step) {
            @Override
            protected ListCellRenderer getListElementRenderer() {
                final PopupListElementRenderer baseRenderer = (PopupListElementRenderer) super.getListElementRenderer();
                final DefaultPsiElementCellRenderer psiRenderer = new DefaultPsiElementCellRenderer();
                return (list, value, index, isSelected, cellHasFocus) -> {
                    JPanel panel = new JPanel(new BorderLayout());
                    baseRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    panel.add(baseRenderer.getNextStepLabel(), BorderLayout.CENTER);
                    panel.add(psiRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus));
                    return panel;
                };
            }
        };
        NavigationUtil.hidePopupIfDumbModeStarts(popup, e.getProject());
        popup.showInFocusCenter();
    }

    private MultiSelectionListPopupStep getFieldList(DasDataSource dasDataSource) {
        JBIterable<? extends DasNamespace> schemas = DasUtil.getSchemas(dasDataSource);
        for (DasNamespace dasNamespace : schemas.toList()) {
            if (dasNamespace.getName().equalsIgnoreCase("efp_console")) {
                DasTable dasTable = DasUtil.getTables(dasDataSource).get(0);
                JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dasTable);
                return new MultiSelectionListPopupStep<DasColumn>("请选择数据表字段", columns.toList()) {
                    @NotNull
                    @Override
                    public String getTextFor(DasColumn value) {
                        return value.getName();
                    }

                    @Override
                    public PopupStep<?> onChosen(List<DasColumn> selectedValues, boolean finalChoice) {
                        return PopupStep.FINAL_CHOICE;
                    }
                };
            }
        }
        return null;
    }
}
