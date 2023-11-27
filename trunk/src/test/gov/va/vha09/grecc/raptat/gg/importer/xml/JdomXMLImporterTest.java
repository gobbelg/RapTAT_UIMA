/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.importer.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.JdomXMLImporter;

/**
 *
 * @author VHATVHSAHAS1
 */
public class JdomXMLImporterTest {

  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}


  public JdomXMLImporterTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of getKnowtXmlSource method, of class JdomXMLImporter.
   */
  @Test
  public void testGetKnowtXmlSource_File() {
    System.out.println("getKnowtXmlSource");
    File theFile = null;
    String expResult = "";
    String result = JdomXMLImporter.getTextSource(theFile);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getKnowtXmlSource method, of class JdomXMLImporter.
   */
  @Test
  public void testGetKnowtXmlSource_String() {
    System.out.println("getKnowtXmlSource");
    String FilePath = "";
    String expResult = "";
    String result = JdomXMLImporter.getTextSource(FilePath);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
