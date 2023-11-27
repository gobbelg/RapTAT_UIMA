package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;

/**
 * The Interface IReaderComplete.
 */
public interface IReaderAuditLog extends IAuditLog {

  /**
   * Mark the item as completed
   *
   * @param rapTATItemToProcess the rap TAT item to process
   * @return the audit log id
   * @throws AuditException the audit exception
   */
  AuditLogId markCompleted(RapTATItemToProcess item) throws AuditException;


  /**
   * Mark loaded.
   *
   * @param item the item
   * @throws AuditException
   */
  AuditLogId markLoaded(RapTATItemToProcess item) throws AuditException;


  /**
   * Mark the item as loading
   *
   * @param item the item
   * @return the audit log id
   * @throws AuditException
   */
  AuditLogId markLoading(RapTATItemToProcess item) throws AuditException;

}
