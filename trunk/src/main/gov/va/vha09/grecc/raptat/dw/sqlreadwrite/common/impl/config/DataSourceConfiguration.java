package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Properties;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.TableDefinition;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.ConfigurationException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;

/**
 * Provides access to configuration information for connection to a MSSQL database.
 */
public class DataSourceConfiguration implements IDataSourceConfiguration {

  /** The properties. */
  private Properties properties;

  /**
   * Instantiates a new data source configuration with a configuration file.
   *
   * @param configurationPropertyFile the configuration property file
   * @throws ConfigurationException the configuration exception
   */
  public DataSourceConfiguration(File configurationPropertyFile) throws ConfigurationException {
    this(loadPropertiesFromDefaultLocation(configurationPropertyFile));
  }


  /**
   * Instantiates a new data source configuration from properties passed into the constructor.
   *
   * @param properties the properties
   */
  public DataSourceConfiguration(Properties properties) {
    this.properties = properties;
  }


  /**
   * Instantiates a new data source configuration with a configuration file path as string relative
   * to library path.
   *
   * @param configurationPropertyFileName the configuration property file name
   * @throws ConfigurationException the configuration exception
   */
  public DataSourceConfiguration(String configurationPropertyFileName)
      throws ConfigurationException {
    this(Paths.get(Thread.currentThread().getContextClassLoader().getResource(".").getFile(),
        configurationPropertyFileName).toFile());
  }


  /**
   * Instantiates a new data source configuration with a configuration URL as string relative to
   * library path.
   *
   * @param resource the resource
   * @throws ConfigurationException the configuration exception
   */
  public DataSourceConfiguration(URL resource) throws ConfigurationException {
    this(new File(resource.getFile()));
  }



  @Override
  public String getApplicationApp() {
    return "eHOST";
  }


  /**
   * Gets the audit log definition.
   *
   * @return the audit log definition
   */
  @Override
  public TableDefinition getAuditLogDefinition() {
    return getTableDefinitionDetails("auditLog");
  }


  @Override
  public String getAuditUpdateQuery(AuditLogId auditLogId) {
    if (auditLogId.isValid()) {
      return properties.getProperty("updateAuditEntry");
    } else {
      return properties.getProperty("insertAuditEntry");
    }
  }


  @Override
  public boolean getCorrectOffsets() {
    String correctOffsetsInExportStr = properties.getProperty("correctOffsetsInExport", "false");
    boolean property = Boolean.parseBoolean(correctOffsetsInExportStr);
    return property;
  }


  /**
   * Gets the database name.
   *
   * @return the database
   */
  @Override
  public String getDatabase() {
    return properties.getProperty("database");
  }


  /**
   * Gets the db driver string.
   *
   * @return the db driver string
   */
  @Override
  public String getDbDriverString() {
    String property = properties.getProperty("dbDriverString");
    return property;
  }


  @Override
  public String getDocumentCountQuery() {
    String property = properties.getProperty("documentCountQuery");
    return property;
  }


  @Override
  public String getSqlExportSchemaName() {
    String property = properties.getProperty("exportSchema", "raptat");
    return property;
  }


  @Override
  public String getIncludedConceptsFilePath() {
    return properties.getProperty("includedConceptsFilePath");
  }


  @Override
  public boolean getInsertPhraseString() {
    String correctOffsetsInExportStr =
        properties.getProperty("insertPhraseStringInExport", "false");
    boolean property = Boolean.parseBoolean(correctOffsetsInExportStr);
    return property;
  }


  @Override
  public String getNextDocumentQuery() {
    String property = properties.getProperty("nextDocumentQuery");
    return property;
  }


  /**
   * Gets the password.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return properties.getProperty("password");
  }


  /**
   * Gets the port open for TCP/IP communication to the database server.
   *
   * @return the port
   */
  @Override
  public String getPort() {
    return properties.getProperty("port");
  }


  /**
   * Gets the processing queue table definition.
   *
   * @return the processing queue table definition
   */
  @Override
  public TableDefinition getProcessingQueueTableDefinition() {
    return getTableDefinitionDetails("processingQueue");
  }


  @Override
  public String getAnnotationSchemaFilePath() {
    String property = properties.getProperty("schemaFilePath");
    return property;
  }


  /**
   * Gets the database server.
   *
   * @return the server
   */
  @Override
  public String getServer() {
    return properties.getProperty("server");
  }


  @Override
  public String getSolutionFilePath() {
    return properties.getProperty("raptatSolutionPath");
  }


  @Override
  public String getSourceId() {
    // TODO Auto-generated method stub
    // return null;
    return "1";
  }


  /**
   * Gets the user name for the DB.
   *
   * @return the user name
   */
  @Override
  public String getUserName() {
    return properties.getProperty("username");
  }


  /**
   * Checks if is trusted connection.
   *
   * @return true, if is trusted connection
   */
  @Override
  public boolean isTrustedConnection() {
    String isTrustedConnectionString = properties.getProperty("isTrustedConnection");
    /*
     * True = true, and anything else is false
     */
    return Boolean.parseBoolean(isTrustedConnectionString);
  }


  /**
   * Gets the table definition details.
   *
   * @param prefix the prefix
   * @return the table definition details
   */
  private TableDefinition getTableDefinitionDetails(String prefix) {
    String tableName = properties.getProperty(MessageFormat.format("{0}TableName", prefix));
    String schemaName = properties.getProperty(MessageFormat.format("{0}SchemaName", prefix));
    String columnsString = properties.getProperty(MessageFormat.format("{0}Columns", prefix));
    String[] columns = columnsString.split(",\\s*");
    return new TableDefinition(schemaName, tableName, columns);
  }


  /**
   * Load properties from default location.
   *
   * @param configurationPropertyFile the configuration property file
   * @return the properties
   * @throws ConfigurationException the configuration exception
   */
  private static Properties loadPropertiesFromDefaultLocation(File configurationPropertyFile)
      throws ConfigurationException {
    Properties properties = new Properties();

    if (configurationPropertyFile == null || configurationPropertyFile.isFile() == false) {
      throw new ConfigurationException(
          MessageFormat.format("The configuration file {0} does not exist.",
              configurationPropertyFile == null ? "null" : configurationPropertyFile.getName()));
    }

    try {
      properties.load(new FileReader(configurationPropertyFile));
    } catch (IOException e) {
      throw new ConfigurationException(MessageFormat.format("Cannot load configuration file {0}",
          configurationPropertyFile.getName()), e);
    }
    return properties;
  }

}
