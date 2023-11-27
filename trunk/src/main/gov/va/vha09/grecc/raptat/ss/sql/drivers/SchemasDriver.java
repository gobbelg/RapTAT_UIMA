/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class SchemasDriver.
 *
 * @author VHATVHSAHAS1
 */
public class SchemasDriver extends DriverBase {

  /**
   * Instantiates a new schemas driver.
   *
   * @param driver the driver
   */
  public SchemasDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the schema.
   *
   * @param fStream the f stream
   * @param schemaType the schema type
   * @param filePath the file path
   * @param fileHash the file hash
   * @param schema the schema
   * @return A unique ID for the schema as a long integer
   * @throws NoConnectionException the no connection exception
   */
  public long addSchemaFileToDb(FileInputStream fStream, String schemaType, String filePath,
      String fileHash, String schema) throws NoConnectionException {

    ISQLDriver sqlDriver = null;
    try {

      String query = String.format(
          "insert into [%s].[schemas] ( schema_file, schema_type, file_path, file_hash ) values (?,?,?,?);",
          schema);

      sqlDriver = getDriver();
      PreparedStatement ps = sqlDriver.prepareStatement(query);
      ps.setBinaryStream(1, fStream);
      ps.setString(2, schemaType);
      ps.setString(3, filePath);
      ps.setString(4, fileHash);

      ps.executeUpdate();

    } catch (SQLException ex) {
      Logger.getLogger(SchemasDriver.class.getName()).log(Level.SEVERE, null, ex);
    }

    return sqlDriver.getLastRowID(schema, "schemas");
  }


  /**
   * Gets the schema ID.
   *
   * @param fStream the f stream
   * @param annotationAppName the schema source
   * @param fileName the file name
   * @param fileHash the file hash
   * @param annotationSchemaInDb the schema exists
   * @param sqlSchemaName the schema
   * @return the schema ID
   * @throws NoConnectionException the no connection exception
   */
  public long getAnnotationSchemaID(FileInputStream fStream, String annotationAppName,
      String pathToAnnotationSchemaFile, String fileHash, AtomicBoolean annotationSchemaInDb,
      String sqlSchemaName) throws NoConnectionException {
    long id;

    try {
      id = schemaExists(fileHash, sqlSchemaName);
      annotationSchemaInDb.set(true);
    } catch (NoDataException ex) {
      id = addSchemaFileToDb(fStream, annotationAppName, pathToAnnotationSchemaFile, fileHash,
          sqlSchemaName);
      annotationSchemaInDb.set(false);
    }

    return id;
  }

  // public void main(String[] args) {
  // FileInputStream fis = null;
  //
  // try {
  // File file = new File("D:\\Saha
  // Workspace\\Test\\config\\projectschema.xml");
  // fis = new FileInputStream(file);
  //
  // long id = SchemasDriver.getSchemaID(fis, "knowtator",
  // "\\projectschema.xml", "dsfs",
  // new AtomicBoolean(false), "");
  //
  // System.out.println(id);
  // } catch (NoConnectionException ex) {
  // ex.printStackTrace();
  // } catch (FileNotFoundException ex) {
  // ex.printStackTrace();
  // } finally {
  // try {
  // fis.close();
  // } catch (IOException ex) {
  // ex.printStackTrace();
  // }
  // }
  // }


  /**
   * Schema exists.
   *
   * @param fileHash the file hash
   * @param sqlSchemaName the schema
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public long schemaExists(String fileHash, String sqlSchemaName)
      throws NoConnectionException, NoDataException {

    long id = 0;

    fileHash = checkAndModifyApostrophe(fileHash);

    String query =
        String.format("select id_schema as id from [%s].[schemas] where file_hash = '%s'",
            sqlSchemaName, fileHash);

    try (ResultSet rs = getDriver().executeSelect(query)) {

      if (rs != null && rs.next()) {
        do {
          id = rs.getLong("id");
        } while (rs.next());
      } else {
        throw new NoDataException("No schema.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return id;
  }


  /**
   * Added by: @gujarkd Purpose: Checks if file hash contains any apostrophes and adds an extra
   * apostrophe after each in order for sql to consider the original apostrophe as the part of the
   * file hash string.
   *
   * @param fileHash the file hash
   * @return the string
   */
  private static String checkAndModifyApostrophe(String fileHash) {
    String modifiedFileHash = "";
    for (char c : fileHash.toCharArray()) {
      if (c == '\'') {
        modifiedFileHash = modifiedFileHash + c + '\'';
      } else {
        modifiedFileHash = modifiedFileHash + c;
      }
    }
    return modifiedFileHash;
  }
}
