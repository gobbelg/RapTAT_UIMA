package src.main.gov.va.vha09.grecc.raptat.gg.importer;

/**
 * The Class SqlDataStorer.
 */
public class SqlDataStorer extends DataStorer {

  /** The document unique id. */
  String documentUniqueId;

  /** The document text. */
  String documentText;

  /**
   * Instantiates a new sql data storer.
   *
   * @param docUniqueId the doc unique id
   * @param docText the doc text
   */
  public SqlDataStorer(String docUniqueId, String docText) {
    this.documentUniqueId = docUniqueId;
    this.documentText = docText;
  }


  /**
   * Gets the document text.
   *
   * @return the document text
   */
  @Override
  public String getDocumentText() {
    return this.documentText;
  }


  /**
   * Gets the document unique id.
   *
   * @return the document unique id
   */
  @Override
  public String getUniqueId() {
    return this.documentUniqueId;
  }

}
