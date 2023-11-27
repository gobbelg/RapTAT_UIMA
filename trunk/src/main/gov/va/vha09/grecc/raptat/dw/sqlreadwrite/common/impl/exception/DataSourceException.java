/*
 *
 */
package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

/**
 * Exception for when an issue arises with the data source
 *
 * @author Dax
 *
 */
public class DataSourceException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 308584679591625975L;

  /**
   * Constructor for message only
   *
   * @param message
   */
  public DataSourceException(String message) {
    super(message);
  }


  /**
   * Constructor for custom message and underlying exception
   *
   * @param message
   * @param e
   */
  public DataSourceException(String message, Exception e) {
    super(message, e);
  }

}
