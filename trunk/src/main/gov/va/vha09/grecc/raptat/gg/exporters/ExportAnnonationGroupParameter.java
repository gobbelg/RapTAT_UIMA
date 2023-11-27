package src.main.gov.va.vha09.grecc.raptat.gg.exporters;

import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.AnnotationImportExportConfiguration;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.IAnnotatorParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.XmlExportAnnotationGroupParameter;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;

/**
 * The Class ExportAnnonationGroupParameter.
 */
public abstract class ExportAnnonationGroupParameter {

  /** The input group. */
  public AnnotationGroup inputGroup;

  /** The correct offsets. */
  public boolean correctOffsets;

  /** The insert phrase strings. */
  public boolean insertPhraseStrings;



  /**
   * Inits the.
   *
   * @param curAnnotationGroup the cur annotation group
   * @param parameters the parameters
   * @return the export annonation group parameter
   */
  public static ExportAnnonationGroupParameter init(AnnotationGroup curAnnotationGroup,
      IAnnotatorParameterObject parameters) {
    ExportAnnonationGroupParameter exportGroupParameter = null;

    /*
     * Glenn comment 9/24/20. Removed parameters.getTextDirectoryPath as it is provided by
     * curAnnotationGroup
     */
    if (AnnotationImportExportConfiguration.useSqlExporter()) {
      exportGroupParameter = new SqlExportAnnotationGroupParameter(curAnnotationGroup,
          AnnotationImportExportConfiguration.isCorrectOffsets(),
          AnnotationImportExportConfiguration
              .isInsertPhraseStrings()/*
                                       * , parameters.getTextDirectoryPath()
                                       */);

    } else {
      /*
       * Glenn comment 9/24/20. Removed parameters.getTextDirectoryPath as it is provided by
       * curAnnotationGroup
       */
      exportGroupParameter = new XmlExportAnnotationGroupParameter(curAnnotationGroup,
          AnnotationImportExportConfiguration.isCorrectOffsets(),
          AnnotationImportExportConfiguration
              .isInsertPhraseStrings() /*
                                        * , parameters.getTextDirectoryPath()
                                        */);
    }

    return exportGroupParameter;
  }


  /**
   * Inits the.
   *
   * @param curAnnotationGroup the cur annotation group
   * @param textDirectoryPath the text directory path
   * @return the export annotation group parameter
   */
  public static ExportAnnonationGroupParameter init(AnnotationGroup curAnnotationGroup,
      String textDirectoryPath) {
    ExportAnnonationGroupParameter exportGroupParameter = null;

    /*
     * Glenn comment 9/24/20. Removed parameters.getTextDirectoryPath as it is provided by
     * curAnnotationGroup
     */
    if (AnnotationImportExportConfiguration.useSqlExporter()) {
      exportGroupParameter = new SqlExportAnnotationGroupParameter(curAnnotationGroup,
          AnnotationImportExportConfiguration.isCorrectOffsets(),
          AnnotationImportExportConfiguration.isInsertPhraseStrings()/* , textDirectoryPath */);

    } else {
      /*
       * Glenn comment 9/24/20. Removed parameters.getTextDirectoryPath as it is provided by
       * curAnnotationGroup
       */
      exportGroupParameter = new XmlExportAnnotationGroupParameter(curAnnotationGroup,
          AnnotationImportExportConfiguration.isCorrectOffsets(),
          AnnotationImportExportConfiguration.isInsertPhraseStrings()/* , textDirectoryPath */);
    }

    return exportGroupParameter;
  }

}
