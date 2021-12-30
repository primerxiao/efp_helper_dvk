package com.efp.plugins.general.db.action;

import com.efp.common.util.DasUtils;
import com.efp.common.util.NotifyUtils;
import com.intellij.database.model.*;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.JBIterable;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 根据选择的数据库导出数据字典
 */
public class DBDictExportAction extends AnAction {

    List<String> withoutSchemas = Arrays.asList("information_schema", "mysql", "performance_schema", "sys", "Server Objects");

    List<String> heads = Arrays.asList("表英文名", "表中文名", "字段英文名", "字段中文名", "字段类型", "字段长度", "主键标志", "默认值");

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            FileChooserDescriptor singleFolderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            singleFolderDescriptor.setTitle("请选择存放路径");
            VirtualFile[] virtualFiles = FileChooser.chooseFiles(singleFolderDescriptor, e.getProject(), null);
            if (virtualFiles.length < 1) {
                return;
            }
            String basePath = virtualFiles[0].getPath();
            PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
            if (psiElement instanceof DasDataSource) {
                String dasDbFolder = basePath + ((DasDataSource) psiElement).getName() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                FileUtils.forceMkdir(new File(dasDbFolder));
                //数据源
                JBIterable<? extends DasNamespace> schemas = DasUtil.getSchemas((DasDataSource) psiElement);
                for (DasNamespace schema : schemas) {
                    if (withoutSchemas.contains(schema.getName())) {
                        continue;
                    }
                    JBIterable<? extends DasTable> dasTables = (JBIterable<? extends DasTable>) schema.getDasChildren(ObjectKind.TABLE);
                    if (dasTables.isEmpty()) {
                        continue;
                    }

                    WritableWorkbook workbook = Workbook.createWorkbook(new File(dasDbFolder + "//" + schema.getName() + ".xlsx"));
                    int sheetIndex = 0;
                    for (int i = 0; i < dasTables.size(); i++) {
                        DasTable dasTable = dasTables.get(i);
                        //write to sheet
                        WritableSheet sheet = workbook.createSheet(dasTable.getName(), i);
                        //one table one sheet
                        JBIterable<? extends DasColumn> dasColumns = (JBIterable<? extends DasColumn>) dasTable.getDasChildren(ObjectKind.COLUMN);
                        // write head
                        // 表英文名
                        sheet.addCell(new Label(0, 0, heads.get(0)));
                        // 表中文名
                        sheet.addCell(new Label(1, 0, heads.get(1)));
                        // 字段英文名
                        sheet.addCell(new Label(2, 0, heads.get(2)));
                        // 字段中文名
                        sheet.addCell(new Label(3, 0, heads.get(3)));
                        // 字段类型
                        sheet.addCell(new Label(4, 0, heads.get(4)));
                        // 字段长度
                        sheet.addCell(new Label(5, 0, heads.get(5)));
                        // 主键标志
                        sheet.addCell(new Label(6, 0, heads.get(6)));
                        // 默认值
                        sheet.addCell(new Label(7, 0, heads.get(7)));

                        setColumnWidth(sheet, dasTable, dasColumns);

                        for (int j = 0; j < dasColumns.size(); j++) {
                            DasColumn dasColumn = dasColumns.get(j);
                            // 表英文名
                            sheet.addCell(new Label(0, j + 1, dasTable.getName()));
                            // 表中文名
                            sheet.addCell(new Label(1, j + 1, dasTable.getComment()));
                            // 字段英文名
                            sheet.addCell(new Label(2, j + 1, dasColumn.getName()));
                            // 字段中文名
                            sheet.addCell(new Label(3, j + 1, dasColumn.getComment()));
                            // 字段类型
                            sheet.addCell(new Label(4, j + 1, dasColumn.getDataType().typeName));
                            // 字段长度
                            sheet.addCell(new Label(5, j + 1, dasColumn.getDataType().getLength() + "" + (dasColumn.getDataType().getScale() > 0 ? "," + dasColumn.getDataType().getScale() : "")));
                            // 主键标志
                            sheet.addCell(new Label(6, j + 1, DasUtils.checkPrimaryKey(dasColumn) ? "是" : "否"));
                            // 默认值
                            sheet.addCell(new Label(7, j + 1, dasColumn.getDefault()));
                        }
                        sheetIndex++;
                    }
                    workbook.write();
                    workbook.close();
                    NotifyUtils.notifyInfo("生成数据字典完成");
                }

            } else {
                NotifyUtils.notifyInfo("请选择数据原");
            }
        } catch (IOException | WriteException ioException) {
            ioException.printStackTrace();
            NotifyUtils.notifyError(ioException.getMessage());
        }
    }

    private void setColumnWidth(WritableSheet sheet, DasTable dasTable, JBIterable<? extends DasColumn> dasColumns) {
        // 表英文名
        sheet.setColumnView(0, StringUtils.isNotEmpty(dasTable.getComment()) ? dasTable.getName().length() : 15);
        // 表中文名
        sheet.setColumnView(1, StringUtils.isNotEmpty(dasTable.getComment()) ? dasTable.getComment().length() * 2 : 20);
        // 字段英文名
        sheet.setColumnView(2, dasColumns
                .toList()
                .stream()
                .map(DasColumn::getName)
                .filter(StringUtils::isNotEmpty)
                .mapToInt(String::length)
                .filter(name -> name >= 10)
                .max()
                .orElse(15));
        // 字段中文名
        sheet.setColumnView(3, (dasColumns
                .toList()
                .stream()
                .map(DasColumn::getComment)
                .filter(StringUtils::isNotEmpty)
                .mapToInt(String::length)
                .filter(name -> name >= 10)
                .max()
                .orElse(10)) * 2);
        // 字段类型
        sheet.setColumnView(4, 10);
        // 字段长度
        sheet.setColumnView(5, 10);
        // 主键标志
        sheet.setColumnView(6, 10);
        // 默认值
        sheet.setColumnView(7, 10);

    }
}
