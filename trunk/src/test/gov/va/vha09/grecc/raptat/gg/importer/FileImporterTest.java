package src.test.gov.va.vha09.grecc.raptat.gg.importer;

import javax.swing.JOptionPane;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImporter;

/************************************************************
 * FileImporterTest -
 *
 * @author Glenn Gobbel, Jan 31, 2011
 *
 ***********************************************************/
public class FileImporterTest {
  @Test
  public void newFileImporterTest() {

    JOptionPane.showMessageDialog(null, "Unable to read first line of file.", "File Reading Error",
        JOptionPane.WARNING_MESSAGE);

    DataImporter testImporter = new DataImporter("Open a file");
  }

}
