/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.core.annotation;

import java.util.Optional;
import org.apache.commons.lang3.builder.ToStringBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.RaptatStringStyle;

/**
 * @author Glenn Gobbel
 *
 */
public class AnnotatorParameterObject implements IAnnotatorParameterObject {
  protected final String solutionFilePath;

  /*
   * This is optional because, when annotating documents from a database, there may not be a file
   * path to a textDirectory.
   *
   * This potential inconsistency will need to be addressed, probably through a class that allow
   * either specification of a file directory or a database
   */
  public final Optional<String> textDirectoryPath;

  public final String exportDirectoryPath;

  public final String outputConceptsFilePath;

  public final AnnotationApp annotationApp;

  public final boolean isBatchDirectory;

  public String fileMapPath = "";

  public String schemaName = "";

  public AnnotatorParameterObject(String solutionFilePath, String textDirectoryPath,
      boolean isBatchDirectory, String exportDirectoryPath, String outputConceptsFilePath,
      AnnotationApp annotationApp) {
    super();
    this.solutionFilePath = solutionFilePath;
    this.textDirectoryPath = Optional.ofNullable(textDirectoryPath);
    this.isBatchDirectory = isBatchDirectory;
    this.exportDirectoryPath = exportDirectoryPath;
    this.outputConceptsFilePath = outputConceptsFilePath;
    this.annotationApp = annotationApp;
  }


  @Override
  public String get_schema_file_path() {
    /*
     * DAX - 4/30/20 - Added to conform with interface
     */
    return null;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getAnnotationApp()
   */
  @Override
  public AnnotationApp getAnnotationApp() {
    return annotationApp;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getExportDirectoryPath()
   */
  @Override
  public String getExportDirectoryPath() {
    return exportDirectoryPath;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getFileMapPath()
   */
  @Override
  public String getFileMapPath() {
    return fileMapPath;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getConceptsFilePath()
   */
  @Override
  public String getOutputConceptsFilePath() {
    return outputConceptsFilePath;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getSchemaName()
   */
  @Override
  public String getSchemaName() {
    return schemaName;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getSolutionFilePath()
   */
  @Override
  public String getSolutionFilePath() {
    return solutionFilePath;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#getTextDirectoryPath()
   */
  @Override
  public Optional<String> getTextDirectoryPath() {
    return textDirectoryPath;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#isBatchDirectory()
   */
  @Override
  public boolean isBatchDirectory() {
    return isBatchDirectory;
  }


  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.
   * IAnnotatorParameterObject#toString()
   */
  @Override
  public String toString() {
    ToStringBuilder.setDefaultStyle(RaptatStringStyle.getInstance());

    ToStringBuilder sb = new ToStringBuilder(this).append("solutionFilePath", solutionFilePath)
        .append("textDirectoryPath", textDirectoryPath.get())
        .append("isBatchDirectory", isBatchDirectory)
        .append("exportDirectoryPath", exportDirectoryPath)
        .append("outputConceptsFilePath", outputConceptsFilePath)
        .append("annotationApp", annotationApp);

    return sb.toString();
  }
}
