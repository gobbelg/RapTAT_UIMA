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
 * The Class AttrValueDriver.
 *
 * @author VHATVHSAHAS1
 */
public class AttrValueDriver extends DriverBase {

  /**
   * Instantiates a new attr value driver.
   *
   * @param driver the driver
   */
  public AttrValueDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the value.
   *
   * @param value the value
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addValue(String value, String schema) throws NoConnectionException {
    String cols = "attribute_value";

    List<String> line = new ArrayList<String>();
    line.add("'" + value + "'");

    getDriver().executeInsert(cols, line, schema, "attr_value");

    return getDriver().getLastRowID(schema, "attr_value");
  }


  /**
   * Gets the attr value ID.
   *
   * @param attribute the attribute
   * @param schema the schema
   * @return the attr value ID
   * @throws NoConnectionException the no connection exception
   */
  public long getAttrValueID(String attribute, String schema) throws NoConnectionException {
    long id;

    try {
      id = valueExists(attribute, schema);
    } catch (NoDataException ex) {
      id = addValue(attribute, schema);
    }

    return id;
  }


  /**
   * Value exists.
   *
   * @param value the value
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long valueExists(String value, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_value as id from [" + schema + "].[attr_value] "
          + "where attribute_value = '" + value + "'";

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        id = rs.getLong("id");
      } else {
        id = addValue(value, schema);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }
}
