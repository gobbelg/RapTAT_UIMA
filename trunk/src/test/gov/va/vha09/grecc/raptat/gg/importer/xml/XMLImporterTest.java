/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.importer.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;

/**
 *
 * @author VHATVHSAHAS1
 */
public class XMLImporterTest {

  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}


  public XMLImporterTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of conformXMLOffsetsToText method, of class XMLImporter.
   */
  @Test
  public void testConformXMLOffsetsToText() {
    System.out.println("conformXMLOffsetsToText");
    String pathToDocument = "";
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    List<AnnotatedPhrase> expResult = null;
    List<AnnotatedPhrase> result =
        instance.conformXMLOffsetsToText(pathToDocument, Constants.AnnotationApp.EHOST);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of domXML method, of class XMLImporter.
   */
  @Test
  public void testDomXML() throws Exception {
    System.out.println("domXML");
    File file = null;
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    Document expResult = null;
    Document result = instance.domXML(file);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getAllAnnotatedPhrases method, of class XMLImporter.
   */
  @Test
  public void testGetAllAnnotatedPhrases() {
    System.out.println("getAllAnnotatedPhrases");
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    List<AnnotatedPhrase> expResult = null;
    List<AnnotatedPhrase> result = instance.getAllAnnotatedPhrases();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getDoc method, of class XMLImporter.
   */
  @Test
  public void testGetDoc() {
    System.out.println("getDoc");
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    Document expResult = null;
    Document result = instance.getDoc();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getUnmatchedPhrases method, of class XMLImporter.
   */
  @Test
  public void testGetUnmatchedPhrases() {
    System.out.println("getUnmatchedPhrases");
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    List<AnnotatedPhrase> expResult = null;
    List<AnnotatedPhrase> result = instance.getUnmatchedPhrases();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of importAnnotations method, of class XMLImporter.
   */
  @Test
  public void testImportAnnotations() {
    System.out.println("importAnnotations");
    String xmlPath = "";
    String textSourcePath = "";
    HashSet<String> acceptedConcepts = null;
    AnnotationImporter instance = new AnnotationImporter(AnnotationApp.EHOST);
    List<AnnotatedPhrase> expResult = null;
    List<AnnotatedPhrase> result =
        instance.importAnnotations(xmlPath, textSourcePath, acceptedConcepts);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of main method, of class XMLImporter.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    AnnotationImporter.main(args);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
