package src.test.gov.va.vha09.grecc.raptat.gg.analysis.scorer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.scorer.PerformanceScorer;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;

/**
 *
 * @author VHATVHSAHAS1
 */
public class PerformanceScorerTest {

  private Hashtable<String, int[]> getExpectedPerformanceScores() {
    return null;
  }


  private boolean scoresEqual(Map<String, int[]> resultScores, Map<String, int[]> expectedScores) {
    /*
     * If both are null, return true
     */
    if (resultScores == null && expectedScores == null) {
      return true;
    }

    /*
     * Return false if only one is null (we know both are not null from above)
     */
    if (resultScores == null || expectedScores == null) {
      return false;
    }

    /* Trivial case - same object */
    if (resultScores.equals(expectedScores)) {
      return true;
    }

    if (resultScores.size() != expectedScores.size()) {
      return false;
    }

    for (String resultKey : resultScores.keySet()) {
      if (!expectedScores.containsKey(resultKey)) {
        return false;
      }
      int[] resultValues = resultScores.get(resultKey);
      int[] expectedValues = expectedScores.get(resultKey);
      boolean valuesEqual = Arrays.equals(resultValues, expectedValues);

      return valuesEqual;
    }

    /* Return true if both objects are empty Hashtables */
    return true;
  }


  /**
   * Tests the method compareScores within this class PerformanceScoreTester
   */
  @Test
  public void testCompareScores() {
    System.out.println("compareScores");
    Hashtable<String, int[]> resultScores = null;
    Hashtable<String, int[]> expectedScores = null;

    assertTrue(this.scoresEqual(resultScores, expectedScores));

    expectedScores = new Hashtable<>();
    assertFalse(this.scoresEqual(resultScores, expectedScores));

    resultScores = new Hashtable<>();
    assertTrue(this.scoresEqual(resultScores, expectedScores));

    int[] resultValues = new int[] {1, 2, 3};
    resultScores.put("a", resultValues);
    assertFalse(this.scoresEqual(resultScores, expectedScores));

    expectedScores.put("a", resultValues);
    assertTrue(this.scoresEqual(resultScores, expectedScores));

    int[] expectedValues = new int[] {1, 2, 3};
    expectedScores.put("a", expectedValues);
    assertTrue(this.scoresEqual(resultScores, expectedScores));

    expectedValues[2] = 4;
    assertFalse(this.scoresEqual(resultScores, expectedScores));

    expectedValues[2] = 3;
    assertTrue(this.scoresEqual(resultScores, expectedScores));

    resultScores.put("b", new int[] {7, 8, 9});
    assertFalse(this.scoresEqual(resultScores, expectedScores));

    expectedScores.put("b", new int[] {7, 8, 9});
    assertTrue(this.scoresEqual(resultScores, expectedScores));

    expectedScores.put("b", new int[] {7, 8, 9, 10});
    assertFalse(this.scoresEqual(resultScores, expectedScores));
  }


  /**
   * Test of main method, of class PerformanceScorer.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    PerformanceScorer.main(args);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of the scorePerformanceWithGraph method of class PerformanceScorer
   * 
   * @throws URISyntaxException
   * 
   */
  @Test
  public void testScorePerformanceWithGraph() throws URISyntaxException {
    System.out.println("scorePerformanceWithGraph");

    UserPreferences.INSTANCE.initializeLVGLocation();
    new PerformanceScorerTest().testCompareScores();

    ClassLoader classLoader = PerformanceScorerTest.class.getClassLoader();
    File textFileDirectory = new File(classLoader
        .getResource("src/main/resources/PerformanceScoreTestWorkspace/corpus").toURI().getPath());
    File raptatFileDirectory = new File(
        classLoader.getResource("src/main/resources/PerformanceScoreTestWorkspace/saved_Hillard")
            .toURI().getPath());
    File referenceFileDirectory = new File(
        classLoader.getResource("src/main/resources/PerformanceScoreTestWorkspace/saved_Gentry")
            .toURI().getPath());
    File schemaConceptFile = new File(classLoader
        .getResource("src/main/resources/PerformanceScoreTestWorkspace/projectschema.xml").toURI()
        .getPath());
    File acceptableConceptsFile = null;

    boolean checkPhraseOffsets = true;
//    Map<String, int[]> resultScores = PerformanceScorer.compareFileSets(textFileDirectory,
//        referenceFileDirectory, raptatFileDirectory, schemaConceptFile, acceptableConceptsFile,
//        AnnotationApp.EHOST, checkPhraseOffsets);
//
//    Map<String, int[]> expectedScores = this.getExpectedPerformanceScores();
//
//    assertTrue(this.scoresEqual(resultScores, expectedScores));
  }


  /**
   * Test of writeResultsSimple method, of class PerformanceScorer.
   */
  @Test
  public void testWriteResultsSimple() throws Exception {
    System.out.println("writeResultsSimple");
    Hashtable resultMap = null;
    List<String> textSources = null;
    PerformanceScorer instance = new PerformanceScorer();
    instance.writeResultsSimple(resultMap, textSources);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
