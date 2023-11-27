package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

/**
 * The Class WriteException.
 */
public class ReadException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 7217819651709037418L;

  /**
   * Instantiates a new write exception.
   *
   * @param message the message
   */
  public ReadException(String message) {
    super(message);
  }


  /**
   * Instantiates a new write exception.
   *
   * @param message the message
   * @param e the e
   */
  public ReadException(String message, Exception e) {
    super(message, e);
  }

}
