package com.efp.dvk.plugins.db.entity;

import lombok.Data;

@Data
public class Columns {

  private String tableCatalog;
  private String tableSchema;
  private String tableName;
  private String columnName;
  private Long ordinalPosition;
  private String columnDefault;
  private String isNullable;
  private String dataType;
  private Long characterMaximumLength;
  private Long characterOctetLength;
  private Long numericPrecision;
  private Long numericScale;
  private Long datetimePrecision;
  private String characterSetName;
  private String collationName;
  private String columnType;
  private String columnKey;
  private String extra;
  private String privileges;
  private String columnComment;
  private String generationExpression;
  private Long srsId;

}
