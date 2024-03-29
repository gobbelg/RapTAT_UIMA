package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import java.util.HashSet;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import java.util.Iterator;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.cas.FeatureStructure;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import java.util.ArrayList;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.SequenceIDStructure;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticSequenceIDEvaluator;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
import org.apache.uima.resource.ResourceAccessException;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.resourceinterfaces.UIMAAlgorithmSolution;
import org.apache.uima.UimaContext;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.SequenceIDEvaluator;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.makers.UIMAAnnotatedPhraseMaker;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;

public class UIMAPhraseFinder extends JCasAnnotator_ImplBase
{
    private static final UIMAAnnotatedPhraseMaker phraseMaker;
    private static final double THRESHOLD = 0.35;
    private static boolean invertTokenSequence;
    private SequenceIDEvaluator tsEvaluator;
    
    static {
        phraseMaker = new UIMAAnnotatedPhraseMaker();
    }
    
    public UIMAPhraseFinder() {
        this.tsEvaluator = null;
    }
    
    public void initialize(final UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        UIMAAlgorithmSolution theSolution = null;
        try {
            theSolution = (UIMAAlgorithmSolution)this.getContext().getResourceObject("Solution");
        }
        catch (ResourceAccessException e) {
            e.printStackTrace();
        }
        final ProbabilisticTSFinderSolution tsFinderSolution = (ProbabilisticTSFinderSolution)theSolution.getTokenSequenceFinderSolution();
        final SequenceIDStructure sequenceIdentifier = tsFinderSolution.getSequenceIdentifier();
        this.tsEvaluator = (SequenceIDEvaluator)new ProbabilisticSequenceIDEvaluator((LabeledHashTree)sequenceIdentifier, 0.35);
    }
    
    public void process(final JCas inputJCas) throws AnalysisEngineProcessException {
        for (Annotation curSentence : inputJCas.getAnnotationIndex(UIMASentence.type)) {
        	UIMASentence uimaSentence = (UIMASentence) curSentence;
            final FSArray tokenArray = uimaSentence.getSentenceTokens();
            final int arraySize = tokenArray.size();
            final List<RaptatToken> tokenList = new ArrayList<RaptatToken>(arraySize);
            for (int i = 0; i < arraySize; ++i) {
                final UIMAToken uimaToken = (UIMAToken)tokenArray.get(i);
                tokenList.add(new RaptatToken(uimaToken.getTokenStringAugmented(), uimaToken.getBegin(), uimaToken.getEnd()));
            }
            final List<List<RaptatToken>> sentenceAnnotations = (List<List<RaptatToken>>)this.tsEvaluator.getSentenceAnnotations((List)tokenList);
            if (!sentenceAnnotations.isEmpty()) {
                final FeatureStructure[] annotationArray = new FeatureStructure[sentenceAnnotations.size()];
                int j = 0;
                for (final List<RaptatToken> curAnnotation : sentenceAnnotations) {
                    annotationArray[j++] = (FeatureStructure)UIMAPhraseFinder.phraseMaker.newAnnotation(inputJCas, (List)curAnnotation);
                }
                final FSArray theAnnotations = new FSArray(inputJCas, annotationArray.length);
                theAnnotations.copyFromArray(annotationArray, 0, 0, annotationArray.length);
                uimaSentence.setPhraseAnnotations(theAnnotations);
                uimaSentence.setContainsAnnotatedPhrase(true);
            }
        }
    }
    
    private HashSet<String> validateOutputConcepts(final HashSet<String> annotationOutputConcepts, HashSet<String> trainingConcepts, final List<SchemaConcept> schemaConcepts) {
        if (trainingConcepts == null || trainingConcepts.isEmpty()) {
            trainingConcepts = new HashSet<String>();
            for (final SchemaConcept schemaConcept : schemaConcepts) {
                trainingConcepts.add(schemaConcept.getConceptName().toLowerCase());
            }
        }
        if (annotationOutputConcepts == null || annotationOutputConcepts.isEmpty()) {
            return trainingConcepts;
        }
        if (!GeneralHelper.setIsSubset((Set)annotationOutputConcepts, (Set)trainingConcepts)) {
            System.err.println("Concepts for annnotation were not used for training");
            System.err.println("TrainingConcepts:");
            for (final String concept : trainingConcepts) {
                System.err.println(concept);
            }
            System.exit(-1);
        }
        return annotationOutputConcepts;
    }
}


///**
// *
// */
//package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.uima.UimaContext;
//import org.apache.uima.analysis_component.AnalysisComponent;
//import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
//import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
//import org.apache.uima.cas.FSIterator;
//import org.apache.uima.cas.FeatureStructure;
//import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.cas.FSArray;
//import org.apache.uima.jcas.tcas.Annotation;
//import org.apache.uima.resource.ResourceAccessException;
//import org.apache.uima.resource.ResourceInitializationException;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.SequenceIDEvaluator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.naivebayes.BOWSequenceIDEvaluator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.naivebayes.BOWSequenceIDSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticSequenceIDEvaluator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ReverseProbabilisticSequenceIDEvaluator;
//import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
//import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.SequenceIDStructure;
//import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
//import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAToken;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.makers.UIMAAnnotatedPhraseMaker;
//
///********************************************************
// *
// *
// * @author Glenn Gobbel - Aug 26, 2013
// *******************************************************/
//public class UIMAPhraseFinder extends JCasAnnotator_ImplBase {
//  private final static UIMAAnnotatedPhraseMaker phraseMaker = new UIMAAnnotatedPhraseMaker();
//  private static final double THRESHOLD = 0.35;
//  private static boolean invertTokenSequence;
//  private SequenceIDEvaluator tsEvaluator = null;
//
//
//  /**********************************************
//   *
//   *
//   * @author Glenn Gobbel - Aug 26, 2013
//   **********************************************/
//  public UIMAPhraseFinder() {
//    // TODO Auto-generated constructor stub
//  }
//
//
//  /**
//   * @see AnalysisComponent#initialize(UimaContext)
//   */
//  @Override
//  public void initialize(UimaContext aContext) throws ResourceInitializationException {
//    super.initialize(aContext);
//
//    try {
//      //ProbabilisticTSFinderSolution theSolution = (ProbabilisticTSFinderSolution) getContext().getResourceObject("Solution");
//      RaptatAnnotationSolution raptatSolution = (RaptatAnnotationSolution) getContext().getResourceObject("Solution");
//      ProbabilisticTSFinderSolution theSolution = (ProbabilisticTSFinderSolution) raptatSolution.getTokenSequenceFinderSolution();
//      SequenceIDStructure sequenceIdentifier = theSolution.getSequenceIdentifier();
//      invertTokenSequence = theSolution.getTokenTrainingOptions().invertTokenSequence();
//
//      if (sequenceIdentifier instanceof BOWSequenceIDSolution) {
//        this.tsEvaluator = new BOWSequenceIDEvaluator((BOWSequenceIDSolution) sequenceIdentifier);
//      } else if (sequenceIdentifier instanceof LabeledHashTree) {
//        if (invertTokenSequence) {
//          this.tsEvaluator = new ReverseProbabilisticSequenceIDEvaluator(
//              (LabeledHashTree) sequenceIdentifier, THRESHOLD);
//        } else {
//          this.tsEvaluator =
//              new ProbabilisticSequenceIDEvaluator((LabeledHashTree) sequenceIdentifier, THRESHOLD);
//        }
//      }
//    } catch (ResourceAccessException e) {
//      throw new ResourceInitializationException(e);
//    }
//  }
//
//
//  @Override
//  public void process(JCas inputJCas) throws AnalysisEngineProcessException {
//    FSIterator<Annotation> sentenceIterator =
//        inputJCas.getAnnotationIndex(UIMASentence.type).iterator();
//    while (sentenceIterator.hasNext()) {
//      UIMASentence curSentence = (UIMASentence) sentenceIterator.next();
//      FSArray tokenArray = curSentence.getSentenceTokens();
//      int arraySize = tokenArray.size();
//      List<RaptatToken> tokenList = new ArrayList<>(arraySize);
//      for (int i = 0; i < arraySize; i++) {
//        UIMAToken uimaToken = (UIMAToken) tokenArray.get(i);
//        tokenList.add(new RaptatToken(uimaToken.getTokenStringAugmented(), uimaToken.getBegin(),
//            uimaToken.getEnd()));
//      }
//
//      List<List<RaptatToken>> sentenceAnnotations =
//          this.tsEvaluator.getSentenceAnnotations(tokenList);
//
//      if (!sentenceAnnotations.isEmpty()) {
//        FeatureStructure[] annotationArray = new FeatureStructure[sentenceAnnotations.size()];
//        int i = 0;
//        for (List<RaptatToken> curAnnotation : sentenceAnnotations) {
//          annotationArray[i++] = phraseMaker.newAnnotation(inputJCas, curAnnotation);
//        }
//        FSArray theAnnotations = new FSArray(inputJCas, annotationArray.length);
//        theAnnotations.copyFromArray(annotationArray, 0, 0, annotationArray.length);
//        curSentence.setPhraseAnnotations(theAnnotations);
//        curSentence.setContainsAnnotatedPhrase(true);
//      }
//
//    }
//
//  }
//}
