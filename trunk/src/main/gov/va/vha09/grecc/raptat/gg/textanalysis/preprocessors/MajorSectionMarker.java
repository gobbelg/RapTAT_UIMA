/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors;

import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatTokenSequence;


/**
 * Implementors of this interface determine if sentences within a document are part of a particular
 * type of section when provided with the sentences in the document and the original document text.
 * Sentences that are found to be part of the section type are "tagged" by a section specific String
 * stored within the sentenceAssociatedConcepts field of the sentence.
 *
 * @author Glenn March 10, 2018
 */
public interface MajorSectionMarker {
  public void markTokenSequences(List<? extends RaptatTokenSequence> documentTokenSequences,
      String documentText);
}
