package src.main.gov.va.vha09.grecc.raptat.gg.importer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.IAnnotatorParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.DbConfigFileReader;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Class SQLImporter.
 *
 * @author westerd
 */
public class SQLImporter extends Importer {

  /**
   * Instantiates a new SQL importer.
   */
  public SQLImporter() {

  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.importer.Importer#getBatch(src.main
   * .gov.va.vha09.grecc. raptat.gg.core.annotation.AnnotatorParameterObject)
   */
  @Override
  public List<DataStorer> getBatch(IAnnotatorParameterObject parameters) {

    List<DataStorer> listOfInputDocuments = new ArrayList<DataStorer>();
    DbConfigFileReader configFileReader = new DbConfigFileReader();

    try (ResultSet result = configFileReader.readTextFileFromDb()) {
      while (result.next()) {
        String docUniqueId = result.getString(configFileReader.getdocumentUniqueIdColumn());
        String docText = result.getString(configFileReader.getTextColumn());
        listOfInputDocuments.add(new SqlDataStorer(docUniqueId, docText));
      }
    } catch (SQLException | IOException | NoConnectionException | SQLDriverException e) {
      e.printStackTrace();
    }

    return listOfInputDocuments;
  }
}
