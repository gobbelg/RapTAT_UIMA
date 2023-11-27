package src.main.gov.va.vha09.grecc.raptat.gg.sql.nlpwrappers;

import java.util.Optional;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.AuditAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RaptatTextAnalysisResult;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

public class TextAnalyzerWrapper implements IRapTATWrapper {

  private static Integer initializeLVGLocation;
  final private static Object syncBlock = new Object();
  private Logger logger;
  private AuditAdapter auditAdapter;
  private TextAnalyzer textAnalyzer;

  /**
   * Instantiates a new wrapper around RapTAT {@link TextAnalyzer} (internal instantiation)
   *
   * @param auditAdapter the audit adapter
   * @param logger the logger
   */
  public TextAnalyzerWrapper(AuditAdapter auditAdapter, Logger logger) {
    this(new TextAnalyzer(), auditAdapter, logger);
  }


  /**
   * Instantiates a new wrapper around RapTAT {@link TextAnalyzer} (provided to constructor)
   *
   * @param textAnalyzer the text analyzer
   * @param auditAdapter the audit adapter
   * @param logger the logger
   */
  public TextAnalyzerWrapper(TextAnalyzer textAnalyzer, AuditAdapter auditAdapter, Logger logger) {
    this.logger = logger;
    this.auditAdapter = auditAdapter;
    this.textAnalyzer = textAnalyzer;
  }


  /**
   * Process {@see RapTATItemToProcess} item
   *
   * @param item the item
   * @return the rap TAT document result
   * @throws AuditException
   */
  @Override
  public RaptatTextAnalysisResult processItem(RapTATItemToProcess item) throws BaseException {
    /*-
     * 2) Send document to RapTAT
     *     a) Record Audit table status as "In RapTAT"
     *     b) Send output from RapTAT to output adapter
     */

    RaptatTextAnalysisResult result = null;

    try {

      item.markSentToRapTAT();

      String documentText = item.getDocumentText();

      /*
       * Glenn comment - modified as this is not needed for reading from a database and writing back
       * to the database.
       */
      // File tempFile = File.createTempFile("raptat", ".txt");
      // try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile))) {
      // bufferedWriter.write(documentText);
      // }

      Optional<String> optionalDocumentPath = Optional.of(item.getDocumentId());
      RaptatDocument doc = textAnalyzer.processText(documentText, optionalDocumentPath);

      result = new RaptatTextAnalysisResult(doc, item);

      switch (result.getResult()) {
        case Error:
          item.markRapTATError(result.getMessage());
          break;
        case Success:
          item.markProcessedByRapTAT();
          break;
        default:
          break;
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // item.markProcessedByRapTAT();

    return result;
  }

}
