package src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration.ConfigurationOptions;

/**
 * Abstract class utilized for preprocessing single documents. The type of DocumentPreprocessor is
 * determined the the configurationOptions passed by DocumentProcessor
 */

public abstract class DocumentPreprocessor {

  public DocumentPreprocessor() {}

  public DocumentPreprocessor(ConfigurationOptions configurationOptions) {

  }

  /**
   * Pass document for preprocessing
   *
   * @param document
   * @return
   */
  public abstract String preprocessDocument(String document);

}
