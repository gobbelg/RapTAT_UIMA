package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

/**
 * The Class TableDefinition.
 */
public class TableDefinition {

  /** The table name. */
  private String tableName;

  /** The column names. */
  private String[] columnNames;

  /** The name of schema under which this table falls */
  private String schemaName;

  /**
   * Instantiates a new table definition.
   *
   * @param tableName the table name
   * @param columnNames the column names
   */
  public TableDefinition(String schemaName, String tableName, String... columnNames) {
    this.tableName = tableName;
    this.columnNames = columnNames;
  }


  /**
   * Gets the column names.
   *
   * @return the column names
   */
  public String[] getColumnNames() {
    return this.columnNames;
  }


  /**
   * Gets the schema name for the table.
   *
   * @return
   */
  public String getSchemaName() {
    return this.schemaName;
  }


  /**
   * Gets the table name.
   *
   * @return the table name
   */
  public String getTableName() {
    return this.tableName;
  }

}
