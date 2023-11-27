package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;

public interface IRapTATAuditLog extends IAuditLog {
  /**
   * Mark processed by rap TAT.
   *
   * @param item the item
   */
  AuditLogId markProcessedByRapTAT(RapTATItemToProcess item) throws AuditException;


  /**
   * Mark rap TAT error.
   *
   * @param item the item
   * @param message the message
   */
  AuditLogId markRapTATError(RapTATItemToProcess item, String message) throws AuditException;


  /**
   * Mark sent to rap TAT.
   *
   * @param item the item
   * @throws AuditException
   */
  AuditLogId markSentToRapTAT(RapTATItemToProcess item) throws AuditException;
}
