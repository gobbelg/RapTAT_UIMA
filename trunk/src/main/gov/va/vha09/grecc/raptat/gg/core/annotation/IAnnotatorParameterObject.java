package src.main.gov.va.vha09.grecc.raptat.gg.core.annotation;

import java.util.Optional;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;


public interface IAnnotatorParameterObject {

  String get_schema_file_path();

  AnnotationApp getAnnotationApp();

  String getExportDirectoryPath();

  String getFileMapPath();

  String getOutputConceptsFilePath();

  String getSchemaName();

  String getSolutionFilePath();

  Optional<String> getTextDirectoryPath();

  boolean isBatchDirectory();

  @Override
  String toString();

}
