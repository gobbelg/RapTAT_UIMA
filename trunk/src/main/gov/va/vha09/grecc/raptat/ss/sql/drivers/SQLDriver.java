/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.DbConnectionObject;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Class SQLDriver.
 *
 * @author VHATVHSAHAS1 Created on March 25, 2015
 *
 *         This class handles the DB operations related to SQLServer
 */
public class SQLDriver implements AutoCloseable, ISQLDriver {

  public enum ConnectionResult {
    Success, Failed
  }

  /** The connection. */
  Connection __connection;

  /** The database. */
  String __database;

  /** The password. */
  String __password;

  /** The server. */
  String __server;

  /** The username. */
  String __username;

  public SQLDriver(DbConnectionObject databaseConnectionInformation) throws SQLDriverException {
    this(databaseConnectionInformation.get_server(), databaseConnectionInformation.get_database(),
        databaseConnectionInformation.get_username(), databaseConnectionInformation.get_password());
  }


  /**
   * Instantiates a new SQL driver.
   *
   * @param serverName the server name
   * @param databaseName the database name
   * @param username the username
   * @param password the password
   * @throws SQLDriverException
   * @throws ClassNotFoundException the class not found exception
   */
  public SQLDriver(String serverName, String databaseName, String username, String password)
      throws SQLDriverException {
    this();
    this.__server = serverName;
    this.__database = databaseName;
    this.__username = username;
    this.__password = password;
  }


  /**
   * Instantiates a new SQL driver.
   *
   * @throws SQLDriverException
   *
   * @throws ClassNotFoundException the class not found exception
   */
  private SQLDriver() throws SQLDriverException {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } catch (ClassNotFoundException e) {
      throw new SQLDriverException(e);
    }
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.AutoCloseable#close()
   */
  @Override
  public void close() throws Exception {
    closeConnection();
  }


  /**
   * Closes the connection to the DB.
   */
  public void closeConnection() {
    try {
      if (this.__connection != null) {
        this.__connection.close();
        this.__connection = null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * Creates a connection using SQL server authentication.
   *
   * @return connection to the DB "AKI_NLP" or null
   */
  public Connection connect() {

    if (this.__connection == null) {
      this.__connection = create_connection();
    }

    return this.__connection;
  }


  /**
   * Creates the database.
   *
   * @return true, if successful
   * @throws NoConnectionException the no connection exception
   */
  public boolean createDatabase() throws NoConnectionException {

    String query =
        String.format("SELECT 1 FROM sys.sysdatabases WHERE name = '%s'", this.__database);

    boolean success = false;

    try (ResultSet rs = executeSelect(query)) {
      if (!rs.next()) { // nothing is returned .. that is DB does not exist
        Statement stmt = connect().createStatement();
        stmt.execute("create database " + this.__database);
        success = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return success;
  }


  /**
   * Creates the tables from query file.
   *
   * @param filePath the file path
   * @param schemaName the schema name
   * @throws NoConnectionException the no connection exception
   * @throws SQLException the SQL exception
   */
  public void createTablesFromQueryFile(String filePath, String schemaName)
      throws NoConnectionException, SQLException {

    if (tablesExists(schemaName)) {
      return;
    }

    StringBuilder query = new StringBuilder();
    String line;

    try (FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader)) {

      while ((line = br.readLine()) != null) {
        query.append(line).append("\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    String queryString = query.toString();
    // what if column name has dbo in it?
    queryString = queryString.replaceAll("dbo", schemaName);

    try (Statement stmt = connect().createStatement()) {
      stmt.execute(queryString);
    }
  }


  /**
   * Drop tables.
   *
   * @param filePath the file path
   * @param schemaName the schema name
   * @throws NoConnectionException the no connection exception
   */
  public void dropTables(String filePath, String schemaName) throws NoConnectionException {
    StringBuilder query = new StringBuilder();
    String line;

    try (FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader)) {

      while ((line = br.readLine()) != null) {
        query.append(line).append("\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    String queryString = query.toString();
    queryString = queryString.replaceAll("dbo", schemaName);

    try (Statement stmt = connect().createStatement()) {
      stmt.execute(queryString);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Execute insert.
   *
   * @param cols the cols
   * @param dataTable the data table
   * @param schema the schema
   * @param table the table
   * @throws NoConnectionException the no connection exception
   */
  @Override
  public void executeInsert(String cols, List<String> dataTable, String schema, String table)
      throws NoConnectionException {

    String query = String.format("insert into [%s].[%s] (%s) values (%s)", schema, table, cols,
        String.join(", ", dataTable));

    try (Statement stmt = connect().createStatement()) {
      stmt.execute(query.toString());
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  /**
   * Execute query file.
   *
   * @param filePath the file path
   * @param dbName the db name
   * @throws NoConnectionException the no connection exception
   */
  public void executeQueryFile(String filePath, String dbName) throws NoConnectionException {
    StringBuilder query = new StringBuilder();
    String line;

    try (FileReader fileReader = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fileReader)) {
      while ((line = br.readLine()) != null) {
        query.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (Statement stmt = connect().createStatement()) {
      stmt.execute(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Executes the select queries and return the result set.
   *
   * @param query the query
   * @return the result set
   * @throws NoConnectionException the no connection exception
   */
  @Override
  public ResultSet executeSelect(String query) throws NoConnectionException {
    ResultSet rs = null;
    try {
      Statement stmt = connect().createStatement();
      rs = stmt.executeQuery(query);
    } catch (SQLException ex) {
      log_exception(ex);
    }
    return rs;
  }


  @Override
  public Connection getConnection() {
    return this.__connection;
  }


  /**
   * Gets the last row attribute ID.
   *
   * @param dbSchemaName the schema name
   * @param table the table
   * @return the last row attribute ID
   * @throws NoConnectionException the no connection exception
   */
  @Override
  public String getLastRowAttributeID(String dbSchemaName, String table)
      throws NoConnectionException {

    String query = String.format("SELECT IDENT_CURRENT('[%s].[%s]') as id", dbSchemaName, table);
    String id = "";

    try (Statement stmt = connect().createStatement(); ResultSet rs = stmt.executeQuery(query);) {
      while (rs.next()) {
        id = rs.getString("id");
      }
    } catch (SQLException ex) {
      log_exception(ex);
    }
    return id;
  }


  /**
   * Gets the last row ID.
   *
   * @param dbSchemaName the schema name
   * @param table the table
   * @return the last row ID
   * @throws NoConnectionException the no connection exception
   */
  @Override
  public long getLastRowID(String dbSchemaName, String table) throws NoConnectionException {

    String query = String.format("SELECT IDENT_CURRENT('[%s].[%s]') as id", dbSchemaName, table);
    long id = 0L;

    try (Statement stmt = connect().createStatement(); ResultSet rs = stmt.executeQuery(query);) {
      while (rs.next()) {
        id = rs.getLong("id");
      }
    } catch (SQLException ex) {
      log_exception(ex);
    }
    return id;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver#
   * prepareStatement(java.lang.String)
   */
  @Override
  public PreparedStatement prepareStatement(String query) throws SQLException {

    PreparedStatement prepareStatement = connect().prepareStatement(query);

    return prepareStatement;
  }


  /**
   * Tables exists.
   *
   * @param schema the schema
   * @return true, if successful
   * @throws NoConnectionException the no connection exception
   */
  public boolean tablesExists(String schema) throws NoConnectionException {

    String queryCheckTableExists = String.format(
        "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG='%s' AND TABLE_SCHEMA='%s' AND TABLE_NAME='schemas'; ",
        this.__database, schema);

    boolean rv = false;

    try (ResultSet rs = executeSelect(queryCheckTableExists)) {
      if (rs.next()) {
        rv = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return rv;
  }


  /**
   * Test connection.
   *
   * @return true, if successful
   */
  public ConnectionResult test_connection() {

    ConnectionResult rv = ConnectionResult.Failed;

    try (Connection connection = create_connection()) {
      rv = connection != null ? ConnectionResult.Success : ConnectionResult.Failed;
    } catch (Exception e) {
      rv = ConnectionResult.Failed;
    }

    return rv;
  }


  private String build_connection_string() {
    StringBuilder connectionUrlBuilder =
        new StringBuilder(String.format("jdbc:sqlserver://%s;", this.__server));
    if (this.__database != null) {
      connectionUrlBuilder.append(String.format("databaseName=%s;", this.__database));
    }

    String connectionUrl = connectionUrlBuilder.toString();
    return connectionUrl;
  }


  /**
   * Creates the connection.
   *
   * @return the connection
   */
  private Connection create_connection() {
    String connectionUrl = build_connection_string();
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(connectionUrl, this.__username, this.__password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }


  public static void closeConnection(Connection connect) {
    if (connect != null) {
      try {
        connect.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }


  /**
   * Log exception.
   *
   * @param ex the ex
   */
  private static void log_exception(SQLException ex) {
    Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
  }

}

// @Deprecated
// public static void connectTest() {
// String dbURL =
// "jdbc:sqlserver://localhost\\sqlexpress;user=Raptat_User;password=raptat_USER";
// Connection conn = null;
// try {
// conn = DriverManager.getConnection(dbURL);
// } catch (SQLException e) {
// e.printStackTrace();
// }
// if (conn != null) {
// System.out.println("Connected");
// }
// }

// /* Deprecated 09/04/18 */
// @Deprecated
// public SQLDriver(String user, String pass) {
// this.username = user;
// this.password = pass;
// }

// TODO - Shuvro... Needs to implement update
// public static void executeUpdate(String cols, List<List<String>>
// dataTable, String table)
// throws NoConnectionException
// {
// // StringBuilder queryInsert = new StringBuilder("insert into (");
// queryInsert.append(cols).append(") values (");
// StringBuilder query;
//
// Connection connection = connect();
// Statement stmt;
//
// if(connection == null){
// throw new NoConnectionException("connection problem");
// }
//
// for(List<String> lines: dataTable){
// query = queryInsert.append(lines.get(0));
//
// for(String data: lines){
// query.append(",").append(data);
// }
//
// query.append(");");
//
// try {
// stmt = connection.createStatement();
// stmt.execute
// } catch (SQLException ex) {
// Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null,
// ex);
// }
// }
//
// closeConnection(connection);
// }

/* Deprecated 09/04/18 */
// @Deprecated
// public void setServerAndDB(String serverName, String db) {
// this.server = serverName;
// this.database = db;
// }
