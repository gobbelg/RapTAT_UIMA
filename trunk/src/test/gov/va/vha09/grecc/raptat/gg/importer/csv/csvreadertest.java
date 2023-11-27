/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.importer.csv;

import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.csv.CSVReader;

/************************************************************
 * csvreadertest -
 *
 * @author Glenn Gobbel, Jan 31, 2011
 *
 ***********************************************************/
public class csvreadertest {
  // @Test
  // public void getNextDataTest()
  // {
  // DataImporter testImporter = new DataImporter("Open a file");
  // DataImportReader testReader = testImporter.getReader();
  //
  // if (testReader != null)
  // {
  // String [] testData= {};
  // do
  // {
  // testData = testReader.getNextData();
  //
  // } while (testData != null && testData.length != 0);
  // }
  // }

  @Test
  public void getStartingTokenPositionTest() {
    CSVReader myReader = new CSVReader();

    int i = myReader.getGroupingColumns();

    assert i == 8;
  }

}
