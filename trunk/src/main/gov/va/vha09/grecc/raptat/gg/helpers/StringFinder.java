package src.main.gov.va.vha09.grecc.raptat.gg.helpers;

import javax.swing.SwingUtilities;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;

/**
 * TestTest
 *
 * @author gtony
 * @date Jul 13, 2020
 *
 */
public class StringFinder {

  private enum RunType {
    TEST_RUN;
  }

  private String stringForSearching = "";

  /**
   * @param stringForSearching
   */
  public StringFinder(String stringForSearching) {
    this.stringForSearching = stringForSearching;
  }

  /**
   * Finds the start offset of a substring within the stringForSearching field of an instance of
   * this class.
   *
   * @param testString
   *
   * @param startSearch
   * @param endSearch
   */
  protected int getOffset(String testString, int startSearch, int endSearch) {

    int result = 0;
    String substringToSearch = this.stringForSearching.substring(startSearch, endSearch);
    result = substringToSearch.indexOf(testString);
    return result;

  }



  public static void main(String[] args) {

    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        RunType runType = RunType.TEST_RUN;

        switch (runType) {
          case TEST_RUN: {
            StringFinder sf = new StringFinder("");
            int startSearch = 0;
            String testString = "";
            sf.getOffset(testString, startSearch, sf.stringForSearching.length());
          }
            break;
          default:
            break;
        }
        System.exit(0);
      }
    });
  }
}
