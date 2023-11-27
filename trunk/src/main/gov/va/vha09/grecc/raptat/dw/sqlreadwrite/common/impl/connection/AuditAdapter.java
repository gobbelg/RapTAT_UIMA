package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.ProcessingState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RunEnvironment;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IRapTATAuditLog;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IReaderAuditLog;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IWriterAuditLog;

/**
 * The Class AuditAdapter.
 */
public class AuditAdapter extends RapTATDatabaseConnection
    implements IReaderAuditLog, IWriterAuditLog, IRapTATAuditLog {

  /**
   * The Enum AuditType.
   */
  private enum AuditType {

    /** The Initial. */
    Initial,
    /** The Update. */
    Update
  }

  /**
   * Instantiates a new audit adapter.
   *
   * @param config the configuration for this connection
   * @param logger the logger
   */
  public AuditAdapter(IDataSourceConfiguration config, Logger logger) {
    super(config, null, logger);
  }


  /**
   * Fail.
   *
   * @param auditLogId the audit log id
   * @param ex the ex
   * @throws AuditException the audit exception
   */
  @Override
  public void fail(AuditLogId auditLogId, Exception ex) throws AuditException {
    update(auditLogId, null, ProcessingState.UnknownFailure, ex.getMessage());
  }


  /**
   * Mark completed.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markCompleted(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.Completed);

  }


  /**
   * Mark loaded.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markLoaded(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.Loaded);
  }


  @Override
  public AuditLogId markLoading(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.Loading);
  }


  /**
   * Mark processed by rap TAT.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markProcessedByRapTAT(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.ProcessedByRapTAT);
  }


  /**
   * Mark rap TAT error.
   *
   * @param item the item
   * @param message the message
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markRapTATError(RapTATItemToProcess item, String message)
      throws AuditException {
    return update(item, ProcessingState.RatTATError, message);
  }


  /**
   * Mark sent to rap TAT.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markSentToRapTAT(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.SentToRapTAT);
  }


  /**
   * Mark writing started to database.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markWritingStartedToDatabase(RapTATItemToProcess item) throws AuditException {
    return update(item, ProcessingState.WritingStarted);
  }


  /**
   * Mark writting completed to database.
   *
   * @param item the item
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId markWrittingCompletedToDatabase(RapTATItemToProcess item)
      throws AuditException {
    return update(item, ProcessingState.ResultWritten);
  }


  /**
   * Update.
   *
   * @param auditLogId the audit log id
   * @param documentId
   * @param state the state
   * @param message the message
   * @return
   * @throws AuditException the audit exception
   */
  @Override
  public synchronized AuditLogId update(AuditLogId auditLogId, String documentId,
      ProcessingState state, String message) throws AuditException {
    String updateSql = getUpdateQuery(auditLogId, documentId, state, message, RunEnvironmentVal);

    return runUpdate(updateSql);

  }


  /**
   * Update.
   *
   * @param item the item
   * @param state the state
   * @throws AuditException the audit exception
   */
  @Override
  public AuditLogId update(RapTATItemToProcess item, ProcessingState state) throws AuditException {
    return update(item.getAuditLogId(), item.getDocumentId(), state, null);
  }


  /**
   * Update.
   *
   * @param item the item
   * @param state the state
   * @param message the message
   * @throws AuditException the audit exception
   */
  public AuditLogId update(RapTATItemToProcess item, ProcessingState state, String message)
      throws AuditException {
    return update(item.getAuditLogId(), item.getDocumentId(), state, message);
  }


  private String formatId(AuditLogId auditLogId) {
    return formatId(auditLogId.getAuditLogId());
  }


  private String formatId(Integer id) {
    return id == 0 || id == null ? "NULL" : id.toString();
  }


  private String formatId(String id) {
    return id == null || id.isEmpty() ? "NULL" : id;
  }


  private String formatMessage(String message) {
    if (message == null) {
      message = "";
    }
    return message;
  }


  private String formatState(ProcessingState state) {
    if (state == null) {
      state = ProcessingState.UnknownFailure;
    }
    return state.name();
  }


  private String formatTimestamp(String timeStamp) {

    if (timeStamp == null) {
      timeStamp = "NULL";
    }
    return timeStamp;
  }


  /**
   * Gets the update query.
   *
   * @param auditLogId the audit log id
   * @param documentId
   * @param state the state
   * @param message the message
   * @param runenvironmentval the runenvironmentval
   * @param startTimeStamp
   * @param endTimeStamp
   * @return the update query
   * @throws AuditException the audit exception
   */
  private String getUpdateQuery(AuditLogId auditLogId, String documentId, ProcessingState state,
      String message, RunEnvironment runenvironmentval) throws AuditException {

    String sql = getConfig().getAuditUpdateQuery(auditLogId);

    AuditType auditType = auditLogId == null ? AuditType.Initial : AuditType.Update;

    String startTimeStamp = "NULL";
    String endTimeStamp = "NULL";

    switch (auditType) {
      case Initial:
        startTimeStamp = Timestamp.from(Instant.now()).toString();
        break;
      case Update:
        if (state == ProcessingState.Completed) {
          endTimeStamp = MessageFormat.format("''{0}''", Timestamp.from(Instant.now()).toString());
        }
        break;
      default:
        throw new AuditException("Invalid audit type");

    }

    String auditLogIdStr = formatId(auditLogId);
    String documentIdStr = formatId(documentId);
    String messageStr = formatMessage(message);
    String stateStr = formatState(state);
    String startTimeStampStr = formatTimestamp(startTimeStamp);
    String endTimeStampStr = formatTimestamp(endTimeStamp);

    String[] params = new String[] {documentIdStr, messageStr, stateStr, startTimeStampStr,
        endTimeStampStr, auditLogIdStr};

    sql = MessageFormat.format(sql, params);

    return sql;
  }


  /**
   * Run update.
   *
   * @param updateSql the update sql
   * @return
   * @throws AuditException the audit exception
   */
  private AuditLogId runUpdate(String updateSql) throws AuditException {

    Logger logger = getLogger();

    Connection connect = null;
    PreparedStatement statement = null;

    /*
     * Open the connection
     */
    try {
      connect = connect();
    } catch (DataSourceException e) {
      logger.error(e.getMessage());
      throw new AuditException(e);
    }

    /*
     * Create a statement
     */
    try {
      statement = connect.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new AuditException(e);
    }

    int auditLogId = 0;

    try {
      boolean hasRecordSet = statement.execute();

      ResultSet resultSet = statement.getGeneratedKeys();

      if (resultSet.next()) {
        auditLogId = resultSet.getInt(1);
      }

    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new AuditException(e);
    }

    return new AuditLogId(auditLogId);
  }

}
