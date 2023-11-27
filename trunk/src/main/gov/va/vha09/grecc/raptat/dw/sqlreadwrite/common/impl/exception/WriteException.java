package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

/**
 * The Class WriteException.
 */
public class WriteException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 7217819651709037418L;

  /**
   * Instantiates a new write exception.
   *
   * @param exception the exception
   */
  public WriteException(Exception exception) {
    super(exception);
  }


  /**
   * Instantiates a new write exception.
   *
   * @param message the message
   */
  public WriteException(String message) {
    super(message);
  }


  /**
   * Instantiates a new write exception.
   *
   * @param message the message
   * @param e the e
   */
  public WriteException(String message, Exception e) {
    super(message, e);
  }

}
