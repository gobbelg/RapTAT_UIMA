package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggingOneLiner1 {
  private static final Logger logger = Logger.getLogger(LoggingOneLiner1.class);


  public static void main(String[] args) {
    // Configure the logging - this does the whole setup
    BasicConfigurator.configure();

    // Optional Step - change the logging level to INFO from default DEBUG
    Logger.getRootLogger().setLevel(Level.DEBUG);

    LoggingOneLiner1.logger.info("Its nice to to be able to configure logging in one line");
    LoggingOneLiner1.logger.debug("I like quick and dirty java tricks");

  }

}
