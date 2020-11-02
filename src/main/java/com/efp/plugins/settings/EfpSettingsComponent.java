package com.efp.plugins.settings;

import javax.swing.*;
import java.util.Objects;

/**
 * 插件配置面板
 */
public class EfpSettingsComponent {
    private JPanel myPanel;
    private JTextField dubboRegistryAddress;
    private JCheckBox providerCheckBox;
    private JCheckBox comsumerCheckBox;

    public JComponent getPanel() {
        return myPanel;
    }

    public void reset() {
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        dubboRegistryAddress.setText(Objects.isNull(state) ? "127.0.0.1:2181" : state.dubboRegistryAddress);
        providerCheckBox.setSelected(Objects.isNull(state) || state.providerCheckBox);
        comsumerCheckBox.setSelected(Objects.isNull(state) || state.comsumerCheckBox);
    }

    public boolean isModified() {
        EfpSettingsState state = EfpSettingsState.getInstance().getState();

        if (Objects.isNull(state)) {
            return true;
        }
        if (!state.dubboRegistryAddress.equals(dubboRegistryAddress.getText())) {
            return true;
        }
        return false;
    }

    public void apply() {
        EfpSettingsState instance = EfpSettingsState.getInstance();
        instance.dubboRegistryAddress = dubboRegistryAddress.getText();
        instance.providerCheckBox = providerCheckBox.isSelected();
        instance.comsumerCheckBox = comsumerCheckBox.isSelected();
        instance.loadState(instance);
    }
}
