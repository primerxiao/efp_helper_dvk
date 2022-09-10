package com.efp.dvk.common.util;

import com.efp.dvk.common.annation.ExcelField;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelUtils {

    public static <T> void export(String fileName, List<T> datas, String sheetName) {
        try {
            if (datas.isEmpty()) {
                NotifyUtils.error(null, "导出的数据为空");
                return;
            }
            Class<?> aClass = datas.get(0).getClass();
            List<Field> sortFields = filterAndSort(aClass);

            LinkedHashMap<Field, String> linkedHashMap = new LinkedHashMap<>(Math.toIntExact(sortFields.size()), 0.75F, false);
            for (Field declaredField : sortFields) {
                ExcelField excelField = declaredField.getAnnotation(ExcelField.class);
                String header = excelField.header();
                linkedHashMap.put(declaredField, header);
            }
            //先处理表头
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet sheet1 = workbook.createSheet(StringUtils.isEmpty(sheetName) ? "sheet1" : sheetName, 0);
            AtomicInteger index = new AtomicInteger(0);
            linkedHashMap.forEach((k, v) -> {
                try {
                    sheet1.addCell(new Label(index.get(), 0, v));
                    index.getAndIncrement();
                } catch (WriteException e) {
                    throw new RuntimeException(e);
                }
            });
            AtomicInteger rowIdx = new AtomicInteger(1);
            AtomicInteger colIdx = new AtomicInteger(0);
            for (T t : datas) {
                linkedHashMap.forEach((k, v) -> {
                    try {
                        k.setAccessible(true);
                        String value = String.valueOf(k.get(t));
                        sheet1.addCell(new Label(colIdx.get(), rowIdx.get(), value));
                        colIdx.getAndIncrement();
                    } catch (IllegalAccessException | WriteException e) {
                        throw new RuntimeException(e);
                    } finally {
                        k.setAccessible(false);
                    }
                });
                colIdx.set(0);
                rowIdx.getAndIncrement();
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            NotifyUtils.error(null, e.getMessage());
        }
    }

    private static List<Field> filterAndSort(Class<?> aClass) {
        Field[] declaredFields = aClass.getDeclaredFields();
        List<Field> fields = Arrays.stream(declaredFields).filter(f -> !Objects.isNull(f.getAnnotation(ExcelField.class))).toList();
        if (fields.isEmpty()) {
            throw new RuntimeException("缺失相应的注解");
        }
        return fields.stream().sorted((o1, o2) -> {
            ExcelField excelField1 = o1.getAnnotation(ExcelField.class);
            ExcelField excelField2 = o2.getAnnotation(ExcelField.class);
            return excelField1.order() - excelField2.order();
        }).toList();
    }
}
