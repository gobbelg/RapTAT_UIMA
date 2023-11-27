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
 * The Class AttributeDriver.
 *
 * @author VHATVHSAHAS1
 */
public class AttributeDriver extends DriverBase {

  /**
   * Instantiates a new attribute driver.
   *
   * @param driver the driver
   */
  public AttributeDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the attribute.
   *
   * @param attributeName the attribute name
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addAttribute(String attributeName, String schema) throws NoConnectionException {
    String cols = "attribute_name";

    List<String> line = new ArrayList<String>();
    line.add("'" + attributeName + "'");

    getDriver().executeInsert(cols, line, schema, "attribute");

    return getDriver().getLastRowID(schema, "attribute");
  }


  /**
   * Adds the attribute values.
   *
   * @param attributeID the attribute ID
   * @param valueID the value ID
   * @param schemaID the schema ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  @Deprecated
  public long addAttributeValues(long attributeID, long valueID, long schemaID, String schema)
      throws NoConnectionException {
    String cols = "id_attribute, id_value";

    List<String> line = new ArrayList<String>();
    line.add(attributeID + ", " + valueID);

    getDriver().executeInsert(cols, line, schema, "rel_attr_attrval");

    return getDriver().getLastRowID(schema, "rel_attr_attrval");
  }


  /**
   * Adds the attribute values.
   *
   * @param attributeID the attribute ID
   * @param valueID the value ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addAttributeValues(long attributeID, long valueID, String schema)
      throws NoConnectionException {
    String cols = "id_attribute, id_value";

    List<String> line = new ArrayList<String>();
    line.add(attributeID + ", " + valueID);

    getDriver().executeInsert(cols, line, schema, "rel_attr_attrval");

    return getDriver().getLastRowID(schema, "rel_attr_attrval");
  }


  /**
   * Attr attr value exists.
   *
   * @param attributeID the attribute ID
   * @param valueID the value ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long attrAttrValueExists(long attributeID, long valueID, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_attr_attrval as id from [" + schema + "].[rel_attr_attrval] "
          + "where id_attribute = " + attributeID + " " + "AND id_value = " + valueID;

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        id = rs.getLong("id");
      } else {
        id = this.addAttributeValues(attributeID, valueID, schema);;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Attribute exists.
   *
   * @param attributeName the attribute name
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long attributeExists(String attributeName, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_attribute as id from [" + schema + "].[attribute] "
          + "where attribute_name = '" + attributeName + "'";

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        id = rs.getLong("id");
      } else {
        id = addAttribute(attributeName, schema);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Gets the attribute ID.
   *
   * @param attribute the attribute
   * @param schema the schema
   * @return the attribute ID
   * @throws NoConnectionException the no connection exception
   */
  public long getAttributeID(String attribute, String schema) throws NoConnectionException {
    long id;

    try {
      id = attributeExists(attribute, schema);
    } catch (NoDataException ex) {
      id = addAttribute(attribute, schema);
    }

    return id;
  }
}
