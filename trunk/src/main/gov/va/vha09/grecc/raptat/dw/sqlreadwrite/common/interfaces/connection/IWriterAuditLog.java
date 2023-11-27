package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;

public interface IWriterAuditLog extends IAuditLog {
  /**
   * Mark writing started to database.
   *
   * @param rapTATItemToProcess the rap TAT item to process
   * @throws AuditException
   */
  AuditLogId markWritingStartedToDatabase(RapTATItemToProcess rapTATItemToProcess)
      throws AuditException;


  /**
   * Mark writting completed to database.
   *
   * @param rapTATItemToProcess the rap TAT item to process
   * @throws AuditException
   */
  AuditLogId markWrittingCompletedToDatabase(RapTATItemToProcess rapTATItemToProcess)
      throws AuditException;
}
