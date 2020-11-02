package com.efp.plugins.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 插件配置
 */
public class EfpSettingsConfigurable implements Configurable {

    /**
     * 配置界面面板
     */
    private EfpSettingsComponent mySettingsPane;

    @Nls
    @Override
    public String getDisplayName() {
        return "插件配置";
    }

    @NotNull
    @Override
    public String getHelpTopic() {
        return "efp.help";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (mySettingsPane == null) {
            mySettingsPane = new EfpSettingsComponent();
        }
        return mySettingsPane.getPanel();
    }

    @Override
    public boolean isModified() {
        return mySettingsPane != null && mySettingsPane.isModified();
    }

    @Override
    public void apply() {
        if (mySettingsPane != null) {
            mySettingsPane.apply();
        }
    }

    @Override
    public void reset() {
        if (mySettingsPane != null) {
            mySettingsPane.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        mySettingsPane = null;
    }
}
