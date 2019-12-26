package com.efp.plugins.codeHelper.action;

import com.google.common.base.CaseFormat;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.BasicDataSourceManager;
import com.intellij.database.psi.DataSourceManager;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.util.DasUtil;
import com.intellij.database.util.DbUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CodeHelperGenerateMethodAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        PsiFile data = anActionEvent.getData(LangDataKeys.PSI_FILE);
        VirtualFile virtualFile = anActionEvent.getData(LangDataKeys.VIRTUAL_FILE);
        FileType fileType = data.getFileType();
        Module currentModule = ModuleUtil.findModuleForFile(virtualFile, anActionEvent.getProject());
        //mybatis-xml
        if (fileType.getDefaultExtension().equalsIgnoreCase("xml")) {
            XmlFile xmlFile = (XmlFile) data;
            XmlTag rootTag = xmlFile.getRootTag();
            XmlTag resultMap = rootTag.findFirstSubTag("resultMap");
            XmlAttribute type = resultMap.getAttribute("type");
            String domainQuaName = type.getValue();
            //table名
            String[] split = domainQuaName.split("\\.");
            String s = split[split.length - 1];
            String tableName = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(s);
            List<DataSourceManager<?>> managers = BasicDataSourceManager.getManagers(anActionEvent.getProject());

            //获取所有的表
            JBIterable<DbDataSource> dataSources = DbUtil.getDataSources(anActionEvent.getProject());
            for (DbDataSource dataSource : dataSources) {
                JBIterable<? extends DasTable> tables = DasUtil.getTables(dataSource);
                for (DasTable table : tables) {
                    System.out.println(table.getName());
                    //console_hsd_mannual_appr_record
                    if (tableName.trim().equalsIgnoreCase(table.getName().trim())) {
                        DasNamespace namespace = DasUtil.getNamespace(table);
                        String[] split1 = currentModule.getName().split("\\.");
                        if (namespace.getName().equals(split1[0] + "_" + split1[1])) {
                            //模块对的上
                            //获取表的所有字段
                            JBIterable<? extends DasColumn> columns = DasUtil.getColumns(table);
                            //将所有字段展示出来
                        }
                    }
                }

            }
        }
        if (fileType instanceof JavaFileType) {

        }
        //service serviceimpl
        //domain vo
    }
}
