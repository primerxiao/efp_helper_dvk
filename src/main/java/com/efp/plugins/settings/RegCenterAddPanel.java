package com.efp.plugins.settings;

import com.efp.common.constant.RegCenterTypeEnum;
import com.efp.common.util.RuleUtils;
import com.efp.plugins.settings.bean.RegCenter;
import com.efp.plugins.settings.bean.RegCenterTableModel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RegCenterAddPanel extends DialogWrapper {

    private JTextField ip;
    private JSpinner port;
    private JComboBox<String> type;
    private JPanel jpanel;

    /**
     * 注册中心数据
     */
    private JTable jTable;

    protected RegCenterAddPanel(boolean canBeParent, JTable jTable) {
        super(canBeParent);
        this.jTable = jTable;
        setTitle("增加注册中心");
        setOKButtonText("确定");
        setCancelButtonText("取消");
        type.addItem(RegCenterTypeEnum.ZOOKEEPER.getValue());
        type.addItem(RegCenterTypeEnum.EUREKA.getValue());
        type.addItem(RegCenterTypeEnum.NACOS.getValue());
        init();
    }

    @Override
    protected void doOKAction() {
        try {
            //校验数据
            if (StringUtils.isEmpty(ip.getText())) {
                throw new Exception("ip或者端口不能为空");
            }
            if (!RuleUtils.isIp(ip.getText())) {
                throw new Exception("ip格式不正确");
            }
            //获取数据
            RegCenterTableModel model = (RegCenterTableModel) jTable.getModel();
            model.getRegCenters().add(new RegCenter(ip.getText().trim(), port.getValue() + "", RegCenterTypeEnum.getEnumBtValue((String) type.getSelectedItem())));
            model.fireTableDataChanged();
            close(200, true);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "错误信息");
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return jpanel;
    }

}
