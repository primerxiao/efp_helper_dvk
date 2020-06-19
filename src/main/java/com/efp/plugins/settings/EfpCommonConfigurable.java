package com.efp.plugins.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class EfpCommonConfigurable implements SearchableConfigurable {
    private EfpCommonConfigurablePanel mySettingsPane;

    @NotNull
    @Override
    public String getId() {
        return getHelpTopic();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Diff & Merge";
    }

    @NotNull
    @Override
    public String getHelpTopic() {
        return "diff.base";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (mySettingsPane == null) {
            mySettingsPane = new EfpCommonConfigurablePanel();
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
            //mySettingsPane.apply();
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
