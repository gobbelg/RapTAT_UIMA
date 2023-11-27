package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import org.apache.log4j.Logger;

public class Bar {
  static Logger logger = Logger.getLogger(Bar.class);


  public void doIt() {
    Bar.logger.debug("Did it again!");
  }
}
