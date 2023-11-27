/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.candidates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.shiftreduce.ShiftReduceParser;
import edu.stanford.nlp.trees.Tree;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.UniqueIDGenerator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.POSTaggerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.TokenizerFactory;

/**
 * Determines whether sentences from a document are part of a template.
 *
 * @author Glenn Gobbel
 *
 */
public class TemplateSentenceMarker {
  private enum RunType {
    PARSE_TEST, PTSD_DOCUMENT_ANALYSIS;
  }

  private int minimalTemplateLength = 1;

  private final String modelPath = "edu/stanford/nlp/models/srparser/englishSR.ser.gz";
  private final ShiftReduceParser parserModel = ShiftReduceParser.loadModel(this.modelPath);


  public TemplateSentenceMarker(int minimalTemplateLength) {
    this.minimalTemplateLength = minimalTemplateLength;
  }


  public List<AnnotatedPhrase> tagTemplateSentences(List<AnnotatedPhrase> sentences) {
    final List<AnnotatedPhrase> templateCandidates =
        new ArrayList<>(2 * this.minimalTemplateLength);
    final List<AnnotatedPhrase> templateSentences = new ArrayList<>(sentences.size());

    for (final AnnotatedPhrase sentence : sentences) {
      final List<TaggedWord> taggedWords = getTaggedWords(sentence);
      final Tree parseTree = this.parserModel.parse(taggedWords);
      System.out.println("\n" + sentence.getPhraseStringUnprocessed());
      if (!hasVerbPhrase(parseTree)) {
        templateCandidates.add(sentence);
      } else {
        if (templateCandidates.size() >= this.minimalTemplateLength) {
          while (!templateCandidates.isEmpty()) {
            markAndAddTemplateSentence(templateCandidates, templateSentences);
          }
        } else {
          templateCandidates.clear();
        }
      }
      parseTree.indentedListPrint();;// parseTree.pennPrint();

    }

    /*
     * If we come to the end of the sentences and we have at least minimalTemplateLength sentences
     * in the templateCandidates list, then mark them as part of a template
     */
    if (templateCandidates.size() >= this.minimalTemplateLength) {
      while (!templateCandidates.isEmpty()) {
        markAndAddTemplateSentence(templateCandidates, templateSentences);
      }
    }

    return templateSentences;
  }


  public List<AnnotatedPhrase> tagTemplateSentences(RaptatDocument raptatDocument) {
    final List<AnnotatedPhrase> sentences = raptatDocument.getActiveSentences();
    return this.tagTemplateSentences(sentences);
  }


  private List<TaggedWord> getTaggedWords(AnnotatedPhrase sentence) {
    final List<RaptatToken> tokens = sentence.getProcessedTokens();
    final List<TaggedWord> resultList = new ArrayList<>(tokens.size());

    for (final RaptatToken token : tokens) {
      resultList.add(new TaggedWord(token.getTokenStringUnprocessed(), token.getPOS()));
    }

    return resultList;
  }


  private boolean hasVerbPhrase(Tree parseTree) {
    for (final Tree subtree : parseTree) {
      final Label label = subtree.label();
      if (label.value().equals("VB") || label.value().equals("VBD") || label.value().equals("VBP")
          || label.value().equals("VBZ")) {
        System.out.println("Verb phrase found");
        return true;
      }
    }
    System.out.println("NO verb phrase found");
    return false;
  }


  /**
   * @param templateCandidates
   * @param templateSentences
   */
  private void markAndAddTemplateSentence(List<AnnotatedPhrase> templateCandidates,
      List<AnnotatedPhrase> templateSentences) {
    final AnnotatedPhrase templateSentence =
        templateCandidates.remove(templateCandidates.size() - 1);
    templateSentence.addSentenceAssociatedConcept(Constants.TEMPLATE_SENTENCE_TAG);
    templateSentences.add(templateSentence);
  }


  /**
   * @param args
   */
  public static void main(String[] args) {
    final int lvgSetting = UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        final RunType runType = RunType.PARSE_TEST;

        switch (runType) {
          case PARSE_TEST:
            testParser();
            break;
          case PTSD_DOCUMENT_ANALYSIS:
            final String txtFolderPath =
                "D:\\MHA_NLP\\eHOSTAnnotationWorkspaces\\GlennWorkspace\\Elliot_RaptatTrainingCorpus\\corpus";
            final int minimumTemplateLength = 1;
            ptsdDocumentAnalysis(txtFolderPath, minimumTemplateLength);
            break;
          default:
            break;
        }
        System.exit(0);
      }


      /**
       * This method is extracted from the original getTokensAndOffsets method. This method is
       * designed to deliver only the list of the modified token list, so that the number of tokens
       * in Raw Token List and in the Negex String Array size is same.
       *
       *
       * @param stringToSplit
       * @return
       */
      public RaptatPair<List<String>, List<Integer>> splitByAlphanumeric(String stringToSplit,
          int curTokenPosition) {
        /*
         * Regex to identify tokens that start with non-alphanumeric characters
         */
        final Matcher nonAlphaTokenStart = Pattern.compile("(^\\W+)").matcher("");

        final Matcher alphaNonAlphaAlternationMatcher =
            Pattern.compile("((?:(?:\\p{Alpha}+)(?:'\\p{Alpha}+)*)|(?:\\w+))(\\W*)").matcher("");

        final ArrayList<String> tokenStringList = new ArrayList<>();
        final List<Integer> tokenPosList = new ArrayList<>();

        /*
         * First get the non-alphanumeric characters that begin the string if they exist, add them
         * to the list, and remove them from the string so we only have to deal with strings that
         * start with alphanumeric characters followed by non-alphanumeric characters
         */
        nonAlphaTokenStart.reset(stringToSplit);

        if (nonAlphaTokenStart.find()) {
          final String startString = nonAlphaTokenStart.group(1);
          stringToSplit = nonAlphaTokenStart.replaceFirst("");
          tokenStringList.add(startString);
          tokenPosList.add(curTokenPosition);
          curTokenPosition += startString.length();
        }

        alphaNonAlphaAlternationMatcher.reset(stringToSplit);
        while (alphaNonAlphaAlternationMatcher.find()) {
          for (int i = 1; i < 3; i++) {
            final String curGroupString = alphaNonAlphaAlternationMatcher.group(i);

            if (curGroupString != null && curGroupString.length() > 0) {
              tokenStringList.add(curGroupString);
              tokenPosList.add(alphaNonAlphaAlternationMatcher.start(i) + curTokenPosition);
            }
          }
        }

        return new RaptatPair<>(tokenStringList, tokenPosList);
      }


      private void convertSentencesToFullAnnotations(List<AnnotatedPhrase> sentences,
          String concept) {
        final UniqueIDGenerator idGenerator = UniqueIDGenerator.INSTANCE;
        for (final AnnotatedPhrase sentence : sentences) {
          sentence.setConceptName(concept);
          sentence.setMentionId(idGenerator.getUnique());
        }
      }


      private String[] getTokenStrings(String sentenceText, Tokenizer tokenizer) {
        final Span[] tokenPositions = tokenizer.tokenizePos(sentenceText);
        String[] baseTokenStrings = Span.spansToStrings(tokenPositions, sentenceText);

        final ArrayList<String> alphanumSplitStrings = new ArrayList<>();
        final ArrayList<Integer> alphanumSplitStringPositions = new ArrayList<>();

        for (int i = 0; i < baseTokenStrings.length; i++) {
          final RaptatPair<List<String>, List<Integer>> alphaNumericSplitList =
              splitByAlphanumeric(baseTokenStrings[i], tokenPositions[i].getStart());

          alphanumSplitStrings.addAll(alphaNumericSplitList.left);
          alphanumSplitStringPositions.addAll(alphaNumericSplitList.right);
        }

        baseTokenStrings = alphanumSplitStrings.toArray(baseTokenStrings);
        return baseTokenStrings;
      }


      private void ptsdDocumentAnalysis(String txtFolderPath, int minimumTemplateLength) {
        final TemplateSentenceMarker templateMarker =
            new TemplateSentenceMarker(minimumTemplateLength);
        final TextAnalyzer ta = new TextAnalyzer();

        final OptionsManager optionsManager = OptionsManager.getInstance();
        optionsManager.setRemoveStopWords(false);
        ta.setTokenProcessingOptions(optionsManager);

        final File txtFolder = new File(txtFolderPath);
        final String annotationFolderPath = txtFolder.getParent() + File.separator
            + "SentenceAnnotations" + "_" + txtFolder.getName();
        final File annotationFolder = new File(annotationFolderPath);
        try {
          FileUtils.forceMkdir(annotationFolder);
        } catch (final IOException e) {
          e.printStackTrace();
          System.exit(-1);
        }

        final XMLExporterRevised xmlExporter =
            new XMLExporterRevised(AnnotationApp.EHOST, annotationFolder, true);

        final Collection<File> txtFiles =
            FileUtils.listFiles(txtFolder, new String[] {"txt"}, false);

        int i = 0;
        final int numberOfFiles = txtFiles.size();
        for (final File txtFile : txtFiles) {
          final RaptatDocument raptatDocument = ta.processDocument(txtFile.getAbsolutePath());
          final List<AnnotatedPhrase> templateSentences =
              templateMarker.tagTemplateSentences(raptatDocument);
          convertSentencesToFullAnnotations(templateSentences, "TemplateSentence");
          final AnnotationGroup annotationGroup = new AnnotationGroup(raptatDocument, null);
          annotationGroup.setRaptatAnnotations(templateSentences);
          System.out.println("Exporting " + ++i + " of " + numberOfFiles + " files");
          xmlExporter.exportRaptatAnnotationGroup(annotationGroup, true, true);
        }

      }


      private void testParser() {
        final String modelPath = "edu/stanford/nlp/models/srparser/englishSR.ser.gz";

        final List<String> testStrings = new ArrayList<>();
        testStrings.add("My dog likes to shake his stuffed chickadee toy.");
        testStrings.add("Will check for additional beds.");
        testStrings.add("Found cardiac arrhythmia on asculation.");
        testStrings.add("Removed the top left panel from the window.");
        testStrings.add("Increased density over left lower lung lobe - consistent with pneumonia.");
        testStrings.add("Cardiac arrhythmia found on ascultation.");
        testStrings.add("Running man");
        testStrings.add("Pressure 50.128e-64/60 despite use of deplin");

        final ShiftReduceParser model = ShiftReduceParser.loadModel(modelPath);
        final POSTagger posTagger = POSTaggerFactory.getInstance();
        final Tokenizer tokenizer = TokenizerFactory.getInstance();

        for (final String text : testStrings) {
          final String[] sentenceTokenStrings = getTokenStrings(text, tokenizer);
          final String[] tokenTags = posTagger.tag(sentenceTokenStrings);

          final List<TaggedWord> tagged = new ArrayList<>(sentenceTokenStrings.length);
          for (int i = 0; i < sentenceTokenStrings.length; i++) {
            tagged.add(new TaggedWord(sentenceTokenStrings[i], tokenTags[i]));
          }

          final Tree tree = model.parse(tagged);
          tree.pennPrint();
          Tree oldNode = null;
          for (Tree node : tree) {
            System.out.println("Node:" + node.value() + "\n\tisLeaf:" + node.isLeaf()
                + " \n\tdepth:" + node.depth());
            if (!(oldNode == null)) {
              System.out.println("\t\tDISTANCE " + node.value() + " to " + oldNode.value() + ":"
                  + oldNode.depth(node));
            }
            if (node.value().equalsIgnoreCase("My")) {
              oldNode = node;
            }

          }
          for (final Tree subtree : tree) {
            final Label label = subtree.label();
            if (label.value().equals("VP")) {
              System.out.println("Verb phrase found");
              break;
            }
          }
          // tree.pennPrint();
          System.out.println();

        }
      }
    });

  }

}
