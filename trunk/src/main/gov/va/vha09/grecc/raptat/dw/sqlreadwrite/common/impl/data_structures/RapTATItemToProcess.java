package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

import java.io.File;
import java.text.MessageFormat;
import java.util.Optional;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RapTATItemProcessingState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.ReadException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IAuditLog;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IRapTATAuditLog;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IReaderAuditLog;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IWriterAuditLog;

/**
 * The data structure used to represent a document as stored in a MSSQL database, representing the
 * document text, a unique identifier, and the current state of the processing for this document.
 */
public class RapTATItemToProcess extends ItemToProcess {

  /** The tiu document SID. */
  private String tiuDocumentSID;

  /** The report text. */
  private String reportText;

  /** The audit log id. */
  private AuditLogId auditLogId;

  /** The message. */
  private String message;

  /** The processing state. */
  private String processingState;

  /** The start timestamp. */
  private String startTimestamp;

  /** The end timestamp. */
  private String endTimestamp;

  private Optional<File> optionalReportTextFile = Optional.empty();

  /**
   * Instantiates a new RapTAT document result with only the reference to the audit log id;
   * primarily used when recording a failure.
   *
   * @param auditAdapter the audit adapter
   * @param auditLogId the audit log id
   */
  public RapTATItemToProcess(IAuditLog auditAdapter, AuditLogId auditLogId) {
    super(auditAdapter);
    this.auditLogId = auditLogId;
  }


  /**
   * Instantiates a new RapTAT document result.
   *
   * @param auditAdapter the audit adapter
   * @param tiuDocumentSID the tiu document SID
   * @param reportText the report text
   * @param auditLogId the audit log id
   * @param message the message
   * @param processingState the processing state
   * @param startTimestamp the start timestamp
   * @param endTimestamp the end timestamp
   */
  public RapTATItemToProcess(IAuditLog auditAdapter, String tiuDocumentSID, String reportText,
      AuditLogId auditLogId, String message, String processingState, String startTimestamp,
      String endTimestamp) {
    this(auditAdapter, auditLogId);
    this.tiuDocumentSID = tiuDocumentSID;
    this.reportText = reportText;
    this.message = message;
    this.processingState = processingState;
    this.startTimestamp = startTimestamp;
    this.endTimestamp = endTimestamp;
  }


  /**
   * Gets the audit log id.
   *
   * @return the audit log id
   */
  public AuditLogId getAuditLogId() {
    return auditLogId;
  }


  /**
   * Gets the tiu document SID.
   *
   * @return the tiu document SID
   */
  public String getDocumentId() {
    return tiuDocumentSID;
  }

  /*
   * A an item like a RapTATItemToProcess does not have to correspond to a file, it can be text form
   * a database, so changed reportTextFile to an Optional object.
   */
  // /**
  // * Gets the document path; if no document has been specified, then create a temp file and return
  // * that
  // *
  // * @return the document path
  // * @throws IOException Signals that an I/O exception has occurred.
  // */
  // public String getDocumentPath() throws IOException {
  //
  // if (this.reportTextFile == null) {
  // String documentText = getDocumentText();
  // this.reportTextFile = File.createTempFile("docText", ".txt");
  // try (FileWriter writer = new FileWriter(this.reportTextFile)) {
  // writer.write(documentText);
  // }
  // }
  // return this.reportTextFile.getAbsolutePath();
  // }


  public Optional<String> getDocumentPath() {
    if (optionalReportTextFile.isEmpty()) {
      return Optional.empty();
    }

    File reportTextFile = optionalReportTextFile.get();
    if (!optionalReportTextFile.get().exists()) {
      return Optional.empty();
    }
    return Optional.of(reportTextFile.getAbsolutePath());
  }

  /**
   * Gets the report text.
   *
   * @return the report text
   */
  public String getDocumentText() {
    return reportText;
  }


  /**
   * Gets the end timestamp.
   *
   * @return the end timestamp
   */
  public String getEndTimestamp() {
    return endTimestamp;
  }


  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }


  /**
   * Gets the processing state.
   *
   * @return the processing state
   * @throws ReadException the read exception
   */
  public RapTATItemProcessingState getProcessingState() throws ReadException {
    switch (processingState) {
      case "ERROR":
        return RapTATItemProcessingState.Error;

      case "SUCCESS":
        return RapTATItemProcessingState.Success;

      default:
        throw new ReadException(
            MessageFormat.format("Unknown processing state: {0}", processingState));
    }
  }


  /**
   * Gets the start timestamp.
   *
   * @return the start timestamp
   */
  public String getStartTimestamp() {
    return startTimestamp;
  }


  /**
   * Mark loaded.
   *
   * @return
   *
   * @throws AuditException the audit exception
   */
  public AuditLogId markLoaded() throws AuditException {
    return getReaderAuditLog().markLoaded(this);
  }


  /**
   * Mark loading.
   *
   * @return the audit log id
   * @throws AuditException
   */
  public AuditLogId markLoading() throws AuditException {
    AuditLogId auditLogId = getReaderAuditLog().markLoading(this);
    setAuditLogId(auditLogId);
    return auditLogId;
  }


  /**
   * Mark processed by rap TAT.
   *
   * @return
   *
   * @throws AuditException the audit exception
   */
  public AuditLogId markProcessedByRapTAT() throws AuditException {
    return getRapTATAuditLog().markProcessedByRapTAT(this);
  }


  /**
   * Mark rap TAT error.
   *
   * @param message the message
   * @return
   * @throws AuditException the audit exception
   */
  public AuditLogId markRapTATError(String message) throws AuditException {
    return getRapTATAuditLog().markRapTATError(this, message);
  }


  /**
   * Mark sent to rap TAT.
   *
   * @return
   *
   * @throws AuditException the audit exception
   */
  public AuditLogId markSentToRapTAT() throws AuditException {
    return getRapTATAuditLog().markSentToRapTAT(this);
  }


  /**
   * Mark writing to database.
   *
   * @return
   *
   * @throws AuditException the audit exception
   */
  public AuditLogId markWritingToDatabase() throws AuditException {
    return getWriterAuditLog().markWritingStartedToDatabase(this);
  }


  /**
   * Mark written to database.
   *
   * @return
   *
   * @throws AuditException the audit exception
   */
  public AuditLogId markWrittenToDatabase() throws AuditException {
    return getWriterAuditLog().markWrittingCompletedToDatabase(this);
  }


  /**
   * Sets the audit log id.
   *
   * @param auditLogId the new audit log id
   */
  public void setAuditLogId(AuditLogId auditLogId) {
    this.auditLogId = auditLogId;
  }



  /**
   * Gets the rap TAT audit log.
   *
   * @return the rap TAT audit log
   */
  private IRapTATAuditLog getRapTATAuditLog() {
    return (IRapTATAuditLog) getAutoAdapter();
  }


  /**
   * Gets the reader audit log.
   *
   * @return the reader audit log
   */
  private IReaderAuditLog getReaderAuditLog() {
    return (IReaderAuditLog) getAutoAdapter();
  }


  /**
   * Gets the writer audit log.
   *
   * @return the writer audit log
   */
  private IWriterAuditLog getWriterAuditLog() {
    return (IWriterAuditLog) getAutoAdapter();
  }


  /**
   * On auto close.
   *
   * @throws AuditException the audit exception
   */
  @Override
  protected void onAutoClose() throws AuditException {
    getReaderAuditLog().markCompleted(this);
  }

}
