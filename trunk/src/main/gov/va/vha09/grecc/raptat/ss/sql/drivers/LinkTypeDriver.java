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
 * The Class LinkTypeDriver.
 *
 * @author VHATVHSAHAS1
 */
public class LinkTypeDriver extends DriverBase {

  /**
   * Instantiates a new link type driver.
   *
   * @param driver the driver
   */
  public LinkTypeDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the link type.
   *
   * @param linkType the link type
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addLinkType(String linkType, long schemaID, String schema)
      throws NoConnectionException {
    String columns = "link_type, id_schema";

    try {
      String query =
          "insert into [" + schema + "].[link_type] " + "(" + columns + " )" + " values (?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setString(1, linkType);
      ps.setLong(2, schemaID);

      ps.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }

    return getDriver().getLastRowID(schema, "link_type");
  }


  /**
   * Adds the information about the concepts involved in a relationship. no 1 denotes the left side
   * concepts and no 2 denotes the right hand side concepts.
   *
   * @param linkTypeID the link type ID
   * @param conceptID the concept ID
   * @param no the no
   * @param schema the schema
   * @throws NoConnectionException the no connection exception
   */
  public void addLinkTypeConcept(long linkTypeID, long conceptID, int no, String schema)
      throws NoConnectionException {
    String columns = "id_link_type, id_concept";

    try {
      String query = "insert into [";

      if (no == 1) {
        query += schema + "].[link_type_concept1] ";
      } else {
        query += schema + "].[link_type_concept2] ";
      }

      query += "(" + columns + " )" + " values (?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setLong(1, linkTypeID);
      ps.setLong(2, conceptID);

      ps.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  /**
   * Gets the link type ID.
   *
   * @param linkType the link type
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the link type ID
   * @throws NoConnectionException the no connection exception
   */
  public long getLinkTypeID(String linkType, long schemaID, String schema)
      throws NoConnectionException {
    long id = -1;

    try {
      id = linkTypeExists(linkType, schemaID, schema);
    } catch (NoDataException ex) {
      id = addLinkType(linkType, schemaID, schema);
    }

    return id;
  }


  /**
   * Link type exists.
   *
   * @param linkType the link type
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long linkTypeExists(String linkType, long schemaID, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_link_type as id from [" + schema + "].[link_type] "
          + "where link_type = '" + linkType + "' " + "AND id_schema = " + schemaID;

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No Link Type exists.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }
}
