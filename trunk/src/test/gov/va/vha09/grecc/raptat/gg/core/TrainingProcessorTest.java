/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.core;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

/********************************************************
 *
 *
 * @author Glenn Gobbel - May 29, 2012
 *******************************************************/
public class TrainingProcessorTest {
  public static void main(String[] args) {
    URI knownExampleFile = null;
    try {
      knownExampleFile =
          TrainingProcessorTest.class.getClassLoader().getResource("en-pos-maxent.bin").toURI();
    } catch (URISyntaxException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    String parentDirectory = new File(knownExampleFile.getSchemeSpecificPart()).getParent();
    File testFile = new File(parentDirectory + File.separator + "testSolution_Training01.soln");
    File dirFile = GeneralHelper.getDirectory("Get directory containing files", parentDirectory);
    // File dirFile = new
    // File("/Users/glenn/_RapTATBoostrapTestSet/txtFiles");
    // File dirFile = new
    // File("/Users/glenn/_RapTATBoostrapTestSet/txtFile20");
    File[] theFiles = dirFile.listFiles();
    List<String> txtFileList = new ArrayList<>();
    for (File curFile : theFiles) {
      txtFileList.add(curFile.getAbsolutePath());
    }

    dirFile = GeneralHelper.getDirectory("Get directory containing xml", parentDirectory);
    // dirFile = new File("/Users/glenn/_RapTATBoostrapTestSet/xmlFiles");
    theFiles = dirFile.listFiles();
    List<String> xmlFileList = new ArrayList<>();
    for (File curFile : theFiles) {
      xmlFileList.add(curFile.getAbsolutePath());
    }

  }

}
