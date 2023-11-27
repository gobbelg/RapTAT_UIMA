package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.impl;

import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.AuditAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.RapTATDatabaseConnection;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RaptatTextAnalysisResult;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.WriteException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.interfaces.IOutputAdapter;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.SQLExporter;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.SqlExportAnnotationGroupParameter;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatResult;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;

/**
 * The Class RapTATWriter.
 */
public class TextAnalysisOutputAdapter extends RapTATDatabaseConnection implements IOutputAdapter {

  /*
   * Since this is for connect to a database, we should not need to correct offsets or insert phrase
   * strings since those are primarily designed for writing to xml files that are used with eHOST
   * for specifying annotations
   */
  private static final boolean DEFAULT_CORRECT_OFFSETS = false;
  private static final boolean DEFAULT_INSERT_PHRASE_STRING = false;
  private static Counter counter = new Counter();

  private File schemaFile;

  /**
   * Instantiates a new RapTAT Writer.
   *
   * @param config the
   *        {@link src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration}
   * @param auditAdapter the audit adapter
   * @param logger the logger
   * @param schemaFile the schema file
   */
  public TextAnalysisOutputAdapter(IDataSourceConfiguration config, AuditAdapter auditAdapter,
      Logger logger, File schemaFile) {
    super(config, auditAdapter, logger);
    this.schemaFile = schemaFile;

  }


  /**
   * Write result to AuditLog
   *
   * @param result the result
   * @return the write state
   * @throws DataSourceException
   * @throws WriteException
   * @throws AuditException
   */
  @Override
  public void writeResult(RaptatResult result) throws BaseException {

    RaptatTextAnalysisResult analysisResult = (RaptatTextAnalysisResult) result;
    /*
     * Initialize the audit log with this stage.
     */
    result.getItem().markWritingToDatabase();

    /*
     * Call export code to write to write DB
     */
    export(analysisResult);

    /*
     * Update the audit log
     */
    result.getItem().markWrittenToDatabase();

  }


  /**
   * Establish annotation group.
   *
   * @param result the result
   * @return the annotation group
   * @throws WriteException the write exception
   */
  private AnnotationGroup establishAnnotationGroup(RaptatTextAnalysisResult result)
      throws WriteException {

    RaptatDocument raptatDocument = result.getRaptatDocument();
    List<AnnotatedPhrase> annotatedPhrases = result.getAnnotatedPhrases();
    AnnotationGroup annotationGroup = new AnnotationGroup(raptatDocument, annotatedPhrases);

    return annotationGroup;

  }


  /**
   * Export all the annotation information to the data source
   *
   * @param result
   * @throws WriteException
   * @throws DataSourceException
   * @throws SQLDriverException
   */
  private void export(RaptatTextAnalysisResult result) throws WriteException, DataSourceException {

    AnnotationGroup annotationGroup = establishAnnotationGroup(result);

    IDataSourceConfiguration config = getConfig();

    boolean correctOffsets =
        config == null ? DEFAULT_CORRECT_OFFSETS : getConfig().getCorrectOffsets();
    boolean insertPhraseString =
        config == null ? DEFAULT_INSERT_PHRASE_STRING : getConfig().getInsertPhraseString();

    /*
     * Glenn comment - note that this writes out a temporary file to create a valid path to the
     * document, but this should not be needed for writing to a database
     *
     * Because of this, we have removed the computation of documentPath and removed it from the
     * parameters in the constructor call.
     */
    // String documentPath;
    // documentPath = result.getItem().getDocumentPath().orElse("NoDocumentPath");

    SqlExportAnnotationGroupParameter parameter = new SqlExportAnnotationGroupParameter(
        annotationGroup, correctOffsets, insertPhraseString/* , documentPath */);

    ISQLDriver sqlDriver;
    try {
      sqlDriver = new SQLDriverWrapper(connect());
    } catch (DataSourceException | SQLDriverException e1) {
      throw new DataSourceException("Cannot connect SQLDriverWrapper", e1);
    }

    AnnotationApp annotationApp =
        AnnotationApp.valueOf(getConfig().getApplicationApp().toUpperCase());
    boolean useReferenceAnnotations = false;
    SQLExporter exporter =
        new SQLExporter(annotationApp, counter.next().toString(), getConfig().getSqlExportSchemaName(),
            schemaFile.getAbsolutePath(), sqlDriver, useReferenceAnnotations);

    try {
      exporter.exportAnnotationGroup(parameter);
    } catch (Exception e) {
      getLogger().error(e);
      throw new WriteException(e);
    }

  }


  /**
   * Gets the destination schema.
   *
   * @return the destination schema
   */
  private String getDestinationSchema() {
    return getConfig().getSqlExportSchemaName();
  }


  public static void main(String[] argv) {

    /** The Constant logger. */
    final Logger logger = Logger.getLogger(TextAnalysisOutputAdapter.class);

  }

}
