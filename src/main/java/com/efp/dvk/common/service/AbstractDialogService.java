package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.NotifyUtils;
import com.efp.dvk.common.lang.annation.ConfField;
import com.efp.dvk.common.orm.entity.PluginDialogConf;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * dialog通用处理能力
 */
public interface AbstractDialogService {
    default void saveConf() {
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            List<Field> fields = Arrays.stream(declaredFields).filter(f -> f.getAnnotation(ConfField.class) != null).toList();
            if (fields.isEmpty()) {
                return;
            }
            for (Field field : fields) {
                String confKey = this.getClass().getName() + "--" + field.getName();
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    field.setAccessible(false);
                    continue;
                }
                if (obj instanceof JComboBox) {
                    //class+field
                    PluginOrmService.instance().insert(
                            PluginDialogConf.builder()
                                    .confKey(confKey)
                                    .confValue(String.valueOf(((JComboBox<?>) obj).getSelectedIndex()))
                                    .build()
                    );
                }
                if (obj instanceof JTextField) {
                    String text = ((JTextField) obj).getText();
                    PluginOrmService.instance().insert(
                            PluginDialogConf.builder()
                                    .confKey(confKey)
                                    .confValue(text)
                                    .build()
                    );
                }
                if (obj instanceof JPasswordField) {
                    String text = String.valueOf(((JPasswordField) obj).getPassword());
                    PluginOrmService.instance().insert(
                            PluginDialogConf.builder()
                                    .confKey(confKey)
                                    .confValue(text)
                                    .build()
                    );
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            NotifyUtils.error(null, e.getMessage());
        }
    }

    default void loadConf() {
        try {
            Field[] declaredFields = this.getClass().getDeclaredFields();
            List<Field> fields = Arrays.stream(declaredFields).filter(f -> f.getAnnotation(ConfField.class) != null).toList();
            if (fields.isEmpty()) {
                return;
            }
            for (Field field : fields) {
                String confKey = this.getClass().getName() + "--" + field.getName();

                PluginDialogConf pluginDialogConf = PluginOrmService.instance().selectById(
                        PluginDialogConf.builder().confKey(confKey).build()
                );
                if (pluginDialogConf == null) {
                    continue;
                }
                String confValue = pluginDialogConf.getConfValue();
                if (confValue == null) {
                    continue;
                }
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    field.setAccessible(false);
                    continue;
                }
                if (obj instanceof JComboBox) {
                    //class+field
                    ((JComboBox<?>) obj).setSelectedIndex(Integer.parseInt(confValue));
                }
                if (obj instanceof JTextField) {
                    ((JTextField) obj).setText(confValue);
                }
                if (obj instanceof JPasswordField) {
                    ((JPasswordField) obj).setText(confValue);
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            NotifyUtils.error(null, e.getMessage());
        }
    }
}
