/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Class LinkDriver.
 *
 * @author VHATVHSAHAS1
 */
public class LinkDriver extends DriverBase {

  /**
   * Instantiates a new link driver.
   *
   * @param driver the driver
   */
  public LinkDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the link.
   *
   * @param sourceAnnID the source ann ID
   * @param targetAnnID the target ann ID
   * @param linkTypeID the link type ID
   * @param xmlRelationID the xml relation ID
   * @param schema the schema
   * @throws NoConnectionException the no connection exception
   */
  public void addLink(long sourceAnnID, long targetAnnID, long linkTypeID, String xmlRelationID,
      String schema) throws NoConnectionException {
    String columns = "id_source_annotation, id_targer_annotation, id_link_type, xml_relation_id";

    try {
      String query = "insert into [" + schema + "].[link] (" + columns + " ) values (?, ?, ?, ?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setLong(1, sourceAnnID);
      ps.setLong(2, targetAnnID);
      ps.setLong(3, linkTypeID);
      ps.setString(4, xmlRelationID);

      ps.executeUpdate();

    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
