
package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.cas.FSIterator;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.SilkAnnotation;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.BridgeClass;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAAnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.UimaContext;
import src.main.gov.va.vha09.grecc.raptat.gg.resultwriters.ResultWriter;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;

public class UIMAResultWriter extends JCasAnnotator_ImplBase
{
    private static final int DOC_INDEX = 0;
    private static final int BEGIN_INDEX = 1;
    private static final int END_INDEX = 2;
    private static final int TEXT_INDEX = 3;
    private static final int CONCEPT_INDEX = 4;
    private ResultWriter theWriter;
    private String outputDirectory;
    private String[] titlesOfResults;
    private String curDocument;
    
    public UIMAResultWriter() {
        this.curDocument = null;
    }
    
    public void destroy() {
        if (this.theWriter != null) {
            this.theWriter.close();
        }
    }
    
    public void initialize(final UimaContext aContext) throws ResourceInitializationException {
        final String writeDirectory = "C:\\Users\\noriegrt\\Documents\\ADHAF_UIMA\\output";
        final String[] resultTitles = { "Document", "StartOffset", "EndOffset", "AnnotatedPhrase" };
        this.outputDirectory = writeDirectory;
        this.titlesOfResults = resultTitles;
    }
    
    public void process(final JCas inputJCas) throws AnalysisEngineProcessException {
        final FSIterator<Annotation> sentenceIterator = (FSIterator<Annotation>)inputJCas.getAnnotationIndex(UIMASentence.type).iterator();
        final String[] writtenLine = new String[5];
        while (sentenceIterator.hasNext()) {
            final UIMASentence curSentence = (UIMASentence)sentenceIterator.next();
            if (curSentence.getContainsAnnotatedPhrase()) {
                writtenLine[0] = "";
                final FSArray annotatedPhrases = curSentence.getPhraseAnnotations();
                for (int i = 0; i < annotatedPhrases.size(); ++i) {
                    final UIMAAnnotatedPhrase curPhrase = (UIMAAnnotatedPhrase)annotatedPhrases.get(i);
                    BridgeClass.silkPredictions.add(new SilkAnnotation(curPhrase.getConcept(), curPhrase.getBegin(), curPhrase.getEnd(), null));
                }
            }
        }
    }
}






///**
// *
// */
//package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;
//
//import static src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.NULL_ELEMENT;
//import java.util.Arrays;
//import org.apache.uima.UimaContext;
//import org.apache.uima.analysis_component.AnalysisComponent;
//import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
//import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
//import org.apache.uima.cas.FSIterator;
//import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.cas.FSArray;
//import org.apache.uima.jcas.tcas.Annotation;
//import org.apache.uima.resource.ResourceInitializationException;
//import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
//import src.main.gov.va.vha09.grecc.raptat.gg.resultwriters.ResultWriter;
//import src.main.gov.va.vha09.grecc.raptat.gg.resultwriters.machinelearning.naivebayes.NBResultWriter;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAAnnotatedPhrase;
//import src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMASentence;
//
///********************************************************
// *
// *
// * @author Glenn Gobbel - Sep 6, 2013
// *******************************************************/
//public class UIMAResultWriter extends JCasAnnotator_ImplBase {
//  private static final int DOC_INDEX = 0, BEGIN_INDEX = 1, END_INDEX = 2, TEXT_INDEX = 3,
//      CONCEPT_INDEX = 4;
//  private ResultWriter theWriter;
//
//
//  /**********************************************
//   *
//   *
//   * @author Glenn Gobbel - Sep 6, 2013
//   **********************************************/
//  public UIMAResultWriter() {}
//
//
//  @Override
//  public void destroy() {
//    if (this.theWriter != null) {
//      this.theWriter.close();
//    }
//  }
//
//
//  /**
//   * @see AnalysisComponent#initialize(UimaContext)
//   */
//  @Override
//  public void initialize(UimaContext aContext) throws ResourceInitializationException {
//    String writeDirectory =
//        GeneralHelper.getDirectory("Select directory for writing results").getAbsolutePath();
//    String[] resultTitles =
//        new String[] {"Document", "StartOffset", "EndOffset", "AnnotatedPhrase"};
//    this.theWriter = new NBResultWriter(writeDirectory, resultTitles);
//  }
//
//
//  @Override
//  public void process(JCas inputJCas) throws AnalysisEngineProcessException {
//    FSIterator<Annotation> sentenceIterator =
//        inputJCas.getAnnotationIndex(UIMASentence.type).iterator();
//    String[] writtenLine = new String[5];
//    while (sentenceIterator.hasNext()) {
//      UIMASentence curSentence = (UIMASentence) sentenceIterator.next();
//      if (curSentence.getContainsAnnotatedPhrase()) {
//        writtenLine[DOC_INDEX] = curSentence.getSourcePath();
//        FSArray annotatedPhrases = curSentence.getPhraseAnnotations();
//        for (int i = 0; i < annotatedPhrases.size(); i++) {
//          UIMAAnnotatedPhrase curPhrase = (UIMAAnnotatedPhrase) annotatedPhrases.get(i);
//          writtenLine[BEGIN_INDEX] = String.valueOf(curPhrase.getBegin());
//          writtenLine[END_INDEX] = String.valueOf(curPhrase.getEnd());
//          writtenLine[TEXT_INDEX] = curPhrase.getCoveredText().replaceAll("(?m)\\s+", " ");
//          writtenLine[CONCEPT_INDEX] = curPhrase.getConcept();
//
//          if (!writtenLine[4].equals(NULL_ELEMENT)) {
//            System.out.println(Arrays.toString(writtenLine));
//            this.theWriter.writeln(writtenLine, "\t");
//          }
//
//        }
//      }
//    }
//  }
//}
