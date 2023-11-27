package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

import java.util.ArrayList;
import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing.DocumentPreprocessorType;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation.BasePreannotator;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation.Preannotator;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation.PreannotatorType;

/**
 * Abstract Control Object that returns a next batch of documents, together with
 * their preannotations. Gets documents from DocumentSelector and the
 * preannotations of those documents from Preannotator
 */

public abstract class DocumentSetGenerator {

	Preannotator preannotator;
	DocumentSelector documentSelector;

	public DocumentSetGenerator(ConfigurationOptions configurationOptions) {
		try {
			DocumentSetGeneratorType documentSetGeneratorType = configurationOptions.getDocumentSetGeneratorType();
			switch (documentSetGeneratorType) {
			case BASE: {
				this.preannotator = new BasePreannotator();
				this.documentSelector = new BaseDocumentSelector();
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

	/**
	 * Get documents from the implementation of DocumentSelector. Afterwards, pass
	 * those documents to Preannotator for preannotation. Returns a list of
	 * RaptatPairs, each pair signifying a document, together with its
	 * preannotations
	 */
	abstract public ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> getDocsAndAnnotations(String user,
			int numberOfDocsInBatch);

}
