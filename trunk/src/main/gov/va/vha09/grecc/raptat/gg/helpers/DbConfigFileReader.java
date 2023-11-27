package src.main.gov.va.vha09.grecc.raptat.gg.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Properties;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.AnnotationImportExportConfiguration;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.DbConnectionObject;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

public class DbConfigFileReader {

  private String username = "";
  private String password = "";
  private String server = "";
  private String dbName = "";
  private String schemaName = "";
  private String query = "";
  private String textColumn = "";
  private String documentUniqueIdColumn = "";
  private String table = "";

  public DbConnectionObject get_db_connection_object() {
    DbConnectionObject rv =
        new DbConnectionObject(getServer(), getDbName(), getUsername(), getPassword());
    return rv;
  }


  public String getDbName() {
    return this.dbName;
  }


  public String getdocumentUniqueIdColumn() {
    return this.documentUniqueIdColumn;
  }


  public String getPassword() {
    return this.password;
  }


  public String getQuery() {
    return this.query;
  }


  public String getSchemaName() {
    return this.schemaName;
  }


  public String getServer() {
    return this.server;
  }


  public String getTable() {
    return this.table;
  }


  public String getTextColumn() {
    return this.textColumn;
  }


  public String getUsername() {
    return this.username;
  }


  public ResultSet readTextFileFromDb()
      throws FileNotFoundException, IOException, NoConnectionException, SQLDriverException {
    readDbConfigFile();
    ResultSet result = connectToDbAndGetData();
    return null;
  }


  private ResultSet connectToDbAndGetData() throws NoConnectionException, SQLDriverException {
    SQLDriver sqlDriver = new SQLDriver(this.server, this.dbName, this.username, this.password);
    return sqlDriver.executeSelect(this.query);
  }


  private void readDbConfigFile() throws FileNotFoundException, IOException, NoConnectionException {
    String dbonfigFilePath = AnnotationImportExportConfiguration.getDbConfigFilePath();
    Properties prop = readPropertiesFile(dbonfigFilePath);

    this.username = prop.getProperty(Constants.PROPERTY_USERNAME);
    this.password = prop.getProperty(Constants.PROPERTY_PASSWORD);
    this.server = prop.getProperty(Constants.PROPERTY_SERVER);
    this.dbName = prop.getProperty(Constants.PROPERTY_DB_NAME);
    this.schemaName = prop.getProperty(Constants.PROPERTY_SCHEMA_NAME);
    this.query = prop.getProperty(Constants.PROPERTY_QUERY);
    this.textColumn = prop.getProperty(Constants.PROPERTY_TEXT_COLUMN);
    this.documentUniqueIdColumn = prop.getProperty(Constants.POPERTY_UNIQUE_ID_COLUMN);
    this.table = prop.getProperty(Constants.PROPERTY_TABLE);

  }


  private Properties readPropertiesFile(String filepath) throws FileNotFoundException, IOException {
    Properties prop = new Properties();
    InputStream in = new FileInputStream(filepath);
    prop.load(in);
    in.close();
    return prop;
  }

}
