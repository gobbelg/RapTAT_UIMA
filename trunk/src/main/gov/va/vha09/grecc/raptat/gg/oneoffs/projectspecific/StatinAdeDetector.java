package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;

import opennlp.tools.util.Span;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.crf.CrfSuiteRunner;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.crf.tagging.CSAttributeTagger;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.AttributeTaggerSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.tab.TabReader;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.raptatutilities.TextCleaner;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.AnnotatedPhraseFields;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

public class StatinAdeDetector {

  private class CanaryOutput {
    private final String noteTitle;
    private final String noteID;
    private final String outputCriteria;
    private final String noteStartOffset;
    private final String noteEndOffset;
    private final String fileStartOffset;
    private final String fileEndOffset;
    private final String sentence;
    private final String[] statins;
    private final String[] adverseEffects;
    private final String[] intolerances;

    private CanaryOutput(String noteTitle, String noteID, String outputCriteria,
        String noteStartOffset, String noteEndOffset, String fileStartOffset, String fileEndOffset,
        String sentence, String[] statins, String[] adverseEffects, String[] intolerances) {
      super();
      this.noteTitle = noteTitle;
      this.noteID = noteID;
      this.outputCriteria = outputCriteria;
      this.noteStartOffset = noteStartOffset;
      this.noteEndOffset = noteEndOffset;
      this.fileStartOffset = fileStartOffset;
      this.fileEndOffset = fileEndOffset;
      this.sentence = sentence;
      this.statins = statins;
      this.adverseEffects = adverseEffects;
      this.intolerances = intolerances;
    }

    private CanaryOutput(String[] canaryOutputFields) {
      this(canaryOutputFields[CanaryOutputColumn.NOTE_TITLE.ordinal()],
          canaryOutputFields[CanaryOutputColumn.NOTE_ID.ordinal()],
          canaryOutputFields[CanaryOutputColumn.OUTPUT_CRITERIA.ordinal()],
          canaryOutputFields[CanaryOutputColumn.NOTE_START_OFFSET.ordinal()],
          canaryOutputFields[CanaryOutputColumn.NOTE_END_OFFSET.ordinal()],
          canaryOutputFields[CanaryOutputColumn.FILE_START_OFFSET.ordinal()],
          canaryOutputFields[CanaryOutputColumn.FILE_END_OFFSET.ordinal()],
          canaryOutputFields[CanaryOutputColumn.SENTENCE.ordinal()],
          canaryOutputFields[CanaryOutputColumn.STATINS.ordinal()].split(","),
          canaryOutputFields[CanaryOutputColumn.ADVERSE_EFFECTS.ordinal()].split(","),
          canaryOutputFields[CanaryOutputColumn.INTOLERANCES.ordinal()].split(","));
    }

    private String[] getField(CanaryOutputColumn field) {

      switch (field) {
        case STATINS: {
          return this.statins;
        }
        case ADVERSE_EFFECTS: {
          return this.adverseEffects;
        }
        case INTOLERANCES: {
          return this.intolerances;
        }
        default: {
          return null;
        }
      }
    }
  }

  private enum CanaryOutputColumn {
    // Ordinal 0
    NOTE_TITLE,
    // Enum Ordinal 1
    NOTE_ID,
    // Enum Ordinal 2
    OUTPUT_CRITERIA,
    // Enum Ordinal 3
    NOTE_START_OFFSET,
    // Enum Ordinal 4
    NOTE_END_OFFSET,
    // Enum Ordinal 5
    FILE_START_OFFSET,
    // Enum Ordinal 6
    FILE_END_OFFSET,
    // Enum Ordinal 7
    SENTENCE,
    // Enum Ordinal 8
    STATINS,
    // Enum Ordinal 9
    ADVERSE_EFFECTS,
    // Enum Ordinal 10
    INTOLERANCES;

    private String toStringRepresentation;

    private CanaryOutputColumn() {
      this.toStringRepresentation = GeneralHelper.underlineToCamelCase(name());
    }

    @Override
    public String toString() {
      return this.toStringRepresentation;
    }
  }

  private enum PerformanceType {
    TRUE_POSITIVES, FALSE_POSITIVES, TRUE_NEGATIVES, FALSE_NEGATIVES;

    private String toStringRepresentation;

    PerformanceType() {
      this.toStringRepresentation = GeneralHelper.underlineToCamelCase(name());
    }

    @Override
    public String toString() {
      return this.toStringRepresentation;
    }
  }

  private enum RunType {
    RUN_CANARY, FULL_VIRANI_PROJECT_RUN, ANALYZE_CANARY_OUTPUT, FILE_CONVERSION_TO_CANARY_FORMAT, CYCLIC_GRAPH_CHECK, ANALYZE_CANARY_OUTPUT_MULTISET
  }

  private class TextRevisionSettings {
    boolean unwrapAllergyLists = true;
    boolean demarcateSections = true;
    boolean replaceRegexes = true;
    boolean removeMedicationLists = true;
    boolean unwrapGeneralText = true;
    protected boolean unwrapSentences = true;

    /*
     * Constructor for creating instance that uses default settings assigned to the fields
     */
    public TextRevisionSettings() {}

    @SuppressWarnings("unused")
    public TextRevisionSettings(boolean unwrapGeneralText, boolean demarcateSections,
        boolean unwrapAllergyLists, boolean replaceRegexes, boolean unwrapSentences,
        boolean removeMedicationLists) {
      super();
      this.unwrapGeneralText = unwrapGeneralText;
      this.demarcateSections = demarcateSections;
      this.unwrapAllergyLists = unwrapAllergyLists;
      this.removeMedicationLists = removeMedicationLists;
      this.unwrapSentences = unwrapSentences;
      this.replaceRegexes = replaceRegexes;
    }
  }


  private TextAnalyzer textAnalyzer = new TextAnalyzer();

  private void addProcessedTokensToPhrases(List<AnnotatedPhrase> phrases,
      AnnotatedPhrase associatedSentence) {
    List<RaptatToken> sentenceTokens = associatedSentence.getProcessedTokens();
    IndexedTree<IndexedObject<RaptatToken>> indexedTokens =
        RaptatToken.generateIndexedTree(sentenceTokens);

    for (AnnotatedPhrase phrase : phrases) {
      List<RaptatToken> phraseTokens = new ArrayList<>();
      int phraseStart = Integer.parseInt(phrase.getRawTokensStartOff());
      int phraseEnd = Integer.parseInt(phrase.getRawTokensEndOff());

      RaptatToken sentenceToken =
          indexedTokens.getClosestLesserOrEqualObject(phraseStart).getIndexedObject();
      while (Integer.parseInt(sentenceToken.getStartOffset()) < phraseEnd) {
        phraseTokens.add(sentenceToken);
        int tokenEnd = Integer.parseInt(sentenceToken.getEndOffset());
        IndexedObject<RaptatToken> indexedTokenObject =
            indexedTokens.getClosestGreaterObject(tokenEnd);

        if (indexedTokenObject == null) {
          break;
        }
        sentenceToken = indexedTokens.getClosestGreaterObject(tokenEnd).getIndexedObject();
      }

      phrase.setProcessedTokens(phraseTokens);
    }
  }

  private List<List<String>> convertToCanaryOutput(
      Map<CanaryOutput, Map<CanaryOutputColumn, List<AnnotatedPhrase>>> outputToPhraseMap) {
    for (Map<CanaryOutputColumn, List<AnnotatedPhrase>> mappedValue : outputToPhraseMap.values()) {
      for (List<AnnotatedPhrase> phraseList : mappedValue.values()) {
        ListIterator<AnnotatedPhrase> listIterator = phraseList.listIterator();
        while (listIterator.hasNext()) {
          removeNextIfNegated(listIterator);
        }
      }
    }

    List<List<String>> outputList = new ArrayList<>();
    // First create title row
    List<String> titleList = new ArrayList<>();
    for (CanaryOutputColumn outputColumn : CanaryOutputColumn.values()) {
      titleList.add(outputColumn.toString());
    }
    outputList.add(titleList);

    for (CanaryOutput outputRow : outputToPhraseMap.keySet()) {
      List<String> row = new ArrayList<>();
      row.add(outputRow.noteTitle);
      row.add(outputRow.noteID);

      row.add(outputRow.outputCriteria);

      row.add(outputRow.noteStartOffset);
      row.add(outputRow.noteEndOffset);

      row.add(outputRow.fileStartOffset);
      row.add(outputRow.fileEndOffset);

      row.add(outputRow.sentence);

      String mappedPhrases;
      Map<CanaryOutputColumn, List<AnnotatedPhrase>> mappedColumns =
          outputToPhraseMap.get(outputRow);
      mappedPhrases = getMappedPhrasesAsString(mappedColumns, CanaryOutputColumn.STATINS);
      row.add(mappedPhrases);
      mappedPhrases = getMappedPhrasesAsString(mappedColumns, CanaryOutputColumn.ADVERSE_EFFECTS);
      row.add(mappedPhrases);
      mappedPhrases = getMappedPhrasesAsString(mappedColumns, CanaryOutputColumn.INTOLERANCES);
      row.add(mappedPhrases);
      outputList.add(row);
    }

    return outputList;
  }

  private List<AnnotatedPhrase> getAssociatedPhrases(CanaryOutput canaryFields,
      CanaryOutputColumn column) {

    List<AnnotatedPhrase> resultPhrases = new ArrayList<>();
    for (String phraseText : canaryFields.getField(column)) {
      phraseText = phraseText.trim();
      if (phraseText != null && !phraseText.isEmpty()) {
        List<RaptatPair<String, String>> textOffsets =
            getTextOffsets(canaryFields.sentence, phraseText);
        for (RaptatPair<String, String> startEndPair : textOffsets) {
          AnnotatedPhraseFields phraseFields = new AnnotatedPhraseFields(column.toString(),
              startEndPair.left, startEndPair.right, phraseText);
          AnnotatedPhrase phrase = new AnnotatedPhrase(phraseFields);
          resultPhrases.add(phrase);
        }
      }
    }
    return resultPhrases;
  }

  private Map<CanaryOutputColumn, List<AnnotatedPhrase>> getAssociatedPhrases(
      CanaryOutput canaryFields, CanaryOutputColumn[] columns) {
    Map<CanaryOutputColumn, List<AnnotatedPhrase>> resultMap = new HashMap<>();

    for (CanaryOutputColumn column : columns) {
      List<AnnotatedPhrase> associatedPhrases = getAssociatedPhrases(canaryFields, column);
      resultMap.put(column, associatedPhrases);
    }

    return resultMap;
  }

  /**
   * @param replacementFilesDirectory
   */
  private File getCanaryFormatFile(File replacementFilesDirectory) {
    String parentDirectoryPath = replacementFilesDirectory.getParent();
    String canaryFormatName =
        replacementFilesDirectory.getName() + "_CanaryFormat_" + GeneralHelper.getTimeStamp();
    String canaryFormatFileDirectory = parentDirectoryPath + File.separator + canaryFormatName;
    try {
      FileUtils.forceMkdir(new File(canaryFormatFileDirectory));
    } catch (IOException e) {
      GeneralHelper
          .errorWriter("Unable to create directory for canary files:" + e.getLocalizedMessage());
      e.printStackTrace();
      System.exit(-1);
    }

    String canaryFormatFileName = "CanaryFormatFile_" + GeneralHelper.getTimeStamp() + ".txt";
    String canaryFormatFilePath = canaryFormatFileDirectory + File.separator + canaryFormatFileName;
    return new File(canaryFormatFilePath);
  }

  /**
   * Create a Java object from an array of data representing a single row of output from Canary.
   * Note that one row may refer to multiple statins, adverse effects, and intolerances, which are
   * then stored as arrays within the CanaryOutputFields object.
   *
   * @param dataLine
   * @return
   */
  private CanaryOutput getCanaryOutputFields(String[] dataLine) {
    String noteTitle = dataLine[CanaryOutputColumn.NOTE_TITLE.ordinal()];
    String noteID = dataLine[CanaryOutputColumn.NOTE_ID.ordinal()];
    String outputCriteria = dataLine[CanaryOutputColumn.OUTPUT_CRITERIA.ordinal()];
    String noteStartOffset = dataLine[CanaryOutputColumn.NOTE_START_OFFSET.ordinal()];
    String noteEndOffset = dataLine[CanaryOutputColumn.NOTE_END_OFFSET.ordinal()];
    String fileStartOffset = dataLine[CanaryOutputColumn.FILE_START_OFFSET.ordinal()];
    String fileEndOffset = dataLine[CanaryOutputColumn.FILE_END_OFFSET.ordinal()];
    String sentenceOffset = dataLine[CanaryOutputColumn.SENTENCE.ordinal()];

    String[] statins = new String[1];
    int column;
    column = CanaryOutputColumn.STATINS.ordinal();
    if (dataLine.length >= column) {
      statins = dataLine[CanaryOutputColumn.STATINS.ordinal()].split(",");
    }

    String[] adverseEffects = new String[1];
    column = CanaryOutputColumn.ADVERSE_EFFECTS.ordinal();
    if (dataLine.length >= column) {
      adverseEffects = dataLine[column].split(",");
    }
    String[] intolerances = new String[1];
    column = CanaryOutputColumn.ADVERSE_EFFECTS.ordinal();
    if (dataLine.length > column) {
      intolerances = dataLine[CanaryOutputColumn.INTOLERANCES.ordinal()].split(",");
    }

    return new CanaryOutput(noteTitle, noteID, outputCriteria, noteStartOffset, noteEndOffset,
        fileStartOffset, fileEndOffset, sentenceOffset, statins, adverseEffects, intolerances);
  }

  private String getMappedPhrasesAsString(
      Map<CanaryOutputColumn, List<AnnotatedPhrase>> mappedColumns, CanaryOutputColumn column) {
    StringBuilder sb = new StringBuilder("");
    List<AnnotatedPhrase> phrases = mappedColumns.get(column);
    String delimiter = "";
    for (AnnotatedPhrase phrase : phrases) {
      sb.append(delimiter).append(phrase.getSequenceStringUnprocessed());
      delimiter = ", ";
    }

    return sb.toString();
  }

  private CSAttributeTagger getNegationTagger(String attributeTaggerPath) {
    AttributeTaggerSolution solution =
        AttributeTaggerSolution.loadFromFile(new File(attributeTaggerPath));
    CrfSuiteRunner taggerRunner = new CrfSuiteRunner();
    return new CSAttributeTagger(solution, taggerRunner);
  }

  private Map<CanaryOutput, Map<CanaryOutputColumn, List<AnnotatedPhrase>>> getOutputToFieldToPhraseMap(
      List<String[]> canaryOutput) {
    Map<CanaryOutput, Map<CanaryOutputColumn, List<AnnotatedPhrase>>> resultMap = new HashMap<>();
    for (String[] dataLine : canaryOutput) {
      CanaryOutput dataFields = getCanaryOutputFields(dataLine);
      CanaryOutputColumn[] columns = new CanaryOutputColumn[] {CanaryOutputColumn.STATINS,
          CanaryOutputColumn.ADVERSE_EFFECTS, CanaryOutputColumn.INTOLERANCES};
      Map<CanaryOutputColumn, List<AnnotatedPhrase>> associatedPhrases =
          getAssociatedPhrases(dataFields, columns);
      resultMap.put(dataFields, associatedPhrases);
    }
    return resultMap;
  }

  private AnnotatedPhrase getSentenceAsPhrase(String sentenceText) {
    RaptatPair<List<RaptatToken>, List<RaptatToken>> sentenceTokens =
        this.textAnalyzer.getSentenceTokens(sentenceText);
    return new AnnotatedPhrase(sentenceText, new Span(0, sentenceText.length()), sentenceTokens,
        true, null, null, null, "", "");
  }

  /**
   * Determine the beginning and end offsets for all matches of the string, phraseText, within
   * another string, sentence, and return them as a list of start,end pairs.
   *
   * @param sentence
   * @param phraseText
   * @return
   */
  private List<RaptatPair<String, String>> getTextOffsets(String sentence, String phraseText) {
    List<RaptatPair<String, String>> resultList = new ArrayList<>();
    Pattern regex = Pattern.compile(phraseText, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    Matcher regexMatcher = regex.matcher(sentence);
    while (regexMatcher.find()) {
      String start = String.valueOf(regexMatcher.start());
      String end = String.valueOf(regexMatcher.end());
      resultList.add(new RaptatPair<>(start, end));
    }
    return resultList;
  }

  private void removeNextIfNegated(ListIterator<AnnotatedPhrase> listIterator) {
    AnnotatedPhrase phrase = listIterator.next();
    for (RaptatAttribute attribute : phrase.getPhraseAttributes()) {
      if (attribute.getName().equalsIgnoreCase("assertionstatus")) {
        for (String value : attribute.getValues()) {
          if (value.startsWith("neg")) {
            listIterator.remove();
            return;
          }
        }
      }
    }
  }

  private List<String[]> runCanary(String absoluteCanaryPath, String canaryModelPath,
      String notesDirectoryPath, String outputFoldername, String outputFilename,
      String absolutePerlPath) {
    Process p;

    String wrapperPath = new File(absoluteCanaryPath, "Wrapper.pl").getAbsolutePath();
    String configPath = "%" + new File(canaryModelPath, "ConfigFile.txt").getAbsolutePath();
    String outputPath = "+" + new File(canaryModelPath, outputFoldername).getAbsolutePath();


    ProcessBuilder builder = new ProcessBuilder(absolutePerlPath, wrapperPath, notesDirectoryPath,
        configPath, outputPath);
    builder.redirectErrorStream(true);

    /*
     * You will need to copy cmd.exe to a location that is accessible to perl to get this to run.
     * Otherwise, it will be unable to "spawn" cmd.exe. By placing cmd.exe in the same directory as
     * Wrapper.pl, "ABSOLUTE_CANARY_PATH," and setting the ProcessBuilder builder directory to this
     * same directory, both can be found when running Canary.
     */
    builder.directory(new File(absoluteCanaryPath));

    try {

      p = builder.start();

      // getInputStream gives an Input stream connected to
      // the process p's standard output. Use it to make
      // a BufferedReader to readLine() what the program writes out.
      InputStream instreamFromProcess = p.getInputStream();
      BufferedReader is = new BufferedReader(new InputStreamReader(instreamFromProcess));

      String line;
      while ((line = is.readLine()) != null) {
        String trimmedLine = line.trim();
        System.out.println("\n" + trimmedLine);
      }
    } catch (IOException exception) {
      System.out.println("Unable to start builder");
      return null;
    }

    return getCanaryOutput(canaryModelPath, outputFoldername, outputFilename);
  }

  private void writeRevisedCanaryOutput(List<List<String>> revisedCanaryOutput,
      String canaryModelPath) {
    revisedCanaryOutput.forEach(line -> {
      line.forEach(word -> System.out.print(word + "\t"));
      System.out.println();
    });
  }

  protected File convertToCanaryFormat(File replacementFilesDirectory, String documentDelimiter) {
    File[] filesForConversion = replacementFilesDirectory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
      }
    });

    File canaryFormatFile = getCanaryFormatFile(replacementFilesDirectory);
    try (PrintWriter pw = new PrintWriter(canaryFormatFile)) {
      for (File file : filesForConversion) {
        String fileName = file.getName();
        fileName = fileName.substring(0, fileName.length() - 4);
        pw.println();
        pw.println(fileName + documentDelimiter);
        System.out.println("Converting " + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = br.readLine()) != null) {
            pw.println(line);
            pw.flush();
          }
          br.close();
        }
      }
      pw.close();
    } catch (IOException e) {
      GeneralHelper.errorWriter("Unable to convert to canary format:" + e.getLocalizedMessage());
      e.printStackTrace();
      System.exit(-1);
    }

    return canaryFormatFile;
  }

  /**
   * Get data from Canary as list of string arrays, where each element in the list corresponds to
   * one row from the Canary output, ignoring the Canary file header. Each string array is filled
   * with empty strings as needed to make the length equal to the number of values in the
   * CanaryOutputColumn enum in case some elements of the row are null.
   *
   * @param canaryModelPath
   * @return
   */
  protected List<String[]> getCanaryOutput(String canaryModelPath, String outputFoldername,
      String canaryOutputFilename) {

    List<String[]> resultList = new ArrayList<>();
    File outputFile =
        new File(canaryModelPath + File.separator + outputFoldername, canaryOutputFilename);
    TabReader outputFileReader = new TabReader(outputFile, false);
    outputFileReader.setTrimData(true);
    String[] dataLine;

    int expectedDataLength = CanaryOutputColumn.values().length;
    while ((dataLine = outputFileReader.getNextData()) != null && dataLine.length > 0) {

      if (dataLine.length < expectedDataLength) {
        String[] tempDataLine = Arrays.copyOf(dataLine, expectedDataLength);
        for (int i = dataLine.length; i < expectedDataLength; i++) {
          tempDataLine[i] = "";
        }
        dataLine = tempDataLine;
      }
      resultList.add(dataLine);
    }

    return resultList;
  }

  /**
   * Use a CSAttributeTagger to determine which phrases that were identified by Canary are negated
   * and exclude them from the output.
   *
   * @param canaryOutput
   * @param attributeTaggerPath
   * @return
   */
  protected List<List<String>> reviseCanaryOutput(List<String[]> canaryOutput,
      String attributeTaggerPath) {
    Map<CanaryOutput, Map<CanaryOutputColumn, List<AnnotatedPhrase>>> outputToFieldToPhraseMap =
        getOutputToFieldToPhraseMap(canaryOutput);
    CSAttributeTagger negationTagger = getNegationTagger(attributeTaggerPath);

    for (CanaryOutput canaryOutputRow : outputToFieldToPhraseMap.keySet()) {
      AnnotatedPhrase sentenceAsPhrase = getSentenceAsPhrase(canaryOutputRow.sentence.trim());
      Map<CanaryOutputColumn, List<AnnotatedPhrase>> columnNameToPhraseMap =
          outputToFieldToPhraseMap.get(canaryOutputRow);
      List<AnnotatedPhrase> allPhrases = new ArrayList<>();
      columnNameToPhraseMap.forEach((key, phrases) -> allPhrases.addAll(phrases));
      addProcessedTokensToPhrases(allPhrases, sentenceAsPhrase);
      negationTagger.assignCSAttributes(allPhrases, sentenceAsPhrase);
    }

    List<List<String>> resultOutput = convertToCanaryOutput(outputToFieldToPhraseMap);

    return resultOutput;
  }

  /**
   * @param args
   */
  public final static void main(String[] args) {

    UserPreferences.INSTANCE.setLvgPath("P:\\ORD_Virani_201706002D\\NLP\\lvg2014");
    StatinAdeDetector viraniStatinRunner = new StatinAdeDetector();

    SwingUtilities.invokeLater(new Runnable() {
      final class PerformanceStats {
        EnumMap<PerformanceType, Set<String>> performanceMap =
            new EnumMap<PerformanceType, Set<String>>(PerformanceType.class);

        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        public PerformanceStats(Set<String> truePositiveDocuments,
            Set<String> falsePositiveDocuments, Set<String> trueNegativeDocuments,
            Set<String> falseNegativeDocuments) {
          super();
          this.performanceMap.put(PerformanceType.TRUE_POSITIVES, truePositiveDocuments);
          this.truePositives = truePositiveDocuments.size();

          this.performanceMap.put(PerformanceType.FALSE_POSITIVES, falsePositiveDocuments);
          this.falsePositives = falsePositiveDocuments.size();

          this.performanceMap.put(PerformanceType.TRUE_NEGATIVES, trueNegativeDocuments);
          this.trueNegatives = trueNegativeDocuments.size();

          this.performanceMap.put(PerformanceType.FALSE_NEGATIVES, falseNegativeDocuments);
          this.falseNegatives = falseNegativeDocuments.size();
        }

        public double getF1() {
          return (double) 2 * this.truePositives
              / (2 * this.truePositives + this.falsePositives + this.falseNegatives);
        }

        public double getPrecision() {
          return (double) this.truePositives / (this.truePositives + this.falsePositives);
        }

        public double getRecall() {
          return (double) this.truePositives / (this.truePositives + this.falseNegatives);
        }

        public double getSensitivity() {
          return getRecall();
        }

        public double getSpecificity() {
          return (double) this.trueNegatives / (this.trueNegatives + this.falsePositives);
        }

        public void print(PrintWriter pw) {
          pw.println("Document\tTP,FP,TN,FN");
          pw.flush();
          for (Entry<PerformanceType, Set<String>> entry : this.performanceMap.entrySet()) {
            String performanceType = entry.getKey().toStringRepresentation;
            for (String documentName : entry.getValue()) {
              pw.println(documentName + "\t" + performanceType);
              pw.flush();
            }
          }

          pw.println();
          pw.println("Performance Measure\tPerformance");
          pw.flush();

          for (Entry<PerformanceType, Set<String>> entry : this.performanceMap.entrySet()) {
            String performanceType = "Total " + entry.getKey().toStringRepresentation;
            int total = entry.getValue().size();
            pw.println(performanceType + "\t" + total);
          }
          pw.flush();

          DecimalFormat decimalFormat = new DecimalFormat("0.000");
          pw.println("Precision\t" + decimalFormat.format(getPrecision()));
          pw.println("Recall\t" + decimalFormat.format(getRecall()));
          pw.println("F1\t" + decimalFormat.format(getF1()));
          pw.println("Sensitivity\t" + decimalFormat.format(getSensitivity()));
          pw.println("Specificity\t" + decimalFormat.format(getSpecificity()));
          pw.flush();
        }

      }

      @Override
      public void run() {
        /*
         * This string path should be the location of both the 'Wrapper.pl' file supplied with
         * Canary and, for the Windows OS, the location of the cmd.exe (Command Line) application.
         * Note that you may need to copy cmd.exe to this location.
         */
        final String ABSOLUTE_CANARY_PATH =
            "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\canary-1.3.1b\\canary-1.3.1b\\canary\\perl_code";

        /*
         * We explicitly specify use of the version of perl distributed with the Canary application
         */
        final String ABSOLUTE_PERL_PATH =
            "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\canary-1.3.1b\\canary-1.3.1b\\canary\\perl64\\bin\\perl.exe";

        final String CANARY_OUTPUT_FILENAME = "SentenceLevelOutput_1.txt";

        final String OUTPUT_FOLDERNAME = "OutputFolder";

        final String NOTES_FOLDERNAME = "Notes"; // "NotesFolderRevised_TestShort";

        final String DOCUMENT_DELIMITER = "*|#*|#*|#1*|#*|#*|#";

        final String REVISED_NOTES_FOLDERNAME =
            "NotesFolderRevised_" + GeneralHelper.getTimeStamp();

        RunType runType = RunType.RUN_CANARY;

        System.out.println("Running RunType " + runType.toString() + " in "
            + new Object() {}.getClass().getEnclosingClass().getName());

        switch (runType) {

          case RUN_CANARY: {

            // String canaryModelPath =
            // "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\"
            // + "Group05_CanaryTest_190619\\CanaryModel_VersionB02_190711";

            String canaryModelPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\FullyDevelopedCanaryModel_200909";

            String notesDirectoryPath = canaryModelPath + File.separator + NOTES_FOLDERNAME;

            List<String[]> canaryOutput;
            if ((canaryOutput = viraniStatinRunner.runCanary(ABSOLUTE_CANARY_PATH, canaryModelPath,
                notesDirectoryPath, OUTPUT_FOLDERNAME, CANARY_OUTPUT_FILENAME,
                ABSOLUTE_PERL_PATH)) == null) {
              GeneralHelper.errorWriter("Unable to run canary and produce output");
              System.exit(-1);
            }

            String attributeTaggerPath =
                "H:\\Documents\\EclipseViraniStatinWorkspace_190515\\CueScopeSolution_Rd00_CSTrainingFeatures_UnprocessedTokens_v01_160721_175700.atsn";
            List<List<String>> revisedCanaryOutput =
                viraniStatinRunner.reviseCanaryOutput(canaryOutput, attributeTaggerPath);
            viraniStatinRunner.writeRevisedCanaryOutput(revisedCanaryOutput, canaryModelPath);
          }
            break;
          case ANALYZE_CANARY_OUTPUT: {
            String canaryModelPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\Groups05_06_07_08_09_ADE_REF_NCMP_200128";
            String xmlDirectoryPath = canaryModelPath + File.separator + "XmlGroups_05_06_07_08_09";
            File xmlDirectory = new File(xmlDirectoryPath);

            String fullTextFileDirectoryPath = canaryModelPath + File.separator + "NotesFolder";
            File fullTextFileDirectory = new File(fullTextFileDirectoryPath);

            String resultDirectoryPath = canaryModelPath;
            File resultFile = new File(resultDirectoryPath,
                "CanaryPerformance_" + GeneralHelper.getTimeStamp() + ".txt");

            String canarySentenceOutputPath = canaryModelPath + File.separator + OUTPUT_FOLDERNAME
                + File.separator + "SentenceLevelOutput_1.txt";
            File canaryOutputFile = new File(canarySentenceOutputPath);

            /*
             * documentNameColumn is the column of the canary output that contains document names
             * 001_document_statinrefusal
             */
            String documentColumnName = "NoteID";
            String ruleColumnName = "OutputCriteria";
            // Set<String> conceptSet =
            // new HashSet<>(Arrays.asList(new String[]
            // {"000_document_statinadverseevent"}));
            Set<String> conceptSet =
                new HashSet<>(Arrays.asList(new String[] {"001_document_statinrefusal"}));
            int ruleToBeMatched = 1;
            PerformanceStats performance =
                compareCanaryToReference(xmlDirectory, conceptSet, canaryOutputFile,
                    fullTextFileDirectory, documentColumnName, ruleColumnName, ruleToBeMatched);
            writeCanaryPerformanceResults(performance, resultFile);
          }
            break;

          case ANALYZE_CANARY_OUTPUT_MULTISET: {

            String canaryModelPath =
                // "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\Groups05_06_07_08_ADE_REF_NCMP_200120";
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\Groups_10_11_12_ADE_REF_NCMP_200205";
            // String xmlDirectoryPath = canaryModelPath +
            // File.separator + "XmlGroups_05_06_07_08";
            String groupTag = "_Group_12";
            String xmlDirectoryPath = canaryModelPath + File.separator + "Xml" + groupTag;
            File xmlDirectory = new File(xmlDirectoryPath);

            String fullTextFileDirectoryPath =
                canaryModelPath + File.separator + "NotesFolder" + groupTag;;
            File fullTextFileDirectory = new File(fullTextFileDirectoryPath);

            String resultDirectoryPath = canaryModelPath;
            File resultFile = new File(resultDirectoryPath,
                "CanaryPerformance_" + GeneralHelper.getTimeStamp() + ".txt");

            String canarySentenceOutputPath =
                resultDirectoryPath + "\\OutputFolder" + groupTag + "\\SentenceLevelOutput_1.txt";
            File canaryOutputFile = new File(canarySentenceOutputPath);

            /*
             * documentNameColumn is the column of the canary output that contains document names
             * 001_document_statinrefusal
             */
            String documentColumnName = "NoteID";
            String ruleColumnName = "OutputCriteria";

            /*
             * This maps a concept name or (conceptString) to the column it is reported in within
             * the results. We can put multiple concepts in the keys of the map and allow checking
             * performance of each one.
             */
            Map<String, Integer> conceptToCanaryOutputMatch = new HashMap<>();
            // String conceptString = "001_Document_StatinRefusal";
            // conceptToCanaryOutputMatch.put(conceptString.toLowerCase(),
            // 1);
            String conceptString = "000_Document_StatinAdverseEvent";
            conceptToCanaryOutputMatch.put(conceptString.toLowerCase(), 2);

            /*
             * Returns results for each concept in hte conceptToCanaryOutputMatch
             */
            Map<String, PerformanceStats> conceptToPerformanceMap =
                compareCanaryToReferenceMultiSet(xmlDirectory, canaryOutputFile,
                    fullTextFileDirectory, documentColumnName, ruleColumnName,
                    conceptToCanaryOutputMatch);
            writeCanaryPerformanceResultsMultiSet(conceptToPerformanceMap, resultDirectoryPath);
          }
            break;

          case FILE_CONVERSION_TO_CANARY_FORMAT: {

            String canaryModelPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\"
                    + "OriginalTurchinModelTestOn_Groups_10_11_12_200728";
            /*
             * Modify text file to concatenate lines after allergies and replace certain text
             * according to a file of regular expressions
             */
            String textFileDirectoryPath = canaryModelPath + File.separator
                + "OriginalRawNotes_Groups_10_11_12\\NotesFolder_Group_12";

            /*
             * We won't use regexFilePath when we don't do any text cleaning
             */
            // String regexFilePath =
            // canaryModelPath + File.separator + "CliticFileRaptat_v17_191106.txt";
            String regexFilePath =
                canaryModelPath + File.separator + "RaptatAdjustedCliticfile_200729.txt";

            /*
             * For doing original Canary conversion, turn off all text revision settings except for
             * regex replacement. We do the regex replacement instead of letting Canary handle it.
             * Otherwise, set them to true (or use default constructor).
             */


            // TextRevisionSettings revisionSettings = viraniStatinRunner.new TextRevisionSettings(
            // false, false, false, true, false, false);
            TextRevisionSettings revisionSettings =
                viraniStatinRunner.new TextRevisionSettings(true, true, true, true, true, true);
            Level loggingLevel = Level.OFF;
            File revisedTextDirectory =
                reviseAndStoreTextFiles(canaryModelPath, textFileDirectoryPath, regexFilePath,
                    revisionSettings, REVISED_NOTES_FOLDERNAME, loggingLevel);

            /*
             * Convert revised text documents to canary format
             */
            File canaryFileDirectory =
                viraniStatinRunner.convertToCanaryFormat(revisedTextDirectory, DOCUMENT_DELIMITER);
            System.out.println(
                "Canary Format File Directory:\n\t" + canaryFileDirectory.getAbsolutePath());
          }
            break;
          case FULL_VIRANI_PROJECT_RUN: {
            String canaryModelPath =
                "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\"
                    + "OriginalTurchinModelTestOn_Groups_10_11_12_200728";
            /*
             * Modify text file to concatenate lines after allergies and replace certain text
             * according to a file of regular expressions
             */
            String textFileDirectoryPath =
                canaryModelPath + File.separator + "Original_Group05_Txt";

            String regexFilePath =
                canaryModelPath + File.separator + "RaptatAdjustedCliticfile_200729.txt";

            TextRevisionSettings revisionSettings =
                viraniStatinRunner.new TextRevisionSettings(true, true, true, true, true, true);
            Level loggingLevel = Level.OFF;
            File revisedTextDirectory =
                reviseAndStoreTextFiles(canaryModelPath, textFileDirectoryPath, regexFilePath,
                    revisionSettings, REVISED_NOTES_FOLDERNAME, loggingLevel);

            /*
             * Convert revised text documents to canary format
             */
            File canaryFileDirectory =
                viraniStatinRunner.convertToCanaryFormat(revisedTextDirectory, DOCUMENT_DELIMITER);

            /*
             * Get original canary output
             */
            List<String[]> canaryOutput;
            if ((canaryOutput = viraniStatinRunner.runCanary(ABSOLUTE_CANARY_PATH, canaryModelPath,
                canaryFileDirectory.getParent(), OUTPUT_FOLDERNAME, CANARY_OUTPUT_FILENAME,
                ABSOLUTE_PERL_PATH)) == null) {
              GeneralHelper.errorWriter("Unable to run canary and produce output");
              System.exit(-1);
            }
            /*
             * Revise output using Raptat trained negation model
             */
            String attributeTaggerPath = "H:\\Documents\\EclipseViraniStatinWorkspace_190515\\"
                + "CueScopeSolution_Rd00_CSTrainingFeatures_UnprocessedTokens_v01_160721_175700.atsn";
            List<List<String>> revisedCanaryOutput =
                viraniStatinRunner.reviseCanaryOutput(canaryOutput, attributeTaggerPath);
            viraniStatinRunner.writeRevisedCanaryOutput(revisedCanaryOutput, canaryModelPath);

          }
            break;
          case CYCLIC_GRAPH_CHECK: {
          }
            break;
          default:
            break;
        }

        System.exit(0);
      }

      private PerformanceStats compareCanaryToReference(File xmlDirectory, Set<String> conceptSet,
          File canaryOutputFile, File textFileDirectory, String documentColumnName,
          String ruleColumnName, int ruleToBeMatched) {

        Set<String> truePositiveDocuments = new HashSet<String>();
        Set<String> trueNegativeDocuments = new HashSet<String>();
        Set<String> falseNegativeDocuments = new HashSet<String>();

        Set<String> adjudicatedPositiveDocuments =
            getAdjudicatedDocumentsWithConcepts(textFileDirectory, xmlDirectory, conceptSet);
        Set<String> canaryPositiveDocuments = getCanaryPositiveDocuments(canaryOutputFile,
            documentColumnName, ruleColumnName, ruleToBeMatched);

        Iterator<String> adjudicatedIterator = adjudicatedPositiveDocuments.iterator();
        while (adjudicatedIterator.hasNext()) {
          String adjudicatedDocument = adjudicatedIterator.next();

          /*
           * Any adjudicated documents that match are true positive. We remove these from the
           * canaryDocuments, which leaves the false positives in the canaryDocuments set. Ones that
           * don't match are false negatives.
           *
           */
          if (canaryPositiveDocuments.contains(adjudicatedDocument)) {
            truePositiveDocuments.add(adjudicatedDocument);
            canaryPositiveDocuments.remove(adjudicatedDocument);
          } else {
            falseNegativeDocuments.add(adjudicatedDocument);
          }
        }

        File[] falsePositiveFiles = textFileDirectory.listFiles(new FilenameFilter() {
          @Override
          public boolean accept(File dir, String name) {
            name = name.replace(".txt", "").trim();
            return !truePositiveDocuments.contains(name) && !canaryPositiveDocuments.contains(name)
                && !falseNegativeDocuments.contains(name);
          }
        });

        /*
         * Unmatched canary files are false positives. True negatives are any text file directory
         * files not in the list of .
         */
        for (File file : falsePositiveFiles) {
          trueNegativeDocuments.add(file.getName().replace(".txt", "").trim());
        }

        return new PerformanceStats(truePositiveDocuments, canaryPositiveDocuments,
            trueNegativeDocuments, falseNegativeDocuments);
      }

      private Map<String, PerformanceStats> compareCanaryToReferenceMultiSet(File xmlDirectory,
          File canaryOutputFile, File textFileDirectory, String documentColumnName,
          String ruleColumnName, Map<String, Integer> conceptToCanaryOutputMatch) {

        Map<String, PerformanceStats> conceptToPerformanceMap = new HashMap<>();
        // Map<String, Set<String>> referenceDocuments =
        // getAdjudicatedDocumentsWithConcepts(
        // textFileDirectory, xmlDirectory, conceptToCanaryOutputMatch);
        Map<String, Set<String>> referenceDocuments = getAdjudicatedDocumentsByConcept(
            textFileDirectory, xmlDirectory, conceptToCanaryOutputMatch.keySet());
        Map<Integer, Set<String>> canaryPositiveDocuments = getCanaryPositiveDocuments(
            canaryOutputFile, documentColumnName, ruleColumnName, conceptToCanaryOutputMatch);

        for (String concept : referenceDocuments.keySet()) {
          Set<String> truePositiveDocuments = new HashSet<String>();
          Set<String> trueNegativeDocuments = new HashSet<String>();
          Set<String> falseNegativeDocuments = new HashSet<String>();
          int canaryRuleMatch = conceptToCanaryOutputMatch.get(concept);
          Set<String> documentsFoundByCanary = canaryPositiveDocuments.get(canaryRuleMatch);

          Iterator<String> referenceIterator = referenceDocuments.get(concept).iterator();
          while (referenceIterator.hasNext()) {
            String referenceDocument = referenceIterator.next();

            /*
             * Any adjudicated documents that match are true positive. We remove these from the
             * canaryDocuments, which leaves the false positives in the canaryDocuments set. Ones
             * that don't match are false negatives.
             *
             */
            if (documentsFoundByCanary.contains(referenceDocument)) {
              truePositiveDocuments.add(referenceDocument);
              documentsFoundByCanary.remove(referenceDocument);
            } else {
              falseNegativeDocuments.add(referenceDocument);
            }
          }
          File[] falsePositiveFiles = textFileDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
              name = name.replace(".txt", "").trim();
              return !truePositiveDocuments.contains(name) && !documentsFoundByCanary.contains(name)
                  && !falseNegativeDocuments.contains(name);
            }
          });
          /*
           * Unmatched canary files are false positives. True negatives are any text file directory
           * files not in the list of .
           */
          for (File file : falsePositiveFiles) {
            trueNegativeDocuments.add(file.getName().replace(".txt", "").trim());
          }
          conceptToPerformanceMap.put(concept, new PerformanceStats(truePositiveDocuments,
              documentsFoundByCanary, trueNegativeDocuments, falseNegativeDocuments));
        }

        return conceptToPerformanceMap;
      }

      /**
       * @param adjudicatedFile
       * @param adjudicatedPositiveDocuments
       * @return
       */
      private Set<String> getAdjudicatedDocuments(File adjudicatedFile) {
        Set<String> documents = new HashSet<>();
        try {
          InputStreamReader isr = new InputStreamReader(new FileInputStream(adjudicatedFile));
          BufferedReader br = new BufferedReader(isr);
          String line = null;
          while ((line = br.readLine()) != null) {
            documents.add(line.replace(".txt", "").trim());
          }
        } catch (IOException e) {
          GeneralHelper
              .errorWriter("Unable to read from file:" + adjudicatedFile.getAbsolutePath());
          e.printStackTrace();
          System.exit(-1);
        }
        return documents;
      }

      /**
       * Inserts a mapping from each concept in conceptSet to the documents the concept is found in.
       *
       * @param textFileDirectory
       * @param xmlDirectory
       * @param conceptSet
       * @return
       */
      private Map<String, Set<String>> getAdjudicatedDocumentsByConcept(File textFileDirectory,
          File xmlDirectory, Set<String> conceptSet) {
        Map<String, Set<String>> resultMap = new HashMap<>();
        List<RaptatPair<File, File>> xmlAndTxtFiles =
            GeneralHelper.getXmlFilesAndMatchTxt(textFileDirectory, xmlDirectory);

        AnnotationImporter importer = new AnnotationImporter(AnnotationApp.EHOST);

        for (RaptatPair<File, File> xmlAndTxtPair : xmlAndTxtFiles) {
          String textDocumentName = xmlAndTxtPair.left.getName().replaceAll(".txt", "");
          System.out.println("Processing:" + textDocumentName);
          List<AnnotatedPhrase> annotations =
              importer.importAnnotations(xmlAndTxtPair.right.getAbsolutePath(),
                  xmlAndTxtPair.left.getAbsolutePath(), conceptSet);

          /*
           * Track concepts found in the document so we can stop as soon as possible if we find a
           * document contains all concepts in conceptSet.
           */
          Set<String> foundConcepts = new HashSet<String>();
          for (AnnotatedPhrase phrase : annotations) {
            String annotatedConcept = phrase.getConceptName().toLowerCase();
            if (!foundConcepts.contains(annotatedConcept)
                && conceptSet.contains(annotatedConcept)) {
              foundConcepts.add(annotatedConcept);
              Set<String> mappedDocuments;
              if ((mappedDocuments = resultMap.get(annotatedConcept)) == null) {
                mappedDocuments = new HashSet<>();
                resultMap.put(annotatedConcept, mappedDocuments);
              }
              mappedDocuments.add(textDocumentName);
              if (foundConcepts.equals(conceptSet)) {
                break;
              }
            }
          }
        }
        return resultMap;
      }

      private Map<String, Set<String>> getAdjudicatedDocumentsWithConcepts(File textFileDirectory,
          File xmlDirectory, Map<String, Integer> conceptToCanaryOutputMatch) {
        Map<String, Set<String>> resultMap = new HashMap<>();

        for (String concept : conceptToCanaryOutputMatch.keySet()) {
          Set<String> conceptSet = new HashSet<>();
          conceptSet.add(concept);
          Set<String> foundDocuments =
              getAdjudicatedDocumentsWithConcepts(textFileDirectory, xmlDirectory, conceptSet);
          resultMap.put(concept, foundDocuments);
        }
        return resultMap;
      }

      private Set<String> getAdjudicatedDocumentsWithConcepts(File textFileDirectory,
          File xmlDirectory, Set<String> conceptSet) {
        Set<String> resultDocuments = new HashSet<String>();
        List<RaptatPair<File, File>> xmlAndTxtFiles =
            GeneralHelper.getXmlFilesAndMatchTxt(textFileDirectory, xmlDirectory);

        AnnotationImporter importer = new AnnotationImporter(AnnotationApp.EHOST);

        for (RaptatPair<File, File> xmlAndTxtPair : xmlAndTxtFiles) {
          String textDocumentName = xmlAndTxtPair.left.getName().replaceAll(".txt", "");
          System.out.println("Processing:" + textDocumentName);
          List<AnnotatedPhrase> annotations =
              importer.importAnnotations(xmlAndTxtPair.right.getAbsolutePath(),
                  xmlAndTxtPair.left.getAbsolutePath(), conceptSet);
          for (AnnotatedPhrase phrase : annotations) {
            if (conceptSet.contains(phrase.getConceptName().toLowerCase())) {
              resultDocuments.add(textDocumentName);
              break;
            }
          }
        }
        return resultDocuments;
      }

      /**
       * @param canaryOutputFile
       * @param documentColumnName
       * @param ruleColumnName
       * @param ruleToBeMatched
       * @param canaryPositiveDocuments
       * @return
       */
      private Set<String> getCanaryPositiveDocuments(File canaryOutputFile,
          String documentColumnName, String ruleColumnName, int ruleToBeMatched) {

        Set<String> documents = new HashSet<>();
        try {
          InputStreamReader isr = new InputStreamReader(new FileInputStream(canaryOutputFile));
          BufferedReader br = new BufferedReader(isr);
          String line = null;

          /*
           * Find which columns contain the document name and rules matched by canary
           */
          int documentColumnIndex = -1;
          int ruleColumnIndex = -1;
          if ((line = br.readLine()) != null) {
            String[] lineElements = line.split("\t");
            for (int i = 0; i < lineElements.length; i++) {
              if (lineElements[i].toLowerCase().equals(documentColumnName.toLowerCase())) {
                documentColumnIndex = i;
              } else if (lineElements[i].toLowerCase().equals(ruleColumnName.toLowerCase())) {
                ruleColumnIndex = i;
              }
            }
          }

          if (documentColumnIndex > -1 && ruleColumnIndex > -1) {
            while ((line = br.readLine()) != null) {
              String[] lineElements = line.split("\t");
              for (String matchedRuleNumber : lineElements[ruleColumnIndex].split(",")) {
                if (ruleToBeMatched == Integer.parseInt(matchedRuleNumber.trim())) {
                  documents.add(lineElements[documentColumnIndex].trim());
                  break;
                }
              }
            }
          }

        } catch (IOException e) {
          GeneralHelper
              .errorWriter("Unable to read from file:" + canaryOutputFile.getAbsolutePath());
          e.printStackTrace();
          System.exit(-1);
        }

        return documents;
      }

      private Map<Integer, Set<String>> getCanaryPositiveDocuments(File canaryOutputFile,
          String documentColumnName, String ruleColumnName,
          Map<String, Integer> conceptToCanaryOutputMatch) {

        Map<Integer, Set<String>> resultMap = new HashMap<>();
        for (Integer ruleNumber : conceptToCanaryOutputMatch.values()) {
          Set<String> canaryPositiveDocuments = getCanaryPositiveDocuments(canaryOutputFile,
              documentColumnName, ruleColumnName, ruleNumber);
          resultMap.put(ruleNumber, canaryPositiveDocuments);
        }
        return resultMap;
      }

      /**
       * @param canaryModelPath
       * @param textFileDirectoryPath
       * @param regexFilePath
       * @return
       */
      private File reviseAndStoreTextFiles(String canaryModelPath, String textFileDirectoryPath,
          String regexFilePath, TextRevisionSettings revisionSettings,
          String revisedNotesFoldername, Level loggingLevel) {
        boolean unwrapAllergyLists = revisionSettings.unwrapAllergyLists;
        boolean replaceRegexes = revisionSettings.replaceRegexes;
        boolean removeMedicationLists = revisionSettings.removeMedicationLists;
        boolean unwrapText = revisionSettings.unwrapGeneralText;
        boolean unwrapSentences = revisionSettings.unwrapSentences;
        boolean demarcateSections = revisionSettings.demarcateSections;
        TextCleaner textCleaner;
        if (replaceRegexes) {
          textCleaner = new TextCleaner(regexFilePath);
        } else {
          textCleaner = new TextCleaner();
        }

        Level loggingLevelStart = TextCleaner.setLoggingLevel(loggingLevel);

        File revisedFileDirectory =
            new File(canaryModelPath + File.separator + revisedNotesFoldername);
        try {
          FileUtils.forceMkdir(revisedFileDirectory);
        } catch (IOException e1) {
          GeneralHelper.errorWriter("Unable to revise files:" + e1.getLocalizedMessage());
          e1.printStackTrace();
          System.exit(-1);
        }

        Collection<File> textFileList =
            FileUtils.listFiles(new File(textFileDirectoryPath), new String[] {"txt"}, false);
        int fileIndex = 0;
        int numberOfFiles = textFileList.size();
        for (File textFile : textFileList) {
          try {
            System.out.println(MessageFormat.format("Converting file {0} of {1}:{2}", fileIndex++,
                numberOfFiles, textFile.getName()));
            textCleaner.modifyFileUpdated(textFile.getAbsolutePath(), revisedFileDirectory,
                demarcateSections, unwrapText, unwrapAllergyLists, removeMedicationLists,
                unwrapSentences, replaceRegexes);
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        TextCleaner.setLoggingLevel(loggingLevelStart);
        return revisedFileDirectory;
      }

      private void writeCanaryPerformanceResults(PerformanceStats performance, File outputFile) {
        try {
          PrintWriter pw = new PrintWriter(System.out);
          performance.print(pw);

          pw = new PrintWriter(outputFile);
          performance.print(pw);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }

      private void writeCanaryPerformanceResultsMultiSet(Map<String, PerformanceStats> performance,
          String resultDirectoryPath) {

        for (String concept : performance.keySet()) {
          File resultFile = new File(resultDirectoryPath, "CanaryPerformance_"
              + concept.toUpperCase() + "_" + GeneralHelper.getTimeStamp() + ".txt");

          writeCanaryPerformanceResults(performance.get(concept), resultFile);
        }
      }
    });

  }
}
