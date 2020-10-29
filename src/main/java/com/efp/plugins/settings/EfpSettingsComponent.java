package com.efp.plugins.settings;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Objects;

public class EfpSettingsComponent {
    private JPanel myPanel;
    private JTextField dubboRegistryAddress;
    public JComponent getPanel() {
        return myPanel;
    }

    public void reset() {
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        if (StringUtils.isNotEmpty(state.dubboRegistryAddress)) {
            dubboRegistryAddress.setText(state.dubboRegistryAddress);
        } else {
            dubboRegistryAddress.setText("127.0.0.1:2181");
        }
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
        instance.loadState(instance);
    }
}
