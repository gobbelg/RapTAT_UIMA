/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

/**
 *
 * @author VHATVHSAHAS1
 */
public class AnnotatedPhraseTest {

  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}


  public AnnotatedPhraseTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of compareTo method, of class AnnotatedPhrase.
   */
  @Test
  public void testCompareTo() {
    System.out.println("compareTo");
    AnnotatedPhrase phrs2 = null;
    src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase instance =
        null;
    int expResult = 0;
    int result = instance.compareTo(phrs2);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of equals method, of class AnnotatedPhrase.
   */
  @Test
  public void testEquals() {
    System.out.println("equals");
    Object obj = null;
    src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase instance =
        null;
    boolean expResult = false;
    boolean result = instance.equals(obj);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getAnnotatorID method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetAnnotatorID() {
    System.out.println("getAnnotatorID");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getAnnotatorID();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getConcept method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetConcept() {
    System.out.println("getConcept");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getConceptName();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getDocumentOfOrigin method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetDocumentOfOrigin() {
    System.out.println("getDocumentOfOrigin");
    AnnotatedPhrase instance = null;
    AnnotatedPhrase expResult = null;
    RaptatDocument result = instance.getDocumentOfOrigin();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getIndexInDocument method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetIndexInDocument() {
    System.out.println("getIndexInDocument");
    AnnotatedPhrase instance = null;
    int expResult = 0;
    int result = instance.getIndexInDocument();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getMentionId method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetMentionId() {
    System.out.println("getMentionId");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getMentionId();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getPhrase method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetPhrase() {
    System.out.println("getPhrase");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getPhraseStringUnprocessed();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getPhraseAttributes method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetPhraseAttributes() {
    System.out.println("getPhraseAttributes");
    AnnotatedPhrase instance = null;
    HashSet<String> expResult = null;
    List<RaptatAttribute> result = instance.getPhraseAttributes();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getProcessedEndOffset method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetProcessedEndOffset() {
    System.out.println("getProcessedEndOffset");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getProcessedTokensEndOffset();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getProcessedStartOffset method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetProcessedStartOffset() {
    System.out.println("getProcessedStartOffset");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getProcessedTokensStartOffset();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getProcessedTokens method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetProcessedTokens() {
    System.out.println("getProcessedTokens");
    AnnotatedPhrase instance = null;
    List<RaptatToken> expResult = null;
    List<RaptatToken> result = instance.getProcessedTokens();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getProcessedTokenStrings method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetProcessedTokenStrings() {
    System.out.println("getProcessedTokenStrings");
    AnnotatedPhrase instance = null;
    List<String> expResult = null;
    List<String> result = instance.getProcessedTokensAugmentedStrings();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getProcessedTokenStringsAsSentence method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetProcessedTokenStringsAsSentence() {
    System.out.println("getProcessedTokenStringsAsSentence");
    AnnotatedPhrase instance = null;
    List<String> expResult = null;
    List<String> result = instance.getProcessedTokensAugmentedStringsAsSentence();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawEndOff method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetRawEndOff() {
    System.out.println("getRawEndOff");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getRawTokensEndOff();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawStartOff method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetRawStartOff() {
    System.out.println("getRawStartOff");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.getRawTokensStartOff();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawTokenNumber method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetRawTokenNumber() {
    System.out.println("getRawTokenNumber");
    AnnotatedPhrase instance = null;
    int expResult = 0;
    int result = instance.getRawTokenNumber();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getRawTokens method, of class AnnotatedPhrase.
   */
  @Test
  public void testGetRawTokens() {
    System.out.println("getRawTokens");
    AnnotatedPhrase instance = null;
    List<RaptatToken> expResult = null;
    List<RaptatToken> result = instance.getRawTokens();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of hashCode method, of class AnnotatedPhrase.
   */
  @Test
  public void testHashCode() {
    System.out.println("hashCode");
    AnnotatedPhrase instance = null;
    int expResult = 0;
    int result = instance.hashCode();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of print method, of class AnnotatedPhrase.
   */
  @Test
  public void testPrint() {
    System.out.println("print");
    AnnotatedPhrase instance = null;
    instance.print();
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setAnnotatorID method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetAnnotatorID() {
    System.out.println("setAnnotatorID");
    String id = "";
    AnnotatedPhrase instance = null;
    instance.setAnnotatorID(id);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setConcept method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetConcept() {
    System.out.println("setConcept");
    String concept = "";
    AnnotatedPhrase instance = null;
    instance.setConceptName(concept);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setIndexInDocument method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetIndexInDocument() {
    System.out.println("setIndexInDocument");
    int indexInDocument = 0;
    AnnotatedPhrase instance = null;
    instance.setIndexInDocument(indexInDocument);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setMentionId method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetMentionId() {
    System.out.println("setMentionId");
    String id = "";
    AnnotatedPhrase instance = null;
    instance.setMentionId(id);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setPhrase method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetPhrase() {
    System.out.println("setPhrase");
    String phrase = "";
    AnnotatedPhrase instance = null;
    instance.setPhraseStringUnprocessed(phrase);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessedEndOffset method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetProcessedEndOffset() {
    System.out.println("setProcessedEndOffset");
    String processedEndOffset = "";
    AnnotatedPhrase instance = null;
    instance.setProcessedEndOffset(processedEndOffset);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessedStartOffset method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetProcessedStartOffset() {
    System.out.println("setProcessedStartOffset");
    String processedStartOffset = "";
    AnnotatedPhrase instance = null;
    instance.setProcessedStartOffset(processedStartOffset);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setProcessedTokens method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetProcessedTokens() {
    System.out.println("setProcessedTokens");
    List<RaptatToken> newTokens = null;
    AnnotatedPhrase instance = null;
    instance.setProcessedTokens(newTokens);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setRawEndOff method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetRawEndOff() {
    System.out.println("setRawEndOff");
    String offset = "";
    AnnotatedPhrase instance = null;
    instance.setRawEndOff(offset);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setRawStartOff method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetRawStartOff() {
    System.out.println("setRawStartOff");
    String offset = "";
    AnnotatedPhrase instance = null;
    instance.setRawStartOff(offset);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setRawTokenNumber method, of class AnnotatedPhrase.
   */
  @Test
  public void testSetRawTokenNumber() {
    System.out.println("setRawTokenNumber");
    int numberOfTokens = 0;
    AnnotatedPhrase instance = null;
    instance.setRawTokenNumber(numberOfTokens);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of toString method, of class AnnotatedPhrase.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    AnnotatedPhrase instance = null;
    String expResult = "";
    String result = instance.toString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
