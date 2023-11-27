/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.textanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import opennlp.tools.util.Span;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.TokenProcessingOptions;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

/**
 *
 * @author VHATVHSAHAS1
 */
public class TextAnalysisTest {

  public TextAnalysisTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of getSentenceTokens method, of class TextAnalyzer.
   */
  @Test
  public void testGetSentenceTokens() {
    System.out.println("getSentenceTokens");
    String sentenceText = "";
    Span sentenceSpan = null;
    TextAnalyzer instance = new TextAnalyzer();
    RaptatPair<List<RaptatToken>, List<RaptatToken>> expResult = null;
    RaptatPair<List<RaptatToken>, List<RaptatToken>> result =
        instance.getSentenceTokens(sentenceText, sentenceSpan, 0);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of main method, of class TextAnalyzer.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    TextAnalyzer.main(args);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of processDocument method, of class TextAnalyzer.
   */
  @Test
  public void testProcessDocument() {
    System.out.println("processDocument");
    String pathToTextDocument = "";
    TextAnalyzer instance = new TextAnalyzer();
    RaptatDocument expResult = null;
    RaptatDocument result = instance.processDocument(pathToTextDocument);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of processText method, of class TextAnalyzer.
   */
  @Test
  public void testProcessText() {
    System.out.println("processText");
    String inputText = "";
    TextAnalyzer instance = new TextAnalyzer();
    RaptatDocument expResult = null;
    RaptatDocument result = instance.processText(inputText, Optional.empty());
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessingParameters method, of class TextAnalyzer.
   */
  @Test
  public void testSetProcessingParameters_6args() {
    System.out.println("setProcessingParameters");
    boolean useStems = false;
    boolean usePOS = false;
    boolean removeStopWords = false;
    int maxElements = 0;
    Constants.ContextHandling reasonNoMedsProcessing = null;
    TextAnalyzer instance = new TextAnalyzer();
    instance.setTokenProcessingParameters(useStems, usePOS, removeStopWords, maxElements,
        reasonNoMedsProcessing);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessingParameters method, of class TextAnalyzer.
   */
  @Test
  public void testSetProcessingParameters_TokenProcessingOptions() {
    System.out.println("setProcessingParameters");
    TokenProcessingOptions theOptions = null;
    TextAnalyzer instance = new TextAnalyzer();
    instance.setTokenProcessingParameters(theOptions);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of updateTrainingGroup method, of class TextAnalyzer.
   */
  @Test
  public void testUpdateTrainingGroup() {
    System.out.println("updateTrainingGroup");
    AnnotationGroup inputGroup = null;
    TextAnalyzer instance = new TextAnalyzer();
    instance.updateTrainingGroup(inputGroup);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of updateTrainingGroups method, of class TextAnalyzer.
   */
  @Test
  public void testUpdateTrainingGroups() {
    System.out.println("updateTrainingGroups");
    List<AnnotationGroup> documentGroups = null;
    TextAnalyzer instance = new TextAnalyzer();
    instance.updateTrainingGroups(documentGroups);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}

}
