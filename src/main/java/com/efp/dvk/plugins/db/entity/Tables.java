package com.efp.dvk.plugins.db.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class Tables {

  private String tableCatalog;
  private String tableSchema;
  private String tableName;
  private String tableType;
  private String engine;
  private Long version;
  private String rowFormat;
  private BigInteger tableRows;
  private BigInteger avgRowLength;
  private BigInteger dataLength;
  private BigInteger maxDataLength;
  private BigInteger indexLength;
  private BigInteger dataFree;
  private BigInteger autoIncrement;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private java.sql.Timestamp checkTime;
  private String tableCollation;
  private BigInteger checksum;
  private String createOptions;
  private String tableComment;
  private List<Columns> columnsList;

}
