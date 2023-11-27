/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.phrasecontext;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;

/**
 * @author glenn
 *
 *         Interface for checking whether an AnnotatedPhrase instance is compliant with the context
 *         as defined by the implementing class.
 */
interface ContextChecker {
  boolean isContextCompliant(AnnotatedPhrase annotatedPhrase);
}
