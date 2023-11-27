package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

/**
 * The Class AuditLogId.
 */
public class AuditLogId {

  /** The audit log id. */
  private Integer auditLogId;

  /**
   * Instantiates a new audit log id.
   *
   * @param auditLogId the audit log id
   */
  public AuditLogId(Integer auditLogId) {
    this.auditLogId = auditLogId;
  }


  /**
   * Gets the audit log id.
   *
   * @return the audit log id
   */
  public Integer getAuditLogId() {
    return this.auditLogId;
  }


  /**
   * Checks if is valid.
   *
   * @return true, if is valid
   */
  public boolean isValid() {
    return this.auditLogId != null && this.auditLogId > 0;
  }


  @Override
  public String toString() {
    return this.auditLogId == null || this.auditLogId <= 0 ? "Not Set" : this.auditLogId.toString();
  }

}
