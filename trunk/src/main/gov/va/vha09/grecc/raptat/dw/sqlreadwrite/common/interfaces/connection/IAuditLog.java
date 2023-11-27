package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.ProcessingState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;

/**
 * The Interface IAuditLog.
 */
public interface IAuditLog {

  /**
   * Fail.
   *
   * @param auditLogId the audit log id
   * @param ex the ex
   * @throws AuditException
   */
  void fail(AuditLogId auditLogId, Exception ex) throws AuditException;


  /**
   * Update the audit table state and add a message
   *
   * @param auditLogId the audit log id
   * @param documentId the document id
   * @param state the state
   * @param message the message
   * @return the audit log id
   * @throws AuditException the audit exception
   */
  AuditLogId update(AuditLogId auditLogId, String documentId, ProcessingState state, String message)
      throws AuditException;


  /**
   * Update the audit table state
   *
   * @param item the item
   * @param state the state
   * @return the audit log id
   * @throws AuditException the audit exception
   */
  AuditLogId update(RapTATItemToProcess item, ProcessingState state) throws AuditException;
}
