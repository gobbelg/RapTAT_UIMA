package src.main.gov.va.vha09.grecc.raptat.gg.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.IAnnotatorParameterObject;

/**
 * The Class FileSystemImporter.
 */
public class FileSystemImporter extends Importer {

  /**
   * Instantiates a new file system importer.
   */
  public FileSystemImporter() {}


  /**
   * Gets the batch.
   *
   * @param parameters the parameters
   * @return the batch
   */
  @Override
  public List<DataStorer> getBatch(IAnnotatorParameterObject parameters) {
    Iterator<File> fileDirectoryIterator = getTextDirectoryIterator(
        parameters.getTextDirectoryPath().get(), parameters.isBatchDirectory());
    List<DataStorer> listOfInputDocuments = new ArrayList<DataStorer>();
    while (fileDirectoryIterator.hasNext()) {
      File textDirectory = fileDirectoryIterator.next();
      getTextFiles(textDirectory, listOfInputDocuments);

    }
    return listOfInputDocuments;
  }


  /**
   * Gets the text directory iterator.
   *
   * @param textDirectoryPath the text directory path
   * @param isBatchDirectory the is batch directory
   * @return the text directory iterator
   */
  private Iterator<File> getTextDirectoryIterator(String textDirectoryPath,
      boolean isBatchDirectory) {
    List<File> fileList = new ArrayList<File>();
    if (!isBatchDirectory) {
      fileList.add(new File(textDirectoryPath));
    } else {
      BufferedReader reader = null;
      try {
        InputStream filePathStream = new FileInputStream(new File(textDirectoryPath));
        reader = new BufferedReader(new InputStreamReader(filePathStream));

        String line;

        while ((line = reader.readLine()) != null) {
          if (!(line == null) && !line.isEmpty()) {
            fileList.add(new File(line));
          }
        }
      } catch (IOException e) {
        System.err.println("Unable to read " + textDirectoryPath + e.getLocalizedMessage());
        e.printStackTrace();
        System.exit(-1);
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

    }

    return fileList.iterator();
  }


  /**
   * Gets the text files.
   *
   * @param textFileDirectory the text file directory
   * @param listOfDataStorers the list of data storers
   * @return the text files
   */
  private void getTextFiles(File textFileDirectory, List<DataStorer> listOfDataStorers) {

    Collection<File> files = FileUtils.listFiles(textFileDirectory, new String[] {"txt"}, false);

    for (File curFile : files) {
      listOfDataStorers.add(new FileSystemDataStorer(curFile.getAbsolutePath()));
    }
  }

}
