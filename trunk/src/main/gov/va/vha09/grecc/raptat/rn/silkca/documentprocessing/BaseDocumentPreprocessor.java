package src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;

/**
 * Base implementation of DocumentPreprocessor, which preprocesses single documents
 */

public class BaseDocumentPreprocessor extends DocumentPreprocessor {

  public BaseDocumentPreprocessor(ConfigurationOptions configurationOptions) {}

  /**
   * Pass document for preprocessing
   *
   * @param document
   * @return
   */
  @Override
  public String preprocessDocument(String document) {
    return null;
  }

}
