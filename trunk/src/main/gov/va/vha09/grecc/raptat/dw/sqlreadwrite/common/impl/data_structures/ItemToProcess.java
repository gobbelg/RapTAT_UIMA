package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IAuditLog;

/**
 * The Class ItemToProcess.
 *
 * @param <T> the generic type
 */
public abstract class ItemToProcess implements AutoCloseable {

  /** The audit adapter. */
  private IAuditLog auditAdapter;

  /**
   * Instantiates a new item to process.
   *
   * @param auditAdapter2 the audit adapter
   */
  public ItemToProcess(IAuditLog auditAdapter) {
    this.auditAdapter = auditAdapter;
  }


  /**
   * Close.
   *
   * @throws Exception the exception
   */
  @Override
  public void close() throws Exception {
    onAutoClose();
  }


  /**
   * Gets the auto adapter.
   *
   * @return the auto adapter
   */
  public IAuditLog getAutoAdapter() {
    return this.auditAdapter;
  }


  /**
   * On auto close.
   *
   * @throws AuditException
   */
  protected abstract void onAutoClose() throws AuditException;
}
