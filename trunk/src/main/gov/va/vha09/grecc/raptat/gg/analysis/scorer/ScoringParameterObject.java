/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.analysis.scorer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.RaptatStringStyle;

/**
 * Stores the various parameters supplied in the command line when scoring performance via a
 * PerformanceScorer instance
 *
 * @author Glenn Gobbel
 *
 */
public class ScoringParameterObject {
  final String textDirectoryPath;
  final String raptatDirectoryPath;
  final String referenceDirectoryPath;
  final String schemaFilePath;
  final String conceptsFilePath;
  final AnnotationApp annotationApp;
  final boolean removeUnmatched;


  public ScoringParameterObject(String textDirectoryPath, String raptatDirectoryPath,
      String referenceDirectoryPath, String schemaFilePath, String conceptsFilePath,
      AnnotationApp annotationApp, String removeUnmatched) {
    super();
    this.textDirectoryPath = textDirectoryPath;
    this.raptatDirectoryPath = raptatDirectoryPath;
    this.referenceDirectoryPath = referenceDirectoryPath;
    this.schemaFilePath = schemaFilePath;
    this.conceptsFilePath = conceptsFilePath;
    this.annotationApp = annotationApp;
    this.removeUnmatched = Boolean.parseBoolean(removeUnmatched);
  }


  @Override
  public String toString() {
    ToStringBuilder.setDefaultStyle(RaptatStringStyle.getInstance());

    ToStringBuilder sb =
        new ToStringBuilder(this).append("textDirectoryPath", this.textDirectoryPath)
            .append("raptatDirectoryPath", this.raptatDirectoryPath)
            .append("referenceDirectoryPath", this.referenceDirectoryPath)
            .append("schemaFilePath", this.schemaFilePath)
            .append("conceptsFilePath", this.conceptsFilePath)
            .append("annotationApp", this.annotationApp)
            .append("checkPhraseOffsets", this.removeUnmatched);

    return sb.toString();
  }
}
