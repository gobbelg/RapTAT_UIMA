package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;

import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAToken;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAAnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
import org.apache.uima.jcas.JCas;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapSolution;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.naivebayes.NBCalculator;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.naivebayes.NBSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.resourceinterfaces.UIMAAlgorithmSolution;
import org.apache.uima.UimaContext;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapCalculator;
import java.util.HashSet;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;

public class UIMAConceptMapper extends JCasAnnotator_ImplBase
{
    private static HashSet<String> acceptableConcepts;
    private ConceptMapCalculator theCalculator;
    
    static {
        UIMAConceptMapper.acceptableConcepts = null;
    }
    
    public void initialize(final UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        try {
            final UIMAAlgorithmSolution theSolution = (UIMAAlgorithmSolution)this.getContext().getResourceObject("Solution");
            final ProbabilisticTSFinderSolution tsFinderSolution = (ProbabilisticTSFinderSolution)theSolution.getTokenSequenceFinderSolution();
            final ConceptMapSolution cmSolution = tsFinderSolution.getConceptMapSolution();
            this.theCalculator = (ConceptMapCalculator)new NBCalculator((NBSolution)cmSolution, Constants.FilterType.SMOOTHED);
        }
        catch (ResourceAccessException e) {
            throw new ResourceInitializationException((Throwable)e);
        }
    }
    
    public void process(final JCas inputJCas) throws AnalysisEngineProcessException {
        for (final Annotation curSentence : inputJCas.getAnnotationIndex(UIMASentence.type)) {
        	UIMASentence uimaSentence = (UIMASentence) curSentence;
            if (uimaSentence.getContainsAnnotatedPhrase()) {
                final FSArray annotatedPhrases = uimaSentence.getPhraseAnnotations();
                for (int i = 0; i < annotatedPhrases.size(); ++i) {
                    final UIMAAnnotatedPhrase curPhrase = (UIMAAnnotatedPhrase)annotatedPhrases.get(i);
                    final FSArray tokenArray = curPhrase.getTokens();
                    final int arraySize = tokenArray.size();
                    final String[] tokenList = new String[arraySize];
                    for (int j = 0; j < arraySize; ++j) {
                        tokenList[j] = ((UIMAToken)tokenArray.get(j)).getTokenStringAugmented();
                        final String s = tokenList[j];
                    }
                    final String bestConcept = this.theCalculator.calcBestConcept(tokenList, (Set)UIMAConceptMapper.acceptableConcepts)[0];
                    curPhrase.setConcept(bestConcept);
                }
            }
        }
    }
}

//package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;
//
//import java.util.HashSet;
//import org.apache.uima.UimaContext;
//import org.apache.uima.analysis_component.AnalysisComponent;
//import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
//import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
//import org.apache.uima.cas.FSIterator;
//import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.cas.FSArray;
//import org.apache.uima.jcas.tcas.Annotation;
//import org.apache.uima.resource.ResourceAccessException;
//import org.apache.uima.resource.ResourceInitializationException;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapCalculator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.naivebayes.NBCalculator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.naivebayes.NBSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.FilterType;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAAnnotatedPhrase;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAToken;
//
///********************************************************
// *
// *
// * @author Glenn Gobbel - Sep 6, 2013
// *******************************************************/
//public class UIMAConceptMapper extends JCasAnnotator_ImplBase {
//	// If acceptable concepts set to null, then all concepts
//	// will be used for mapping.
//	private static HashSet<String> acceptableConcepts = null;
//
//	private ConceptMapCalculator theCalculator;
//
//	// new HashSet<String>( Arrays.asList( "reason not on meds" ) );
//
//	/**********************************************
//	 *
//	 *
//	 * @author Glenn Gobbel - Sep 6, 2013
//	 **********************************************/
//	public UIMAConceptMapper() {
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @see AnalysisComponent#initialize(UimaContext)
//	 */
//	@Override
//	public void initialize(UimaContext aContext) throws ResourceInitializationException {
//		super.initialize(aContext);
//
//		try {
//			//ProbabilisticTSFinderSolution theSolution = (ProbabilisticTSFinderSolution) getContext().getResourceObject("Solution");
//			RaptatAnnotationSolution raptatSolution = (RaptatAnnotationSolution) getContext().getResourceObject("Solution");
//			ProbabilisticTSFinderSolution theSolution = (ProbabilisticTSFinderSolution) raptatSolution.getTokenSequenceFinderSolution();
//			ConceptMapSolution cmSolution = theSolution.getConceptMapSolution();
//			this.theCalculator = new NBCalculator((NBSolution) cmSolution, FilterType.SMOOTHED);
//
//		} catch (ResourceAccessException e) {
//			throw new ResourceInitializationException(e);
//		}
//	}
//
//	@Override
//	public void process(JCas inputJCas) throws AnalysisEngineProcessException {
//		FSIterator<Annotation> sentenceIterator =
//				inputJCas.getAnnotationIndex(UIMASentence.type).iterator();
//		while (sentenceIterator.hasNext()) {
//			UIMASentence curSentence = (UIMASentence) sentenceIterator.next();
//			if (curSentence.getContainsAnnotatedPhrase()) {
//				FSArray annotatedPhrases = curSentence.getPhraseAnnotations();
//				for (int i = 0; i < annotatedPhrases.size(); i++) {
//					UIMAAnnotatedPhrase curPhrase = (UIMAAnnotatedPhrase) annotatedPhrases.get(i);
//					FSArray tokenArray = curPhrase.getTokens();
//					int arraySize = tokenArray.size();
//					String[] tokenList = new String[arraySize];
//					for (int j = 0; j < arraySize; j++) {
//						tokenList[j] = ((UIMAToken) tokenArray.get(j)).getTokenStringAugmented();
//						String temp = tokenList[j];
//					}
//
//					String bestConcept = this.theCalculator.calcBestConcept(tokenList, null)[0];
//					curPhrase.setConcept(bestConcept);
//
//				}
//			}
//		}
//	}
//}
