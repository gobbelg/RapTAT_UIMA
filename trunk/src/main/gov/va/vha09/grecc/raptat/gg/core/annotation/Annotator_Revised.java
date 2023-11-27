/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.core.annotation;

import java.util.HashSet;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.TokenSequenceFinderBuilder;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.crf.CrfSuiteRunner;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.crf.tagging.CSAttributeTagger;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.AttributeTaggerSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.attributeconflictmanagers.AttributeConflictManager;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TSTrainingParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TSTrainingParameterObject.AnnotationOnlyParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TSTrainingParameterObject.TrainingAndAnnotationParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TSTrainingParameterObject.TrainingOnlyParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.phrasecontext.ContextAttributeWriter;

/**
 * Class created only to add an alternative Annotator class constructor that uses the same
 * parameters as the original Annotator class constructor. The original constructor is being
 * deprecated but kept in place while converting to the new constructor. Annotator and
 * Annotator_Revised classes should eventually be merged.
 *
 * @author Glenn Gobbel
 *
 */
public class Annotator_Revised extends Annotator {

  /**
   *
   */
  public Annotator_Revised(RaptatAnnotationSolution raptatSolution, HashSet<String> outputConcepts,
      double probabilisticThreshold) {
    super();

    TSTrainingParameterObject trainingParameters = raptatSolution.getTrainingParameters();
    TrainingOnlyParameterObject trainingOnlyParameters = trainingParameters.trainingOnlyParameters;
    AnnotationOnlyParameterObject annotationParameters =
        trainingParameters.annotationOnlyParameters;
    TrainingAndAnnotationParameterObject taParameters =
        trainingParameters.trainingAndAnnotationParameters;

    OptionsManager optionsManager = OptionsManager.getInstance();
    optionsManager.initializeTrainingAndAnnotationTokenOptions(taParameters);
    this.textAnalyzer = new TextAnalyzer(annotationParameters.dictionary);
    this.textAnalyzer.addSentenceAndSectionMatchers(annotationParameters.sentenceMatchers,
        annotationParameters.sectionMatchers);

    outputConcepts = validateOutputConcepts(outputConcepts, trainingOnlyParameters.trainingConcepts,
        trainingOnlyParameters.schemaConcepts);
    setConceptsToAnnotate(outputConcepts);

    this.tsFinder = TokenSequenceFinderBuilder.createTSFinder(
        raptatSolution.getTokenSequenceFinderSolution(), outputConcepts, probabilisticThreshold);

    for (AttributeTaggerSolution taggerSolution : annotationParameters.attributeTaggers) {
      CrfSuiteRunner taggerRunner = new CrfSuiteRunner();
      this.csAttributeTaggers.add(new CSAttributeTagger(taggerSolution, taggerRunner));
    }

    this.contextAttributeWriter =
        new ContextAttributeWriter(annotationParameters.sentenceContextAnalyzers);
    for (AttributeConflictManager conflictManager : annotationParameters.attributeConflictManagers) {
      this.attributeConflictManagers.put(conflictManager.attributeName, conflictManager);
    }

    this.contextSifter = annotationParameters.contextSifter;
    this.postProcessors = annotationParameters.annotationGroupPostProcessors;
  }

  /**
   * Make sure that the concepts to be annotated were part of the set used for training Feb 20,
   * 2018. Note that if the user specifies no concepts during training all the schema concepts will
   * be used for training. Also, if the user specifies, no accepted concepts during annotation, all
   * the schema concepts used for training should be used. This means that if both the accepted
   * concepts for annotation are empty and the concepts used for training are empty, the full schema
   * concept list will be used for annotation.
   *
   * @param annotationOutputConcepts
   * @param trainingConcepts
   * @param schemaConcepts
   * @return
   */
  private HashSet<String> validateOutputConcepts(HashSet<String> annotationOutputConcepts,
      HashSet<String> trainingConcepts, List<SchemaConcept> schemaConcepts) {
    if (trainingConcepts == null || trainingConcepts.isEmpty()) {
      trainingConcepts = new HashSet<>();
      for (SchemaConcept schemaConcept : schemaConcepts) {
        trainingConcepts.add(schemaConcept.getConceptName().toLowerCase());
      }
    }

    if (annotationOutputConcepts == null || annotationOutputConcepts.isEmpty()) {
      return trainingConcepts;
    }

    if (!GeneralHelper.setIsSubset(annotationOutputConcepts, trainingConcepts)) {
      System.err.println("Concepts for annnotation were not used for training");
      System.err.println("TrainingConcepts:");

      for (String concept : trainingConcepts) {
        System.err.println(concept);
      }
      System.exit(-1);
    }

    return annotationOutputConcepts;
  }

}
