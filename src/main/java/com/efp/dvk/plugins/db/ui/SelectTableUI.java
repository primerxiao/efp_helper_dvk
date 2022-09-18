package com.efp.dvk.plugins.db.ui;

import com.efp.dvk.common.lang.annation.ConfField;
import com.efp.dvk.common.service.AbstractDialogService;
import com.efp.dvk.plugins.db.entity.Tables;
import com.efp.dvk.plugins.db.model.DbConnectParam;
import com.efp.dvk.plugins.db.model.DbRunEvent;
import com.efp.dvk.plugins.db.model.SelectTableModel;
import com.efp.dvk.plugins.db.service.DBRunnable;
import com.efp.dvk.plugins.db.service.DbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SelectTableUI extends DialogWrapper implements AbstractDialogService {


    private final DbConnectParam dbConnectParam;

    private final Project project;

    private final DBRunnable dbRunnable;

    private JPanel jPanel;

    @ConfField
    private JTextField schema;

    @ConfField
    private JTextField table;

    private JButton searchButton;

    private JTable dataTable;

    private JScrollPane jScrollPane;

    public SelectTableUI(@Nullable Project project,
                         boolean canBeParent,
                         DbConnectParam dbConnectParam,
                         DBRunnable dbRunnable) {
        super(project, canBeParent);
        this.project = project;
        this.dbConnectParam = dbConnectParam;
        this.dbRunnable = dbRunnable;
        searchButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assert project != null;
                DbService dbService = project.getService(DbService.class);
                List<Tables> tables = dbService.fetchTablses(dbConnectParam);
                if (StringUtils.isNotEmpty(schema.getText())) {
                    tables = tables.stream().filter(s -> schema.getText().equalsIgnoreCase(s.getTableSchema())).toList();
                }
                if (StringUtils.isNotEmpty(table.getText())) {
                    tables = tables.stream().filter(t -> table.getText().equalsIgnoreCase(t.getTableName())).toList();
                }
                //展示数据
                SelectTableModel model = (SelectTableModel) dataTable.getModel();
                model.getData().clear();
                model.getData().addAll(tables);
                model.fireTableDataChanged();
                dataTable.updateUI();
            }

        });
        searchButton.setText("search");
        SelectTableModel selectTableModel = new SelectTableModel();
        dataTable.setModel(selectTableModel);
        init();
    }

    @Override
    protected void doOKAction() {
        int selectedRow = dataTable.getSelectedRow();
        SelectTableModel selectTableModel = (SelectTableModel) dataTable.getModel();
        Tables tables = selectTableModel.getData().get(selectedRow);
        dbRunnable.run(
                DbRunEvent.builder()
                        .selectTables(tables)
                        .project(project)
                        .dbConnectParam(dbConnectParam)
                        .build()
        );

    }

    @Override
    public @Nullable JComponent createCenterPanel() {
        return jPanel;
    }

}
