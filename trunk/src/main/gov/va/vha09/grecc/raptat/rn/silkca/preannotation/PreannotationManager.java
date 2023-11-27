package src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.core.BaseDocumentSetGenerator;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.core.DocumentSetGenerator;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.core.DocumentSetGeneratorType;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.DocumentCorpus;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing.DocumentProcessor;

/**
 * Central class that serves as an interface between SilkCA and Raptat. Handles adding Documents,
 * updating the DocumentCorpus, and getting new batches of documents
 */

public class PreannotationManager implements Serializable {

  private static final long serialVersionUID = 962421549595468411L;
private DocumentSetGenerator documentSetGenerator;
  private DocumentProcessor documentProcessor;

  public PreannotationManager(ConfigurationOptions configurationOptions) {
    this.documentProcessor = new DocumentProcessor(configurationOptions);
    setDocumentSetGenerator(configurationOptions);
  }

  private PreannotationManager() {}

  /**
   * add document to be processed, which consists of being preprocessed, and then added to the
   * DocumentCorpus
   *
   * @param document
   * @return
   */
  public boolean addDocument(List<String> users, String document) {
    return this.documentProcessor.addDocument(users, document);
  }

  /**
   * get a new document batch, together with their preannotations
   *
   * @param numberOfDocsInBatch
   * @return
   */
  public ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> getNewDocBatch(String user,
      int numberOfDocsInBatch) {
    return this.documentSetGenerator.getDocsAndAnnotations(user, numberOfDocsInBatch);
  }

  /**
   * update the DocumentCorpus with annotations assigned by the annotator
   *
   * @param listPairs
   * @return
   */
  public boolean update(String user, ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> listPairs) {
    return DocumentCorpus.INSTANCE.updateReviewerAnnotations(user, listPairs);
  }
  
  public boolean update(String user, RaptatPair<String, List<SimpleAnnotatedPhrase>> pair) {
	  ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> docAnnotationPairList = new ArrayList<>();
	  docAnnotationPairList.add(pair);
	  return this.update(user, docAnnotationPairList);
  }
  
  private void setDocumentSetGenerator(ConfigurationOptions configurationOptions) {
		try {
			DocumentSetGeneratorType documentSetGeneratorType = configurationOptions.getDocumentSetGeneratorType();
			switch (documentSetGeneratorType) {
			case BASE: {
				this.documentSetGenerator = new BaseDocumentSetGenerator(configurationOptions);
			}
			default: {

			}
			}
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(-1);
		}
  }
  
}
