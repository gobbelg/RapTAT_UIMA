package src.test.gov.va.vha09.grecc.raptat.gg.importer.knowtator;

import java.util.Arrays;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImportReader;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImporter;

/************************************************************
 * KnowtatorReaderTester -
 *
 * @author Glenn Gobbel, Feb 1, 2011
 *
 ***********************************************************/
public class KnowtatorReaderTester {
  @Test
  public void KnowtatorReaderTest() {
    DataImporter testImporter = new DataImporter("Select a knowtator project");
    DataImportReader theReader = testImporter.getReader();
    System.out.println("Longest phrase is " + theReader.getNumPhraseTokens() + " elements long.");
    String[] theData = theReader.getNextData();
    while (theData != null) {
      System.out.println("Current Data:  " + Arrays.toString(theData));
      theData = theReader.getNextData();
    }
  }
}
