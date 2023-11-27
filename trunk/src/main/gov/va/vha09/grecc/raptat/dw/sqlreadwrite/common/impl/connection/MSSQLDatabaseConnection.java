package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.google.common.base.Joiner;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.TableDefinition;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;

/**
 * Implementation of {@see IDatabaseConnection} for connection to MSSQL database.
 */
public abstract class MSSQLDatabaseConnection extends DataSourceConnection {

  /** The Constant TIMEOUT_TO_VERIFY_MSSQL_CONNECTION. */
  private static final int TIMEOUT_TO_VERIFY_MSSQL_CONNECTION = 30;

  /** The connection. */
  private Connection connection;

  /**
   * Constructor accepting a configuration defining the MSSQL data source target.
   *
   * @param config configuration for this connection
   * @param auditAdapter
   * @param logger
   */
  protected MSSQLDatabaseConnection(IDataSourceConfiguration config, Logger logger) {
    super(config, logger);
  }


  /**
   * Connect to MSSQL database using the config information provided.
   *
   * @return
   *
   * @throws DataSourceException the data source exception
   */
  @Override
  public Connection connect() throws DataSourceException {

    if (isConnected() == false) {

      IDataSourceConfiguration config = getConfig();
      try {
        String dbDriverString = config.getDbDriverString();
        Class<?> forName = Class.forName(dbDriverString);
        getLogger().info("JDBC driver loaded");
      } catch (ClassNotFoundException e) {
        throw new DataSourceException("Could not load SQLServerDriver driver");
      }

      try {
        this.connection = DriverManager.getConnection(getDataSourceUrl());
        getLogger().info("Connection created");
      } catch (SQLException e) {
        throw new DataSourceException(MessageFormat.format("Cannot connect to database {0}.{1}",
            config.getServer(), config.getDatabase()));
      }

    }

    return this.connection;

  }


  /*
   * (non-Javadoc)
   *
   * @see gov.va.med.v09.vhatvh.raptatcommon.interfaces.IDatabaseConnection# getDataSourceUrl()
   */
  @Override
  public String getDataSourceUrl() {
    IDataSourceConfiguration dsconfig = getConfig();

    String serverString = dsconfig.getServer();
    String port = dsconfig.getPort();
    String databaseString = dsconfig.getDatabase();

    StringBuilder urlBuilder = new StringBuilder("jdbc:sqlserver://");
    urlBuilder.append(serverString);
    urlBuilder.append(":");
    urlBuilder.append(port == null ? "1433" : port);
    urlBuilder.append(";databaseName=");
    urlBuilder.append(databaseString);

    if (dsconfig.isTrustedConnection()) {
      /*
       * Implement the connection under the identity of the current user
       */
      urlBuilder.append(";integratedSecurity=true;");

    } else {
      /*
       * Implement the connection using the user name and password provided
       */
      String userName = dsconfig.getUserName();
      String password = dsconfig.getPassword();

      urlBuilder.append(";user=");
      urlBuilder.append(userName);
      urlBuilder.append(";password=");
      urlBuilder.append(password);
    }

    String dataSourceUrl = urlBuilder.toString();
    return dataSourceUrl;
  }


  /*
   * (non-Javadoc)
   *
   * @see gov.va.med.v09.vhatvh.raptatcommon.interfaces.IDatabaseConnection# isConnected ()
   */
  @Override
  public Boolean isConnected() {
    try {
      return this.connection != null && this.connection.isValid(TIMEOUT_TO_VERIFY_MSSQL_CONNECTION);
    } catch (SQLException e) {
      /*
       * This should not happen, as the timeout should be forced to be positive and greater than 0
       */
      return false;
    }

  }


  /**
   * Make the SQL query to SELECT rows
   *
   * @param processingQueue
   * @return
   */
  private String createQueryOverTable(TableDefinition processingQueue) {
    return createQueryOverTable(processingQueue, 0);
  }


  /**
   * Make the SQL query to SELECT on {@link numberToSelect} rows
   *
   * @param processingQueue
   * @return
   */
  private String createQueryOverTable(TableDefinition processingQueue, int numberToSelect) {

    StringBuilder sb = new StringBuilder();
    sb.append("SELECT ");
    if (numberToSelect > 0) {
      sb.append(" TOP ");
      sb.append(numberToSelect);
    }

    sb.append(" ");

    String[] columnNames = processingQueue.getColumnNames();

    Iterator<String> columnsMappedToSQL =
        Arrays.asList(columnNames).stream().map(x -> "[" + x + "]").iterator();
    String columns = Joiner.on(", ").join(columnsMappedToSQL);

    sb.append(columns);
    sb.append(" FROM ");
    sb.append("[");
    sb.append(processingQueue.getSchemaName());
    sb.append("].[");
    sb.append(processingQueue.getTableName());
    sb.append("] with (nolock)");

    return sb.toString();
  }


  /**
   *
   *
   * @param processingQueue
   * @return
   * @throws DataSourceException
   */
  protected boolean checkToMakeSureTableExists(TableDefinition processingQueue)
      throws DataSourceException {

    Statement stmt = createStatement();

    String query = this.createQueryOverTable(processingQueue);

    ResultSet resultSet;
    try {
      resultSet = stmt.executeQuery(query);
    } catch (SQLException e) {
      throw new DataSourceException("Cannot execute query", e);
    }

    SQLWarning warnings;
    try {
      warnings = resultSet.getWarnings();
    } catch (SQLException e) {
      throw new DataSourceException("Cannot query to check warnings");
    }

    return warnings == null;
  }


  /**
   * Create the statement to run against the MSSQL DB
   *
   * @return
   * @throws DataSourceException
   */
  protected Statement createStatement() throws DataSourceException {
    try {
      return this.connection.createStatement();
    } catch (SQLException e) {
      throw new DataSourceException("Cannot create statement", e);
    }
  }

}
