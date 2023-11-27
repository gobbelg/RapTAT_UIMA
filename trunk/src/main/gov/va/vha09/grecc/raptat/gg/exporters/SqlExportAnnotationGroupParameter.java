package src.main.gov.va.vha09.grecc.raptat.gg.exporters;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;

public class SqlExportAnnotationGroupParameter extends ExportAnnonationGroupParameter {

  /**
   * Instantiates a new sql export annotation group parameter.
   *
   * @param curAnnotationGroup the cur annotation group
   * @param correctOffsets the correct offsets
   * @param insertPhraseStrings the insert phrase strings
   * @param textFilePath the text file path
   */
  /*
   * Removed textFilePath parameter as inputGroup, and AnnotationGroup, should contain that in its
   * RaptatDocument field
   */
  public SqlExportAnnotationGroupParameter(AnnotationGroup curAnnotationGroup,
      boolean correctOffsets, boolean insertPhraseStrings /* , String textFilePath */) {
    inputGroup = curAnnotationGroup;
    this.correctOffsets = correctOffsets;
    this.insertPhraseStrings = insertPhraseStrings;
    // this.textFilePath = textFilePath;
  }

}
