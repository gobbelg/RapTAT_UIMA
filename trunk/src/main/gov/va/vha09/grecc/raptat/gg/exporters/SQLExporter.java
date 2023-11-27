package src.main.gov.va.vha09.grecc.raptat.gg.exporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.StartOffsetPhraseComparator;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.Stopwatch;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.UTFToAnnotatorOffsetConverter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.engine.AnnotationsToDbUploader;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.engine.SchemaParser;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Class SQLExporter.
 *
 * @author westerd
 */
public class SQLExporter extends Exporter {

  private static final Logger LOGGER = Logger.getLogger(SQLExporter.class);
  private static final Stopwatch SW = new Stopwatch();

  {
    LOGGER.setLevel(Level.DEBUG);
  }

  /** The target application. */
  private AnnotationApp targetApplication;

  /** The source id. */
  String annotationGroupExportIndex;

  /** The schema name. */
  String dbSchemaName;

  /** The schema file path. */
  String annotationSchemaFilePath;

  /** The sql driver. */
  private ISQLDriver sqlDriver;



  private boolean useReferenceAnnotations = false;

  /**
   * Instantiates a new SQL exporter.
   *
   * @param targetApplication the target application
   * @param annotationGroupExportIndex the source id
   * @param dbSchemaName the name of the SQL schema in the database
   * @param annotationSchemaFilePath the file path to the schema used to guide annotation
   * @param sqlDriver the sql driver
   */
  public SQLExporter(AnnotationApp targetApplication, String annotationGroupExportIndex,
      String dbSchemaName, String annotationSchemaFilePath, ISQLDriver sqlDriver,
      boolean useReferenceAnnotations) {
    this();
    this.targetApplication = targetApplication;
    this.annotationGroupExportIndex = annotationGroupExportIndex;
    this.dbSchemaName = dbSchemaName;
    this.annotationSchemaFilePath = annotationSchemaFilePath;
    this.sqlDriver = sqlDriver;
    this.useReferenceAnnotations = useReferenceAnnotations;
  }


  /**
   * Instantiates a new SQL exporter.
   */
  protected SQLExporter() {

  }

  /**
   * Export annotation group.
   *
   * @param exportAnnonationGroupParameter the export annonation group parameter
   * @throws ExporterException the exporter exception
   */
  @Override
  public void exportAnnotationGroup(ExportAnnonationGroupParameter exportAnnonationGroupParameter)
      throws ExporterException {

    /*
     * Glenn comment - The parameter useReferenceAnnotation was originally set to true, but this
     * should not be a constant value of true because it depends on the source of the annotations.
     * If Raptat generates the annotations, the annotations are generally stored in the
     * raptatAnnotations field of an AnnotationGroup object instance. The referenceAnnotations field
     * is reserved for annotations that serve as a reference set for comparison to the
     * raptatAnnotations field. If annotations are read in from an xml file, they go into the
     * referenceAnnotations field (by default, I believe).
     */
    try {
      exportRapTATAnnotationGroup(exportAnnonationGroupParameter, this.useReferenceAnnotations);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      GeneralHelper.errorWriter(
          "Cannot insert phrase strings during SQL upload without a physical file present");
      System.exit(-1);
      e.printStackTrace();
    } catch (NoSuchElementException e) {
      e.printStackTrace();
      GeneralHelper.errorWriter("Error connecting to or writing results to database");
      System.exit(-1);
    } catch (ExporterException e) {
      GeneralHelper.errorWriter("Error connecting to or writing results to database");
      System.exit(-1);
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.exporters.Exporter#
   * exportRaptatAnnotationGroup(src.main.gov.va.vha09.grecc.
   * raptat.gg.exporters.ExportAnnonationGroupParameter)
   */
  @Override
  public void exportRaptatAnnotationGroup(
      ExportAnnonationGroupParameter exportAnnonationGroupParameter) {
    // TODO Auto-generated method stub

  }

  /**
   * Export Raptat annotation group.
   *
   * @param exportAnnonationGroupParameter the export annotation group parameter
   * @param useReferenceAnnotations - Chooses between picking the referenceAnnotations field within
   *        the inputGroup field of the exportAnnotationGroupParameter and the referenceAnnotations
   *        field. Generally, automated production of Raptat annotations are stored in the
   *        raptatAnnotations field of the inputGroup field of this instance. Manually generated
   *        annotations are generally stored in the referenceAnnoptations group parameters.
   * @throws ExporterException the exporter exception
   */
  public void exportRapTATAnnotationGroup(
      ExportAnnonationGroupParameter exportAnnonationGroupParameter,
      boolean useReferenceAnnotations)
      throws ExporterException, IllegalArgumentException, NoSuchElementException {

    AnnotationGroup annotationGroup = exportAnnonationGroupParameter.inputGroup;
    List<AnnotatedPhrase> annotationsForExport =
        useReferenceAnnotations ? annotationGroup.referenceAnnotations
            : annotationGroup.raptatAnnotations;
    RaptatDocument raptatDocument = annotationGroup.getRaptatDocument();


    /*
     * We will use the textSourcePath as the identifier unless it is not present, in which case
     * we'll use the textSource.
     */
    Optional<String> optionalPath = raptatDocument.getTextSourcePath();
    Optional<String> optionalName = raptatDocument.getTextSourceName();
    String textSourceId = optionalPath.orElse(optionalName.orElseThrow());

    System.out.println("Exporting:" + textSourceId);
    /*
     * Glenn removed inserting phrase strings and correcting offsets is generally only needed for
     * creation of xml files that store annotations for eHOST
     */
    if (exportAnnonationGroupParameter.insertPhraseStrings) {

      if (optionalPath.isEmpty()) {
        System.err.println(
            "Incorrect configuration\n\tInsertion of phrase strings requires a physical text source path");
        throw new IllegalArgumentException();
      }
      boolean insertIntoDictionaryAnnotations = true;
      insertPhraseStrings(annotationsForExport, optionalPath.get(),
          insertIntoDictionaryAnnotations);
    }

    if (exportAnnonationGroupParameter.correctOffsets) {
      String rawDocumentText = raptatDocument.getRawInputText();
      if (rawDocumentText == null) {
        if (optionalPath.isEmpty()) {
          System.err.println(
              "Incorrect configuration\n\tCorrection of annotation phrase offsets requires a physical text source path");
          throw new IllegalArgumentException();
        }
        File textFile = new File(optionalPath.get());
        UTFToAnnotatorOffsetConverter offsetConverter =
            new UTFToAnnotatorOffsetConverter(textFile, this.targetApplication);
        offsetConverter.convertAnnotatedPhraseOffsetList(annotationsForExport);
      } else {
        UTFToAnnotatorOffsetConverter offsetConverter =
            new UTFToAnnotatorOffsetConverter(rawDocumentText, this.targetApplication);
        offsetConverter.convertAnnotatedPhraseOffsetList(annotationsForExport);
      }
    }

    try {
      uploadAnnotationGroup(annotationGroup, textSourceId, useReferenceAnnotations);
    } catch (NoConnectionException | IOException e) {
      throw new ExporterException(e);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.exporters.Exporter#
   * exportReferenceAnnotationGroup(src.main.gov.va.vha09.grecc.
   * raptat.gg.exporters.ExportAnnonationGroupParameter)
   */
  @Override
  public void exportReferenceAnnotationGroup(
      ExportAnnonationGroupParameter exportAnnonationGroupParameter) throws ExporterException {
    exportRapTATAnnotationGroup(exportAnnonationGroupParameter, true);

  }

  @Override
  public void setResultFolder(File resultFolder) {
    /*
     * DAX - 4/30/20 - added to fix breaking code from Exporter class
     */
  }

  /**
   * Write annotations.
   *
   * @param inputGroup the input group
   * @param optionalTextSourcePath text file containing the annotations
   * @param useReferenceAnnotations the use reference annotations
   * @throws NoConnectionException the no connection exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void uploadAnnotationGroup(AnnotationGroup inputGroup, String textSourceId,
      boolean useReferenceAnnotations) throws NoConnectionException, IOException {

    List<AnnotatedPhrase> annotations =
        useReferenceAnnotations ? inputGroup.referenceAnnotations : inputGroup.raptatAnnotations;
    Optional<String> optionalDocumentText =
        Optional.ofNullable(inputGroup.getRaptatDocument().getRawInputText());

    /*
     * The next two lines should be moved up to the RaptatRunner class because we should check there
     * if the annotation schema has been added to the database, add it if not, and get its ID
     * regardless.
     */
    SchemaParser sParser = new SchemaParser(this.dbSchemaName, this.sqlDriver);
    long annotationSchemaId = sParser.processAnnotationSchemaReturnId(
        new File(this.annotationSchemaFilePath), this.targetApplication.getCommonName());
    AnnotationsToDbUploader annotationsToDbUploader = new AnnotationsToDbUploader(this.sqlDriver);


    if (LOGGER.isDebugEnabled()) {
      SW.markTime("Begin uploading");
    }
    annotationsToDbUploader.uploadAnnotations(annotationSchemaId, annotations, optionalDocumentText,
        textSourceId, this.dbSchemaName);
  }

  /**
   * Insert phrase strings.
   *
   * @param allAnnData the all ann data
   * @param documentPath the document path
   * @param includeDictionaryAnnotations the include dictionary annotations
   */
  private void insertPhraseStrings(List<AnnotatedPhrase> allAnnData, String documentPath,
      boolean includeDictionaryAnnotations) {
    BufferedReader br = null;
    int curStreamPosition = 0;
    int startOffset, endOffset;
    Collections.sort(allAnnData, new StartOffsetPhraseComparator());

    try {
      FileInputStream fis = new FileInputStream(documentPath);
      br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

      for (AnnotatedPhrase curPhrase : allAnnData) {
        if (includeDictionaryAnnotations || !curPhrase.isDictionaryBasedPhrase()) {
          startOffset = Integer.parseInt(curPhrase.getRawTokensStartOff());
          endOffset = Integer.parseInt(curPhrase.getRawTokensEndOff());
          char[] phrase = new char[endOffset - startOffset];

          /*
           * If two annotated phrases overlap, the reader will read past an annotation, so we need
           * to allow for repositioning it to next annotated phrase when this happens
           */
          if (startOffset < curStreamPosition) {
            curStreamPosition = startOffset;
            fis.getChannel().position(curStreamPosition);
            br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

          }
          br.skip(startOffset - curStreamPosition);
          br.read(phrase);
          curStreamPosition = endOffset;
          String phraseForInsert = new String(phrase);
          curPhrase.setPhraseStringUnprocessed(phraseForInsert);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println(e);
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(e);
          e.printStackTrace();
        }
      }
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see src.main.gov.va.vha09.grecc.raptat.gg.exporters.Exporter#writeAnnotations(src
   * .main.gov.va.vha09.grecc.raptat.gg. datastructures.annotationcomponents.AnnotationGroup,
   * java.io.File, java.lang.String, boolean)
   */
  @Override
  void writeAnnotations(AnnotationGroup inputGroup, File annotatedFile, String targetFolderPath,
      boolean useReferenceAnnotations) {
    // TODO Auto-generated method stub

  }

}
