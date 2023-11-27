package src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing;

import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.DocumentCorpus;

/**
 * Passes documents to the Preprocessor, and afterwards passes the preprocessed
 * document to the DocumentCorpus. The preprocessor implementation is selected
 * through the configurationOptions
 */

public class DocumentProcessor {

	private DocumentPreprocessor documentPreprocessor;

	public DocumentProcessor(ConfigurationOptions configurationOptions) {
	  DocumentPreprocessorType documentPreprocessorType = configurationOptions.getDocumentPreprocessorType(); 
    switch (documentPreprocessorType) {
      case BASE: {
        this.documentPreprocessor = new BaseDocumentPreprocessor(configurationOptions);
      }
      default: {

      }
    }
  }

	private DocumentProcessor() {
	}

	/**
	 * Prior to adding a document to the DocumentCorpus, the document is
	 * preprocessed
	 *
	 * @param document
	 * @return
	 */
	public boolean addDocument(List<String> users, String document) {
		String preprocessedDocument = this.documentPreprocessor.preprocessDocument(document);
		return DocumentCorpus.INSTANCE.addDocument(users, document);
	}

}
