/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class AnnotationDriver.
 *
 * @author VHATVHSAHAS1
 */
public class AnnotationDriver extends DriverBase {

  /**
   * Instantiates a new annotation driver.
   *
   * @param driver the driver
   */
  public AnnotationDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the annotation.
   *
   * @param annotatorID the annotator ID
   * @param conceptID the concept ID
   * @param xmlAnnotationID the xml annotation ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  @Deprecated
  public long addAnnotation(String annotatorID, long conceptID, String xmlAnnotationID,
      String schema) throws NoConnectionException {
    String cols = "id_annotator, id_concept, xml_annotation_id ";

    List<String> line = new ArrayList<String>();
    line.add(annotatorID + "," + conceptID + ", '" + xmlAnnotationID + "'");

    getDriver().executeInsert(cols, line, schema, "annotation");

    return getDriver().getLastRowID(schema, "annotation");
  }


  /**
   * Adds the annotation.
   *
   * @param annotatorID the annotator ID
   * @param conceptID the concept ID
   * @param xmlAnnotationID the xml annotation ID
   * @param schema the schema
   * @param annotationCreationDate the annotation creation date
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addAnnotation(String annotatorID, long conceptID, String xmlAnnotationID,
      String schema, String annotationCreationDate) throws NoConnectionException {

    String cols = "id_annotator, id_concept, xml_annotation_id, annotation_creation_date ";

    List<String> line = new ArrayList<String>();
    line.add(annotatorID + "," + conceptID + ", '" + xmlAnnotationID + "'" + ", '"
        + annotationCreationDate + "'");

    getDriver().executeInsert(cols, line, schema, "annotation");

    return getDriver().getLastRowID(schema, "annotation");
  }


  /**
   * Adds the annotation attribute.
   *
   * @param annID the ann ID
   * @param attrAttrValID the attr attr val ID
   * @param xmlAttributeID the xml attribute ID
   * @param schema the schema
   * @throws NoConnectionException the no connection exception
   */
  public void addAnnotationAttribute(long annID, long attrAttrValID, String xmlAttributeID,
      String schema) throws NoConnectionException {
    String cols = "id_annotation, id_attr_attrval, xml_attribute_id";

    List<String> line = new ArrayList<String>();
    line.add(annID + "," + attrAttrValID + ", '" + xmlAttributeID + "'");

    getDriver().executeInsert(cols, line, schema, "rel_ann_attr");
  }


  /**
   * Annotation exists.
   *
   * @param annotatorID the annotator ID
   * @param conceptID the concept ID
   * @param xmlAnnotationID the xml annotation ID
   * @param schema the schema
   * @return true, if successful
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public boolean annotationExists(long annotatorID, long conceptID, String xmlAnnotationID,
      String schema) throws NoConnectionException, NoDataException {

    try {
      String query = "select id_annotation as id from [" + schema + "].[annotation] "
          + "where id_annotation = '" + annotatorID + "'" + "and id_concept = '" + conceptID + "'"
          + "and xml_annotation_id = '" + xmlAnnotationID + "'";

      ResultSet rs = getDriver().executeSelect(query);

      if (rs.next()) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return false;
  }
}
