/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.exporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.dialogs.ButtonQueryDialog;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.StartOffsetPhraseComparator;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.UTFToAnnotatorOffsetConverter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.PhraseTokenIntegrator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

/**
 * Revised version of XMLExporter that creates XML files from annotations for use in various
 * annotation programs such as Knowtator and eHost
 *
 * @author Glenn T. Gobbel Nov 28, 2016
 */
public class XMLExporterRevised extends Exporter {
  private enum RunType {
    XML_FILES_TO_TAB_DELIMITED;
  }

  private AnnotationApp targetApplication;

  private File exportDirectory;

  private boolean overwriteExisting = false;

  public XMLExporterRevised() {

  }


  /**
   * @param targetApplication
   * @param exportDirectory
   * @param overwriteExisting
   */

  public XMLExporterRevised(AnnotationApp targetApplication, File exportDirectory,
      boolean overwriteExisting) {
    /*
     * Get target application for annotations if not specified
     */
    if (targetApplication == null) {
      XMLExporterRevised tempExporter =
          XMLExporterRevised.getExporter(exportDirectory, overwriteExisting);
      this.targetApplication = tempExporter.targetApplication;
      this.exportDirectory = tempExporter.exportDirectory;
      this.overwriteExisting = tempExporter.overwriteExisting;
      return;
    }

    this.targetApplication = targetApplication;
    this.exportDirectory = exportDirectory;
    this.overwriteExisting = overwriteExisting;
  }

  public void exportAnnotationGroup(AnnotationGroup inputGroup, boolean correctOffsets,
      boolean insertPhraseStrings, boolean useReferenceAnnotations) {
    RaptatDocument raptatDocument = inputGroup.getRaptatDocument();
    List<AnnotatedPhrase> annotations =
        useReferenceAnnotations ? inputGroup.referenceAnnotations : inputGroup.raptatAnnotations;

    String docPath = raptatDocument.getTextSourcePath().get();
    System.out.println("Exporting:" + docPath);
    if (insertPhraseStrings) {
      boolean insertIntoDictionaryAnnotations = true;
      insertPhraseStrings(annotations, raptatDocument.getTextSourcePath().get(),
          insertIntoDictionaryAnnotations);
    }

    if (correctOffsets) {
      UTFToAnnotatorOffsetConverter offsetConverter =
          new UTFToAnnotatorOffsetConverter(docPath, this.targetApplication);
      offsetConverter.convertAnnotatedPhraseOffsetList(annotations);
    }

    writeAnnotations(inputGroup, new File(docPath), this.exportDirectory.getAbsolutePath(),
        useReferenceAnnotations);
  }

  @Override
  public void exportAnnotationGroup(ExportAnnonationGroupParameter exportAnnonationGroupParameter)
      throws ExporterException {
    // TODO Auto-generated method stub

  }

  public void exportRaptatAnnotationGroup(AnnotationGroup inputGroup, boolean correctOffsets,
      boolean insertPhraseStrings) {
    exportAnnotationGroup(inputGroup, correctOffsets, insertPhraseStrings, false);
  }

  @Override
  public void exportRaptatAnnotationGroup(
      ExportAnnonationGroupParameter exportAnnonationGroupParameter) {
    // TODO Auto-generated method stub

  }

  public void exportReferenceAnnotationGroup(AnnotationGroup inputGroup, boolean correctOffsets,
      boolean insertPhraseStrings) {
    exportAnnotationGroup(inputGroup, correctOffsets, insertPhraseStrings, true);

  }

  @Override
  public void exportReferenceAnnotationGroup(
      ExportAnnonationGroupParameter exportAnnonationGroupParameter) {
    // TODO Auto-generated method stub

  }

  /**
   * @return the resultFolder
   */
  public File getResultFolder() {
    return this.exportDirectory;
  }

  /**
   * @return the targetApplication
   */
  public AnnotationApp getTargetApplication() {
    return this.targetApplication;
  }

  /**
   * @return the overwriteExisting
   */
  public boolean isOverwriteExisting() {
    return this.overwriteExisting;
  }

  /**
   * @param overwriteExisting the overwriteExisting to set
   */
  public void setOverwriteExisting(boolean overwriteExisting) {
    this.overwriteExisting = overwriteExisting;
  }

  /**
   * @param resultFolder the resultFolder to set
   */
  @Override
  public void setResultFolder(File resultFolder) {
    this.exportDirectory = resultFolder;
  }

  /**
   * @param targetApplication the targetApplication to set
   */
  public void setTargetApplication(AnnotationApp targetApplication) {
    this.targetApplication = targetApplication;
  }

  /**
   * Write EHost compliant XML knowtator file using the Annotated Phrase Data
   *
   * @author Glenn Gobbel - May 29, 2013
   *
   * @param inputGroup annotated phrase data
   * @param annotatedFile
   * @param targetFolderPath
   */
  @Override
  public void writeAnnotations(AnnotationGroup inputGroup, File annotatedFile,
      String targetFolderPath, boolean useReferenceAnnotations) {
    Document doc = createXMLDocument(inputGroup.getRaptatDocument());
    List<AnnotatedPhrase> annotations =
        useReferenceAnnotations ? inputGroup.referenceAnnotations : inputGroup.raptatAnnotations;
    String annotatorSource = useReferenceAnnotations ? "REFERENCE" : "RAPTAT";
    switch (this.targetApplication) {
      case EHOST:
        attachEhostAnnotations(annotations, doc.getRootElement(), annotatorSource);
        break;
      case KNOWTATOR:
        attachKnowtatorAnnotations(annotations, doc.getRootElement(), annotatorSource);
        break;
    }

    if (targetFolderPath == null || targetFolderPath.isEmpty()) {
      targetFolderPath = annotatedFile.getParent();
    }
    writeXMLFile(targetFolderPath, "", annotatedFile, doc);
  }

  /**
   * Prepare the root XML element for EHost compatible XML file output using annotated phrase list
   *
   * @param thePhrases annotated phrases
   * @param annotatorSource
   * @return element containing XML output file data
   */
  private void attachEhostAnnotations(List<AnnotatedPhrase> thePhrases, Element rootElement,
      String annotatorSource) {
    AnnotatedPhrase iterator;

    for (AnnotatedPhrase phraseData : thePhrases) {
      if (phraseData.getPrevPhrase() != null) {
        continue;
      }

      List<String> slotMentionList = new ArrayList<>();

      Element annotation = new Element("annotation");

      Element mentionId = new Element("mention");
      mentionId.setAttribute("id", "RAPTAT_INSTANCE_" + phraseData.getMentionId());
      annotation.addContent(mentionId);

      Element annotator = new Element("annotator");
      annotator.setAttribute("id", annotatorSource);
      annotator.setText(annotatorSource);
      annotation.addContent(annotator);

      Element span = new Element("span");
      span.setAttribute("start", phraseData.getRawTokensStartOff());
      span.setAttribute("end", phraseData.getRawTokensEndOff());
      annotation.addContent(span);

      String spanText = phraseData.getSequenceStringUnprocessed();

      // Scope for the multiple spanned annotations
      iterator = phraseData;

      while ((iterator = iterator.getNextPhrase()) != null) {
        span = new Element("span");
        span.setAttribute("start", iterator.getRawTokensStartOff());
        span.setAttribute("end", iterator.getRawTokensEndOff());
        annotation.addContent(span);

        spanText += " ... " + iterator.getSequenceStringUnprocessed();
      }

      Element spannedText = new Element("spannedText");
      spannedText.setText(spanText);
      annotation.addContent(spannedText);

      Element creationDate = new Element("creationDate");
      creationDate.setText(phraseData.getCreationDate());
      annotation.addContent(creationDate);

      rootElement.addContent(annotation);

      for (RaptatAttribute phraseAttribute : phraseData.getPhraseAttributes()) {
        Element stringSlotMention = new Element("stringSlotMention");
        stringSlotMention.setAttribute("id", "RAPTAT_INSTANCE_" + phraseAttribute.getID());

        Element mentionSlot = new Element("mentionSlot");
        mentionSlot.setAttribute("id", phraseAttribute.getName());
        stringSlotMention.addContent(mentionSlot);

        for (String value : phraseAttribute.getValues()) {
          Element stringSlotMentionValue = new Element("stringSlotMentionValue");
          stringSlotMentionValue.setAttribute("value", value);
          stringSlotMention.addContent(stringSlotMentionValue);
        }

        slotMentionList.add("RAPTAT_INSTANCE_" + phraseAttribute.getID());
        rootElement.addContent(stringSlotMention);
      }

      for (ConceptRelation relation : phraseData.getConceptRelations()) {
        Element complexSlotMention = new Element("complexSlotMention");
        complexSlotMention.setAttribute("id", "RAPTAT_INSTANCE_" + relation.getID());

        Element mentionSlot = new Element("mentionSlot");
        mentionSlot.setAttribute("id", relation.getConceptRelationName());
        complexSlotMention.addContent(mentionSlot);

        for (RaptatAttribute conceptAttribute : relation.getAttributes()) {
          for (String value : conceptAttribute.getValues()) {
            Element attribute = new Element("attribute");
            attribute.setAttribute("id", conceptAttribute.getName());
            attribute.setText(value);
            complexSlotMention.addContent(attribute);
          }
        }

        String annotationID = relation.getRelatedAnnotationID();
        Element complexSlotMentionValue = new Element("complexSlotMentionValue");
        complexSlotMentionValue.setAttribute("value", "RAPTAT_INSTANCE_" + annotationID);
        complexSlotMention.addContent(complexSlotMentionValue);

        slotMentionList.add("RAPTAT_INSTANCE_" + relation.getID());
        rootElement.addContent(complexSlotMention);
      }

      Element classMention = new Element("classMention");
      classMention.setAttribute("id", "RAPTAT_INSTANCE_" + phraseData.getMentionId());

      for (String slotMention : slotMentionList) {
        Element hasSlotMention = new Element("hasSlotMention");
        hasSlotMention.setAttribute("id", slotMention);
        classMention.addContent(hasSlotMention);
      }

      Element mentionClass = new Element("mentionClass");
      mentionClass.setAttribute("id", phraseData.getConceptName());
      mentionClass.setText(spanText);

      classMention.addContent(mentionClass);
      rootElement.addContent(classMention);
    }

  }

  /**
   * Attaches annotations to an XML root element and puts them in Knowtator format for XML output
   *
   * @param thePhrases
   * @param inputRoot
   */
  private void attachKnowtatorAnnotations(List<AnnotatedPhrase> thePhrases, Element inputRoot,
      String annotatorSource) {
    /** Write annotation elements followed by classMention elements */

    for (AnnotatedPhrase phraseData : thePhrases) {
      Element annotation = new Element("annotation");

      Element mentionId = new Element("mention");
      mentionId.setAttribute("id", "RAPTAT_INSTANCE_" + phraseData.getMentionId());
      annotation.addContent(mentionId);

      Element annotator = new Element("annotator");
      annotator.setAttribute("id", annotatorSource);
      annotator.setText(annotatorSource);
      annotation.addContent(annotator);

      Element span = new Element("span");
      span.setAttribute("start", phraseData.getRawTokensStartOff());
      span.setAttribute("end", phraseData.getRawTokensEndOff());
      annotation.addContent(span);

      /*
       * Concept name - Added by Karuna - July 7, 2018 to add concept name in the mentionClass node
       */
      Element concept = new Element("concept");
      concept.setAttribute("concept", phraseData.getConceptName());
      annotation.addContent(concept);

      Element spannedText = new Element("spannedText");
      spannedText.setText(phraseData.getSequenceStringUnprocessed());
      annotation.addContent(spannedText);

      Element creationDate = new Element("creationDate");
      creationDate.setText(phraseData.getCreationDate());
      annotation.addContent(creationDate);

      /*
       * Concept name - Added by Karuna - July 7, 2018 to add concept name in the mentionClass node
       */
      Element mentionClassId = new Element("mentionClassId");
      mentionClassId.setText(phraseData.getConceptName());
      annotation.addContent(mentionClassId);

      inputRoot.addContent(annotation);

      // Class mention data
      Element classMention = new Element("classMention");
      classMention.setAttribute("id", "RAPTAT_INSTANCE_" + phraseData.getMentionId());

      Element mentionClass = new Element("mentionClass");
      mentionClass.setAttribute("id", phraseData.getConceptName());
      mentionClass.setText(phraseData.getConceptName());
      classMention.addContent(mentionClass);

      /*
       * Annotation attributes - Added by sanjib - April 1, 2015 to add attribute and relationdata
       * for annotations
       */
      for (RaptatAttribute attr : phraseData.getPhraseAttributes()) {
        mentionClass = new Element("hasSlotMention");
        mentionClass.setAttribute("id", "RAPTAT_INSTANCE_" + attr.getID());
        classMention.addContent(mentionClass);

        Element slotMention = new Element("stringSlotMention");
        slotMention.setAttribute("id", "RAPTAT_INSTANCE_" + attr.getID());

        Element slotClass = new Element("mentionSlot");
        slotClass.setAttribute("id", attr.getName());
        slotMention.addContent(slotClass);

        for (String value : attr.getValues()) {
          Element slotValue = new Element("stringSlotMentionValue");
          slotValue.setAttribute("value", value);
          slotMention.addContent(slotValue);
        }

        inputRoot.addContent(slotMention);
      }

      try {
        /*
         * Annotation relations
         */
        for (ConceptRelation conceptRelation : phraseData.getConceptRelations()) {
          mentionClass = new Element("hasSlotMention");
          mentionClass.setAttribute("id", "RAPTAT_INSTANCE_" + conceptRelation.getID());
          classMention.addContent(mentionClass);

          Element conceptMention = new Element("complexSlotMention");
          conceptMention.setAttribute("id", "RAPTAT_INSTANCE_" + conceptRelation.getID());

          Element conceptClass = new Element("mentionSlot");
          conceptClass.setAttribute("id", conceptRelation.getConceptRelationName());
          conceptMention.addContent(conceptClass);

          String value = conceptRelation.getRelatedAnnotationID();

          Element slotValue = new Element("complexSlotMentionValue");
          slotValue.setAttribute("value", "RAPTAT_INSTANCE_" + value);
          conceptMention.addContent(slotValue);

          inputRoot.addContent(conceptMention);
        }
      } catch (Exception ex) {
        // Skip over it
      }

      /*
       * Note by Glenn - 11/28/16 - Not sure why this is here; seems redundant
       */
      inputRoot.addContent(classMention);
    }

  }

  private Document createXMLDocument(RaptatDocument inputDocument) {
    Document resultDocument = new Document();
    Element rootElement = new Element("annotations");
    rootElement.setAttribute("textSource", inputDocument.getTextSourceName().get());
    resultDocument.setRootElement(rootElement);
    return resultDocument;
  }

  /*************************************************************
   * @param allAnnData
   *
   * @author Glenn Gobbel - Jun 21, 2012
   *************************************************************/
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

  private String writeXMLFile(String filePath, String fileMarker, File annotatedFile,
      Document xmlDocument) {
    int overwriteFile = JOptionPane.YES_OPTION;
    File outputFile = new File(
        filePath + File.separator + annotatedFile.getName() + fileMarker + ".knowtator.xml");

    try {
      if (!this.overwriteExisting && outputFile.createNewFile() != true) {
        overwriteFile = JOptionPane.showConfirmDialog(null, "An XML file with this name\n"
            + "already exists in this directory.\n" + "Do you want to replace it?");
      }
      if (overwriteFile == JOptionPane.YES_OPTION) {
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        FileWriter writer = new FileWriter(outputFile, false);
        out.output(xmlDocument, writer);
        writer.close();

        return outputFile.getAbsolutePath();
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    return null;
  }

  public static XMLExporterRevised getExporter(File resultFolder, boolean overwriteExisting) {
    String queryString = "Application for viewing annotations?";
    String[] buttonNames = new String[AnnotationApp.values().length];
    int i = 0;
    for (AnnotationApp app : AnnotationApp.values()) {
      buttonNames[i++] = app.getCommonName();
    }
    ButtonQueryDialog bqd = new ButtonQueryDialog(buttonNames, queryString);
    String annotationSource = bqd.showQuery();
    AnnotationApp annotationApp = AnnotationApp.EHOST;
    for (AnnotationApp app : AnnotationApp.values()) {
      if (annotationSource.toLowerCase().equals(app.toString().toLowerCase())) {
        annotationApp = app;
        break;
      }
    }
    return new XMLExporterRevised(annotationApp, resultFolder, overwriteExisting);
  }

  public static void main(String[] args) {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        RunType runType = RunType.XML_FILES_TO_TAB_DELIMITED;

        switch (runType) {
          case XML_FILES_TO_TAB_DELIMITED:
            // String xmlFolderPath =
            // "D:\\ORD_Matheny_201312053D\\Glenn\\AKI_AnalysisForManuscript_161108\\Evaluation_161124\\TestingXML_UncertainToPositive";
            // String txtFolderPath =
            // "D:\\ORD_Matheny_201312053D\\Glenn\\AKI_AnalysisForManuscript_161108\\Evaluation_161124\\TestingCorpus";
            // String xmlFolderPath =
            // "P:\\ORD_Luther_201308073D\\Glenn\\AnnotationsPass2ForNLP\\LivingSituationPromotedAndCorrected_171220\\xml";
            // String txtFolderPath =
            // "P:\\ORD_Luther_201308073D\\Glenn\\AnnotationsPass2ForNLP\\LivingSituationPromotedAndCorrected_171220\\txt";

            String xmlFolderPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\ManuscriptDataCanaryTool_200722\\IAA\\NancyAnnotations_Groups_9_10_11_12_Xml";
            String txtFolderPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\ManuscriptDataCanaryTool_200722\\IAA\\CorpusGroups_09_10_11_12";

            File resultFile =
            XMLExporterRevised.xmlFilesToTabDelimited(xmlFolderPath, txtFolderPath);
            System.out.println("Tab delimited file saved at:" + resultFile.getAbsolutePath());
            break;
          default:
            break;
        }
        System.exit(0);

      }

    });
  }

  public static File xmlFilesToTabDelimited(String xmlFolderPath, String txtFolderPath) {
    return xmlFilesToTabDelimited(xmlFolderPath, txtFolderPath, null);
  }

  public static File xmlFilesToTabDelimited(String xmlFolderPath, String txtFolderPath,
      Set<String> conceptSet) {
    AnnotationImporter xmlImporter = AnnotationImporter.getImporter();
    TextAnalyzer ta = new TextAnalyzer();
    PhraseTokenIntegrator integrator = new PhraseTokenIntegrator();
    PrintWriter pw = null;
    String pwFilePath =
        xmlFolderPath + File.separator + "Annotations_" + GeneralHelper.getTimeStamp() + ".txt";
    File pwFile = new File(pwFilePath);
    try {
      pw = new PrintWriter(pwFile);
    } catch (FileNotFoundException e) {
      GeneralHelper.errorWriter("Unable to create printwriter");
      e.printStackTrace();
      System.exit(-1);
    }

    Collection<File> xmlFiles =
        FileUtils.listFiles(new File(xmlFolderPath), new String[] {"xml"}, false);

    Collection<File> txtFiles =
        FileUtils.listFiles(new File(txtFolderPath), new String[] {"txt"}, false);
    HashMap<String, File> xmlToTxtFileMap = new HashMap<>(txtFiles.size());
    for (File txtFile : txtFiles) {
      xmlToTxtFileMap.put(txtFile.getName() + ".knowtator.xml", txtFile);
    }

    for (File curXmlFile : xmlFiles) {
      System.out.println("Importing and Writing:" + curXmlFile.getName());
      File curTxtFile = xmlToTxtFileMap.get(curXmlFile.getName());
      xmlImporter.writeXMLToTab(curXmlFile, curTxtFile, ta, integrator, pw, conceptSet);
    }

    return pwFile;
  }

}
