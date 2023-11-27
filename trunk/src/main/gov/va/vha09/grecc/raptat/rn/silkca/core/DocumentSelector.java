package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

import java.util.List;

/**
 * Inteface for selecting a next batch of documents
 */

public interface DocumentSelector {

  /**
   * Get a batch of n documents. Depending on the implementation, the document batch is determined
   * randomly or by factors such as their corresponding document vectors
   *
   * @param n
   * @return
   */
  public List<String> getDocuments(String user, int n);

}
