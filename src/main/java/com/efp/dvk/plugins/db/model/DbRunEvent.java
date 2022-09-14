package com.efp.dvk.plugins.db.model;

import com.efp.dvk.plugins.db.entity.Tables;
import com.intellij.openapi.project.Project;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbRunEvent {
    private Project project;
    private DbConnectParam dbConnectParam;
    private Tables selectTables;
}
