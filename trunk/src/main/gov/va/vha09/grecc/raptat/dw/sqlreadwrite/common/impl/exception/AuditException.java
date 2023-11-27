package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

/**
 * The Class AuditException.
 */
public class AuditException extends BaseException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 3555541668642797502L;

  /**
   * Instantiates a new audit exception.
   *
   * @param e the e
   */
  public AuditException(Exception e) {
    super(e);
  }


  /**
   * Instantiates a new audit exception.
   *
   * @param message the message
   */
  public AuditException(String message) {
    super(message);
  }

}
