package src.test.gov.va.vha09.grecc.raptat.gg.testing;

// Import log4j classes.
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class MyApp {
  // Define a static logger variable so that it references the
  // Logger instance named "MyApp".
  static Logger logger = Logger.getLogger(MyApp.class);


  public static void main(String[] args) {
    // Set up a simple configuration that logs on the console.
    BasicConfigurator.configure();

    MyApp.logger.info("Entering application.");
    Bar bar = new Bar();
    bar.doIt();
    MyApp.logger.info("Exiting application.");
  }
}
