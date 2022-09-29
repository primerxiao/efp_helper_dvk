package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.annation.Id;
import com.efp.dvk.common.lang.util.StrUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public interface IPluginOrmService {

    <T> T selectById(T t) ;

    <T> void insert(T t) ;

    <T> void insertOrUpdate(T t);

    <T> void updateById(T t);

    <T> void deleteById(T t);

    <T> List<T> selectAll(Class<T> tClass);

    default <T> String getTableNameByClass(Class<T> tClass) {
        String name = tClass.getSimpleName();
        return StrUtils.convertLineToHump(name);
    }

    default <T> String getColumnNameByField(Field field) {
        String name = field.getName();
        return StrUtils.convertLineToHump(name);
    }

    default <T> String getIdColumnName(Class<T> tClass) {
        Field field = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) != null)
                .findFirst().orElse(null);

        if (field == null) {
            throw new RuntimeException(tClass.getSimpleName() + "have no id");
        }
        return getColumnNameByField(field);
    }

    default <T> Field getIdField(Class<T> tClass) {
        Field field = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) != null)
                .findFirst().orElse(null);

        if (field == null) {
            throw new RuntimeException(tClass.getSimpleName() + "have no id");
        }
        return field;
    }


}
