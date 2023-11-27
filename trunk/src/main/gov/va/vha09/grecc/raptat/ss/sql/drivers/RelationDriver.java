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
 * The Class RelationDriver.
 *
 * @author VHATVHSAHAS1
 */
public class RelationDriver extends DriverBase {

  /**
   * Instantiates a new relation driver.
   *
   * @param driver the driver
   */
  public RelationDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the relation.
   *
   * @param relationName the relation name
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addRelation(String relationName, String schema) throws NoConnectionException {
    String cols = "relation_name";

    List<String> line = new ArrayList<String>();
    line.add("'" + relationName + "'");

    getDriver().executeInsert(cols, line, schema, "relation");

    return getDriver().getLastRowID(schema, "relation");
  }


  /**
   * Gets the relation ID.
   *
   * @param relationName the relation name
   * @param schema the schema
   * @return the relation ID
   * @throws NoConnectionException the no connection exception
   */
  public long getRelationID(String relationName, String schema) throws NoConnectionException {
    long id;

    try {
      id = relationExists(relationName, schema);
    } catch (NoDataException ex) {
      id = addRelation(relationName, schema);
    }

    return id;
  }


  /**
   * Relation exists.
   *
   * @param relationName the relation name
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long relationExists(String relationName, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_relation as id from [" + schema + "].[relation] "
          + "where relation_name = '" + relationName + "'";

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No relation: " + relationName);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }
}
