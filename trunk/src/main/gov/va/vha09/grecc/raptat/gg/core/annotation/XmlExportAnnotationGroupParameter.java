package src.main.gov.va.vha09.grecc.raptat.gg.core.annotation;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.ExportAnnonationGroupParameter;

/**
 * The Class XmlExportAnnotationGroupParameter.
 */
public class XmlExportAnnotationGroupParameter extends ExportAnnonationGroupParameter {

  /**
   * Instantiates a new xml export annotation group parameter.
   *
   * @param curAnnotationGroup the cur annotation group
   * @param correctOffsets the correct offsets
   * @param insertPhraseStrings the insert phrase strings
   * @param textDirectoryPath the text directory path
   */
  /*
   * Glenn comment 9/24/20/ Removed textDirectoryPath assignment as this would be redundant relative
   * to inputGroup, which stores the textSourcePath in its RaptatDocument field.
   */
  public XmlExportAnnotationGroupParameter(AnnotationGroup curAnnotationGroup,
      boolean correctOffsets, boolean insertPhraseStrings /* , String textDirectoryPath */) {
    inputGroup = curAnnotationGroup;
    this.correctOffsets = correctOffsets;
    this.insertPhraseStrings = insertPhraseStrings;
    /* this.textDirectoryPath = textDirectoryPath; */
  }
}
