package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

/**
 * The Class BaseException.
 */
public class BaseException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -2686588214959359279L;

  /**
   * Instantiates a new base exception with an incoming exception
   *
   * @param exception
   */
  public BaseException(Exception exception) {
    super(exception);
  }


  /**
   * Instantiates a new base exception.
   *
   * @param message the message
   */
  public BaseException(String message) {
    super(message);
  }


  /**
   * Instantiates a new base exception.
   *
   * @param message the message
   * @param e the e
   */
  public BaseException(String message, Exception e) {
    super(message, e);
  }

}
