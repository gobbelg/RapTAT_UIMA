/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.textanalysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.ContextHandling;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.SentenceSectionTaggerType;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.TokenProcessingOptions;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.TextDocumentLine;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.StartOffsetTokenComparator;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.Stopwatch;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.UniqueIDGenerator;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.tab.TabReader;
import src.main.gov.va.vha09.grecc.raptat.gg.lexicaltools.Lemmatiser;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.FacilitatorBlockerDictionaryFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.LemmatiserFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.MajorSectionMarker;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.MedListSectionMarker;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.NegationContextAnalyzerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.POSTaggerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.ReasonNoMedsContextAnalyzerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.RegexTextSplitter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.TokenizerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.UncertaintyContextAnalyzerFactory;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext.Mark;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext.TokenContextAnalyzer;

/**
 * Singleton that analyzes text documents and breaks them down to sentences and their tokens. These
 * along with a String indicating the text source path are returned in a RaptatDocument instance by
 * a call to the primary method in this class, processDocument().
 *
 * @author Glenn Gobbel - Jun 27, 2012
 */
public class TextAnalyzer {
  private enum RunType {
    RUN_SENTENCES, CHECK_LEMMATIZATION, ANALYZE_DOCUMENT, CHECK_GETPROCESSEDTOKENSTRINGS_METHOD;
  }

  // Synchronized as this is reported to be necessary according to UIMA
  private final static Collection<String> stopWordSet =
      Collections.unmodifiableCollection(new HashSet<>(Arrays.asList(Constants.STOP_WORDS)));

  /*
   * Create a hash map to go from OpenNLP parts of speech to an LVG part of speech
   */
  private static final Hashtable<String, Integer> openNLPToLVGMap = new Hashtable<>(20);

  private static final StartOffsetTokenComparator tokenSorter = new StartOffsetTokenComparator();

  /* Regex to identify tokens that start with non-alphanumeric characters */
  private static final Matcher nonAlphaTokenStart = Pattern.compile("(^\\W+)").matcher("");

  private static final Matcher alphaNonAlphaAlternationMatcher =
      Pattern.compile("((?:(?:\\p{Alpha}+)(?:'\\p{Alpha}+)*)|(?:\\w+))(\\W*)").matcher("");

  private static final Matcher simpleLineBreak = Pattern.compile("\r\n|\r|\n").matcher("");

  private static final PhraseTokenIntegrator tokenIntegrator = new PhraseTokenIntegrator();
  private static final Logger logger = Logger.getLogger(TextAnalyzer.class);

  private static final long serialVersionUID = -2052454515695348353L;

  /*
   * Generate map from OpenNLP parts of speech to LVG. This is used for LVG stemmming. Note that
   * some parts of speech, such as prepositions, are not included because they do not have
   * inflection variants.
   */
  static {
    String[] curArray = Constants.OPEN_NLP_NOUNS;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_NOUN_BITFLAG);
    }
    curArray = Constants.OPEN_NLP_PRONOUNS;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_PRONOUN_BITFLAG);
    }
    curArray = Constants.OPEN_NLP_ADJECTIVES;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_ADJECTIVE_BITFLAG);
    }
    curArray = Constants.OPEN_NLP_MODALS;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_MODAL_BITFLAG);
    }
    curArray = Constants.OPEN_NLP_ADVERBS;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_ADVERB_BITFLAG);
    }
    curArray = Constants.OPEN_NLP_VERBS;
    for (String curString : curArray) {
      TextAnalyzer.openNLPToLVGMap.put(curString, Constants.LVG_VERB_BITFLAG);
    }
  }

  private final static UniqueIDGenerator idGenerator = UniqueIDGenerator.INSTANCE;

  private final static boolean GET_COLUMN_AND_ROW_FEATURES = false;

  private RegexTextSplitter textSplitter = null;
  private Tokenizer tokenizer;
  private POSTagger posTagger;

  private TokenContextAnalyzer negationContextAnalyzer;

  private TokenContextAnalyzer uncertainContextAnalyzer;

  private TokenContextAnalyzer reasonNoMedsContextAnalyzer;
  private TokenContextAnalyzer facilitatorBlockerDictionary;

  private Lemmatiser stemmer;
  private boolean useStems;
  private boolean usePOS;
  private boolean removeStopWords;

  private ContextHandling reasonNoMedsProcessing;

  private int maxPhraseTokens = Constants.MAX_TOKENS_DEFAULT;

  private Map<SentenceSectionTaggerType, List<RaptatPair<String, String>>> sentenceAndSectionMatchers;

  private MajorSectionMarker medListSectionMarker;

  private LinkedList<TextDocumentLine> textDocumentLines;
  //
  // private TokenPhraseMaker tokenPhraseMaker;

  // Make class a singleton because creating
  // these processors is expensive, so we only
  // want to do it once and then reuse them
  // until no longer needed
  public TextAnalyzer() {
    initialize();

    TextAnalyzer.logger.setLevel(Level.INFO);
  }

  public TextAnalyzer(String dictionaryPath, HashSet<String> acceptedConcepts) {
    this();
    createDictionary(dictionaryPath, acceptedConcepts);
  }

  public TextAnalyzer(TokenContextAnalyzer dictionary) {
    this();
    this.facilitatorBlockerDictionary = dictionary;
  }

  public void addSectionMatcher(String patternName, String patternToMatch) {
    this.textSplitter.addSectionMatcher(patternName, patternToMatch);
  }

  public void addSentenceAndSectionMatchers(List<RaptatPair<String, String>> sentenceMatchers,
      List<RaptatPair<String, String>> sectionMatchers) {
    if (sentenceMatchers != null) {
      for (RaptatPair<String, String> matcherPair : sentenceMatchers) {
        addSentenceMatcher(matcherPair.left, matcherPair.right);
      }
    }

    if (sectionMatchers != null) {
      for (RaptatPair<String, String> matcherPair : sectionMatchers) {
        addSectionMatcher(matcherPair.left, matcherPair.right);
      }
    }

  }

  public void addSentenceAndSectionMatchers(
      Map<SentenceSectionTaggerType, List<RaptatPair<String, String>>> matchers) {
    if (matchers != null) {
      for (SentenceSectionTaggerType type : matchers.keySet()) {
        if (type.equals(SentenceSectionTaggerType.SENTENCE)) {
          for (RaptatPair<String, String> matcherPair : matchers.get(type)) {
            addSentenceMatcher(matcherPair.left, matcherPair.right);
          }
        }
        if (type.equals(SentenceSectionTaggerType.SECTION)) {
          for (RaptatPair<String, String> matcherPair : matchers.get(type)) {
            addSectionMatcher(matcherPair.left, matcherPair.right);
          }
        }
      }
    }
  }

  public void addSentenceMatcher(String patternName, String patternToMatch) {
    this.textSplitter.addSentenceMatcher(patternName, patternToMatch);
  }

  public void createDictionary(String dictionaryPath, HashSet<String> acceptedConcepts) {
    if (dictionaryPath != null && !dictionaryPath.isEmpty()) {

      if (!new File(dictionaryPath).exists()) {
        GeneralHelper.errorWriter("No dictionary found at:\n\n    " + dictionaryPath);
        System.exit(-1);
      }
      this.facilitatorBlockerDictionary =
          FacilitatorBlockerDictionaryFactory.getInstance(dictionaryPath, acceptedConcepts);

    }
  }

  /**
   * @return the facilitatorBlockerDictionary
   */
  public TokenContextAnalyzer getFacilitatorBlockerDictionary() {
    return this.facilitatorBlockerDictionary;
  }

  public String[] getNegatedSentenceCue(String sentenceText) {
    String[] tokenStrings = this.tokenizer.tokenize(sentenceText);
    Span[] tokenPositions = this.tokenizer.tokenizePos(sentenceText);

    ArrayList<String> processedTokenStrings = new ArrayList<>();

    for (int i = 0; i < tokenStrings.length; i++) {
      RaptatPair<List<String>, List<Integer>> processedList =
          splitByAlphanumeric(tokenStrings[i], tokenPositions[i].getStart());

      processedTokenStrings.addAll(processedList.left);
    }

    tokenStrings = processedTokenStrings.toArray(tokenStrings);
    String[] negationContext = this.negationContextAnalyzer.getContextCue(tokenStrings);

    return negationContext;
  }

  public String[] getNegatedSentenceTokensTest(String sentenceText) {
    String[] tokenStrings = this.tokenizer.tokenize(sentenceText);
    String[] negationContext =
        this.negationContextAnalyzer.getContext(tokenStrings, Constants.NEGATION_TAG);

    return negationContext;
  }

  public String[] getNegatedSentenceTokensTest(String[] sentenceTokens) {
    String[] negationContext =
        this.negationContextAnalyzer.getContext(sentenceTokens, Constants.NEGATION_TAG);

    return negationContext;
  }

  public List<String> getProcessedSentencesFromText(String inputText) {
    return getSentencesFromText(inputText, false);
  }

  /**
   * @return the sentenceAndSectionMatchers
   */
  public Map<SentenceSectionTaggerType, List<RaptatPair<String, String>>> getSentenceAndSectionMatchers() {
    return this.sentenceAndSectionMatchers;
  }

  public List<String> getSentencesFromText(String inputText, boolean getRawTokens) {

    Map<Span, Set<String>> sentenceSpansAndContext =
        this.textSplitter.detectSentenceSpans(inputText);
    if (sentenceSpansAndContext.isEmpty()) {
      return Collections.emptyList();
    }

    List<Span> sentenceSpans = new ArrayList<>(sentenceSpansAndContext.keySet());
    Collections.sort(sentenceSpans);
    List<String> sentences = new ArrayList<String>();

    for (Span curSpan : sentenceSpans) {
      String sentenceText = curSpan.getCoveredText(inputText).toString();

      /*
       * getSentenceTokens() returns a pair of lists, with the raw tokens (no stop words removed) on
       * the left side of the pair and the processed tokens (stop words removed if indicated)) on
       * the right.
       */
      RaptatPair<List<RaptatToken>, List<RaptatToken>> sentenceTokens =
          this.getSentenceTokens(sentenceText, curSpan);

      List<RaptatToken> tokenList = getRawTokens ? sentenceTokens.left : sentenceTokens.right;
      sentences.add(RaptatToken.getPreprocessedStringFromTokens(tokenList));
    }
    return sentences;
  }

  public RaptatPair<List<RaptatToken>, List<RaptatToken>> getSentenceTokens(String sentenceText) {
    Span span = new Span(0, sentenceText.length());
    return this.getSentenceTokens(sentenceText, span, 0);
  }

  public RaptatPair<List<RaptatToken>, List<RaptatToken>> getSentenceTokens(String sentenceText,
      Span sentenceSpan) {
    return getSentenceTokens(sentenceText, sentenceSpan, 0);
  }

  /**
   * This method is written to replace the original getSentenceTokens method. Here the processed
   * token list will be used for further processing rather than the raw token list.
   *
   * @ author Sanjib Saha
   *
   * @param sentenceText
   * @param sentenceSpan
   * @param tokenIndex
   * @return
   */
  public RaptatPair<List<RaptatToken>, List<RaptatToken>> getSentenceTokens(String sentenceText,
      Span sentenceSpan, int tokenIndex) {

    String stemString, augmentedTokenString;
    RaptatToken newToken;

    Span[] tokenPositions = this.tokenizer.tokenizePos(sentenceText);
    String[] baseTokenStrings = Span.spansToStrings(tokenPositions, sentenceText);

    // REGION: Logging Region
    if (TextAnalyzer.logger.isDebugEnabled()) {
      System.out.println("SentenceSpan:" + sentenceSpan + ", Sentence:" + sentenceText);
      for (int i = 0; i < tokenPositions.length; i++) {
        System.out.println("TokenSpan:" + tokenPositions[i] + ", Token:" + baseTokenStrings[i]);
      }
    }
    // ENDREGION

    ArrayList<String> alphanumSplitStrings = new ArrayList<>();
    ArrayList<Integer> alphanumSplitStringPositions = new ArrayList<>();
    for (int i = 0; i < baseTokenStrings.length; i++) {
      RaptatPair<List<String>, List<Integer>> alphaNumericSplitList =
          splitByAlphanumeric(baseTokenStrings[i], tokenPositions[i].getStart());

      alphanumSplitStrings.addAll(alphaNumericSplitList.left);
      alphanumSplitStringPositions.addAll(alphaNumericSplitList.right);
    }
    baseTokenStrings = alphanumSplitStrings.toArray(baseTokenStrings);

    String[] partsOfSpeech = this.posTagger.tag(baseTokenStrings);
    String[] preProcessedTokenStrings = this.stemmer.preProcess(baseTokenStrings);
    String[] reasonNoMedsContext = this.reasonNoMedsContextAnalyzer
        .getContext(preProcessedTokenStrings, Constants.REASON_NO_MEDS_TAG);

    /*
     * Note that this call which creates negationContextAndMarks is performed before removal of stop
     * words, so all words in the text are used for getting context
     */
    RaptatPair<String[], Mark[]> negationContextAndMarks = this.negationContextAnalyzer
        .getContextAndContextMarks(preProcessedTokenStrings, Constants.NEGATION_TAG);

    /*
     * Negation context tracks which tokens are negated in the preProcessedTokenString array
     */
    String[] negationContext = negationContextAndMarks.left;

    /*
     * The negationMarks array tracks tokens that are part of pre-, post-, pseudo-, or term-
     * negation phrases. Elements in this array may be used by other systems, such as a CRF
     * cue-scope marker for negation.
     */
    Mark[] negationMarks = negationContextAndMarks.right;

    /*
     * Create uncertain context just like negation context above
     */
    /*
     * Note that this call which creates negationContextAndMarks is performed before removal of stop
     * words, so all words in the text are used for getting context
     */
    RaptatPair<String[], Mark[]> uncertainContextAndMarks = this.uncertainContextAnalyzer
        .getContextAndContextMarks(preProcessedTokenStrings, Constants.UNCERTAIN_TAG);

    /*
     * Negation context tracks which tokens are uncertain in the preProcessedTokenString array
     */
    String[] uncertainContext = uncertainContextAndMarks.left;

    int sentenceStart = sentenceSpan.getStart();
    int tokenStart, tokenEnd;
    RaptatToken previousToken = null;

    List<RaptatToken> rawTokenList = new ArrayList<>(baseTokenStrings.length);
    List<RaptatToken> processedTokenList = new ArrayList<>(baseTokenStrings.length);

    for (int i = 0; i < preProcessedTokenStrings.length; i++) {
      tokenStart = alphanumSplitStringPositions.get(i) + sentenceStart;
      tokenEnd = tokenStart + baseTokenStrings[i].length();

      /*
       * Only stem tokens that have a part of speech within the openNLPToLVGMap - other parts of
       * speech are unlikely to change
       */
      if (TextAnalyzer.openNLPToLVGMap.containsKey(partsOfSpeech[i])) {
        stemString = this.stemmer.lemmatize(preProcessedTokenStrings[i],
            TextAnalyzer.openNLPToLVGMap.get(partsOfSpeech[i]));
      } else {
        stemString = preProcessedTokenStrings[i];
      }

      if (this.useStems) {
        augmentedTokenString = stemString;
      } else {
        augmentedTokenString = preProcessedTokenStrings[i];
      }

      if (this.usePOS) {
        augmentedTokenString += "_" + partsOfSpeech[i];
      }

      if (this.reasonNoMedsProcessing == ContextHandling.PROBABILISTIC) {
        augmentedTokenString += "_" + reasonNoMedsContext[i];
      }

      newToken = new RaptatToken(baseTokenStrings[i], preProcessedTokenStrings[i], stemString,
          augmentedTokenString, partsOfSpeech[i], String.valueOf(tokenStart),
          String.valueOf(tokenEnd), previousToken, reasonNoMedsContext[i],
          negationMarks[i].toString(), negationContext[i].equals(Constants.NEGATION_TAG),
          uncertainContext[i].equals(Constants.UNCERTAIN_TAG), tokenIndex++);
      previousToken = newToken;

      rawTokenList.add(newToken);

      if (!this.removeStopWords
          || !TextAnalyzer.stopWordSet.contains(preProcessedTokenStrings[i])) {
        processedTokenList.add(newToken);
        newToken.setProcessed(true);
      }
    }

    Collections.sort(processedTokenList, TextAnalyzer.tokenSorter);

    /*
     * Note that the only difference between the rawTokenList and the processedTokenList is that the
     * processedTokenList has stop words removed (if removeStopWords field is set to true)
     */
    return new RaptatPair<>(rawTokenList, processedTokenList);
  }

  public LinkedList<TextDocumentLine> getTextDocumentLines() {
    return (LinkedList<TextDocumentLine>) Collections.unmodifiableList(this.textDocumentLines);
  }

  public String[] getUncertainSentenceCue(String sentenceText) {
    String[] tokenStrings = this.tokenizer.tokenize(sentenceText);
    Span[] tokenPositions = this.tokenizer.tokenizePos(sentenceText);

    ArrayList<String> processedTokenStrings = new ArrayList<>();

    for (int i = 0; i < tokenStrings.length; i++) {
      RaptatPair<List<String>, List<Integer>> processedList =
          splitByAlphanumeric(tokenStrings[i], tokenPositions[i].getStart());

      processedTokenStrings.addAll(processedList.left);
    }

    tokenStrings = processedTokenStrings.toArray(tokenStrings);
    String[] uncertainContext = this.uncertainContextAnalyzer.getContextCue(tokenStrings);

    return uncertainContext;
  }

  // /////// Uncertainty detection... Sanjib June 25, 2015
  public String[] getUncertainSentenceTokensTest(String sentenceText) {
    String[] tokenStrings = this.tokenizer.tokenize(sentenceText);
    String[] uncertainContext =
        this.uncertainContextAnalyzer.getContext(tokenStrings, Constants.UNCERTAIN_TAG);

    return uncertainContext;
  }

  public String[] getUncertainSentenceTokensTest(String[] sentenceTokens) {
    String[] uncertainContext =
        this.uncertainContextAnalyzer.getContext(sentenceTokens, Constants.UNCERTAIN_TAG);

    return uncertainContext;
  }

  /**
   * @param matcherPath
   */
  public void loadSectionAndSentenceMatchers(String matcherPath) {
    if (matcherPath != null && !matcherPath.isEmpty() && new File(matcherPath).exists()) {
      TabReader reader = new TabReader(new File(matcherPath));
      String[] data;
      if (this.sentenceAndSectionMatchers == null) {
        this.sentenceAndSectionMatchers = new HashMap<>();
      }

      while ((data = reader.getNextData()) != null) {
        if (isValidMatcher(data)) {
          List<RaptatPair<String, String>> matchers;
          if (data[0].equalsIgnoreCase("section")) {
            addSectionMatcher(data[1], data[2]);
            if ((matchers =
                this.sentenceAndSectionMatchers.get(SentenceSectionTaggerType.SECTION)) == null) {
              matchers = new ArrayList<>();
              this.sentenceAndSectionMatchers.put(SentenceSectionTaggerType.SECTION, matchers);
            }
            matchers.add(new RaptatPair<>(data[1], data[2]));
          } else if (data[0].equalsIgnoreCase("sentence")) {
            addSentenceMatcher(data[1], data[2]);
            if ((matchers =
                this.sentenceAndSectionMatchers.get(SentenceSectionTaggerType.SENTENCE)) == null) {
              matchers = new ArrayList<>();
              this.sentenceAndSectionMatchers.put(SentenceSectionTaggerType.SENTENCE, matchers);
            }
            matchers.add(new RaptatPair<>(data[1], data[2]));
          }

        }
      }
    }

  }

  /**
   * Given a path to a document, break it down to sentences and tokens. Return it as a
   * RaptatDocument. Each sentence is represented as an annotated phrase. Tokens in the sentence are
   * stored in "raw" form as found in the document and in processed form, where they are stemmed,
   * tagged with POS, or with stop words removed according to the booleans useStems, usePOS, and
   * removeStopWrods.
   *
   * @param pathToTextDocument
   * @return
   *
   * @author Glenn Gobbel - Sep 10, 2012
   *
   */
  @SuppressWarnings("CallToPrintStackTrace")
  public RaptatDocument processDocument(String pathToTextDocument) {
    String inputText = null;
    Stopwatch timer = new Stopwatch();

    try {
      // timer.tic( "Reading in file from disk" );
      inputText = FileUtils.readFileToString(new File(pathToTextDocument), "UTF-8");
      // timer.toc( "File read complete" );
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    if (inputText != null) {
      timer.tic(
          "Processing sentences and tokens in document " + new File(pathToTextDocument).getName());
      RaptatDocument resultDocument = processText(inputText, Optional.of(pathToTextDocument));
      timer.toc("Document processed");
      return resultDocument;
    }
    return null;
  }


  public RaptatDocument processDocumentText(String inputText, Optional<String> documentSource) {
    Stopwatch timer = new Stopwatch();
    if (inputText != null) {
      timer.tic("Processing sentences and tokens in document " + documentSource);
      RaptatDocument resultDocument = processText(inputText, documentSource);
      timer.toc("Document processed");
      return resultDocument;
    } else {
      return null;
    }
  }


  public RaptatDocument processText(String inputText, Optional<String> documentPath) {
    boolean addTokensAsSentence = true;

    String sentenceText;

    IndexedTree<IndexedObject<RaptatToken>> processedTokensAsTree = null;
    IndexedTree<IndexedObject<RaptatToken>> rawTokensAsTree = null;
    List<AnnotatedPhrase> dictionaryAnnotations = new ArrayList<>();

    RaptatPair<List<RaptatToken>, List<RaptatToken>> sentenceTokens;

    Map<Span, Set<String>> sentenceSpansAndContext =
        this.textSplitter.detectSentenceSpans(inputText);

    List<AnnotatedPhrase> theSentences = new ArrayList<>(sentenceSpansAndContext.size());
    RaptatDocument theDocument = new RaptatDocument();
    theDocument.setTextSourcePath(documentPath);
    theDocument.setRawInputText(inputText);

    List<RaptatToken> processedTokensAsList = new ArrayList<>(Constants.TOKENS_PER_DOCUMENT);
    List<RaptatToken> rawTokensAsList = new ArrayList<>(Constants.TOKENS_PER_DOCUMENT);

    if (!sentenceSpansAndContext.isEmpty()) {

      AnnotatedPhrase previousSentence = null;

      List<Span> sentenceSpans = new ArrayList<>(sentenceSpansAndContext.keySet());
      Collections.sort(sentenceSpans);
      int tokenIndex = 0;
      for (Span curSpan : sentenceSpans) {
        sentenceText = curSpan.getCoveredText(inputText).toString();

        TextAnalyzer.logger.debug("Sentence:" + sentenceText);

        /*
         * getSentenceTokens() returns a pair of lists, with the raw tokens (no stop words removed)
         * on the left side of the pair and the processed tokens (stop words removed if indicated))
         * on the right.
         */
        // Added by Sanjib to replace the previous method
        sentenceTokens = this.getSentenceTokens(sentenceText, curSpan, tokenIndex);
        tokenIndex += sentenceTokens.left.size();

        Set<String> sentenceConcepts = sentenceSpansAndContext.get(curSpan);
        String annotatorID = "RaptatSentence_" + UniqueIDGenerator.INSTANCE.getUnique();
        String annotatorName = "RaptatSentence";
        AnnotatedPhrase newSentence =
            new AnnotatedPhrase(sentenceText, curSpan, sentenceTokens, addTokensAsSentence,
                previousSentence, theDocument, sentenceConcepts, annotatorID, annotatorName);
        theSentences.add(newSentence);

        previousSentence = newSentence;

        /*
         * The generalTokenContextAnalyzer is a dictionary-based annotator that adds annotations to
         * the document so they can be added later to the list of annotations found. This is
         * generally done if they do not overlap probabilistic annotations (probabilistic
         * annotations take precedence)
         */
        if (this.facilitatorBlockerDictionary != null) {
          List<AnnotatedPhrase> dictionaryAnnotationsForSentence =
              this.facilitatorBlockerDictionary.getSentenceAnnotations(newSentence);
          dictionaryAnnotations.addAll(dictionaryAnnotationsForSentence);
        }

        processedTokensAsList.addAll(sentenceTokens.right);
        rawTokensAsList.addAll(sentenceTokens.left);
      }

      processedTokensAsTree = generateIndexedTree(processedTokensAsList);
      rawTokensAsTree = generateIndexedTree(rawTokensAsList);
    }

    this.textDocumentLines =
        this.getTextDocumentLines(inputText, processedTokensAsTree, rawTokensAsTree);
    this.medListSectionMarker.markTokenSequences(theSentences, inputText);
    theDocument.setBaseSentences(theSentences);
    theDocument.setDictionaryAnnotations(dictionaryAnnotations);
    theDocument.setProcessedTokens(processedTokensAsTree);
    theDocument.setRawTokens(rawTokensAsTree);

    theDocument.setTextDocumentLines(this.textDocumentLines);
    return theDocument;
  }

  public void setFacilitatorBlockerDictionary(TokenContextAnalyzer dictionary) {
    this.facilitatorBlockerDictionary = dictionary;
  }

  public synchronized void setTokenProcessingOptions(OptionsManager optionsManager) {
    this.useStems = optionsManager.getUseStems();
    this.usePOS = optionsManager.getUsePOS();
    this.removeStopWords = optionsManager.getRemoveStopWords();
    this.maxPhraseTokens = optionsManager.getTrainingTokenNumber();
    this.reasonNoMedsProcessing = optionsManager.getReasonNoMedsProcessing();
  }

  /**
   * @param useStems
   * @param usePOS
   * @param removeStopWords
   *
   * @author Glenn Gobbel - Jun 28, 2012
   * @param maxElements
   * @param reasonNoMedsProcessing
   */
  public synchronized void setTokenProcessingParameters(boolean useStems, boolean usePOS,
      boolean removeStopWords, int maxElements, ContextHandling reasonNoMedsProcessing) {
    this.useStems = useStems;
    this.usePOS = usePOS;
    this.removeStopWords = removeStopWords;
    this.maxPhraseTokens = maxElements;
    this.reasonNoMedsProcessing = reasonNoMedsProcessing;
  }

  public synchronized void setTokenProcessingParameters(TokenProcessingOptions theOptions) {
    this.useStems = theOptions.useStems();
    this.usePOS = theOptions.usePOS();
    this.removeStopWords = theOptions.removeStopWords();
    this.maxPhraseTokens = theOptions.getMaxTokenNumber();
    this.reasonNoMedsProcessing = theOptions.getReasonNoMedsProcessing();
  }

  /**
   * This method is extracted from the original getTokensAndOffsets method. This method is designed
   * to deliver only the list of the modified token list, so that the number of tokens in Raw Token
   * List and in the Negex String Array size is same.
   *
   *
   * @param stringToSplit
   * @return
   */
  public RaptatPair<List<String>, List<Integer>> splitByAlphanumeric(String stringToSplit,
      int curTokenPosition) {
    ArrayList<String> tokenStringList = new ArrayList<>();
    List<Integer> tokenPosList = new ArrayList<>();

    /*
     * First get the non-alphanumeric characters that begin the string if they exist, add them to
     * the list, and remove them from the string so we only have to deal with strings that start
     * with alphanumeric characters followed by non-alphanumeric characters
     */
    TextAnalyzer.nonAlphaTokenStart.reset(stringToSplit);

    if (TextAnalyzer.nonAlphaTokenStart.find()) {
      String startString = TextAnalyzer.nonAlphaTokenStart.group(1);
      stringToSplit = TextAnalyzer.nonAlphaTokenStart.replaceFirst("");
      tokenStringList.add(startString);
      tokenPosList.add(curTokenPosition);
      curTokenPosition += startString.length();
    }

    TextAnalyzer.alphaNonAlphaAlternationMatcher.reset(stringToSplit);
    while (TextAnalyzer.alphaNonAlphaAlternationMatcher.find()) {
      for (int i = 1; i < 3; i++) {
        String curGroupString = TextAnalyzer.alphaNonAlphaAlternationMatcher.group(i);

        if (curGroupString != null && curGroupString.length() > 0) {
          tokenStringList.add(curGroupString);
          tokenPosList
              .add(TextAnalyzer.alphaNonAlphaAlternationMatcher.start(i) + curTokenPosition);
        }
      }
    }

    return new RaptatPair<>(tokenStringList, tokenPosList);
  }

  /**
   * Adjust the token lists in the document sentences and annotations according to the options
   * within this instance of the TextAnalyzer. The method regenerates "processed tokens," which are
   * those that are not in the stopWord list. Also, the "augmentedString" field of the processed
   * tokens is updated according to useStems, usePOS, and reasonNoMeds tagging set for this
   * TextAnalyzer instance. The processed tokens are added to the annotations in the
   * AnnotationGroup, and the annotations are split to conform to the field maxTokens for this
   * TextAnalyzer instance.
   *
   *
   * @author Glenn Gobbel - Jul 13, 2012
   * @param inputGroup
   */
  public void updateTrainingGroup(AnnotationGroup inputGroup) {
    PhraseTokenIntegrator theIntegrator = new PhraseTokenIntegrator();

    RaptatDocument raptatDocument = inputGroup.getRaptatDocument();
    TextAnalyzer.logger
        .debug("Regenerating processed tokens for document:" + raptatDocument.getTextSourceName());
    List<RaptatToken> processedTokens =
        raptatDocument.regenerateProcessedTokens(this.removeStopWords, this.useStems, this.usePOS,
            this.reasonNoMedsProcessing == ContextHandling.PROBABILISTIC, TextAnalyzer.stopWordSet);

    TextAnalyzer.logger.debug("Adding processed tokens to document");
    raptatDocument.setProcessedTokens(generateIndexedTree(processedTokens));

    TextAnalyzer.logger.debug("Adding processed tokens to document annotations");

    inputGroup.restoreSplitPhrases();
    Hashtable<Integer, AnnotatedPhrase> splitPhrases =
        theIntegrator.addProcessedTokensToAnnotations(inputGroup.referenceAnnotations,
            raptatDocument, this.usePOS, this.maxPhraseTokens);

    // Split phrases contains the processed tokens.
    inputGroup.setSplitPhrases(splitPhrases);
    inputGroup.setMaxTokens(this.maxPhraseTokens);
  }
  
  /**
   * Richard Noriega 3-24-2022 UIMARaptatInception
   * @param inputGroup
   */
  
  public static void updateTrainingGroup(AnnotationGroup inputGroup, boolean removeStopWords, boolean useStems, 
		  boolean usePOS, ContextHandling reasonNoMedsProcessing, int maxPhraseTokens) {
	    PhraseTokenIntegrator theIntegrator = new PhraseTokenIntegrator();

	    RaptatDocument raptatDocument = inputGroup.getRaptatDocument();
	    TextAnalyzer.logger
	        .debug("Regenerating processed tokens for document:" + raptatDocument.getTextSourceName());
	    List<RaptatToken> processedTokens =
	        raptatDocument.regenerateProcessedTokens(removeStopWords, useStems, usePOS,
	            reasonNoMedsProcessing == ContextHandling.PROBABILISTIC, TextAnalyzer.stopWordSet);

	    TextAnalyzer.logger.debug("Adding processed tokens to document");
	    raptatDocument.setProcessedTokens(generateIndexedTree(processedTokens));

	    TextAnalyzer.logger.debug("Adding processed tokens to document annotations");

	    inputGroup.restoreSplitPhrases();
	    Hashtable<Integer, AnnotatedPhrase> splitPhrases =
	        theIntegrator.addProcessedTokensToAnnotations(inputGroup.referenceAnnotations,
	            raptatDocument, usePOS, maxPhraseTokens);

	    // Split phrases contains the processed tokens.
	    inputGroup.setSplitPhrases(splitPhrases);
	    inputGroup.setMaxTokens(maxPhraseTokens);
	  }

  /**
   * Adjust the token lists in the document sentences and annotations according to the options
   * within this instance of the TextAnalyzer
   *
   * @param documentGroups
   *
   * @author Glenn Gobbel - Jul 13, 2012
   */
  public void updateTrainingGroups(List<AnnotationGroup> documentGroups) {
    for (AnnotationGroup curGroup : documentGroups) {
      updateTrainingGroup(curGroup);
    }

  }



  /**
   * Create an indexed tree out of a list of tokens so that the tree can be used to identify tokens
   * within a range of values making up a phrase or sentence
   *
   * @param theTokens
   *
   * @author Glenn Gobbel - Jun 28, 2012
   *
   * @return
   */
  private static IndexedTree<IndexedObject<RaptatToken>> generateIndexedTree(List<RaptatToken> theTokens) {
    IndexedObject<RaptatToken> curObject;
    List<IndexedObject<RaptatToken>> tokensAsObjects = new ArrayList<>(theTokens.size());
    for (RaptatToken curToken : theTokens) {
      curObject = new IndexedObject<>(Integer.parseInt(curToken.getStartOffset()), curToken);
      tokensAsObjects.add(curObject);
    }
    return new IndexedTree<>(tokensAsObjects);
  }

  private LinkedList<TextDocumentLine> getTextDocumentLines(String inputText,
      IndexedTree<IndexedObject<RaptatToken>> processedTokensAsTree,
      IndexedTree<IndexedObject<RaptatToken>> rawTokensAsTree) {

    LinkedList<TextDocumentLine> documentLines = new LinkedList<>();
    Matcher lineBreakMatcher = TextAnalyzer.simpleLineBreak.reset(inputText);
    int startOffset = 0;
    /**
     * 7/8/19 - DAX - Added line number reference
     */
    int lineNumber = 0;

    while (lineBreakMatcher.find()) {
      int endOffset = lineBreakMatcher.start();
      TextDocumentLine line =
          new TextDocumentLine(startOffset, endOffset, processedTokensAsTree, rawTokensAsTree);
      line.set_line_index(lineNumber);
      documentLines.add(line);

      startOffset = lineBreakMatcher.end();
      lineNumber++;
    }

    /*
     * The last bit of the text
     */
    if (startOffset < inputText.length()) {
      int endOffset = inputText.length();
      TextDocumentLine line =
          new TextDocumentLine(startOffset, endOffset, processedTokensAsTree, rawTokensAsTree);
      line.set_line_index(lineNumber);
      documentLines.add(line);
    }

    return documentLines;
  }

  private void initialize() {
    OptionsManager optionsManager = OptionsManager.getInstance();
    setTokenProcessingOptions(optionsManager);

    /*
     * Leave textSplitter set to null to use sentenceSplitter instead for splitting sentences
     */
    this.textSplitter = new RegexTextSplitter();
    this.posTagger = POSTaggerFactory.getInstance();
    this.tokenizer = TokenizerFactory.getInstance();
    this.negationContextAnalyzer = NegationContextAnalyzerFactory.getInstance();
    this.uncertainContextAnalyzer = UncertaintyContextAnalyzerFactory.getInstance();
    this.reasonNoMedsContextAnalyzer = ReasonNoMedsContextAnalyzerFactory.getInstance();
    this.medListSectionMarker = new MedListSectionMarker();
    // this.tokenPhraseMaker = new TokenPhraseMaker(null);

    /*
     * Note that we changed the line below so that the stemmer object never removes stop words -
     * this is handled elsewhere in the code by looking up words in the stopWordSet object.
     */
    this.stemmer = LemmatiserFactory.getInstance(false);
  }

  /**
   * @param data
   * @return
   */
  private boolean isValidMatcher(String[] data) {
    if (data.length != 3) {
      return false;
    }
    for (int i = 0; i < 3; i++) {
      if (data[i] == null || data[i].isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    /*
     * This is new testing code used in Sanjib's new code to test annotation. To implement,
     * uncomment the two lines below this one and comment out all other lines in this main method.
     */
    // TextAnalyzer ta = new TextAnalyzer();
    // ta.testAnnotateFile();

    int lvgSetting = UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      @SuppressWarnings("ResultOfObjectAllocationIgnored")
      public void run() {
        RunType runType = RunType.ANALYZE_DOCUMENT;

        switch (runType) {
          case CHECK_LEMMATIZATION:
            checkLemmatization();
            break;
          case RUN_SENTENCES:
            checkSentences();
            break;
          case ANALYZE_DOCUMENT:
            // String pathToDocument =
            // "D:\\CARTCL-IIR\\Glenn\\AKI_Analysis_160427\\TrainingCorpusXMLForCrossValidation\\Block_06_Note_048.txt";
            // String pathToDictionary =
            // "D:\\CARTCL-IIR\\Glenn\\AKI_Analysis_160427\\facilitatorBlockerDictionary_Updated_v01_160628.txt";
            // String pathToDocument =
            // "D:\\OneDrive_Vanderbilt\\OneDrive -
            // Vanderbilt\\Grants\\NuclearPower\\WestinghouseStandardTechSpecs_PW_Vol02.txt";
            String pathToDocument =
                "C:\\Users\\noriegrt\\Documents\\ADHAF_UIMA_TEXTS\\test.txt";
            analyzeDocument(pathToDocument, null);
            break;
          case CHECK_GETPROCESSEDTOKENSTRINGS_METHOD:
            String startString = "[: -]";
            checkGetProcessedTokenStrings(startString);
            break;
          // case RUN_
        }
      }

      private void analyzeDocument(String pathToDocument, String pathToDictionary) {
        TextAnalyzer ta = new TextAnalyzer(pathToDictionary, null);
        RaptatDocument doc = ta.processDocument(pathToDocument);

        for (AnnotatedPhrase curPhrase : doc.getDictionaryAnnotations(false)) {
          System.out.println(curPhrase.toString());
        }

      }

      private void checkGetProcessedTokenStrings(String startString) {
        TextAnalyzer ta = new TextAnalyzer();
        RaptatPair<List<String>, List<Integer>> results = ta.splitByAlphanumeric(startString, 0);

        System.out.println("StartString:" + startString);

        System.out.println("SplittingResults:");
        Iterator<String> stringIterator = results.left.iterator();
        Iterator<Integer> integerIterator = results.right.iterator();

        while (stringIterator.hasNext() && integerIterator.hasNext()) {
          System.out.println("[" + stringIterator.next() + "," + integerIterator.next() + "]");
        }
      }

      private void checkLemmatization() {
        TextAnalyzer ta = new TextAnalyzer();
        String inputSentence = "The entities were overwhelming and ran behind the cats and dogs";
        String[] tokenStrings = inputSentence.split(" ");
        String[] partsOfSpeech = ta.posTagger.tag(tokenStrings);

        String tokenStem;
        for (int i = 0; i < tokenStrings.length; i++) {
          String tokenString = tokenStrings[i];
          if (TextAnalyzer.openNLPToLVGMap.containsKey(partsOfSpeech[i])) {
            tokenStem = ta.stemmer.lemmatize(tokenString,
                TextAnalyzer.openNLPToLVGMap.get(partsOfSpeech[i]));
          } else {
            tokenStem = tokenString;
          }
          System.out.println("IN:" + tokenString + " OUT:" + tokenStem);
        }
      }

      private void checkSentences() {
        List<String> sentences = new ArrayList<>();

        {

          /*
           * "calculus" should NOT be tagged as an 'anatomicalkidneyfunction'concept because bladder
           * is a blocker
           */
          sentences.add("The bladder calculus did not compromise kidney function.");

          /*
           * "stones" should be tagged as an 'anatomicalkidneyfunction' concept.
           */
          sentences.add("His renal stones were compromising his ability to excrete by-products.");

          /* Nothing should be tagged. */
          sentences.add(
              "Her cystic ovaries were what was seen on the images and there were no signs of kidney dysfunction.");

          /*
           * "atrophic" should be tagged as an 'anatomicalkidneyfunction' concept.
           */
          sentences
              .add("Both her kidney and adrenal gland were atrophic leading to all the problems.");

          /*
           * "difficulty eating", "restrict fluids", and "appetite increase" should all be tagged as
           * "intake".
           */
          sentences.add(
              "The patient did not have difficulty eating, so we decided to restrict fluids and monitor whether his appetite increased.");

          /*
           * "place an icd" should be tagged as a 'contrastexposure' concept.
           */
          sentences.add(
              "The nurse will place an icd line so that contrast can be given during the procedure.");

          /*
           * "aortofemoral bypass" should be tagged as a 'potentialcontrastexposure' concept.
           */
          sentences.add("The aortofemoral bypass was done both with and without contrast.");

          /*
           * "aortofemoral bypass" should be tagged as a'potentialcontrastexposure' concept.
           */
          sentences.add(
              "We decided to go ahead with the aortofemoral bypass, even though the surgeon was not present and the mri image was to be a noncontrast procedure.");

          /*
           * "cancer" should be marked as an 'anatomicalkidneyfunction' concept.
           */
          sentences.add(
              "The surgery CT did not show any cancer left, but the CT taken after kidney surgery did.");
        }

        TextAnalyzer ta = new TextAnalyzer();

        /*
         * Change dictionaryPath below to switch from work PC to home Mac
         */
        // String dictionaryPath =
        // "/Users/glenn/SubversionMatheny/branches/GlennWorkspace/Resources/FaciliatorBlockerExamples/ConceptDictionaries/dictionary.txt";
        String dictionaryPath =
            "C:\\Users\\gobbelgt\\SubversionMatheny\\branches\\GlennWorkspace\\Resources\\FaciliatorBlockerExamples\\ConceptDictionaries\\dictionary.txt";

        /*
         * This method is better described as generating a context analyzer
         */
        TextAnalyzer.logger.debug("Generating context analyzer");
        ta.createDictionary(dictionaryPath, null);
        // ta.generalTokenContextAnalyzer.getGenContextBoundaryMarker().printPhraseTrees();

        // String str = "He is drug free.";
        // str =
        // "Both her kidney stone is leading appetite to all the
        // problems.";
        // str =
        // "The surgery CT did not show any cancer left, but the CT
        // taken after kidney surgery did.";
        int i = 0;
        for (String curSentence : sentences) {
          RaptatDocument doc = ta.processText(curSentence, Optional.empty());

          List<AnnotatedPhrase> phrases = doc.getActiveSentences();
          List<RaptatToken> sentenceTokens = phrases.get(0).getProcessedTokens();

          ta.facilitatorBlockerDictionary.setTokenConcepts(sentenceTokens);

          AnnotatedPhrase sentence = phrases.get(0);

          List<AnnotatedPhrase> annotations =
              ta.facilitatorBlockerDictionary.getSentenceAnnotations(sentence);

          // List<AnnotatedPhrase> annotations =
          // ta.generalTokenContextAnalyzer
          // .getTokenConcepts( phrases.get( 0 ) );

          System.out.println("\n\n\n\n\n\n---------------------------------------------------"
              + "--------------------------------------------");
          System.out.println("Sentence " + ++i + ") " + curSentence);
          System.out.println("-----------------------------------------------------------------"
              + "------------------------------\n");
          for (RaptatToken curToken : sentenceTokens) {
            System.out.print("String:");
            System.out.println(curToken.getTokenStringPreprocessed());

            System.out.print("Concept:");
            for (String s : curToken.getAssignedConcepts()) {
              System.out.print("\u001B[37;46m" + s + "\u001B[0m");
            }

            System.out.print("\nFacilitated:");
            for (String s : curToken.getFacilitated()) {
              System.out.print(" " + "\u001B[32m" + s + "\u001B[0m");
            }

            System.out.print("\nBlocked:");
            for (String s : curToken.getBlocked()) {
              System.out.print(" " + "\u001B[31m" + s + "\u001B[0m");
            }

            System.out.print("\n ---\n");
          }

          int j = 0;
          for (AnnotatedPhrase curPhrase : annotations) {
            System.out.println("Annotation " + j++ + ":" + curPhrase);

          }
        }
      }
    });
  }

  // /**
  // * Determine if tokens are part of a larger phrase
  // *
  // * @param tokenPhraseMaker2
  // *
  // * @param index_value_fsm_defn
  // * @param fsm_feature_labeler
  // * @param index_value_fsm_defn
  // * @param index_range_fsm_defn
  // * @param fsmDefinitions
  // *
  // * @param callback
  // *
  // * @param rawTokensAsList
  // * @return
  // */
  // private static List<RaptatTokenPhrase> identifyTokenPhrases(TokenPhraseMaker maker,
  // List<AnnotatedPhrase> theSentences, FSMFeatureLabeler fsm_feature_labeler,
  // FSMDefinition index_value_fsm_defn, FSMDefinition index_range_fsm_defn) {
  //
  // Collections.sort(theSentences);
  // List<RaptatTokenPhrase> tokenPhrases = maker.getTokenPhrases(theSentences, fsm_feature_labeler,
  // index_value_fsm_defn, index_range_fsm_defn);
  // return tokenPhrases;
  // }
}
