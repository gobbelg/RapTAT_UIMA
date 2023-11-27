package src.test.gov.va.vha09.grecc.raptat.gg.scorer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.scorer.PerformanceScorer;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;

public class PerformanceScorerTester {
  private void checkScores(Hashtable<String, int[]> performanceScores, int[][] expectedScores,
      String[] expectedConcepts, String fileName) {
    String curConcept;
    int[] curScores;
    for (int i = 0; i < expectedConcepts.length; i++) {
      curConcept = expectedConcepts[i];
      if (performanceScores.containsKey(curConcept)) {
        curScores = performanceScores.get(curConcept);
        for (int j = 0; j < 3; j++) {
          if (expectedScores[i][j] != curScores[j]) {
            fail(this.reportFailureCause(curConcept, fileName, j));
          }
        }
      } else {
        fail("Expected concept missing from performance data");
      }
    }
  }


  /*************************************************************
   * Gets the paths to all the files in a folder where the folder is a path relative to the "trunk"
   * of the source code.
   *
   * Preconditions: None Postconditions: None
   *
   * @param relativePath
   * @return
   *
   * @author Glenn Gobbel - Apr 12, 2012
   *************************************************************/
  private Hashtable<String, String> getFilePaths(String relativePath) {
    URI pathURI = null;
    File fileFolder;
    List<File> filesInFolder = null;
    // The file name will be a key to the path to the file
    Hashtable<String, String> pathsToFiles;
    try {
      pathURI = PerformanceScorerTester.class.getClassLoader().getResource(relativePath).toURI();

    } catch (URISyntaxException e) {
      System.out.println(e);
      e.printStackTrace();
    }
    if (pathURI != null) {
      fileFolder = new File(pathURI);
      if (fileFolder.isDirectory()) {
        filesInFolder = Arrays.asList(fileFolder.listFiles());
      } else {
        fail("No testing files found in directory");
      }
    }

    pathsToFiles = new Hashtable<>(filesInFolder.size());
    for (File curFile : filesInFolder) {
      try {
        pathsToFiles.put(curFile.getName(), curFile.getCanonicalPath());
      } catch (IOException e) {
        System.out.println(e);
        e.printStackTrace();
      }
    }

    return pathsToFiles;
  }


  private String reportFailureCause(String curConcept, String fileName, int j) {
    if (j == 0) {
      return "TP count error for " + curConcept + "concept within " + fileName;
    }
    if (j == 1) {
      return "FP count error for " + curConcept + "concept within " + fileName;
    }
    if (j == 2) {
      return "FN count error for " + curConcept + "concept within " + fileName;
    }
    return "Unknown scoring error for " + curConcept + "concept within " + fileName;

  }


  @Test
  public void testScorePerformance() {
    final String RELATIVE_RAPTAT_LOCATION = "RapTATTestingData/RapTATXML";
    final String RELATIVE_HUMAN_LOCATION = "RapTATTestingData/HumanXML";
    final String RELATIVE_TEXT_LOCATION = "RapTATTestingData/RapTATKnowtatorProject/TextData";
    final String[] testFiles =
        {"textDocument_01.txt.knowtator.xml", "textDocument_02.txt.knowtator.xml"};
    int expectedNumberOfTests = testFiles.length;
    final String[] concept = {"roman", "greek", "PartialPhraseMatch", "ExactPhraseMatch"};

    // Row 1 represents expected results (TP,FP,FN) for concept[0] for
    // testFiles[0]
    // and row 2 represents expected results for concept[1] in testFiles[0]
    final int[][] testFiles00Expected = {{4, 3, 0}, {5, 2, 1}, {12, 3, 1}, {5, 9, 2}};

    // Row 1 represents expected results (TP,FP,FN) for concept[0] in
    // testFiles[1]
    // and row 2 represents expected results for concept[1] in testFiles[1]
    final int[][] testFiles01Expected = {{1, 1, 2}, {2, 2, 0}, {5, 1, 1}, {2, 4, 3}};
    String curFileName, curTextFileName;
    String pathToRaptatFile, pathToHumanFile, pathToTextFile;
    Hashtable<String, int[]> performanceScores;
    List<AnnotatedPhrase> deletedPhrases = new ArrayList<>();

    Hashtable<String, String> raptatFilePaths = this.getFilePaths(RELATIVE_RAPTAT_LOCATION);
    Hashtable<String, String> humanFilePaths = this.getFilePaths(RELATIVE_HUMAN_LOCATION);
    Hashtable<String, String> textFilePaths = this.getFilePaths(RELATIVE_TEXT_LOCATION);

    PerformanceScorer testScorer = new PerformanceScorer();
    Enumeration<String> fileNames = raptatFilePaths.keys();
    Enumeration<String> textFileNames = textFilePaths.keys();
    int actualNumberOfTests = 0;

    assertEquals(expectedNumberOfTests, actualNumberOfTests);
  }

}
