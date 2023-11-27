/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class DocumentDriver.
 *
 * @author VHATVHSAHAS1
 */
/**
 * @author westerd
 *
 */
public class DocumentDriver extends DriverBase {

  /**
   * Instantiates a new document driver.
   *
   * @param driver the driver
   */
  public DocumentDriver(ISQLDriver driver) {
    super(driver);
  }



  /**
   * Adds the document.
   *
   * @param inputStream the f stream
   * @param sourceName the doc source
   * @param sourceID the source ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   */
  public long addDocument(InputStream inputStream, String sourceID, String sourceName,
      String schema) throws NoConnectionException {

    try {
      String columns = "document_blob, document_source, doc_source_id ";
      String query = "insert into [" + schema + "].[document] (" + columns + " ) values (?,?,?);";

      PreparedStatement ps = getDriver().prepareStatement(query);
      ps.setBinaryStream(1, inputStream);
      ps.setString(2, sourceName);
      ps.setString(3, sourceID);

      ps.executeUpdate();

    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }
    return getDriver().getLastRowID(schema, "document");
  }


  /**
   * Document exists.
   *
   * @param sourceID the source ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  @Deprecated
  public long documentExists(long sourceID, String schema)
      throws NoConnectionException, NoDataException {

    ResultSet rs;
    long id = 0;

    try {
      String query = "select id_document as id from [" + schema + "].[document] "
          + "where doc_source_id = " + sourceID;

      rs = getDriver().executeSelect(query);

      if (rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("doc does not exits");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Document exists.
   *
   * @param documentSourceId the source ID
   * @param schema the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long documentExists(String documentSourceId, String schema)
      throws NoConnectionException, NoDataException {

    long id = 0;

    String query = "select id_document as id from [" + schema + "].[document] "
        + "where doc_source_id = " + documentSourceId;

    try (ResultSet rs = getDriver().executeSelect(query)) {

      if (rs.next()) {
        id = rs.getLong("id");
      }
    } catch (SQLException e) {

      e.printStackTrace();
    }

    return id;
  }



  /**
   * Gets the document ID when the file exists and can be accessed through the fileStream parameter.
   *
   * @param is an InputStream instance to read the document from
   * @param documentIdentifier the doc source
   * @param sourceID the source ID
   * @param dbSchemaName the schema
   * @return the document ID
   * @throws NoConnectionException the no connection exception
   */
  public long getDocumentID(InputStream inputStream, String documentIdentifier, String documentName,
      String dbSchemaName) throws NoConnectionException {
    long id = 0;

    try {
      id = this.documentExists(documentIdentifier, dbSchemaName);
    } catch (NoDataException e) {
      e.printStackTrace();
    }

    if (id == 0) {
      id = this.addDocument(inputStream, documentName, documentIdentifier, dbSchemaName);
    }

    return id;
  }


}
