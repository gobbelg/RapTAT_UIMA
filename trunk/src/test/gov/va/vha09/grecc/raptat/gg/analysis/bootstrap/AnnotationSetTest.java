/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap;

import static org.junit.Assert.assertNotNull;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.ConceptMapBootstrapSampler;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImportReader;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImporter;

/**
 * @author jayaras
 *
 */
public class AnnotationSetTest {

  /*
   *//**
       * Test method for
       * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getAllAnnotationsAsMap()}
       * .
       */
  /*
   * @Test public void testGetAllAnnotationsAsMap() { fail( "Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getUniquePatientIds()}
      * .
      */
  /*
   * @Test public void testGetUniquePatientIds() { fail("Not yet implemented" ); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getUniqueDocIds()}
      * .
      */
  /*
   * @Test public void testGetUniqueDocIds() { fail("Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getAllPatientIds()}
      * .
      */
  /*
   * @Test public void testGetAllPatientIds() { fail("Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getUniquePatientCount()}
      * .
      */
  /*
   * @Test public void testGetUniquePatientCount() { fail( "Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#getUniqueDocumentCount()}
      * .
      */
  /*
   * @Test public void testGetUniqueDocumentCount() { fail( "Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.AnnotationSet#allRecordsFromDoc(java.lang.String)}
      * .
      */
  /*
   * @Test public void testAllRecordsFromDoc() { fail("Not yet implemented"); }
   *//**
      * Test method for
      * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.ConceptMapBootstrapSampler#allRecordsFromPatient(java.lang.String)}
      * .
      */
  /*
   * @Test public void testAllRecordsFromPatient() { fail( "Not yet implemented"); }
   */

  public void printRandomSet(List<String[]> data) {
    String tmpstr = "";
    Iterator<String[]> it = data.iterator();
    while (it.hasNext()) {
      String[] temp = it.next();

      for (int i = 0; i < temp.length; i++) {
        tmpstr = tmpstr + " " + temp[i];
      }
      System.out.println(tmpstr);
      tmpstr = "";
    }

  }


  /**
   * Test method for
   * {@link src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.ConceptMapBootstrapSampler#AnnotationSet(src.main.gov.va.vha09.grecc.raptat.gg.importer.DataImportReader)}
   * .
   */
  @Test
  public void testAnnotationSetDataImportReader() {
    DataImporter testImporter = new DataImporter("Open a file");
    DataImportReader testReader = testImporter.getReader();
    try {

      ConceptMapBootstrapSampler set = new ConceptMapBootstrapSampler(testReader);
      assertNotNull(set);
      System.out.println("Original data read from csv file: ");
      set.printAnnotationSet();
      System.out.println("Size of the annotation list: " + set.getSize());
      // System.out.println("Number of Documents used:
      // "+set.getUniqueDocumentCount());
      // System.out.println("All records: ");
      // set.printMapData(set.getAllAnnotationsAsMap());
      // System.out.println("All records from the Document with id=HRGIS:
      // ");
      // set.printVectorData(set.allRecordsFromDoc("HRGIS"));
      // System.out.println("Selected records from all Patients: ");
      // set.printMapData(set.patientsMap);
      // System.out.println("Length of the longest phrase in the set:
      // "+set.getMaxPhraseLength());

      // System.out.println("All records from the Patient with id=77899:
      // ");
      // set.printVectorData(set.allRecordsFromPatient("77899"));

      // System.out.println("Phrases and Concepts: ");
      // set.printVectorData(set.getPhraseAndConcept(2));

      // set.printMapData(set.documentsMap);
    } catch (Exception e) {
      System.out.println("testAnnotationSetDataImportReader() failed");
    }

    /*
     * @Test public void testGetAllAnnotationsAsMap() { fail( "Not yet implemented"); }
     * 
     * @Test public void testCreateAnnotationSet() { fail( "Not yet implemented"); }
     */
  }

}
