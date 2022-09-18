package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.annation.ConfField;
import com.efp.dvk.common.lang.enums.CacheNameEnum;
import com.efp.dvk.common.lang.NotifyUtils;

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
                    CacheService.instance().hashMapSet(CacheNameEnum.DialogConfig,
                            confKey,
                            String.valueOf(((JComboBox<?>) obj).getSelectedIndex())
                    );
                }
                if (obj instanceof JTextField) {
                    String text = ((JTextField) obj).getText();
                    CacheService.instance().hashMapSet(CacheNameEnum.DialogConfig,
                            confKey,
                            text);
                }
                if (obj instanceof JPasswordField) {
                    String text = String.valueOf(((JPasswordField) obj).getPassword());
                    CacheService.instance().hashMapSet(CacheNameEnum.DialogConfig,
                            confKey,
                            text);
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

                String confValue = CacheService.instance().hashMapGet(CacheNameEnum.DialogConfig, confKey);
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
