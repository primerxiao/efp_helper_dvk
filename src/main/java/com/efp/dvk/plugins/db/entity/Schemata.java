package com.efp.dvk.plugins.db.entity;

import lombok.Data;

import java.util.List;

@Data
public class Schemata {
  private String catalogName;
  private String schemaName;
  private String defaultCharacterSetName;
  private String defaultCollationName;
  private String sqlPath;
  private List<Tables> tablesList;
}
