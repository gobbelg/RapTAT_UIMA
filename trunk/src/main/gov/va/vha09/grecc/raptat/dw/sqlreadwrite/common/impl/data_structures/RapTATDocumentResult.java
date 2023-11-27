package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures;

import java.util.Collections;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RapTATItemToProcessProcessingState;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

/**
 * The Class RapTATDocumentResult.
 */
public class RapTATDocumentResult {

  /** The result. */
  public RapTATItemToProcessProcessingState result;

  /** The process text result. */
  private RaptatDocument processedTextResult;

  private RapTATItemToProcess raptatItemToProcess;


  /**
   * Instantiates a new rap TAT document result.
   *
   * @param processedTextResult the process text result
   */
  public RapTATDocumentResult(RaptatDocument processedTextResult, RapTATItemToProcess item) {
    this.processedTextResult = processedTextResult;
    this.result = RapTATItemToProcessProcessingState.Success;
    this.raptatItemToProcess = item;
  }


  /**
   * Gets the annotated phrases from the RapTAT document
   *
   * @return the annotated phrases
   */
  public List<AnnotatedPhrase> getAnnotatedPhrases() {
    List<AnnotatedPhrase> baseSentences = this.processedTextResult.getBaseSentences();
    return Collections.unmodifiableList(baseSentences);
  }


  public RapTATItemToProcess getItem() {
    return this.raptatItemToProcess;
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
    return this.processedTextResult;
  }


  /**
   * Gets the result.
   *
   * @return the result
   */
  public RapTATItemToProcessProcessingState getResult() {
    return this.result;
  }

}
