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
 * The Class SentenceDriver.
 *
 * @author VHATVHSAHAS1
 */
public class SentenceDriver extends DriverBase {

  /**
   * Instantiates a new sentence driver.
   *
   * @param driver the driver
   */
  public SentenceDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the sentence.
   *
   * @param docID the doc ID
   * @param text the text
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addSentence(long docID, String text, String schema) throws NoConnectionException {

    String columns = "id_document, sentence_text";

    try {
      String query = "insert into [" + schema + "].[sentence] (" + columns + " ) values (?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setLong(1, docID);
      ps.setString(2, text);

      ps.executeUpdate();

    } catch (SQLException ex) {
      Logger.getLogger(SentenceDriver.class.getName()).log(Level.SEVERE, null, ex);
    }

    return getDriver().getLastRowID(schema, "sentence");
  }


  /**
   * Gets the sentence ID.
   *
   * @param text the text
   * @param docID the doc ID
   * @param schema the schema
   * @return the sentence ID
   * @throws NoConnectionException the no connection exception
   */
  public long getsentenceID(String text, long docID, String schema) throws NoConnectionException {
    long id;

    try {
      id = sentenceExists(docID, text, schema);
    } catch (NoDataException ex) {
      id = addSentence(docID, text, schema);
    }

    return id;
  }


  /**
   * Sentence exists.
   *
   * @param docID the doc ID
   * @param text the text
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long sentenceExists(long docID, String text, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    text = text.replaceAll("\'", "\'\'");

    try {
      String query = "select id_sentence as id from [" + schema + "].[sentence] "
          + "where sentence_text = '" + text + "' " + "AND id_document = " + docID;

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No annotator: " + text);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }
}
