package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
import java.util.Iterator;
import java.util.List;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.FSIterator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.SourceDocumentInformation;
import java.util.Optional;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.resourceinterfaces.UIMAAlgorithmSolution;
import org.apache.uima.UimaContext;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.makers.UIMASentenceMaker;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;

public class UIMATextAnalyzer extends JCasAnnotator_ImplBase
{
    private TextAnalyzer theAnalyzer;
    private UIMASentenceMaker sentenceMaker;
    
    public UIMATextAnalyzer() {
        this.theAnalyzer = new TextAnalyzer();
        this.sentenceMaker = new UIMASentenceMaker();
    }
    
    public void initialize(final UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        try {
            final UIMAAlgorithmSolution uimaAlgorithmSolution = (UIMAAlgorithmSolution)this.getContext().getResourceObject("Solution");
        }
        catch (ResourceAccessException e) {
            throw new ResourceInitializationException((Throwable)e);
        }
    }
    
    public void process(final JCas inputJCas) throws AnalysisEngineProcessException {
        final RaptatDocument theDocument = this.theAnalyzer.processText(inputJCas.getDocumentText(), (Optional)Optional.of("test"));
        final FSIterator<Annotation> sourceIterator = (FSIterator<Annotation>)inputJCas.getAnnotationIndex(SourceDocumentInformation.type).iterator();
        final String sourcePath = null;
        theDocument.activateBaseSentences();
        final List<AnnotatedPhrase> theSentences = (List<AnnotatedPhrase>)theDocument.getActiveSentences();
        for (final AnnotatedPhrase curPhrase : theSentences) {
            final UIMASentence newPhrase = this.sentenceMaker.newAnnotation(inputJCas, curPhrase, sourcePath);
            newPhrase.addToIndexes(inputJCas);
        }
    }
}


///**
// *
// */
//package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;
//
//import java.util.List;
//import java.util.Optional;
//import org.apache.uima.UimaContext;
//import org.apache.uima.analysis_component.AnalysisComponent;
//import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
//import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
//import org.apache.uima.cas.FSIterator;
//import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.tcas.Annotation;
//import org.apache.uima.resource.ResourceAccessException;
//import org.apache.uima.resource.ResourceInitializationException;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
//import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
//import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
//import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.SourceDocumentInformation;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.makers.UIMASentenceMaker;
//import src.main.gov.va.vha09.grecc.raptat.rn.misc.BridgeClass;
//
///********************************************************
// *
// *
// * @author Glenn Gobbel - Aug 22, 2013
// *******************************************************/
//public class UIMATextAnalyzer extends JCasAnnotator_ImplBase {
//
//	private TextAnalyzer theAnalyzer = new TextAnalyzer();
//	private UIMASentenceMaker sentenceMaker = new UIMASentenceMaker();
//
//	/**
//	 * @see AnalysisComponent#initialize(UimaContext)
//	 */
//	@Override
//	public void initialize(UimaContext aContext) throws ResourceInitializationException {
//		super.initialize(aContext);
//		// get a reference to the String Map Resource
//		if (BridgeClass.solutionFile != null) {
//			try {
//				ProbabilisticTSFinderSolution theSolution =
//						(ProbabilisticTSFinderSolution) getContext().getResourceObject("Solution");
//				this.theAnalyzer.setTokenProcessingParameters(theSolution.getTokenTrainingOptions());
//			} catch (ResourceAccessException e) {
//				throw new ResourceInitializationException(e);
//			}
//		}
//	}
//
//
//	/*************************************************************
//	 * OVERRIDES PARENT METHOD
//	 *
//	 * @param arg0
//	 * @throws AnalysisEngineProcessException
//	 *
//	 * @author Glenn Gobbel - Aug 22, 2013
//	 *************************************************************/
//	@Override
//	public void process(JCas inputJCas) throws AnalysisEngineProcessException {
//		// String theDocURI = ( (SourceDocumentInformation) inJCas ).getUri();
//		RaptatDocument theDocument =
//				//this.theAnalyzer.processText(inputJCas.getDocumentText(), Optional.of(BridgeClass.nextTrainingDocumentFilePath));
//				this.theAnalyzer.processText(inputJCas.getDocumentText(), Optional.of("test"));
//		System.out.println(BridgeClass.solutionFile.getAbsolutePath());
//		if (BridgeClass.solutionFile != null) {
//
//			FSIterator<Annotation> sourceIterator =
//					inputJCas.getAnnotationIndex(SourceDocumentInformation.type).iterator();
//			SourceDocumentInformation sourceInfo = (SourceDocumentInformation) sourceIterator.next();
//			String sourcePath = sourceInfo.getUri();
//
//			List<AnnotatedPhrase> theSentences = theDocument.getActiveSentences();
//			for (AnnotatedPhrase curPhrase : theSentences) {
//				UIMASentence newPhrase = this.sentenceMaker.newAnnotation(inputJCas, curPhrase, sourcePath);
//				newPhrase.addToIndexes(inputJCas);
//			}
//		}
//		BridgeClass.raptatDocument = theDocument;
//	}
//
//}
