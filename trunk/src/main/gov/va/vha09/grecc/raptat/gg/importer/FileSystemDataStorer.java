package src.main.gov.va.vha09.grecc.raptat.gg.importer;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * The Class FileSystemDataStorer.
 */
public class FileSystemDataStorer extends DataStorer {

  /** The file path. */
  String filePath;

  /**
   * Instantiates a new file system data storer.
   *
   * @param absolutePath the absolute path
   */
  public FileSystemDataStorer(String absolutePath) {
    this.filePath = absolutePath;
  }


  /**
   * Gets the document text.
   *
   * @return the document text
   */
  @Override
  public String getDocumentText() {
    String inputText = "";
    try {
      inputText = FileUtils.readFileToString(new File(this.filePath), "UTF-8");
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return inputText;
  }


  /**
   * Gets the document unique id.
   *
   * @return the document unique id
   */
  @Override
  public String getUniqueId() {
    return this.filePath;
  }

}
