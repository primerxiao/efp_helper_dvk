package com.efp.dvk.common.service;

import com.efp.dvk.common.annation.ConfField;
import com.efp.dvk.common.util.NotifyUtils;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public interface DialogAbstractService {
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
                    PropertiesComponent.getInstance().setValue(confKey,
                            String.valueOf(((JComboBox<?>) obj).getSelectedIndex()));
                }
                if (obj instanceof JTextField) {
                    String text = ((JTextField) obj).getText();
                    PropertiesComponent.getInstance().setValue(confKey, text);
                }
                if (obj instanceof JPasswordField) {
                    String text = String.valueOf(((JPasswordField) obj).getPassword());
                    PropertiesComponent.getInstance().setValue(confKey, text);
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
                String confValue = PropertiesComponent.getInstance().getValue(confKey);
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    field.setAccessible(false);
                    continue;
                }
                if (obj instanceof JComboBox) {
                    //class+field
                    if (confValue != null) {
                        ((JComboBox<?>) obj).setSelectedIndex(Integer.parseInt(confValue));
                    }
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
