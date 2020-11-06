package com.efp.plugins.settings;

import com.efp.plugins.settings.bean.RegCenter;
import com.efp.plugins.settings.bean.RegCenterTableModel;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 插件配置面板
 */
public class EfpSettingsComponent {
    private JPanel myPanel;
    private JCheckBox providerCheckBox;
    private JCheckBox comsumerCheckBox;
    private JPanel regCenterPanel;
    private JTable regCenterTable;
    private JButton addRegCenterButton;
    private JButton delRegCenterButton;

    EfpSettingsComponent(){
        super();
        addRegCenterButton.addActionListener(e -> new RegCenterAddPanel(true, regCenterTable).show());
        delRegCenterButton.addActionListener(e -> {
            //获取表格选中数据
            int[] selectedRows = regCenterTable.getSelectedRows();
            RegCenterTableModel model = (RegCenterTableModel) regCenterTable.getModel();
            List<RegCenter> regCenters = model.getRegCenters();
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                if (i <= regCenters.size()) {
                    regCenters.remove(selectedRows[i]);
                }
            }
            model.fireTableDataChanged();
        });
    }

    public JComponent getPanel() {
        return myPanel;
    }

    public void reset() {
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        providerCheckBox.setSelected(Objects.isNull(state) || state.providerCheckBox);
        comsumerCheckBox.setSelected(Objects.isNull(state) || state.comsumerCheckBox);
        regCenterTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        regCenterTable.setModel(new RegCenterTableModel(state.regCenters));

    }

    public boolean isModified() {
        EfpSettingsState state = EfpSettingsState.getInstance().getState();
        if (Objects.isNull(state)) {
            return true;
        }
        List<RegCenter> stateRegCenters = state.regCenters;
        RegCenterTableModel model = (RegCenterTableModel) regCenterTable.getModel();
        List<RegCenter> regCenters = model.getRegCenters();
        //如果缓存跟表格的长度不一样则变化
        if (stateRegCenters.size() != regCenters.size()) {
            return true;
        }
        //如果表格的数据在缓存中没有 则变化
        if (regCenters.stream().filter(regCenter -> !stateRegCenters.contains(regCenter)).collect(Collectors.toList()).size() > 0) {
            return true;
        }
        return false;
    }

    public void apply() {
        EfpSettingsState instance = EfpSettingsState.getInstance();
        instance.providerCheckBox = providerCheckBox.isSelected();
        instance.comsumerCheckBox = comsumerCheckBox.isSelected();
        RegCenterTableModel model = (RegCenterTableModel) regCenterTable.getModel();
        instance.regCenters = model.getRegCenters();
        instance.loadState(instance);
    }
}
