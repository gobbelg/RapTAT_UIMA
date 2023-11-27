package src.main.gov.va.vha09.grecc.raptat.gg.sql.nlprunners;

import java.io.File;
import java.text.MessageFormat;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.config.DataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection.AuditAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.AuditException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.ConfigurationException;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.reader.impl.RapTATInputAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.reader.interfaces.IInputAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.impl.AnnotationOutputAdapter;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.interfaces.IOutputAdapter;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.Stopwatch;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatResult;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.nlpwrappers.IRapTATWrapper;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.nlpwrappers.RaptatWrapper;

public class RapTATRunner {

  private static final int PROGRAM_ENDED_SUCCESSFULLY = 0;
  private static final int PROGRAM_FAILED_UNEXPECTEDLY = -1;
  public static final Stopwatch sw = new Stopwatch();

  /**
   * Gets the annotation schema file.
   *
   * @param outputConfiguration the output configuration
   * @return the annotation schema file
   */
  private File getAnnotationSchemaFile(IDataSourceConfiguration outputConfiguration) {

    String schemaFilePath = outputConfiguration.getAnnotationSchemaFilePath();

    File schemaFile = null;

    if (schemaFilePath == null) {
      schemaFile = loadResource("AMIProjectSchemaForMoonstoneOutput_181218.xml");
    } else {
      schemaFile = new File(schemaFilePath);
    }

    return schemaFile;
  }

  // private static void setLibraryPath()
  // {
  // File directory = loadResource( "sqljdbc_auth.dll" ).getParentFile();
  // System.setProperty( "java.library.path", directory.getAbsolutePath() );
  // }


  /**
   * Gets the audit adapter properties.
   *
   * @return the audit adapter properties
   */
  private File getAuditAdapterProperties() {
    return loadResource("AuditAdapter.properties");
  }


  /**
   * Gets the input adapter properties.
   *
   * @return the input adapter properties
   */
  private File getInputAdapterProperties() {
    return loadResource("InputAdapter.properties");
  }


  /**
   * Gets the output adapter properties.
   *
   * @return the output adapter properties
   */
  private File getOutputAdapterProperties() {
    return loadResource("OutputAdapter.properties");
  }


  /**
   * Gets the properties needed for annotating with RapTAT
   *
   * @return rapTAT annotator properties
   */
  private File getRaptatAnnotatorProperties() {
    return loadResource("RaptatAnnotator.properties");
  }

  /**
   * Load resource.
   *
   * @param fileName the file name
   * @return the file
   */
  private File loadResource(String fileName) {
    Class thisClass = this.getClass();
    String relativeResourcePath = MessageFormat.format("resources/{0}/{1}",
        thisClass.getSimpleName().toLowerCase(), fileName);
    String absoluteResourcePath = thisClass.getResource(relativeResourcePath).getFile();
    return new File(absoluteResourcePath);
  }

  public static void main(String[] args) throws ConfigurationException {

    UserPreferences.INSTANCE.initializeLVGLocation();

    // setLibraryPath();

    /*-
     * Workflow cycle
     *
     * {@see gov.va.med.v09.vhatvh.raptatcommon.constants.ProcessingState} for states.
     *
     * 1) If
     * 		a) Document Exists
     * 			i) Read in next document from input adapter
     * 			ii) Record Audit table status as "Loading" with start timestamp of now (SQL level, UTC)
     * 		b) No More Documents: Halt program
     * 2) Send document to RapTAT
     * 		a) Record Audit table status as "In RapTAT"
     * 		b )Send output from RapTAT to output adapter
     * 3) Writing
     * 		a) On Start
     * 			i) Record Audit table status as "Writing"
     * 			ii) Export to Moonstone --> SQL Exporter schema
     * 		b) On End
     * 			i) Record audit table status as "Complete" with end timestamp of now (SQL level, UTC)
     * 8) Goto 1
     */

    final Logger logger = Logger.getLogger(RapTATRunner.class);
    logger.setLevel(Level.DEBUG);
    System.out.println("Is Logger Debug Enabled:" + logger.isDebugEnabled());

    RapTATRunner runner = new RapTATRunner();

    /*
     * Configure audit adapter
     */
    File auditAdapterProperties = runner.getAuditAdapterProperties();
    IDataSourceConfiguration auditConfiguration =
        new DataSourceConfiguration(auditAdapterProperties);
    AuditAdapter auditAdapter = new AuditAdapter(auditConfiguration, logger);

    /*
     * Configure input adapter
     */
    File inputAdapterProperties = runner.getInputAdapterProperties();
    IDataSourceConfiguration inputConfiguration =
        new DataSourceConfiguration(inputAdapterProperties);
    IInputAdapter inputAdapter = new RapTATInputAdapter(inputConfiguration, auditAdapter, logger);


    /*
     * Configure output adapter
     *
     */
    File outputAdapterProperties = runner.getOutputAdapterProperties();
    IDataSourceConfiguration outputConfiguration =
        new DataSourceConfiguration(outputAdapterProperties);
    File schemaFile = runner.getAnnotationSchemaFile(outputConfiguration);

    /*
     * Use a 'TextAnalysisOutputAdapter' instance to write out results if doing only TextAnalysis
     */
    IOutputAdapter outputAdapter =
        new AnnotationOutputAdapter(outputConfiguration, auditAdapter, logger, schemaFile);

    /*
     * Configure RapTAT
     */
    File annotatorProperties = runner.getRaptatAnnotatorProperties();
    IDataSourceConfiguration dataProcessingConfiguration =
        new DataSourceConfiguration(annotatorProperties);

    /*
     * We should add code here to check whether the solution or annotation schema exist in the
     * database. If not, they should be added.
     */

    IRapTATWrapper raptatWrapper = new RaptatWrapper(dataProcessingConfiguration, logger);

    while (inputAdapter.hasNext()) {

      AuditLogId auditLogId = null;

      try (RapTATItemToProcess item = (RapTATItemToProcess) inputAdapter.next()) {

        auditLogId = item.getAuditLogId();

        if (logger.isDebugEnabled()) {
          sw.markTime("processing next item");
        }
        /*
         * Note that this writes the document text a temporary file on disk. This is only needed
         * because the textAnalyzer.process method has a document path as a parameter, so it should
         * not be needed here.
         *
         * Also, the only thing that really needs to be returned here is the AnnotationGroup
         * instance conctained in the processed item. That instance should contain everything that
         * needs to be written to disk at this point.
         */
        RaptatResult processedItem = raptatWrapper.processItem(item);

        if (logger.isDebugEnabled()) {
          sw.getSecondsSinceMark("completed processing next item");
        }


        if (logger.isDebugEnabled()) {
          sw.markTime("writing next item");
        }
        outputAdapter.writeResult(processedItem);

        if (logger.isDebugEnabled()) {
          sw.getSecondsSinceMark("completed writing next item");
        }

      } catch (Exception ex) {

        assert ex instanceof BaseException;

        if (auditLogId == null) {
          /*
           * This indicates that the audit log could not be reached, so stop the program for review.
           */
          logger.error("The audit log could not be reached (auditLogId == 0).  Halting process.");
          System.exit(PROGRAM_FAILED_UNEXPECTEDLY);
        }
        try {
          auditAdapter.fail(auditLogId, ex);
        } catch (AuditException e) {
          inputAdapter.halt();
          logger.error(e);
        }
      }

    }

    System.exit(PROGRAM_ENDED_SUCCESSFULLY);
  }

}
