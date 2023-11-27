package src.main.gov.va.vha09.grecc.raptat.gg.importer;

import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.AnnotationImportExportConfiguration;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.AnnotationImportExportConfiguration.From;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.IAnnotatorParameterObject;

/**
 * The Class Importer.
 */
public abstract class Importer {

  /**
   * Gets the batch.
   *
   * @param parameters the parameters
   * @return the batch
   */
  public abstract List<DataStorer> getBatch(IAnnotatorParameterObject parameters);


  /**
   * Initializes the.
   *
   * @return the importer
   */
  public static Importer init(/* AnnotatorParameterObject parameters */) {
    Importer importer = null;
    if (AnnotationImportExportConfiguration.read_from() == From.Database) {
      importer = new SQLImporter();
    } else {
      importer = new FileSystemImporter();
    }
    return importer;
  }
}
