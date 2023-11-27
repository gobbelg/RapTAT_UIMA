/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.MenuResponseDispatcher;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;

/**
 *
 * @author VHATVHSAHAS1
 */
public class MenuResponseDispatcherTest {

  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}


  public MenuResponseDispatcherTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of evaluateConceptMapSolution method, of class MenuResponseDispatcher.
   */
  @Test
  public void testEvaluateConceptMapSolution() {
    System.out.println("evaluateConceptMapSolution");
    ConceptMapSolution curSolution = null;
    OptionsManager annotatorOptions = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    //instance.evaluateConceptMapSolution(curSolution, annotatorOptions);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of loadAlgorithmSolution method, of class MenuResponseDispatcher.
   */
  @Test
  public void testLoadAlgorithmSolution() {
    System.out.println("loadAlgorithmSolution");
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    RaptatPair<ProbabilisticTSFinderSolution, String> expResult = null;
    RaptatPair<ProbabilisticTSFinderSolution, String> result = instance.loadProbabilisticSolution();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of loadConceptMapSolution method, of class MenuResponseDispatcher.
   */
  @Test
  public void testLoadConceptMapSolution() {
    System.out.println("loadConceptMapSolution");
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    ConceptMapSolution expResult = null;
    ConceptMapSolution result = instance.loadConceptMapSolution();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of mapSolution method, of class MenuResponseDispatcher.
   */
  @Test
  public void testMapSolution() {
    System.out.println("mapSolution");
    ConceptMapSolution curSolution = null;
    OptionsManager annotatorOptions = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    instance.mapSolution(curSolution, annotatorOptions);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of raptatAnnotateDocs method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRaptatAnnotateDocs() {
    System.out.println("raptatAnnotateDocs");
    Point thePosition = null;
    Dimension theDimension = null;
    JFrame applicationWindow = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    instance.annotateDocuments(thePosition, theDimension, applicationWindow);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of raptatExportResults method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRaptatExportResults() {
    System.out.println("raptatExportResults");
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    instance.exportResults();
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of raptatTrainML method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRaptatTrainML() {
    System.out.println("raptatTrainML");
    Point thePosition = null;
    Dimension theDimension = null;
    JFrame applicationWindow = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    instance.trainAnnotator(thePosition, theDimension, applicationWindow);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of runConceptMapBootstrapAnalysis method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRunConceptMapBootstrapAnalysis() {
    System.out.println("runConceptMapBootstrapAnalysis");
    OptionsManager theOptions = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    //instance.runConceptMapBootstrapAnalysis(theOptions);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of runConceptMapCrossValidationAnalysis method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRunConceptMapCrossValidationAnalysis() {
    System.out.println("runConceptMapCrossValidationAnalysis");
    OptionsManager mappingOptions = null;
    JFrame mainWindow = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
   // instance.runConceptMapCrossValidationAnalysis(mappingOptions, mainWindow);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of runPhraseIDBootstrapAnalysis method, of class MenuResponseDispatcher.
   */
  @Test
  public void testRunPhraseIDBootstrapAnalysis() {
    System.out.println("runPhraseIDBootstrapAnalysis");
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
   // instance.runPhraseIDBootstrapAnalysis();
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of scoreAndUpdatePerformance method, of class MenuResponseDispatcher.
   */
  @Test
  public void testScoreAndUpdatePerformance() {
    System.out.println("scoreAndUpdatePerformance");
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    instance.scorePerformance(null, null);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of trainConceptMapSolution method, of class MenuResponseDispatcher.
   */
  @Test
  public void testTrainConceptMapSolution() {
    System.out.println("trainConceptMapSolution");
    String learnMethod = "";
    boolean useNulls = false;
    boolean useStems = false;
    boolean usePOS = false;
    boolean invertTokenSequence = false;
    boolean removeStopWords = false;
    Constants.ContextHandling reasonNoMedsProcessing = null;
    MenuResponseDispatcher instance = new MenuResponseDispatcher();
    ConceptMapSolution expResult = null;
    ConceptMapSolution result = instance.trainConceptMapSolution(learnMethod, useNulls, useStems,
        usePOS, invertTokenSequence, removeStopWords, reasonNoMedsProcessing);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
