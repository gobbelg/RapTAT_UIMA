/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.core;

import java.io.File;

/********************************************************
 *
 *
 * @author Glenn Gobbel - Jun 6, 2012
 *******************************************************/
public class SolutionUpdateProcessorTest {

  /*************************************************************
   * @param args
   *
   * @author Glenn Gobbel - Jun 6, 2012
   *************************************************************/
  public static void main(String[] args) {
    File newSolutionFile = new File("/Users/glenn/Desktop/RapTAT_XML/testUpdated01.soln");
    File previousSolutionFile =
        new File("/Users/glenn/_RapTATBoostrapTestSet/testSolution_Training01.soln");

    String[] txtFiles = {"/Users/glenn/Desktop/RapTAT_XML/TextFiles/trainingTestDocument02.txt",
        "/Users/glenn/Desktop/RapTAT_XML/TextFiles/trainingTestDocument03.txt"};
    String[] xmlFiles =
        {"/Users/glenn/Desktop/RapTAT_XML/XMLFiles/trainingTestDocument02.txt.knowtator.xml",
            "/Users/glenn/Desktop/RapTAT_XML/XMLFiles/trainingTestDocument03.txt.knowtator.xml"};

  }

}
