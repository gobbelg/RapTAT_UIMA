/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.StartOffsetPhraseComparator;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.PhraseTokenIntegrator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

/**
 * Class to modify attributes - it takes the values of attributes and promotes them to concepts.
 * Also can take concepts and remap them to other concepts.
 *
 * @author glenn
 *
 */
public class AnnotationMutator {

  public enum ConceptRelationshipKey {
    CONCEPT, RELATIONSHIP;
  }

  private enum RunType {
    TEST;
  }

  private final static Logger LOGGER = Logger.getLogger(AnnotationMutator.class);

  private static StartOffsetPhraseComparator phraseSorter = new StartOffsetPhraseComparator();

  public AnnotationMutator() {
    AnnotationMutator.LOGGER.setLevel(Level.OFF);
  }

  public void combineAttributeValuesIntoConcepts(String xmlSourceFolderPath,
      String textSourceFolderPath, String targetFolderPath, String schemaConceptFilePath,
      String acceptedConceptsPath) {
    XMLExporterRevised exporter = XMLExporterRevised.getExporter(new File(targetFolderPath), true);

    List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaConceptFilePath);

    HashSet<String> acceptedConcepts =
        SchemaConcept.getAcceptableConceptNames(acceptedConceptsPath);

    AnnotationImporter importer = new AnnotationImporter(AnnotationApp.EHOST);

    List<RaptatPair<File, File>> txtAndXmlFiles =
        GeneralHelper.getXmlFilesAndMatchTxt(textSourceFolderPath, xmlSourceFolderPath);

    Iterator<RaptatPair<File, File>> fileIterator = txtAndXmlFiles.iterator();

    while (fileIterator.hasNext()) {
      RaptatPair<File, File> txtAndXmlFilePair = fileIterator.next();
      String txtFilePath = txtAndXmlFilePair.left.getAbsolutePath();
      String xmlFilePath = txtAndXmlFilePair.right.getAbsolutePath();

      List<AnnotatedPhrase> annotations = importer.importAnnotations(xmlFilePath, txtFilePath,
          acceptedConcepts, schemaConcepts, null, false, false, false);

      abiProjectCombineAttributesForConcepts(annotations);

      RaptatDocument doc = new RaptatDocument();
      doc.setTextSourcePath(Optional.of(txtFilePath));
      AnnotationGroup annotationGroup = new AnnotationGroup(doc, null);
      annotationGroup.setRaptatAnnotations(annotations);

      System.out.println("Exporting:" + xmlFilePath);
      exporter.exportRaptatAnnotationGroup(annotationGroup, false, false);
    }

  }

  public List<AnnotationGroup> filterAnnotations(File targetAnnotationsDirectory,
      File targetAnnotationsSchemaFile, HashSet<String> targetConcepts,
      File filteringAnnotationsDirectory, File filteringAnnotationsSchemaFile,
      HashSet<String> filteringConcepts, File textFileDirectory) {
    List<SchemaConcept> targetSchemaConcepts =
        SchemaImporter.importSchemaConcepts(targetAnnotationsSchemaFile);
    List<SchemaConcept> filteringSchemaConcepts =
        SchemaImporter.importSchemaConcepts(filteringAnnotationsSchemaFile);

    List<File> targetFiles = Arrays.asList(targetAnnotationsDirectory.listFiles());
    List<File> filteringFiles = Arrays.asList(filteringAnnotationsDirectory.listFiles());
    List<File> textFiles = Arrays.asList(textFileDirectory.listFiles());

    Collections.sort(targetFiles);
    Collections.sort(filteringFiles);
    Collections.sort(textFiles);

    List<RaptatPair<AnnotationGroup, List<AnnotatedPhrase>>> filterPairs =
        getFilteringPairs(targetConcepts, filteringConcepts, targetSchemaConcepts,
            filteringSchemaConcepts, targetFiles, filteringFiles, textFiles);

    List<AnnotationGroup> filteredAnnotationGroups = this.filterAnnotations(filterPairs);

    return filteredAnnotationGroups;
  }

  public void promoteValuesToConcepts(String xmlSourceFolderPath, String textSourceFolderPath,
      String targetFolderPath, String schemaConceptFilePath, String acceptedConceptsPath) {
    XMLExporterRevised exporter = XMLExporterRevised.getExporter(new File(targetFolderPath), true);

    List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaConceptFilePath);

    HashSet<String> acceptedConcepts =
        SchemaConcept.getAcceptableConceptNames(acceptedConceptsPath);

    AnnotationImporter importer = new AnnotationImporter(AnnotationApp.EHOST);

    String targetConceptName = "ptsd_sign_symptom";
    String targetAttributeName = "symptomcategory";

    List<RaptatPair<File, File>> txtAndXmlFiles =
        GeneralHelper.getXmlFilesAndMatchTxt(textSourceFolderPath, xmlSourceFolderPath);

    Iterator<RaptatPair<File, File>> fileIterator = txtAndXmlFiles.iterator();

    while (fileIterator.hasNext()) {
      RaptatPair<File, File> txtAndXmlFilePair = fileIterator.next();
      String txtFilePath = txtAndXmlFilePair.left.getAbsolutePath();
      String xmlFilePath = txtAndXmlFilePair.right.getAbsolutePath();

      List<AnnotatedPhrase> annotations = importer.importAnnotations(xmlFilePath, txtFilePath,
          acceptedConcepts, schemaConcepts, null);

      promoteAttributeValuesToConcepts(annotations, targetConceptName, targetAttributeName);

      RaptatDocument doc = new RaptatDocument();
      doc.setTextSourcePath(Optional.of(txtFilePath));
      AnnotationGroup annotationGroup = new AnnotationGroup(doc, null);
      annotationGroup.setRaptatAnnotations(annotations);

      System.out.println("Exporting:" + xmlFilePath);
      exporter.exportRaptatAnnotationGroup(annotationGroup, true, false);
    }

  }

  /**
   * Remaps concepts and relationships to new names. The method examines all the annotations in the
   * baseAnnotations list. If the examined annotation concept name is within the remapping HashMap
   * and it contains the relationship relationshipName, it remaps the relationshipName to the name
   * retrieved using the name of the concept as a key.
   *
   * May 7, 2018
   *
   * @param xmlAnnotations
   * @param targetedRelationship
   * @param remapping
   * @return
   */
  public void remapRelationship(List<AnnotatedPhrase> xmlAnnotations, String targetedConcept,
      String targetedRelationship,
      HashMap<String, HashMap<ConceptRelationshipKey, String>> remapping) {
    HashMap<String, AnnotatedPhrase> mentionIdToAnnotatedPhraseMap = new HashMap<>();
    List<AnnotatedPhrase> targetedAnnotations = new ArrayList<>();
    List<AnnotatedPhrase> targetedAnnotationsUpdated = new ArrayList<>();

    if (AnnotationMutator.LOGGER.isDebugEnabled()) {
      AnnotationMutator.LOGGER.debug("Start of remapRelationship for concept:" + targetedConcept
          + " relationship:" + targetedRelationship);
      AnnotationMutator.LOGGER.debug("\n\nStarting " + xmlAnnotations.size() + " annotations:");
      for (int i = 0; i < xmlAnnotations.size(); i++) {
        System.out.println("\n\nAnnotation " + i + ":\n" + xmlAnnotations.get(i));
      }
    }
    /*
     * Select all phrases that have targetedConcept, remove them, and put them in another list.
     * We'll need them later to remap concept and relationship names.
     *
     * Also, while iterating through the concepts, store a map from id to each annotated phrase. We
     * need to find the concept the targetedConcept is linked to in order to determine how to remap
     * the concept and relationship name
     */
    ListIterator<AnnotatedPhrase> annotationIterator = xmlAnnotations.listIterator();
    while (annotationIterator.hasNext()) {
      AnnotatedPhrase annotatedPhrase = annotationIterator.next();
      AnnotationMutator.LOGGER.debug("Processsing annotation:\n" + annotatedPhrase + "\n\n");
      mentionIdToAnnotatedPhraseMap.put(annotatedPhrase.getMentionId(), annotatedPhrase);

      if (annotatedPhrase.getConceptName().equalsIgnoreCase(targetedConcept)) {
        targetedAnnotations.add(annotatedPhrase);
        annotationIterator.remove();

        AnnotationMutator.LOGGER
            .debug("\n\nPhrase:" + annotatedPhrase + " added to targetedAnnotations\n\n");
      }
    }

    if (AnnotationMutator.LOGGER.isDebugEnabled()) {
      AnnotationMutator.LOGGER
          .debug("Completed Transfer of targetedAnnotations\nCurrent xmlAnnotations:");
      for (int i = 0; i < xmlAnnotations.size(); i++) {
        System.out.println("\n\nAnnotation " + i + ":" + xmlAnnotations.get(i));
      }
      AnnotationMutator.LOGGER.debug("targetedAnnotations:");
      for (int i = 0; i < targetedAnnotations.size(); i++) {
        System.out.println("\n\nAnnotation " + i + ":" + targetedAnnotations.get(i));
      }
    }
    ListIterator<AnnotatedPhrase> targetedAnnotationIterator = targetedAnnotations.listIterator();

    /*
     * Process all the targeted concept annotations found in the previous loop. For every concept
     * relation found that has the name targetedRelationship, create a new annotation with a new
     * concept name specified by the name of the concept that the relation links to and the
     * remapping map supplied as a parameter.
     */
    while (targetedAnnotationIterator.hasNext()) {
      AnnotatedPhrase targetedAnnotation = targetedAnnotationIterator.next();
      List<ConceptRelation> potentialTargetedRelations = targetedAnnotation.getConceptRelations();

      AnnotationMutator.LOGGER.debug("\n\n=====================================\n"
          + "Processing targeted annotation:\n" + targetedAnnotation + "\n\n");
      if (AnnotationMutator.LOGGER.isDebugEnabled()) {
        AnnotationMutator.LOGGER.debug("Finding targeted relations from all relations:\n\n");
        for (int i = 0; i < potentialTargetedRelations.size(); i++) {
          System.out.println("Relation " + i + ":" + potentialTargetedRelations.get(i) + "\n\n");
        }
        System.out.print("\n\n");
      }

      AnnotationMutator.LOGGER
          .debug("Finding targeted relations with name:" + targetedRelationship);
      List<ConceptRelation> targetedRelations =
          extractTargetedRelations(targetedRelationship, potentialTargetedRelations);

      if (AnnotationMutator.LOGGER.isDebugEnabled()) {
        AnnotationMutator.LOGGER.debug("Completed find targeted relations\nRemaining relations:");
        for (ConceptRelation conceptRelation : potentialTargetedRelations) {
          System.out.println(conceptRelation);
        }

        AnnotationMutator.LOGGER
            .debug("Targeted Relations with name " + targetedRelationship + ":");
        for (ConceptRelation conceptRelation : targetedRelations) {
          System.out.println(conceptRelation);
        }
      }

      /*
       * If there are no targeted relationships, just place the annotation, as is, into the list of
       * new annotations
       */
      if (targetedRelations.isEmpty()) {
        AnnotationMutator.LOGGER.debug("No targeted relations found for current concept");
        targetedAnnotationsUpdated.add(targetedAnnotation);
      }
      /*
       * Else create a new annotation for each of the targeted relations stored in
       * targetedRelationsNew and rename them according to the remapping HashMap
       */
      else {
        ListIterator<ConceptRelation> relationsIterator = targetedRelations.listIterator();

        AnnotationMutator.LOGGER.debug("Targeted relations found\nProcessing targeted relations");

        while (relationsIterator.hasNext()) {
          ConceptRelation targetedRelation = relationsIterator.next();

          AnnotationMutator.LOGGER
              .debug("Processing linked concept ID of targeted relation:" + targetedRelation);

          /*
           * We add one replacementRelation for every linkedConcept because of the remapping
           */
          String linkedConceptID = targetedRelation.getRelatedAnnotationID();

          AnnotationMutator.LOGGER.debug("Processing linked concept ID:" + linkedConceptID);
          /*
           * Note that the copy constructor for AnnotatedPhrase does NOT do a deep copy of the
           * conceptRelationList, so the relationsIterator, instantiated above, will apply also to
           * the new AnnotatedPhrase instance, annotatedPhraseUpdated
           */
          AnnotationMutator.LOGGER
              .debug("Creating copy of targeted annotation:" + targetedAnnotation);
          AnnotatedPhrase annotatedPhraseUpdated = new AnnotatedPhrase(targetedAnnotation);

          AnnotationMutator.LOGGER
              .debug("Completed copy of TargetedAnnotation:" + targetedAnnotation);
          AnnotationMutator.LOGGER
              .debug("Coped to updated Annotated Phrase:" + annotatedPhraseUpdated);

          ConceptRelation replacementRelation = new ConceptRelation(targetedRelation);

          AnnotationMutator.LOGGER.debug("Copied targeted relation to:" + replacementRelation);

          replacementRelation.setRelatedAnnotationID(linkedConceptID);

          if (AnnotationMutator.LOGGER.isDebugEnabled()) {
            AnnotationMutator.LOGGER.debug("Adding relation " + replacementRelation + "to:");
            for (ConceptRelation conceptRelation : potentialTargetedRelations) {
              AnnotationMutator.LOGGER.debug("\tPotentialTargetedRelation:" + conceptRelation);
            }
          }

          potentialTargetedRelations.add(replacementRelation);

          if (AnnotationMutator.LOGGER.isDebugEnabled()) {
            AnnotationMutator.LOGGER
                .debug("Completed adding relation " + replacementRelation + "to:");
            for (ConceptRelation conceptRelation : potentialTargetedRelations) {
              AnnotationMutator.LOGGER.debug("\tPotentialTargetedRelation:" + conceptRelation);
            }
          }

          AnnotatedPhrase linkedAnnotation = mentionIdToAnnotatedPhraseMap.get(linkedConceptID);
          String linkedAnnotationConceptName = linkedAnnotation.getConceptName();
          HashMap<ConceptRelationshipKey, String> conceptAndRelationNameMap =
              remapping.get(linkedAnnotationConceptName);

          /*
           * If the conceptAndRelationNameMap has the concept name, then do the remapping of concept
           * and relationship name
           */
          if (conceptAndRelationNameMap != null) {
            String remappedConceptName =
                conceptAndRelationNameMap.get(ConceptRelationshipKey.CONCEPT);
            annotatedPhraseUpdated.setConceptName(remappedConceptName);
            targetedAnnotationsUpdated.add(annotatedPhraseUpdated);

            String replacementRelationName =
                conceptAndRelationNameMap.get(ConceptRelationshipKey.RELATIONSHIP);
            replacementRelation.setConceptRelationName(replacementRelationName);

            AnnotationMutator.LOGGER.debug("Completed remapping of concept relation:"
                + replacementRelation + "\n\tfor conceptID:" + linkedConceptID
                + "\n\tfor targeted relation:" + targetedRelation);
          }

          AnnotationMutator.LOGGER
              .debug("Completed processing targetedRelation:" + targetedRelation);
        }

        AnnotationMutator.LOGGER
            .debug("Completed remapping of targeted annotation:" + targetedAnnotation);

      }
      if (AnnotationMutator.LOGGER.isDebugEnabled()) {
        AnnotationMutator.LOGGER.debug("Updated list of targeted Annotations");
        for (int i = 0; i < targetedAnnotationsUpdated.size(); i++) {
          System.out.println("Annotation " + i + ":" + targetedAnnotationsUpdated.get(i));
        }
      }
    }

    xmlAnnotations.addAll(targetedAnnotationsUpdated);

    if (AnnotationMutator.LOGGER.isDebugEnabled()) {
      System.out.println("Completed xml annotations:\n\n");
      AnnotationMutator.LOGGER.debug("Final " + xmlAnnotations.size() + " annotations:");
      for (int i = 0; i < xmlAnnotations.size(); i++) {
        System.out.println("\n\nAnnotation " + i + ":\n" + xmlAnnotations.get(i));
      }
    }

    Collections.sort(xmlAnnotations, AnnotationMutator.phraseSorter);
  }

  public void removeAttributesFromPhrases(String targetAttribute, String acceptedConceptsPath,
      String xmlSourceFolderPath, String txtSourceFolderPath, String targetXmlFolderPath) {
    AnnotationImporter xmlImporter = new AnnotationImporter(AnnotationApp.EHOST);
    TextAnalyzer ta = new TextAnalyzer();
    PhraseTokenIntegrator integrator = new PhraseTokenIntegrator();
    File targetFolderFile = new File(targetXmlFolderPath);
    XMLExporterRevised xmlExporter =
        new XMLExporterRevised(AnnotationApp.EHOST, targetFolderFile, true);


    HashSet<String> acceptedConcepts = SchemaConcept.getConcepts(acceptedConceptsPath);

    List<RaptatPair<File, File>> txtAndXmlFiles =
        GeneralHelper.getXmlFilesAndMatchTxt(txtSourceFolderPath, xmlSourceFolderPath);

    int numberOfFiles = txtAndXmlFiles.size();
    int fileCounter = 1;
    Iterator<RaptatPair<File, File>> fileIterator = txtAndXmlFiles.iterator();

    while (fileIterator.hasNext()) {
      RaptatDocument raptatDocument = null;
      List<AnnotatedPhrase> thePhrases = null;

      try {
        RaptatPair<File, File> txtAndXmlFilePair = fileIterator.next();
        String txtFilePath = txtAndXmlFilePair.left.getAbsolutePath();
        String xmlFilePath = txtAndXmlFilePair.right.getAbsolutePath();

        System.out.println("Importing file " + fileCounter++ + " of " + numberOfFiles + ":"
            + txtAndXmlFilePair.left.getName());

        /* Import the annotations */
        thePhrases = xmlImporter.importAnnotations(xmlFilePath, txtFilePath, acceptedConcepts);
        if (thePhrases == null) {
          thePhrases = new ArrayList<>();
        }

        /*
         * If we have a valid TextAnalyzer and PhraseTokenIntegrator instances, add tokens to
         * annotations so we can get the sentences associated with annotations
         */
        if (ta != null && integrator != null) {
          raptatDocument = ta.processDocument(txtFilePath);
          integrator.addProcessedTokensToAnnotations(thePhrases, raptatDocument, true, 0);
        }

        filterOutAttribute(targetAttribute, thePhrases);

      }

      catch (Exception e) {
        System.out.println(e);
        e.printStackTrace();
      }

      AnnotationGroup annotationGroup = new AnnotationGroup(raptatDocument, null);
      annotationGroup.setRaptatAnnotations(thePhrases);
      xmlExporter.exportRaptatAnnotationGroup(annotationGroup, true, true);
    }

  }

  /**
   * Replace any annotations from the baseAnnotations list with overlapping annotations from the
   * replacementAnnotations list
   *
   * @param baseAnnotations
   * @param replacementAnnotations
   * @return
   */
  public List<AnnotatedPhrase> replaceOverlapped(List<AnnotatedPhrase> baseAnnotations,
      List<AnnotatedPhrase> replacementAnnotations) {

    Collections.sort(baseAnnotations, phraseSorter);
    Collections.sort(replacementAnnotations, phraseSorter);
    List<AnnotatedPhrase> resultAnnotations = new ArrayList<>();
    int minReplacementIndex = 0;

    for (int baseIndex = 0; baseIndex < baseAnnotations.size(); baseIndex++) {

      AnnotatedPhrase baseAnnotation = baseAnnotations.get(baseIndex);
      int replacementIndex = minReplacementIndex;
      while (replacementIndex < replacementAnnotations.size()) {
        AnnotatedPhrase replacementAnnotation = replacementAnnotations.get(replacementIndex);
        if (AnnotatedPhrase.rawOffsetsOverlap(baseAnnotation, replacementAnnotation)) {
          resultAnnotations.add(replacementAnnotation);
          minReplacementIndex = replacementIndex;
          break;
        }
        replacementIndex++;
      }
      resultAnnotations.add(baseAnnotation);
    }
    return resultAnnotations;
  }

  private void abiProjectCombineAttributesForConcepts(List<AnnotatedPhrase> annotations) {
    Map<String, Integer> conceptNamePositions = new HashMap<>();
    conceptNamePositions.put("laterality_value", 0);
    conceptNamePositions.put("index_type", 2);

    Map<String, String> mappedAttributeValue = new HashMap<>();
    mappedAttributeValue.put("left", "Left");
    mappedAttributeValue.put("right", "Right");
    mappedAttributeValue.put("abi", "ABI");
    mappedAttributeValue.put("tbi", "TBI");
    for (AnnotatedPhrase annotatedPhrase : annotations) {
      String[] assignedConceptArray = new String[] {"", "_", "", "_Value"};
      for (RaptatAttribute attribute : annotatedPhrase.getPhraseAttributes()) {
        Integer positionIndex;
        if ((positionIndex = conceptNamePositions.get(attribute.getName().toLowerCase())) != null) {
          assignedConceptArray[positionIndex] =
              mappedAttributeValue.get(attribute.getValues().get(0));
        }
      }
      String assignedConceptName = String.join("", assignedConceptArray);
      annotatedPhrase.setConceptName(assignedConceptName);
      annotatedPhrase.setPhraseAttributes(null);
    }
  }

  /**
   * May 8, 2018
   *
   * @param targetedRelationship
   * @param potentialTargetedRelations
   * @return
   */
  private List<ConceptRelation> extractTargetedRelations(String targetedRelationship,
      List<ConceptRelation> potentialTargetedRelations) {
    List<ConceptRelation> targetedRelations = new ArrayList<>();
    ListIterator<ConceptRelation> relationIterator = potentialTargetedRelations.listIterator();
    /*
     * Remove all the concept relations whose name is equal to the targetedRelationship and store
     * them in a separate list. This list, if it is not empty, will be used to generate new
     * concepts.
     */
    while (relationIterator.hasNext()) {
      ConceptRelation conceptRelation = relationIterator.next();
      String conceptRelationName = conceptRelation.getConceptRelationName();

      if (conceptRelationName.equalsIgnoreCase(targetedRelationship)) {
        relationIterator.remove();
        targetedRelations.add(conceptRelation);
      }
    }
    return targetedRelations;
  }

  private AnnotationGroup filterAnnotations(AnnotationGroup groupForFiltering,
      List<AnnotatedPhrase> filterPhrases) {
    HashSet<AnnotatedPhrase> filterSentences = new HashSet<>();
    List<AnnotatedPhrase> resultAnnotations = new ArrayList<>();
    for (AnnotatedPhrase filterPhrase : filterPhrases) {
      AnnotatedPhrase filterPhraseSentence = filterPhrase.getAssociatedSentence();
      AnnotatedPhrase priorSentence = filterPhraseSentence.getPreviousSentence();
      AnnotatedPhrase nextSentence = filterPhraseSentence.getNextSentence();

      filterSentences.add(filterPhraseSentence);
      if (priorSentence != null) {
        filterSentences.add(priorSentence);
      }

      if (nextSentence != null) {
        filterSentences.add(nextSentence);
      }
    }

    for (AnnotatedPhrase targetPhrase : groupForFiltering.referenceAnnotations) {
      if (filterSentences.contains(targetPhrase.getAssociatedSentence())) {
        resultAnnotations.add(targetPhrase);
      }
    }

    return new AnnotationGroup(groupForFiltering.getRaptatDocument(), resultAnnotations);
  }

  private List<AnnotationGroup> filterAnnotations(
      List<RaptatPair<AnnotationGroup, List<AnnotatedPhrase>>> filterPairs) {
    List<AnnotationGroup> resultGroups = new ArrayList<>(filterPairs.size());
    for (RaptatPair<AnnotationGroup, List<AnnotatedPhrase>> raptatPair : filterPairs) {
      AnnotationGroup resultGroup = this.filterAnnotations(raptatPair.left, raptatPair.right);
      resultGroups.add(resultGroup);
    }
    return resultGroups;

  }

  private void filterOutAttribute(String targetAttribute, List<AnnotatedPhrase> thePhrases) {
    String lowerCaseTargetAttribute = targetAttribute.toLowerCase();
    for (AnnotatedPhrase phrase : thePhrases) {
      List<RaptatAttribute> attributes = phrase.getPhraseAttributes();
      Iterator<RaptatAttribute> attributeIterator = attributes.iterator();
      while (attributeIterator.hasNext()) {
        RaptatAttribute attribute = attributeIterator.next();
        if (attribute.getName().toLowerCase().equals(lowerCaseTargetAttribute)) {
          attributeIterator.remove();
        }
      }
    }
  }

  /**
   * @param targetConcepts
   * @param filteringConcepts
   * @param targetSchemaConcepts
   * @param filteringSchemaConcepts
   * @param targetFiles
   * @param filteringFiles
   * @param textFiles
   * @return
   */
  private List<RaptatPair<AnnotationGroup, List<AnnotatedPhrase>>> getFilteringPairs(
      HashSet<String> targetConcepts, HashSet<String> filteringConcepts,
      List<SchemaConcept> targetSchemaConcepts, List<SchemaConcept> filteringSchemaConcepts,
      List<File> targetFiles, List<File> filteringFiles, List<File> textFiles) {
    List<RaptatPair<AnnotationGroup, List<AnnotatedPhrase>>> resultPairList =
        new ArrayList<>(textFiles.size());

    TextAnalyzer ta = new TextAnalyzer();
    PhraseTokenIntegrator pti = new PhraseTokenIntegrator();

    /*
     * Assume files are in such an order that they correspond to one another and process lists.
     */
    Iterator<File> targetFileIterator = targetFiles.iterator();
    Iterator<File> filteringFileIterator = filteringFiles.iterator();
    Iterator<File> textIterator = textFiles.iterator();

    AnnotationImporter annotationImporter = AnnotationImporter.getImporter();
    while (targetFileIterator.hasNext() && filteringFileIterator.hasNext()
        && textIterator.hasNext()) {
      File targetFile = targetFileIterator.next();
      File filteringFile = filteringFileIterator.next();
      File textFile = textIterator.next();

      /*
       * Check to be that files correspond (both reference and raptat files should refer to text
       * file)
       */
      String textFileName = textFile.getName();
      if (!targetFile.getName().contains(textFileName)
          || !filteringFile.getName().contains(textFileName)) {
        GeneralHelper.errorWriter("Unmatched text file");
        System.exit(-1);
      }

      String targetPath = targetFile.getAbsolutePath();
      String filteringPath = filteringFile.getAbsolutePath();
      String textPath = textFile.getAbsolutePath();

      RaptatDocument textDocument = ta.processDocument(textPath);
      List<AnnotatedPhrase> targetAnnotations = annotationImporter.importAnnotations(targetPath,
          textPath, targetConcepts, targetSchemaConcepts);
      List<AnnotatedPhrase> filteringAnnotations = annotationImporter
          .importAnnotations(filteringPath, textPath, filteringConcepts, filteringSchemaConcepts);
      pti.addProcessedTokensToAnnotations(targetAnnotations, textDocument, false, 7);
      pti.addProcessedTokensToAnnotations(filteringAnnotations, textDocument, false, 7);

      AnnotationGroup annotationGroup = new AnnotationGroup(textDocument, targetAnnotations);

      RaptatPair<AnnotationGroup, List<AnnotatedPhrase>> resultPair =
          new RaptatPair<>(annotationGroup, filteringAnnotations);

      resultPairList.add(resultPair);

    }

    return resultPairList;
  }

  private void promoteAttributeValuesToConcepts(List<AnnotatedPhrase> annotations,
      String targetConceptName, String targetAttributeName) {
    for (AnnotatedPhrase annotatedPhrase : annotations) {
      if (annotatedPhrase.getConceptName().equalsIgnoreCase(targetConceptName)) {
        List<RaptatAttribute> replacementAttributes = new ArrayList<>();
        for (RaptatAttribute attribute : annotatedPhrase.getPhraseAttributes()) {
          if (attribute.getName().equalsIgnoreCase(targetAttributeName)) {
            annotatedPhrase.setConceptName(attribute.getValues().get(0));
          } else {
            replacementAttributes.add(attribute);
          }
        }
        annotatedPhrase.setPhraseAttributes(replacementAttributes);
      }
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        RunType runType = RunType.TEST;
        AnnotationMutator mutator = new AnnotationMutator();

        switch (runType) {
          case TEST:

            break;
          default:
            break;
        }
      }
    });
  }

}
