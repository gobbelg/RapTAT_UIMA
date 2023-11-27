package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception;

public class ConfigurationException extends BaseException {

  /**
   *
   */
  private static final long serialVersionUID = 3714640588302752229L;

  public ConfigurationException(String message) {
    super(message);
  }


  public ConfigurationException(String message, Exception e) {
    super(message, e);
  }

}
