package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

/**
 * The Class SQLDriverException.
 */
public class SQLDriverException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 7368711575427220013L;

  /**
   * Instantiates a new SQL driver exception.
   *
   * @param e the e
   */
  public SQLDriverException(ClassNotFoundException e) {
    super(e);
  }

}
