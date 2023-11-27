package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticSequenceIDTrainer;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.PhraseIDSmoothingMethod;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.rn.importer.json.InceptionAnnotationElementsRN;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.BridgeClass;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.SilkAnnotation;

public class UIMAProbabilisticSequenceIDTrainer extends JCasAnnotator_ImplBase{

	private static ProbabilisticSequenceIDTrainer sequenceIDTrainer = BridgeClass.probabilisticSequenceIDTrainer;
	private static boolean isNewSequenceIDTrainer = false;
	private static OptionsManager optionsManager;

	public UIMAProbabilisticSequenceIDTrainer() {
	}

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		optionsManager = OptionsManager.getInstance();
		if (sequenceIDTrainer == null) {
			sequenceIDTrainer = new ProbabilisticSequenceIDTrainer(System.getProperty("user.home") + "\\tree.txt", optionsManager.getKeepUnlabeledOnDisk(), optionsManager.getSmoothingMethod());
			isNewSequenceIDTrainer = true;
		}
	}

	@Override
	public void process( JCas inputJCas ) throws AnalysisEngineProcessException {
		
		ArrayList<InceptionAnnotationElementsRN> inceptionAnnotationElements = new ArrayList<InceptionAnnotationElementsRN>();
		RaptatDocument raptatDocument = BridgeClass.raptatDocument;
		File raptatFile = new File(raptatDocument.getTextSourcePath().get());
		String raptatPathString = raptatFile.getAbsolutePath();
		File silkFile;
		String silkPathString;
		for (SilkAnnotation silkAnnotation: BridgeClass.annotatedDocumentAnnotations) {
			silkFile = new File(silkAnnotation.documentSource);
			silkPathString = silkFile.getAbsolutePath();
			if (raptatPathString.equals(silkPathString))
				inceptionAnnotationElements.add(new InceptionAnnotationElementsRN(silkAnnotation.begin, silkAnnotation.end, silkAnnotation.label, silkAnnotation.documentSource));
		}

		List<AnnotatedPhrase> referenceAnnotations = InceptionAnnotationElementsRN.toAnnotatedPhraseList(inceptionAnnotationElements, raptatDocument);
		BridgeClass.curRefAnnotations = referenceAnnotations;

		AnnotationGroup annotationGroup = new AnnotationGroup(raptatDocument, referenceAnnotations);
		
		TextAnalyzer.updateTrainingGroup(annotationGroup, optionsManager.getRemoveStopWords(), optionsManager.getUseStems(), 
				optionsManager.getUsePOS(), optionsManager.getReasonNoMedsProcessing(), optionsManager.getTrainingTokenNumber());
		
		sequenceIDTrainer.buildAllUnlabeledHashTrees(annotationGroup);
		
		//TODO: eventually activateFilteredSentences(), where filtering in separate module, but before activating, createFilteredSentences()
		//Richard Noriega 3-7-22
		annotationGroup.activateBaseSentences();
		
		if (isNewSequenceIDTrainer) {
			sequenceIDTrainer.updateTraining(annotationGroup);
			sequenceIDTrainer.addUnlabeledToLabeledTree();
		}
		else
			sequenceIDTrainer.updatePreviousTraining(annotationGroup);
		
		sequenceIDTrainer.updateProbabilities();
		sequenceIDTrainer.closeTreeStreams();

		isNewSequenceIDTrainer = false;
		BridgeClass.probabilisticSequenceIDTrainer = sequenceIDTrainer;

	}
}	

