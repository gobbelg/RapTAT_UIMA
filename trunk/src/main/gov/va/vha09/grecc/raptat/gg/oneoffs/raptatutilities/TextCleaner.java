/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.raptatutilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.TextDocumentLine;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.stringtypeanalysis.SectionHeaderDetector;

/**
 * Utility class to replace all the strings in a given text file using a set of regular expression
 * to match the string to and the strings to replace any matches. The search and replace is
 * automatically case insensitive.
 *
 * @author gobbelgt
 *
 */
public class TextCleaner {

  private static final String ALLERGY_LIST_START_STRING = "^(\\s)*allerg(y|ies|\\.)*.*:.*$";
  private static Pattern ALLERGY_LIST_START_PATTERN =
      Pattern.compile(ALLERGY_LIST_START_STRING, Pattern.CASE_INSENSITIVE);
  private static final String ALLERGY_WRAP_STRING = "\\p{Alpha}+.*";
  private static final Pattern STOP_ALLERGY_WRAP_PATTERN =
      Pattern.compile("^.*(?<!aller(g|gy|gies|g\\.)):.*$");
  private static final String START_LINE_STRING = "^(\\s*)";
  private static String ALLERGY_LIST_CONTINUE_STRING = "\\p{Alpha}+.*";

  private static Pattern ALLERGY_LIST_START_CONTINUING_PATTERN =
      Pattern.compile(START_LINE_STRING + ALLERGY_LIST_CONTINUE_STRING, Pattern.CASE_INSENSITIVE);
  private static final Pattern LINE_WRAP_PATTERN = Pattern
      .compile("^([ \t]*)" + "(-[ \t]*|\\d{1,2}\\.[ \t]*|\\([ \t]{0,3}[xX][ \t]{0,3}\\)[ \t]*)?"
          + "(((A|I) \\w)|([A-Z]\\p{Alpha}+))" + ".*$"
          + "(((\r\n?|\n)[ \t]+)(((a|i) \\w)|([a-z]\\p{Alpha}+)).*$)"
          + "(?:\\7(((a|i) \\w)|([a-z]\\p{Alpha}+)).*$)*", Pattern.MULTILINE);

  private static final Logger LOGGER = Logger.getLogger(TextCleaner.class);
  private static TextAnalyzer textAnalyzer = new TextAnalyzer();

  private boolean unwrapAllergyLists = true;


  private boolean replaceRegexes = true;


  private boolean removeMedicationLists = true;


  private boolean unwrapGeneralText = true;


  private boolean unwrapSentences = true;


  private boolean demarcateSections = true;


  {
    LOGGER.setLevel(Level.INFO);
  }


  private Optional<Map<Integer, RaptatPair<String, String>>> regexAndStringReplacements =
      Optional.empty();


  public TextCleaner() {}


  /**
   * @param regexAndStringReplacements
   * @param unwrapAllergyLists
   * @param replaceRegexes
   * @param removeMedicationLists
   * @param unwrapGeneralText
   * @param unwrapSentences
   * @param demarcateSections
   */
  public TextCleaner(Optional<Map<Integer, RaptatPair<String, String>>> regexAndStringReplacements,
      boolean unwrapAllergyLists, boolean replaceRegexes, boolean removeMedicationLists,
      boolean unwrapGeneralText, boolean unwrapSentences, boolean demarcateSections) {
    super();
    this.regexAndStringReplacements = regexAndStringReplacements;
    this.unwrapAllergyLists = unwrapAllergyLists;
    this.replaceRegexes = replaceRegexes;
    this.removeMedicationLists = removeMedicationLists;
    this.unwrapGeneralText = unwrapGeneralText;
    this.unwrapSentences = unwrapSentences;
    this.demarcateSections = demarcateSections;
  }

  public TextCleaner(String regexFilePath) {
    super();
    if (regexFilePath != null) {
      File regexFile = new File(regexFilePath);
      if (!regexFile.exists()) {
        regexFile = GeneralHelper.getFile(
            "Select text file containing regular expressions and strings to be used for replacement.\n"
                + "Each line should contain the regular expression to locate followed by a tab followed by the string to use for replacement. ");
      }
      Map<Integer, RaptatPair<String, String>> replacements =
          getReplacementRegexesAndStrings(regexFile);
      regexAndStringReplacements = Optional.of(replacements);
    }
  }


  public TextCleaner(String regexFilePath, boolean unwrapAllergyLists, boolean replaceRegexes,
      boolean removeMedicationLists, boolean unwrapGeneralText, boolean unwrapSentences,
      boolean demarcateSections) {
    this(regexFilePath);
    this.unwrapAllergyLists = unwrapAllergyLists;
    this.replaceRegexes = replaceRegexes;
    this.removeMedicationLists = removeMedicationLists;
    this.unwrapGeneralText = unwrapGeneralText;
    this.unwrapSentences = unwrapSentences;
    this.demarcateSections = demarcateSections;
  }


  public List<String> cleanText(File textFile) {
    List<String> textLines = getFileText(textFile);
    Optional<String> optionalTextFilePath = Optional.of(textFile.getAbsolutePath());

    return cleanText(textLines, optionalTextFilePath);
  }


  /**
   * @return the demarcateSections
   */
  public boolean demarcateSections() {
    return demarcateSections;
  }


  /**
   * @return the regexAndStringReplacements
   */
  public Optional<Map<Integer, RaptatPair<String, String>>> getRegexAndStringReplacements() {
    return regexAndStringReplacements;
  }


  public void modifyFileUpdated(String textFilePath, File replacementFileDirectory,
      boolean demarcateSections, boolean unwrapGeneralText, boolean unwrapAllergyLists,
      boolean removeMedicationLists, boolean unwrapSentences, boolean replaceRegexes)
      throws IOException, IllegalArgumentException {

    File textFile = new File(textFilePath);
    if (!textFile.exists()) {
      textFile = GeneralHelper.getFile("Select text file containing text to be replaced");
    }

    List<String> textLines = getFileText(textFile);
    textLines =
        cleanText(textLines, Optional.of(textFilePath), demarcateSections, unwrapGeneralText,
            unwrapAllergyLists, removeMedicationLists, unwrapSentences, replaceRegexes);

    File replacementFile = getReplacementFile(textFile, replacementFileDirectory);
    try (PrintWriter pw = new PrintWriter(replacementFile)) {
      textLines.forEach(line -> {
        line = line.replaceAll("\\s+$", "");
        pw.println(line);
        pw.flush();
      });
      pw.flush();
      pw.close();
    }
  }


  /**
   * @return the removeMedicationLists
   */
  public boolean removeMedicationLists() {
    return removeMedicationLists;
  }

  /**
   * @return the replaceRegexes
   */
  public boolean replaceRegexes() {
    return replaceRegexes;
  }

  /**
   * @param demarcateSections the demarcateSections to set
   */
  public void setDemarcateSections(boolean demarcateSections) {
    this.demarcateSections = demarcateSections;
  }

  /**
   * @param regexAndStringReplacements the regexAndStringReplacements to set
   */
  public void setRegexAndStringReplacements(
      Optional<Map<Integer, RaptatPair<String, String>>> regexAndStringReplacements) {
    this.regexAndStringReplacements = regexAndStringReplacements;
  }

  /**
   * @param removeMedicationLists the removeMedicationLists to set
   */
  public void setRemoveMedicationLists(boolean removeMedicationLists) {
    this.removeMedicationLists = removeMedicationLists;
  }

  /**
   * @param replaceRegexes the replaceRegexes to set
   */
  public void setReplaceRegexes(boolean replaceRegexes) {
    this.replaceRegexes = replaceRegexes;
  }

  /**
   * @param unwrapAllergyLists the unwrapAllergyLists to set
   */
  public void setUnwrapAllergyLists(boolean unwrapAllergyLists) {
    this.unwrapAllergyLists = unwrapAllergyLists;
  }

  /**
   * @param unwrapGeneralText the unwrapGeneralText to set
   */
  public void setUnwrapGeneralText(boolean unwrapGeneralText) {
    this.unwrapGeneralText = unwrapGeneralText;
  }


  /**
   * @param unwrapSentences the unwrapSentences to set
   */
  public void setUnwrapSentences(boolean unwrapSentences) {
    this.unwrapSentences = unwrapSentences;
  }


  /**
   * @return the unwrapAllergyLists
   */
  public boolean unwrapAllergyLists() {
    return unwrapAllergyLists;
  }


  /**
   * @return the unwrapGeneralText
   */
  public boolean unwrapGeneralText() {
    return unwrapGeneralText;
  }

  /**
   * @return the unwrapSentences
   */
  public boolean unwrapSentences() {
    return unwrapSentences;
  }

  private List<String> cleanText(List<String> textLines, Optional<String> textFilePath) {

    boolean excludeHeadersFromWrapping = true;
    Pattern stopMatchingPattern = Pattern.compile("^\\s*-\\s{0,2}.*$");

    if (LOGGER.isInfoEnabled() & textFilePath.isPresent()) {

      LOGGER.info(MessageFormat.format("Original Text File {0}:\n\n",
          new File(textFilePath.get()).getName()));
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    /*
     * Add a blank line before headers so they are clearly separated from the preceding text
     */
    if (demarcateSections) {
      textLines = demarcateSections(textLines);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Headers detected:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    /*
     * Remove general text wrapping where a token sequence of a phrase or sentence appear to wrap
     * across lines
     */
    if (unwrapGeneralText) {
      textLines =
          unWrapText(textLines, LINE_WRAP_PATTERN, excludeHeadersFromWrapping, stopMatchingPattern);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Unwrapped general text:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (unwrapAllergyLists) {
      textLines = unwrapAllergyListsUpdated(textLines);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Unwrapped allergy lines:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (removeMedicationLists) {
      textLines = removeMedicationLists(textLines, textFilePath);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Med lists removed:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (unwrapSentences) {
      textLines = unwrapSentences(textLines, textFilePath);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Med lists removed:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (replaceRegexes && regexAndStringReplacements.isPresent()) {
      textLines = replaceTextUsingMap(textLines, regexAndStringReplacements.get());
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Strings replaced using regexes:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    return textLines;
  }


  private List<String> cleanText(List<String> textLines, Optional<String> textFilePath,
      boolean demarcateSections, boolean unwrapGeneralText, boolean unwrapAllergyLists,
      boolean removeMedicationLists, boolean unwrapSentences, boolean replaceRegexes) {
    boolean excludeHeadersFromWrapping = true;
    Pattern stopMatchingPattern = Pattern.compile("^\\s*-\\s{0,2}.*$");

    if (LOGGER.isInfoEnabled() & textFilePath.isPresent()) {

      LOGGER.info(MessageFormat.format("Original Text File {0}:\n\n",
          new File(textFilePath.get()).getName()));
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    /*
     * Add a blank line before headers so they are clearly separated from the preceding text
     */
    if (demarcateSections) {
      textLines = demarcateSections(textLines);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Headers detected:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    /*
     * Remove general text wrapping where a token sequence of a phrase or sentence appear to wrap
     * across lines
     */
    if (unwrapGeneralText) {
      textLines =
          unWrapText(textLines, LINE_WRAP_PATTERN, excludeHeadersFromWrapping, stopMatchingPattern);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Unwrapped general text:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (unwrapAllergyLists) {
      textLines = unwrapAllergyListsUpdated(textLines);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Unwrapped allergy lines:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (removeMedicationLists) {
      textLines = removeMedicationLists(textLines, textFilePath);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Med lists removed:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (unwrapSentences) {
      textLines = unwrapSentences(textLines, textFilePath);
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Med lists removed:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    if (replaceRegexes && regexAndStringReplacements.isPresent()) {
      textLines = replaceTextUsingMap(textLines, regexAndStringReplacements.get());
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Strings replaced using regexes:\n\n");
      textLines.forEach(line -> System.out.println(line));
      System.out.println("\n==========================\n\n");
    }

    return textLines;
  }


  private List<String> demarcateSections(List<String> textLines) {
    List<String> resultList = new ArrayList<String>(textLines.size());
    if (textLines == null || textLines.isEmpty()) {
      return resultList;
    }

    SectionHeaderDetector headerTyper = new SectionHeaderDetector();
    String systemLineBreak = System.lineSeparator();
    for (String line : textLines) {
      if (headerTyper.isType(line)) {
        resultList.add(systemLineBreak + line);
      } else {
        resultList.add(line);
      }
    }

    return resultList;
  }


  private List<String> getFileText(File textFile) {

    List<String> resultList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultList.add(line);
      }
    } catch (FileNotFoundException e) {
      System.out
          .println("File not found:" + textFile.getAbsolutePath() + "\n" + e.getLocalizedMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Unable to read from file:" + textFile.getAbsolutePath() + "\n"
          + e.getLocalizedMessage());
      e.printStackTrace();
    }
    return resultList;
  }


  private File getReplacementFile(File textFile, File replacementDirectory) throws IOException {
    String textFileNameOld = textFile.getName();
    String[] textFileNameOnly = textFileNameOld.split("\\.");
    return new File(replacementDirectory.getAbsolutePath(), textFileNameOnly[0] + ".txt");
  }

  private String getReplacementLine(String textLine,
      Map<Integer, RaptatPair<String, String>> regexAndStringReplacements) {

    for (Integer mapKey : regexAndStringReplacements.keySet()) {
      RaptatPair<String, String> regexAndReplacement = regexAndStringReplacements.get(mapKey);
      textLine =
          textLine.replaceAll(regexAndReplacement.left, " " + regexAndReplacement.right + " ");
    }
    return textLine;
  }

  /*
   * Generates an ordered map of regular expression replacements. The ordering will either be in the
   * order in the file, or, if an index is provided as the first element in the file according to
   * the index. If an index occurs more than once, the index will be incremented by one until a free
   * index is identified.
   */
  private Map<Integer, RaptatPair<String, String>> getReplacementRegexesAndStrings(File regexFile) {
    Map<Integer, RaptatPair<String, String>> resultMap = new TreeMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(regexFile))) {
      String textLine;
      int line = 0;
      while ((textLine = br.readLine()) != null) {
        // LOGGER.debug("\n\tREGEX:" + textLine + "\n");
        String[] regexReplacementPair = textLine.split("\t");

        /*
         * Handle spoecial case where a string is replace with an empty character
         */
        if (regexReplacementPair.length == 1) {
          regexReplacementPair = new String[] {regexReplacementPair[0], ""};
        }

        int replacedStringIndex = 0;
        Integer regexIndex = 0;
        if (regexReplacementPair.length == 2) {
          regexIndex = line;
        } else if (regexReplacementPair.length == 3) {
          regexIndex = Integer.parseInt(regexReplacementPair[0]);
          ++replacedStringIndex;
        } else {
          GeneralHelper.errorWriter("Incorrect formatting on line " + line
              + " of regex string replacement file\n" + "TEXT:" + textLine);
          System.exit(-1);
        }
        while (resultMap.containsKey(regexIndex)) {
          ++regexIndex;
        }
        resultMap.put(regexIndex,
            new RaptatPair<>("(?i)" + regexReplacementPair[replacedStringIndex].trim(),
                regexReplacementPair[replacedStringIndex + 1]));

        ++line;

      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return resultMap;
  }

  private List<String> removeMedicationLists(List<String> textLines,
      Optional<String> textFilePath) {
    String lineSeparator = System.lineSeparator();
    List<String> resultList = new ArrayList<>(textLines.size());

    final StringBuilder sb = new StringBuilder("");
    textLines.forEach(line -> sb.append(line).append(lineSeparator));
    RaptatDocument raptatDocument = textAnalyzer.processText(sb.toString(), textFilePath);

    // for (AnnotatedPhrase sentence : raptatDocument.getBaseSentences()) {
    // String fillString = sentence.getSequenceStringUnprocessed();
    // /*
    // * Keeps sentences together on a single line
    // */
    // fillString = fillString.replaceAll("\\r\\n?", "");
    //
    // /*
    // * Allergy lists often at beginning and end of med lists, and we don't want to exclude them
    // * even if they are labeled as part of the med list. We may need to revise how med lists are
    // * identified based on this, so this code is a temporary fix.
    // */
    // Matcher allergyStartMatcher = ALLERGY_LIST_START_PATTERN.matcher(fillString);
    //
    // if (!sentence.getSentenceAssociatedConcepts().contains(RaptatConstants.MED_SECTION_TAG)
    // || allergyStartMatcher.find()) {
    // resultList.add(fillString);
    // /*
    // * Add blank line between sentences
    // */
    // resultList.add("");
    // }
    // }

    for (TextDocumentLine textLine : raptatDocument.getTextDocumentLines()) {
      String fillString = textLine.getSequenceStringUnprocessed();

      /*
       * Allergy lists often at beginning and end of med lists, and we don't want to exclude them
       * even if they are labeled as part of the med list. We may need to revise how med lists are
       * identified based on this, so this code is a temporary fix.
       */
      Matcher allergyStartMatcher = ALLERGY_LIST_START_PATTERN.matcher(fillString);

      if (!textLine.getSequenceAssociatedConcepts().contains(Constants.MED_SECTION_TAG)
          || allergyStartMatcher.find()) {
        resultList.add(fillString);
      }
    }

    return resultList;
  }



  private List<String> replaceTextUsingMap(List<String> textLines,
      Map<Integer, RaptatPair<String, String>> regexAndStringReplacements) {
    List<String> resultList = new ArrayList<String>(textLines.size());
    textLines.forEach(line -> {
      resultList.add(getReplacementLine(line, regexAndStringReplacements));
    });
    return resultList;
  }

  private List<String> unwrapAllergyListsUpdated(List<String> textLines) {
    // return unWrapText(textLines, ALLERGY_WRAP_PATTERN, false, STOP_ALLERGY_MATCH_PATTERN);

    List<String> resultList = new ArrayList<String>(textLines.size());
    if (textLines == null || textLines.isEmpty()) {
      return resultList;
    }

    Iterator<String> lineIterator = textLines.iterator();
    String priorLine = lineIterator.next();
    Matcher startWrappingMatcher = ALLERGY_LIST_START_PATTERN.matcher(priorLine);
    boolean wrappingStarted = startWrappingMatcher.find();

    boolean continuedWrappingStarted = false;
    Pattern continueWrappingPattern = ALLERGY_LIST_START_CONTINUING_PATTERN;
    String line;
    while (lineIterator.hasNext() && (line = lineIterator.next()) != null) {

      Matcher stopWrappingMatcher = STOP_ALLERGY_WRAP_PATTERN.matcher(line);
      if (!wrappingStarted || stopWrappingMatcher.find()) {
        startWrappingMatcher = ALLERGY_LIST_START_PATTERN.matcher(line);
        wrappingStarted = startWrappingMatcher.find();
        continueWrappingPattern = ALLERGY_LIST_START_CONTINUING_PATTERN;
        resultList.add(priorLine);
        priorLine = line;
        continue;
      }

      Matcher continuedWrappingMatcher = continueWrappingPattern.matcher(line);
      if (continuedWrappingMatcher.find()) {
        if (!continuedWrappingStarted) {
          String backReference = continuedWrappingMatcher.group(1);
          continueWrappingPattern =
              Pattern.compile("^" + backReference + ALLERGY_WRAP_STRING + "$");
          continuedWrappingStarted = true;
        }
        priorLine += " " + line;
        continue;
      }

      wrappingStarted = false;
      continuedWrappingStarted = false;
      resultList.add(priorLine);
      priorLine = line;
    }
    resultList.add(priorLine);

    return resultList;
  }


  /**
   * Place each detected sentence on a single line with a blank line between. This reduces the need
   * for Canary to detect sentences.
   *
   * @param textLines
   * @param textFilePath
   * @return
   */
  private List<String> unwrapSentences(List<String> textLines, Optional<String> textFilePath) {
    String lineSeparator = System.lineSeparator();
    List<String> resultList = new ArrayList<>(textLines.size());

    final StringBuilder sb = new StringBuilder("");
    textLines.forEach(line -> sb.append(line).append(lineSeparator));
    RaptatDocument raptatDocument = textAnalyzer.processText(sb.toString(), textFilePath);

    for (AnnotatedPhrase sentence : raptatDocument.getBaseSentences()) {
      String fillString = sentence.getSequenceStringUnprocessed();
      /*
       * Keeps sentences together on a single line
       */
      fillString = fillString.replaceAll("\\r\\n?", " ");

      resultList.add(fillString);
      /*
       * Add blank line between sentences
       */
      resultList.add("");
    }
    return resultList;
  }



  /**
   * Remove any wrapping where a sentence may wrap to another line. At the end, the number of lines
   * for each sentence should be reduced, generally. This does not put each sentence on a single
   * line - there may be multiple sentences on a single line, and putting each sentence on a
   * separate line is handled by another method that detects sentences.
   *
   * @param textLines
   * @param unwrapPattern
   * @param excludeHeadersFromWrapping
   * @param stopMatchingPattern
   * @return
   */
  private List<String> unWrapText(List<String> textLines, Pattern unwrapPattern,
      boolean excludeHeadersFromWrapping, Pattern stopMatchingPattern) {
    List<String> resultList = new ArrayList<String>(textLines.size());
    if (textLines == null || textLines.isEmpty()) {
      return resultList;
    }

    SectionHeaderDetector headerTyper = new SectionHeaderDetector();
    Iterator<String> lineIterator = textLines.iterator();
    String priorLine = null;
    String systemLineBreak = System.lineSeparator();

    while (lineIterator.hasNext()) {

      String line = lineIterator.next();
      if (excludeHeadersFromWrapping && headerTyper.isType(line)) {

        if (priorLine != null) {
          resultList.add(priorLine);
        }
        resultList.add(line);
        priorLine = null;
        continue;
      }

      /*
       * priorLine will be null first time through while loop or after header. Otherwise, it will be
       * equivalent to the previous line and may be wrapped with current line.
       */
      if (priorLine != null) {
        /*
         * The stopMatchingPattern (if not null) creates a stopLineMatcher that will halt further
         * matching if the line variable matches it
         */
        Matcher stopLineMatcher =
            stopMatchingPattern == null ? null : stopMatchingPattern.matcher(line);

        if (stopMatchingPattern == null || !stopLineMatcher.find()) {
          String testLine = priorLine + systemLineBreak + line;
          Matcher lineWrapMatcher = unwrapPattern.matcher(testLine);
          if (lineWrapMatcher.find()) {
            line = line.replaceAll("^\\s+", "");
            priorLine += " " + line;
            continue;
          }
        }
        resultList.add(priorLine);
      }

      priorLine = line;
    }

    if (priorLine != null)

    {
      resultList.add(priorLine);
    }

    return resultList;
  }


  public static Level getLoggingState() {
    return LOGGER.getLevel();
  }


  /*
   * Call main with args[0] as the text filepath and args[1] the regex filepath or the final static
   * strings variables will be used
   */
  public static void main(String[] args) {

    UserPreferences.INSTANCE.initializeLVGLocation();
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {}

    });
  }


  public static Level setLoggingLevel(Level loggingLevel) {
    Level oldLevel = LOGGER.getLevel();
    LOGGER.setLevel(loggingLevel);

    return oldLevel;
  }
}
