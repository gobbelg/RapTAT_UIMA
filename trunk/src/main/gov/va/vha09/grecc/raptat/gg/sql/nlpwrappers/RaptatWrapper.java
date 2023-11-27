/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.sql.nlpwrappers;

import java.io.File;
import java.util.HashSet;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.Annotator;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.Annotator_Revised;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.SqlDataStorer;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatAnnotationResult;

/**
 * Implementation of IRapTATWrapper interface. Instances of this class process documents through the
 * RapTAT NLP system using a RapTAT solution file provided via the getAnnotator() method.
 *
 * @author Glenn Gobbel
 * @version September 4, 2020
 *
 */
public class RaptatWrapper implements IRapTATWrapper {

  private Logger logger;
  private Annotator annotator;

  public RaptatWrapper(IDataSourceConfiguration annotatorConfiguration, Logger logger) {
    this.logger = logger;
    annotator = getAnnotator(annotatorConfiguration);
  }

  @Override
  public RaptatAnnotationResult processItem(RapTATItemToProcess item) throws BaseException {
    String documentText = item.getDocumentText();
    String documentID = item.getDocumentId();


    AnnotationGroup annotationGroup =
        annotator.annotate(new SqlDataStorer(documentID, documentText));
    return new RaptatAnnotationResult(annotationGroup, item);
  }

  private Annotator getAnnotator(IDataSourceConfiguration annotatorConfiguration) {
    RaptatAnnotationSolution theSolution = getRaptatSolution(annotatorConfiguration);
    HashSet<String> includedConcepts = getIncludedConcepts(annotatorConfiguration);
    Annotator annotator = new Annotator_Revised(theSolution, includedConcepts,
        Constants.SEQUENCE_PROBABILITY_THRESHOLD_DEFAULT);

    return annotator;
  }

  /**
   * @param annotatorConfiguration
   * @return
   */
  private HashSet<String> getIncludedConcepts(IDataSourceConfiguration annotatorConfiguration) {
    HashSet<String> includedConcepts = new HashSet<>();
    String includedConceptsFilePath = annotatorConfiguration.getIncludedConceptsFilePath();
    if (includedConceptsFilePath != null && !includedConceptsFilePath.isEmpty()) {
      includedConcepts = SchemaConcept.getConcepts(includedConceptsFilePath);
    }
    return includedConcepts;
  }

  /**
   * @param annotatorConfiguration
   * @return
   */
  private RaptatAnnotationSolution getRaptatSolution(
      IDataSourceConfiguration annotatorConfiguration) {
    annotatorConfiguration.getSolutionFilePath();
    RaptatAnnotationSolution theSolution = RaptatAnnotationSolution
        .loadFromFile(new File(annotatorConfiguration.getSolutionFilePath()));
    return theSolution;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

  }

}
