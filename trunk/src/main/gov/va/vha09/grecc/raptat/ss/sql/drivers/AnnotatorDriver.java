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
 * The Class AnnotatorDriver.
 *
 * @author VHATVHSAHAS1
 */
public class AnnotatorDriver extends DriverBase {

  /**
   * Instantiates a new annotator driver.
   *
   * @param driver the driver
   */
  public AnnotatorDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the annotator.
   *
   * @param annotatorName the annotator name
   * @param schema the schema
   * @return the string
   * @throws NoConnectionException the no connection exception
   */
  public String addAnnotator(String annotatorName, String schema) throws NoConnectionException {
    String cols = "annotator_name";

    List<String> line = new ArrayList<String>();
    line.add("'" + annotatorName + "'");

    getDriver().executeInsert(cols, line, schema, "annotator");

    return getDriver().getLastRowAttributeID(schema, "annotator");
  }


  /**
   * Annotator exists.
   *
   * @param annotatorName the annotator name
   * @param schema the schema
   * @return the string
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public String annotatorExists(String annotatorName, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    String id = "";

    try {
      String query = "select id_annotator as id from [" + schema + "].[annotator] "
          + "where annotator_name = '" + annotatorName + "'";

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getString("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No annotator: " + annotatorName);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Gets the annotator ID.
   *
   * @param annotator the annotator
   * @param schema the schema
   * @return the annotator ID
   * @throws NoConnectionException the no connection exception
   */
  public String getAnnotatorID(String annotator, String schema) throws NoConnectionException {
    String id;

    try {
      id = annotatorExists(annotator, schema);
    } catch (NoDataException ex) {
      id = addAnnotator(annotator, schema);
    }

    return id;
  }
}
