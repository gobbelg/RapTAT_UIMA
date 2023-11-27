/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class ConceptDriver.
 *
 * @author VHATVHSAHAS1
 */
public class ConceptDriver extends DriverBase {

  /**
   * Instantiates a new concept driver.
   *
   * @param driver the driver
   */
  public ConceptDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the concept.
   *
   * @param conceptName the concept name
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addConcept(String conceptName, long schemaID, String schema)
      throws NoConnectionException {
    String columns = "concept_name, id_schema";

    try {
      String query = "insert into [" + schema + "].[concept] (" + columns + " ) values (?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setString(1, conceptName);
      ps.setLong(2, schemaID);

      ps.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }

    return getDriver().getLastRowID(schema, "concept");
  }


  /**
   * Adds the concept attribute.
   *
   * @param attributeID the attribute ID
   * @param conceptID the concept ID
   * @param schema the schema
   * @throws NoConnectionException the no connection exception
   */
  public void addConceptAttribute(long attributeID, long conceptID, String schema)
      throws NoConnectionException {
    String columns = "id_attribute, id_concept";

    try {
      String query = "insert into [" + schema + "].[rel_con_attr] (" + columns + " ) values (?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setLong(1, attributeID);
      ps.setLong(2, conceptID);

      ps.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  /**
   * Concept exists.
   *
   * @param conceptName the concept name
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long conceptExists(String conceptName, long schemaID, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_concept as id from [" + schema + "].[concept] "
          + "where concept_name = '" + conceptName + "'" + " AND id_schema = " + schemaID;

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        id = rs.getLong("id");
      } else {
        throw new NoDataException("No concept: " + conceptName);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Gets the concept ID.
   *
   * @param concept the concept
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the concept ID
   * @throws NoConnectionException the no connection exception
   */
  public long getConceptID(String concept, long schemaID, String schema)
      throws NoConnectionException {
    long id;

    try {
      id = conceptExists(concept, schemaID, schema);
    } catch (NoDataException ex) {
      id = addConcept(concept, schemaID, schema);
    }

    return id;
  }
}
