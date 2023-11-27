/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.textanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

/**
 *
 * @author VHATVHSAHAS1
 */
public class RaptatDocumentTest {

  public RaptatDocumentTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of getProcessedTokens method, of class RaptatDocument.
   */
  @Test
  public void testGetProcessedTokens() {
    System.out.println("getProcessedTokens");
    RaptatDocument instance = new RaptatDocument();
    IndexedTree<IndexedObject<RaptatToken>> expResult = null;
    IndexedTree<IndexedObject<RaptatToken>> result = instance.getProcessedTokens();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawTextInRange method, of class RaptatDocument.
   */
  @Test
  public void testGetRawTextInRange() {
    System.out.println("getRawTextInRange");
    int startOffset = 0;
    int endOffset = 0;
    RaptatDocument instance = new RaptatDocument();
    String expResult = "";
    String result = instance.getRawTextInRange(startOffset, endOffset);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawTokens method, of class RaptatDocument.
   */
  @Test
  public void testGetRawTokens() {
    System.out.println("getRawTokens");
    RaptatDocument instance = new RaptatDocument();
    IndexedTree<IndexedObject<RaptatToken>> expResult = null;
    IndexedTree<IndexedObject<RaptatToken>> result = instance.getRawTokens();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getSentences method, of class RaptatDocument.
   */
  @Test
  public void testGetSentences() {
    System.out.println("getSentences");
    RaptatDocument instance = new RaptatDocument();
    List<AnnotatedPhrase> expResult = null;
    List<AnnotatedPhrase> result = instance.getActiveSentences();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getTextSource method, of class RaptatDocument.
   */
  @Test
  public void testGetTextSource() {
    System.out.println("getTextSource");
    RaptatDocument instance = new RaptatDocument();
    String expResult = "";
    String result = instance.getTextSource().get();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getTextSourcePath method, of class RaptatDocument.
   */
  @Test
  public void testGetTextSourcePath() {
    System.out.println("getTextSourcePath");
    RaptatDocument instance = new RaptatDocument();
    String expResult = "";
    String result = instance.getTextSourcePath().get();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getUniqueIdentifier method, of class RaptatDocument.
   */
  @Test
  public void testGetUniqueIdentifier() {
    System.out.println("getUniqueIdentifier");
    RaptatDocument instance = new RaptatDocument();
    UUID expResult = null;
    UUID result = instance.getUniqueIdentifier();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of regenerateProcessedTokens method, of class RaptatDocument.
   */
  @Test
  public void testRegenerateProcessedTokens() {
    System.out.println("regenerateProcessedTokens");
    boolean removeStopWords = false;
    boolean useStems = false;
    boolean usePOS = false;
    boolean tagReasonNoMeds = false;
    Collection<String> stopWordSet = null;
    RaptatDocument instance = new RaptatDocument();
    List<RaptatToken> expResult = null;
    List<RaptatToken> result = instance.regenerateProcessedTokens(removeStopWords, useStems, usePOS,
        tagReasonNoMeds, stopWordSet);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessedTokens method, of class RaptatDocument.
   */
  @Test
  public void testSetProcessedTokens() {
    System.out.println("setProcessedTokens");
    IndexedTree<IndexedObject<RaptatToken>> inputTokens = null;
    RaptatDocument instance = new RaptatDocument();
    instance.setProcessedTokens(inputTokens);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setRawTokens method, of class RaptatDocument.
   */
  @Test
  public void testSetRawTokens() {
    System.out.println("setRawTokens");
    IndexedTree<IndexedObject<RaptatToken>> rawTokens = null;
    RaptatDocument instance = new RaptatDocument();
    instance.setRawTokens(rawTokens);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setTextSourcePath method, of class RaptatDocument.
   */
  @Test
  public void testSetTextSourcePath() {
    System.out.println("setTextSourcePath");
    RaptatDocument instance = new RaptatDocument();
    instance.setTextSourcePath(Optional.empty());
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}

}
