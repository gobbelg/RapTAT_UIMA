/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.test.gov.va.vha09.grecc.raptat.gg.core;

import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.core.RapTAT;

/**
 *
 * @author VHATVHSAHAS1
 */
public class RapTATTest {

  @BeforeClass
  public static void setUpClass() {}


  @AfterClass
  public static void tearDownClass() {}


  public RapTATTest() {}


  @Before
  public void setUp() {}


  @After
  public void tearDown() {}


  /**
   * Test of main method, of class RapTAT.
   */
  @Test
  public void testMain() {
    System.out.println("main");
    String[] args = null;
    RapTAT.main(args);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of setBackgroundImage method, of class RapTAT.
   */
  @Test
  public void testSetBackgroundImage() {
    System.out.println("setBackgroundImage");
    String str = "";
    RapTAT instance = new RapTAT();
    instance.setBackgroundImage(str);
    // TODO review the generated test code and remove the default call to
    // fail.
    fail("The test case is a prototype.");
  }

}
