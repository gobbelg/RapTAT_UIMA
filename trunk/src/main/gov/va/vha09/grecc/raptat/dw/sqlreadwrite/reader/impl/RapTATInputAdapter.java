package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.reader.impl;

import java.io.File;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RunEnvironment;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.config.DataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.AuditAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.RapTATDatabaseConnection;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.ConfigurationException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.reader.interfaces.IInputAdapter;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.Stopwatch;

/**
 * Implementation of the IRapTATReader to access a MSSQL DB to get collection of
 * {@link src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess}
 * items to process
 */
public class RapTATInputAdapter extends RapTATDatabaseConnection implements IInputAdapter {

  private static Stopwatch stopwatch = new Stopwatch();

  /** The current record. */
  private RapTATItemToProcess currentRecord;

  private boolean doContinue = true;

  /**
   * Instantiates a new RapTAT Reader.
   *
   * @param config the
   *        {@link src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration}
   * @param auditAdapter
   * @param logger2
   */
  public RapTATInputAdapter(IDataSourceConfiguration config, AuditAdapter auditAdapter,
      Logger logger) {
    super(config, auditAdapter, logger);
  }


  @Override
  public void halt() {
    doContinue = false;
  }


  /**
   * Is there another
   * {@link src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess}
   * to process.
   *
   * @return true, if successful
   */
  @Override
  public boolean hasNext() {

    if (this.getLogger().isDebugEnabled()) {
      stopwatch.markTime("StartDocumentRead");
    }
    /*
     * If the halt has been called, exit out
     */
    if (doContinue == false) {
      return false;
    }

    String selectSql = getDocumentSelectionQuery(RunEnvironmentVal);

    currentRecord = null;

    Logger logger = getLogger();

    ResultSet result = queryResult(selectSql);

    boolean hasRecord = false;
    try {
      hasRecord = result != null && result.next();
    } catch (SQLException e) {
      logger.error(e.getMessage());
    }

    if (hasRecord) {
      try {
        String documentId = result.getString("TIUDocumentSID");
        String reportText = result.getString("ReportText");
        AuditLogId auditLogId = new AuditLogId(result.getInt("AuditLogId"));
        String message = result.getString("Message");
        String processingState = result.getString("ProcessingState");
        String startTimestamp = result.getString("StartTimestamp");
        String endTimestamp = result.getString("EndTimestamp");

        currentRecord = new RapTATItemToProcess(getAuditAdapter(), documentId, reportText,
            auditLogId, message, processingState, startTimestamp, endTimestamp);

        auditLogId = currentRecord.markLoading();
        logger.info(MessageFormat.format("Next record is {0}", currentRecord.getDocumentId()));
      } catch (SQLException | AuditException e) {
        currentRecord = null;
        hasRecord = false;
        logger.error(e.getMessage());
      }
    }

    if (this.getLogger().isDebugEnabled()) {
      stopwatch.getSecondsSinceMark("CompletedDocumentRead");
    }
    return hasRecord;
  }


  /**
   * Get the next
   * {@link src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess}
   * to process.
   *
   * @return the rap TAT item to process
   */
  @Override
  public RapTATItemToProcess next() {
    /*-
     * 1) If
     * 		a) Document Exists
     * 			i) Read in next document from input adapter
     * 			ii) Record Audit table status as "Loading"
     * 				with start timestamp of now (SQL level, UTC)
     */
    RapTATItemToProcess item = currentRecord;
    if (item != null) {
      try {
        AuditLogId auditLogId = item.markLoaded();

        if (item.getAuditLogId().isValid() == false && auditLogId.isValid()) {
          item.setAuditLogId(auditLogId);
        } else if (item.getAuditLogId().isValid() == false && auditLogId.isValid() == false) {
          getLogger().error(MessageFormat.format("AuditLogId for DocumentId {0} was invalid ({1}).",
              item.getDocumentId(), auditLogId.getAuditLogId()));
          item = null;
        }
      } catch (AuditException e) {
        getLogger().error(e);
        item = null;
      }
    }

    return item;
  }


  /**
   * Gets the document count query.
   *
   * @param Prod or Dev
   * @return the document count query
   */
  @SuppressWarnings("unused")
  private String getDocumentCountQuery(RunEnvironment runenvironmentval) {
    return getConfig().getDocumentCountQuery();
  }


  /**
   * Gets the document selection query.
   *
   * @param Prod or Dev
   * @return the document selection query
   */
  private String getDocumentSelectionQuery(RunEnvironment development) {
    return getConfig().getNextDocumentQuery();
  }


  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {

    try {

      /** The logger. */
      final Logger logger = createLogger();

      File propertiesFile = Paths
          .get(new File(Thread.currentThread().getContextClassLoader().getResource(".").getFile())
              .getParent(), "InputAdapter.properties")
          .toFile();
      IDataSourceConfiguration configuration = new DataSourceConfiguration(propertiesFile);
      AuditAdapter auditLogger = createAuditLogger();
      RapTATInputAdapter reader = new RapTATInputAdapter(configuration, auditLogger, logger);

      int count = 1;

      while (reader.hasNext() && count-- > 0) {
        try (RapTATItemToProcess readerItem = reader.next()) {
          String documentText = readerItem.getDocumentText();
          logger.info(documentText);
        } catch (Exception e) {
          // Mark failure
        }
      }

    } catch (ConfigurationException e) {
      e.printStackTrace();
    }

  }


  /**
   * Creates the audit logger; currently not implemented for testing purposes.
   *
   * @return the audit adapter
   */
  private static AuditAdapter createAuditLogger() {
    IDataSourceConfiguration config = null;
    Logger logger = null;
    return new AuditAdapter(config, logger) {

    };
  }


  private static Logger createLogger() {
    // return Logger.getLogger(RapTATReader.class);

    /*
     * Overwriting Logger class for intermediate testing.
     */
    return new Logger(null) {
      @Override
      public void error(Object message) {
        System.err.println(message);
      }


      @Override
      public void info(Object message) {
        System.out.println(message);
      }

    };
  }

}
