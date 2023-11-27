package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RunEnvironment;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.SqlTargetState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.TableDefinition;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IDatabaseConnection;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IValidateSqlTarget;

public abstract class RapTATDatabaseConnection extends MSSQLDatabaseConnection
    implements IValidateSqlTarget, IDatabaseConnection {

  protected static final RunEnvironment RunEnvironmentVal = RunEnvironment.Development;
  private AuditAdapter auditAdapter;

  /**
   * Instantiates a new rap TAT database connection.
   *
   * @param config the configuration for this connection
   * @param auditAdapter
   * @param logger the logger
   */
  protected RapTATDatabaseConnection(IDataSourceConfiguration config, AuditAdapter auditAdapter,
      Logger logger) {
    super(config, logger);
    this.auditAdapter = auditAdapter;
  }


  /**
   * Check to make sure that the specified schema exists
   *
   * @return
   * @throws DataSourceException
   */
  public boolean checkToMakeSureDesignatedSchemaExists() throws DataSourceException {

    boolean retVal = false;

    TableDefinition processingQueue = getConfig().getProcessingQueueTableDefinition();

    if (checkToMakeSureTableExists(processingQueue)) {

      TableDefinition auditLog = getConfig().getAuditLogDefinition();

      if (checkToMakeSureTableExists(auditLog)) {
        retVal = true;
      }

    }

    return retVal;
  }


  /*
   * (non-Javadoc)
   *
   * @see gov.va.med.v09.vhatvh.raptatcommon.interfaces.IValidateSqlTarget# isSqlTargetValid()
   */
  @Override
  public SqlTargetState isSqlTargetValid() throws DataSourceException {

    try {
      connect();
    } catch (DataSourceException dse) {
      return SqlTargetState.CannotConnectToDatabase;
    }

    boolean doesExpectedSchemaExist = checkToMakeSureDesignatedSchemaExists();
    if (doesExpectedSchemaExist == false) {
      return SqlTargetState.ExpectedSchemaDoesNotExist;
    }

    return SqlTargetState.Valid;
  }


  /**
   * Gets the audit adapter.
   *
   * @return the audit adapter
   */
  protected AuditAdapter getAuditAdapter() {
    return this.auditAdapter;
  }


  /**
   * Query result using the given query string
   *
   * @param sql the select sql
   * @return the {@link java.sql.ResultSet} if found, null otherwise
   */
  protected ResultSet queryResult(String sql) {

    Logger logger = getLogger();

    ResultSet result = null;
    Connection connect = null;
    PreparedStatement statement = null;

    /*
     * Open the connection
     */
    try {
      connect = connect();
    } catch (DataSourceException e) {
      logger.error(e.getMessage());
      return null;
    }

    /*
     * Create a statement
     */
    try {
      statement = connect.prepareStatement(sql);
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    try {
      boolean execute = statement.execute();
      if (execute) {
        result = statement.getResultSet();
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      logger = null;
    }

    return result;
  }

}
