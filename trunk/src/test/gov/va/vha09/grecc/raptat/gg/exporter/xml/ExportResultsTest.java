/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.exporter.xml;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.ExportResults;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

/********************************************************
 *
 *
 * @author Glenn Gobbel - Jun 11, 2012
 *******************************************************/
public class ExportResultsTest {
  public static void main(String[] args) {
    // String [] txtFiles =
    // {"/Users/glenn/Desktop/RapTAT_XML/trainingTestDocument07.ser"};

    File dirFile = GeneralHelper.getDirectory("Get directory containing files");
    File[] theFiles = dirFile.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        if (pathname.getName().endsWith(".ser")) {
          return true;
        }
        return false;
      }
    });
    List<String> txtFiles = new ArrayList<>();
    for (File curFile : theFiles) {
      txtFiles.add(curFile.getAbsolutePath());
    }

    OptionsManager.getInstance().setAnnotationResultPaths(txtFiles);
    ExportResults.runExporter();
    System.out.println("Export completed");
  }
}
