package com.efp.dvk.common.service;

import com.efp.dvk.common.lang.util.StrUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

public interface IPluginOrmService {

    <T> T selectById(T t) ;

    <T> int insert(T t) ;

    <T> int deleteById(T t);

    <T> List<T> selectAll(Class<T> tClass);

    default <T> String getTableNameByClass(Class<T> tClass) {
        String name = tClass.getSimpleName();
        return StrUtils.convertLineToHump(name);
    }

    default <T> String getColumnNameByField(Field field) {
        String name = field.getName();
        return StrUtils.convertLineToHump(name);
    }
}
