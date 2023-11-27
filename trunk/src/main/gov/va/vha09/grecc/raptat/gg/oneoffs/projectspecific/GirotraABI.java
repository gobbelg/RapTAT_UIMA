/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.SwingUtilities;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.csv.CSVReader;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.annotation.AnnotationMutator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

/**
 * @author VHATVHGOBBEG
 *
 */
public class GirotraABI {
  private enum RunType {
    ADJUDICATE_SLC_CSV_OUTPUT, CORRECT_SALT_LAKE_ABI_TOOL_OFFSETS, MERGE_ANNOTATION_SETS;
  }

  private void adjudicateSaltLakeCsvOutput(String pathToCsvFile) {
    final int NUMBER_OF_COLUMNS_EXPECTED = 10;

    File csvFile = new File(pathToCsvFile);
    CSVReader reader = new CSVReader(csvFile);

    // The first line should have column headings and no data
    if (!reader.isValid() || reader.getNextData() == null) {
      System.err.println("No valid data for file at:" + pathToCsvFile);
      System.exit(-1);
    }

    String[] csvData;

    while ((csvData = reader.getNextData()) != null) {
      if (csvData.length < NUMBER_OF_COLUMNS_EXPECTED) {
        continue;
      }

      processAbiData(csvData);
    }

  }

  /**
   * @param raptatAnnotations
   * @param textFile
   * @param xmlExporter
   */
  private void exportAnnotationList(List<AnnotatedPhrase> annotationList, File textFile,
      XMLExporterRevised xmlExporter) {
    RaptatDocument textDocument = new RaptatDocument();
    textDocument.setTextSourcePath(Optional.of(textFile.getAbsolutePath()));
    AnnotationGroup annotationGroup = new AnnotationGroup(textDocument, annotationList);
    xmlExporter.exportReferenceAnnotationGroup(annotationGroup, false, false);
  }

  private void processAbiData(String[] csvData) {
    final int DOC_NAME_INDEX = 0;
    final int START_OFFSET_INDEX = 1;
    final int END_OFFSET_INDEX = 2;
    final int PHRASE_TEXT_INDEX = 4;
    final int ABI_VALUE_INDEX = 5;
    final int INDEX_TYPE_INDEX = 6;
    final int LATERALITY_INDEX = 7;

  }

  protected void correctSaltLakeAbiToolOffsets(String textFilesPath, String xmlFilesPathString,
      String outputDirectoryPath, String schemaFilePath) {
    File outputDirectory = new File(outputDirectoryPath);
    XMLExporterRevised exporter =
        new XMLExporterRevised(AnnotationApp.EHOST, outputDirectory, true);

    Map<File, File> txtToXmlFileMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathString);
    List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaFilePath);

    AnnotationImporter annotationImporter = new AnnotationImporter(AnnotationApp.EHOST);
    annotationImporter.setReconcileAnnotationsWithSchema(true);
    annotationImporter.setReconcileAnnotationsWithText(true);

    int fileNumber = 0;
    for (File textFile : txtToXmlFileMap.keySet()) {
      String xmlFilePath = txtToXmlFileMap.get(textFile).getAbsolutePath();
      List<AnnotatedPhrase> annotationList = annotationImporter.importAnnotations(xmlFilePath,
          textFile.getAbsolutePath(), null, schemaConcepts, null, false, true, true);
      System.out
          .println("\n\nFile " + fileNumber++ + ":  " + textFile.getName() + "\nAnnnotations:");
      System.out.println(annotationList);

      RaptatDocument textDocument = new RaptatDocument();
      textDocument.setTextSourcePath(Optional.of(textFile.getAbsolutePath()));
      AnnotationGroup annotationGroup = new AnnotationGroup(textDocument, annotationList);
      exporter.exportReferenceAnnotationGroup(annotationGroup, false, false);
    }
  }

  /**
   * @param textFilesPath
   * @param xmlFilesPathManual
   * @param xmlFilesPathRaptat
   * @param outputDirectoryPath
   */
  protected void mergeAnnotationSets(String textFilesPath, String xmlFilesPathManual,
      String xmlFilesPathRaptat, String outputDirectoryPath) {
    AnnotationImporter xmlImporter = new AnnotationImporter(AnnotationApp.EHOST);
    AnnotationMutator mutator = new AnnotationMutator();
    XMLExporterRevised xmlExporter =
        XMLExporterRevised.getExporter(new File(outputDirectoryPath), true);

    Map<File, File> txtToXmlManualMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathManual);
    Map<File, File> txtToXmlRapatMap =
        GeneralHelper.getTxtToXmlFileMap(textFilesPath, xmlFilesPathRaptat);

    for (File textFile : txtToXmlManualMap.keySet()) {
      String xmlFilePathManual = txtToXmlManualMap.get(textFile).getAbsolutePath();
      String xmlFilePathRaptat = txtToXmlRapatMap.get(textFile).getAbsolutePath();

      List<AnnotatedPhrase> manualAnnotations =
          xmlImporter.importAnnotations(xmlFilePathManual, textFile.getAbsolutePath(), null);
      List<AnnotatedPhrase> raptatAnnotations =
          xmlImporter.importAnnotations(xmlFilePathRaptat, textFile.getAbsolutePath(), null);

      raptatAnnotations = mutator.replaceOverlapped(raptatAnnotations, manualAnnotations);
      exportAnnotationList(raptatAnnotations, textFile, xmlExporter);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {
        RunType runType = RunType.MERGE_ANNOTATION_SETS;
        GirotraABI raptatAbiTool = new GirotraABI();

        switch (runType) {
          case ADJUDICATE_SLC_CSV_OUTPUT:
            String pathToCsvFile =
                "P:\\ORD_Girotra_201607120D\\Glenn\\GlennInstallOfABIToolEvaluation\\csvOutputOfTool\\ABI_output.csv";
            raptatAbiTool.adjudicateSaltLakeCsvOutput(pathToCsvFile);
            break;

          case CORRECT_SALT_LAKE_ABI_TOOL_OFFSETS: {
            String textFilesPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\corpus";
            String xmlFilesPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\saved";
            String schemaFilePath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\config\\projectschema.xml";
            String outputDirectoryPath =
                "P:\\ORD_Girotra_201607120D\\Glenn\\ABIAnnotations_181212\\ABIValueAnnotationWorkspace\\ABIAnnotations_200Docs_181212\\offsetsCorrected";
            raptatAbiTool.correctSaltLakeAbiToolOffsets(textFilesPath, xmlFilesPath,
                outputDirectoryPath, schemaFilePath);
          }
            break;
          case MERGE_ANNOTATION_SETS: {
            String textFilesPath = "";
            String xmlFilesPathManual = "";
            String xmlFilesPathRaptat = "";
            String outputDirectoryPath = "";
            raptatAbiTool.mergeAnnotationSets(textFilesPath, xmlFilesPathManual, xmlFilesPathRaptat,
                outputDirectoryPath);
          }
            break;
          default:
            break;
        }

        System.exit(0);
      }
    });

  }

}
