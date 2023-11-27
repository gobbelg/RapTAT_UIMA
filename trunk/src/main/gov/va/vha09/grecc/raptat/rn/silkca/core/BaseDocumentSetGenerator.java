package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

import java.util.ArrayList;
import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;

/**
 * Base implementation of DocumentSetGenerator, which is the Control Object that returns a next
 * batch of documents, together with their preannotations. Gets documents from DocumentSelector and
 * the preannotations of those documents from Preannotator.
 */

public class BaseDocumentSetGenerator extends DocumentSetGenerator {

  public BaseDocumentSetGenerator(ConfigurationOptions configurationOptions) {
    super(configurationOptions);
  }

  /**
   * Get documents from the implementation of DocumentSelector. Afterwards, pass those documents to
   * Preannotator for preannotation. Returns a list of RaptatPairs, each pair signifying a document,
   * together with its preannotations
   */
  @Override
  public ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> getDocsAndAnnotations(String user,
      int numberOfDocsInBatch) {
	  ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> docsAndAnnotations = new ArrayList<>();
	  List<String> nextBatchDocuments = this.documentSelector.getDocuments(user, numberOfDocsInBatch);
	  List<SimpleAnnotatedPhrase> preannotations;
	  RaptatPair<String, List<SimpleAnnotatedPhrase>> pair;
	  for (String document: nextBatchDocuments) {
		  preannotations = this.preannotator.getPreannotations(document);
		  pair = new RaptatPair<>(document, preannotations);
		  docsAndAnnotations.add(pair);
	  }
	  return docsAndAnnotations;
  }

}
