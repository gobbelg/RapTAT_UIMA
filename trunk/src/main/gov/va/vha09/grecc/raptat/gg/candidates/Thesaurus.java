/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.candidates;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import components.SwingConsole;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.TextReader;

/**
 *
 *
 * @author Glenn T. Gobbel Jan 6, 2017
 */
public enum Thesaurus {
  INSTANCE;

  private enum RunType {
    COMMAND_LINE_SYNONYMS, FIND_COMMON_SYNONYMS;
  }

  private static final Logger LOGGER = Logger.getLogger(Thesaurus.class);
  static {
    LOGGER.setLevel(Level.INFO);
  }

  private final static HashSet<String> ALLOWED_POS_SET =
      new HashSet<>(Arrays.asList(new String[] {"verb", "adj", "noun", "adv"}));

  HashMap<String, List<RaptatPair<String, List<String>>>> synonymMap;


  private Thesaurus() {
    InputStream dictionaryInputStream =
        this.getClass().getResourceAsStream(Constants.RELATIVE_THESAURUS_PATH);

    this.synonymMap = this.read(dictionaryInputStream);

    System.out.println("Completed thesaurus read");
  }


  /**
   * May 4, 2018
   *
   * Get parts-of-speech for given word
   *
   * @param synonymWord
   * @return
   */
  public List<String> getPosAsList(String synonymWord) {
    List<String> resultList = new ArrayList<>();
    List<RaptatPair<String, List<String>>> posList = this.synonymMap.get(synonymWord);

    if (posList != null) {
      for (RaptatPair<String, List<String>> element : posList) {
        resultList.add(element.left);
      }
    }
    return resultList;
  }


  public Set<String> getSynonymSet(String synonymWord) {
    return getSynonymSet(synonymWord, null);
  }


  /**
   * Get word synonyms for the specified part-of-speech May 4, 2018
   *
   * @param synonymWord
   * @param pos
   * @return
   */
  public Set<String> getSynonymSet(String synonymWord, String pos) {
    Set<String> resultSet = new HashSet<>();
    List<RaptatPair<String, List<String>>> synonymList = this.synonymMap.get(synonymWord);

    if (pos != null) {
      pos = pos.toLowerCase();
      if (!Thesaurus.ALLOWED_POS_SET.contains(pos)) {
        System.err.println(
            "When specifying thesaurus parts-of-speech, specify 'noun,' 'verb,' 'adj,' or 'adv' only");
        System.exit(-1);
      }
    }

    if (synonymList != null) {
      for (RaptatPair<String, List<String>> element : synonymList) {
        if (pos == null || element.left.equals("(" + pos + ")")) {
          resultSet.addAll(element.right);
        }
      }
    }
    return resultSet;
  }


  /**
   * Gets word synonyms without consideration of the part of speech
   *
   * @param synonymWord
   * @return
   */
  public List<String> getSynonymsList(String synonymWord) {
    return this.getSynonymsList(synonymWord, null);
  }

  /**
   * Get word synonyms for the specified part-of-speech May 4, 2018
   *
   * @param synonymWord
   * @param pos
   * @return
   */
  public List<String> getSynonymsList(String synonymWord, String pos) {
    List<String> resultList = new ArrayList<>();
    List<RaptatPair<String, List<String>>> synonymList = this.synonymMap.get(synonymWord);

    if (pos != null) {
      pos = pos.toLowerCase();
      if (!Thesaurus.ALLOWED_POS_SET.contains(pos)) {
        System.err.println(
            "When specifying thesaurus parts-of-speech, specify 'noun,' 'verb,' 'adj,' or 'adv' only");
        System.exit(-1);
      }
    }

    if (synonymList != null) {
      for (RaptatPair<String, List<String>> element : synonymList) {
        if (pos == null || element.left.equals("(" + pos + ")")) {
          resultList.addAll(element.right);
        }
      }
    }
    return resultList;
  }

  /**
   * Get word synonyms for the collection of words and specified part-of-speech going up to a depth
   * of synonymDepth from the initial collection. Only return those for which the synonyms are in
   * common with synonyms of countThreshold times.
   *
   * May 4, 2018
   *
   * @param wordsToEvaluate
   * @param partOfSpeech
   * @param synonymDepth
   * @param countThreshold
   * @return
   */
  public Set<String> mineSynonymsAtDepth(Collection<String> wordsToEvaluate, String partOfSpeech,
      int synonymDepth, int countThreshold) {

    if (wordsToEvaluate == null || wordsToEvaluate.isEmpty()) {
      return Collections.emptySet();
    }

    if (partOfSpeech != null) {
      partOfSpeech = partOfSpeech.toLowerCase();
      if (!Thesaurus.ALLOWED_POS_SET.contains(partOfSpeech)) {
        System.err.println(
            "When specifying thesaurus parts-of-speech, specify 'noun,' 'verb,' 'adj,' or 'adv' only");
        System.exit(-1);
      }
    }

    Set<String> currentWordsForEvaluation =
        wordsToEvaluate.stream().map(str -> str.toLowerCase()).collect(Collectors.toSet());

    Set<String> nextWordsToEvaluate = new HashSet<String>(currentWordsForEvaluation);
    HashSet<String> evaluatedWords = new HashSet<String>();
    Set<String> resultSynonyms = new HashSet<>(currentWordsForEvaluation);

    if (LOGGER.isDebugEnabled()) {
      System.out.println("\n\n--------------------------------------------------------");
      System.out
          .println("RUNNING METHOD getSynonymsAtDept(Collection<String>, String, int, int)\n");
      System.out.println("Starting word set: " + wordsToEvaluate);
      System.out.println("Synonym mining depth: " + synonymDepth);
      System.out.println("Count Threshold:" + countThreshold
          + "\n--------------------------------------------------------\n");
    }

    Map<String, Integer> synonymCounts = new HashMap<String, Integer>();
    for (int depth = 0; depth < synonymDepth; depth++) {

      nextWordsToEvaluate.removeAll(evaluatedWords);
      if (nextWordsToEvaluate.isEmpty()) {
        break;
      }

      currentWordsForEvaluation = nextWordsToEvaluate;
      nextWordsToEvaluate = new HashSet<String>();

      LOGGER.debug(currentWordsForEvaluation.size() + " Current words for evaluation: "
          + currentWordsForEvaluation + "\n");
      LOGGER.debug("Current depth: " + depth);

      for (String testWord : currentWordsForEvaluation) {

        LOGGER.debug("Word to assess: " + testWord);

        Set<String> synonymsFound = getSynonymSet(testWord, partOfSpeech);
        nextWordsToEvaluate.addAll(synonymsFound);

        LOGGER.debug(synonymsFound.size() + " Synonyms found: " + synonymsFound);

        // We don't need to track counts for synonyms already in the results
        LOGGER.debug(synonymsFound.size() + " Synonyms found not currently in the results: "
            + synonymsFound);
        synonymsFound.removeAll(resultSynonyms);
        synonymsFound
            .forEach(wordKey -> synonymCounts.compute(wordKey, (k, v) -> v == null ? 1 : v + 1));

        LOGGER.debug("Synonym counts: " + synonymCounts + "\n");
      }

      for (String foundWord : synonymCounts.keySet()) {

        int wordCount = synonymCounts.get(foundWord);

        LOGGER.debug("Word, Count:" + foundWord + ", " + wordCount);

        if (wordCount >= countThreshold) {

          LOGGER.debug("Word count equals or excceds " + countThreshold
              + " count threshold; adding " + foundWord + " to result synonyms");

          resultSynonyms.add(foundWord);

        } else if (LOGGER.isDebugEnabled()) {
          LOGGER
              .debug("Word count does NOT equal or exceed " + countThreshold + "; not adding word");
        }
      }

      if (LOGGER.isDebugEnabled()) {
        System.out.println("\n");
      }
      LOGGER.debug(resultSynonyms.size() + " Result synonyms at depth " + (depth + 1) + ": "
          + resultSynonyms);

      if (depth + 1 < synonymDepth) {

        LOGGER.debug("Pruning synonyms already added to results from synonym counts");

        resultSynonyms.forEach(word -> synonymCounts.remove(word));

        LOGGER.debug("Counts of synonyms after pruning: " + synonymCounts);
        LOGGER.debug(currentWordsForEvaluation.size() + " Words just evaluated for synonyms: "
            + currentWordsForEvaluation);

        evaluatedWords.addAll(currentWordsForEvaluation);
      }
    }

    return resultSynonyms;
  }


  private HashMap<String, List<RaptatPair<String, List<String>>>> read(InputStream inputStream) {
    HashMap<String, List<RaptatPair<String, List<String>>>> synonymMap = new HashMap<>();
    try {
      String delimiter = "\\|";
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String nextLine;

      /* Skip first line which is just heading of character type */
      nextLine = br.readLine();

      while ((nextLine = br.readLine()) != null && nextLine.length() > 0) {
        String[] nextData = nextLine.split(delimiter);
        String phrase = nextData[0];
        int synonymLines = Integer.parseInt(nextData[1]);
        List<RaptatPair<String, List<String>>> synonymLists = new ArrayList<>(synonymLines);
        synonymMap.put(phrase, synonymLists);

        for (int i = 0; i < synonymLines; i++) {
          nextLine = br.readLine();
          nextData = nextLine.split(delimiter);
          String pos = nextData[0];
          List<String> synonyms = new ArrayList<>(nextData.length - 1);
          for (int j = 1; j < nextData.length; j++) {
            synonyms.add(nextData[j]);
          }
          synonymLists.add(new RaptatPair<>(pos, synonyms));
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return synonymMap;
  }


  /**
   * @param thesaurusFilePath
   * @return
   */
  protected HashMap<String, List<RaptatPair<String, List<String>>>> read(String thesaurusFilePath) {
    HashMap<String, List<RaptatPair<String, List<String>>>> synonymMap = new HashMap<>();
    File thesaurusFile = new File(thesaurusFilePath);
    TextReader tr = new TextReader();
    tr.setFile(thesaurusFile);
    tr.setDelimiter("\\|");

    /* Skip first line which is just heading of character type */
    tr.getNextData();

    String[] nextData = null;
    while ((nextData = tr.getNextData()) != null && nextData.length > 0) {
      String phrase = nextData[0];
      int synonymLines = Integer.parseInt(nextData[1]);
      List<RaptatPair<String, List<String>>> synonymLists = new ArrayList<>(synonymLines);
      synonymMap.put(phrase, synonymLists);

      for (int i = 0; i < synonymLines; i++) {
        nextData = tr.getNextData();
        String pos = nextData[0];
        List<String> synonyms = new ArrayList<>(nextData.length - 1);
        for (int j = 1; j < nextData.length; j++) {
          synonyms.add(nextData[j]);
        }
        synonymLists.add(new RaptatPair<>(pos, synonyms));
      }
    }

    return synonymMap;

  }


  Set<String> findCommonSynonyms(List<String> words, String pos) {

    if (words.isEmpty()) {
      return Collections.emptySet();
    }

    Iterator<String> wordIterator = words.iterator();
    String word = wordIterator.next();
    Set<String> synonymSet = new HashSet<String>(getSynonymsList(word, pos));
    if (synonymSet.isEmpty()) {
      System.out.println(word + " has no synomyms\nReturning base set");
    } else {
      while (wordIterator.hasNext()) {
        word = wordIterator.next();
        Set<String> currentSynonymSet = new HashSet<String>(getSynonymsList(word, pos));
        if (currentSynonymSet.isEmpty()) {
          System.out.println(word + " has no synomyms\nReturning empty set");
          break;
        }
        synonymSet.retainAll(currentSynonymSet);
      }
    }
    synonymSet.addAll(words);
    return synonymSet;
  }


  /**
   * @param args
   */
  public static void main(String[] args) {

    RunType runType = RunType.COMMAND_LINE_SYNONYMS;

    switch (runType) {
      case COMMAND_LINE_SYNONYMS: {
        commandLineSynonyms();
      }
      /*
       * Given a list of words, find synonyms they have in common at various depths using various
       * thresholds, which are the number of synonyms that are also related to other words in the
       * list.
       */
      case FIND_COMMON_SYNONYMS: {
        List<String> words = new ArrayList<String>(List.of(

            "note", "express", "endorse", "state", "report", "verbalize", "voice", "evidence",
            "indicate"

        // , "vocalize", "vocalise", "testify", "talk", "support", "suggest", "submit",
        // "speak", "signal", "second", "sanction", "reveal", "remark", "put forward",
        // "present", "posit", "observe", "notice", "mention", "manifest", "indorse",
        // "display", "describe", "demonstrate", "convey", "complain", "communicate",
        // "certify", "attest", "articulate", "approve", "announce", "advise", "suggest",
        // "refer", "communicate", "certify", "show", "say", "inform", "verbalise", "utter",
        // "tell"
        //
        // , "assert", "affirm", "declare", "bring up", "confirm", "corroborate", "expose",
        // "identify", "record", "substantiate", "acknowledge", "bring out", "comment",
        // "depict", "disclose", "divulge", "let on", "maintain", "swear", "think of",
        // "verify", "accede", "agree", "allege", "allow", "assure", "concur", "delineate",
        // "discuss", "hint", "imply", "intimate", "notify", "recount", "relate", "resist",
        // "spill the beans", "touch on", "write down"
        //
        // , "let out", "sustain", "accept", "put across", "authorise", "authorize", "cite",
        // "guarantee", "reassert", "unveil", "defend", "point out", "respond", "believe",
        // "blab", "blab out", "consent", "dissent", "establish", "insist", "advance",
        // "emphasise", "emphasize", "enumerate", "explain", "remember", "remind", "validate"

        ));


        String partOfSpeech = "verb";

        /*
         * Depth range will go from minDepth to maxDepth - 1
         */
        int minDepth = 1;
        int maxDepth = 2;

        /*
         * Threshold range will go from maxThreshold down to minThreshold + 1
         */
        int minThreshold = 0;
        int maxThreshold = 20;

        boolean printSynonyms = true;

        for (int synonymDepth = minDepth; synonymDepth < maxDepth; synonymDepth++) {

          Set<String> oldSynonyms = new HashSet<String>(words);

          for (int synonymCountThreshold =
              maxThreshold; synonymCountThreshold > minThreshold; synonymCountThreshold--) {

            Set<String> synonyms =
                findCommonSynonyms(words, partOfSpeech, synonymDepth, synonymCountThreshold);
            synonyms.removeAll(oldSynonyms);
            oldSynonyms.addAll(synonyms);
            System.out.println("\n\n==================================================");
            System.out.println("SynonymDepth:\t" + synonymDepth + "\tSynonymCountThreshold:\t"
                + synonymCountThreshold + "\tNew Synonyms Found:\t" + synonyms.size());
            System.out.println("==================================================");
            if (printSynonyms) {
              List<String> synonymList = new ArrayList<String>(synonyms);
              Collections.sort(synonymList);
              synonymList.stream().forEach(s -> System.out.println(s));
            }

          }
        }
      }
        break;
    }

    System.exit(0);

  }

  private static void commandLineSynonyms() {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingConsole sc = new SwingConsole();
    sc.setTitle("Raptat Thesaurus");
    sc.setSize(800, 600);
    sc.setLocationRelativeTo(null);


    Thesaurus thesaurus = Thesaurus.INSTANCE;
    sc.println("Welcome to the Raptat Implementation of the WordNet Thesaurus!\n");
    sc.println("Enter test phrase or 'EXIT' (all caps) to quit:");

    Scanner scanner = new Scanner(sc.pipedReader);

    String testString;
    while (!(testString = scanner.nextLine()).equals("EXIT")) {

      sc.println("User phrase: '" + testString.toLowerCase() + "'");
      sc.println();

      String[] testElements = testString.toLowerCase().split("\\s+");
      try {
        int synonymIndex;
        try {
          synonymIndex = Integer.parseInt(testElements[testElements.length - 1]);
        } catch (NumberFormatException e) {
          sc.println(
              "*********Synonym index is not an integer************\n************Getting synonym of first word************\n\n");
          synonymIndex = 0;
        }

        String synonymWord = testElements[synonymIndex];
        List<String> pos = thesaurus.getPosAsList(synonymWord);
        sc.println("==========================================\n" + "  Parts-of-Speech of '"
            + testString + "':\n" + "==========================================");
        String posOutput = pos.isEmpty() ? "No POS Found" : new HashSet<String>(pos).toString();
        String regexString = "[\\]\\[\\(\\)]";
        posOutput = posOutput.replaceAll(regexString, "");
        sc.println(posOutput);
        sc.println("---------------------------------------\n");



        List<String> synonyms = thesaurus.getSynonymsList(synonymWord);
        Set<String> phraseSet = new HashSet<>();
        if (synonyms != null && synonyms.size() > 0) {
          for (String synonym : synonyms) {
            testElements[synonymIndex] = synonym;
            StringBuilder sb = new StringBuilder(testElements[0]);
            for (int i = 1; i < testElements.length - 1; i++) {
              sb.append(" ").append(testElements[i]);
            }
            phraseSet.add(sb.toString());
          }

          List<String> phrasesForOutput = new ArrayList<>(phraseSet);
          Collections.sort(phrasesForOutput);
          for (String string : phrasesForOutput) {
            sc.println(string);
          }
        } else {
          sc.println("No synomyms found for " + "\"" + synonymWord + "\"");
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        sc.println("Synonym index outside of number of elements in phrase");
      }

      sc.println("\n\nEnter test phrase or EXIT to stop:");
    }
    scanner.close();
  }

  private static Set<String> findCommonSynonyms(List<String> words, String pos, int synonymDepth,
      int synonymCountThreshold) {
    Thesaurus thesaurus = Thesaurus.INSTANCE;
    return thesaurus.mineSynonymsAtDepth(words, pos, synonymDepth, synonymCountThreshold);
  }

}


