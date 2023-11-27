package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

import java.util.Collections;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RapTATItemProcessingState;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatResult;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

/**
 * The Class RaptatTextAnalysisResult.
 *
 * Stores data from processing text with a TextAnalyzer instance.
 */
public class RaptatTextAnalysisResult extends RaptatResult {

  /** The process text result. */
  private RaptatDocument processedTextResult;

  /**
   * Instantiates a new rap TAT document result.
   *
   * @param processedTextResult the process text result
   */
  public RaptatTextAnalysisResult(RaptatDocument processedTextResult, RapTATItemToProcess item) {
    super(item);
    this.processedTextResult = processedTextResult;
  }


  /**
   * Gets the annotated phrases from the RapTAT document
   *
   * @return the annotated phrases
   */
  public List<AnnotatedPhrase> getAnnotatedPhrases() {
    List<AnnotatedPhrase> baseSentences = processedTextResult.getBaseSentences();
    return Collections.unmodifiableList(baseSentences);
  }


  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * Gets the raptat document.
   *
   * @return the raptat document
   */
  public RaptatDocument getRaptatDocument() {
    return processedTextResult;
  }


  /**
   * Gets the result.
   *
   * @return the result
   */
  public RapTATItemProcessingState getResult() {
    return result;
  }

}
