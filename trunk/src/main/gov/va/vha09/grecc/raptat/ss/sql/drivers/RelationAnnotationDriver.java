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

// TODO: Auto-generated Javadoc
/**
 * The Class RelationAnnotationDriver.
 *
 * @author VHATVHSAHAS1
 */
public class RelationAnnotationDriver extends DriverBase {

  /** The relation driver. */
  private RelationDriver __relation_driver;

  /**
   * Instantiates a new relation annotation driver.
   *
   * @param driver the driver
   */
  public RelationAnnotationDriver(ISQLDriver driver) {
    super(driver);

    this.__relation_driver = new RelationDriver(driver);
  }


  /**
   * Adds the relation annotation.
   *
   * @param relationName the relation name
   * @param idAnnotation the id annotation
   * @param sequence the sequence
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addRelationAnnotation(String relationName, long idAnnotation, int sequence,
      String schema) throws NoConnectionException {
    String cols = "id_relation, id_annotation, annotation_sequence";
    long idRelation;

    idRelation = this.__relation_driver.getRelationID(relationName, schema);

    List<String> line = new ArrayList<String>();
    line.add(idRelation + ", " + idAnnotation + ", '" + sequence + "'");

    getDriver().executeInsert(cols, line, schema, "relation_concept_attribute");

    return getDriver().getLastRowID(schema, "relation_concept_attribute");
  }


  /**
   * Relation annotation exists.
   *
   * @param relationName the relation name
   * @param idAnnotation the id annotation
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long relationAnnotationExists(String relationName, long idAnnotation, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;
    long idRelation;

    idRelation = this.__relation_driver.relationExists(relationName, schema);

    try {
      String query = "select id_rel_ann as id from [" + schema + "].[relation_relation_annotation] "
          + "where id_relation = '" + idRelation + "'" + " and id_annotation = '" + idAnnotation
          + "'";

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No value");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }
}
