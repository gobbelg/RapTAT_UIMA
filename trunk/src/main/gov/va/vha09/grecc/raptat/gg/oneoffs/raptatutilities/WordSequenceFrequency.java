package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.raptatutilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.ContextHandling;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.HashTreeFields;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTreeFields;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

public class WordSequenceFrequency {
  public RaptatToken tokenForInsert = new RaptatToken("$**NOTOKEN@@$");


  public WordSequenceFrequency() {

  }

  private UnlabeledHashTree buildTree(Hashtable<String, RaptatDocument> theDocuments,
      int maxPhraseLength) {
    UnlabeledHashTree theFrequencyTree = new UnlabeledHashTree();
    Enumeration<String> documentNames = theDocuments.keys();
    while (documentNames.hasMoreElements()) {
      String curDocumentName = documentNames.nextElement();
      System.out.println("Processing document:" + curDocumentName);

      RaptatDocument curDocument = theDocuments.get(curDocumentName);
      List<AnnotatedPhrase> docSentences = curDocument.getActiveSentences();
      for (AnnotatedPhrase curSentence : docSentences) {
        List<RaptatToken> sentenceTokens = curSentence.getProcessedTokens();
        int sentenceSize = sentenceTokens.size();
        for (int curTokenIndex = 0; curTokenIndex < sentenceSize; curTokenIndex++) {
          theFrequencyTree.addUnlabeledSequence(sentenceTokens, curTokenIndex, -1, maxPhraseLength);
        }
      }
    }
    return theFrequencyTree;
  }

  public static void main(String[] args) {

    UserPreferences.INSTANCE.initializeLVGLocation();

    // Set to any integer less than 1 to print all phrase lengths
    final int NGRAM_LENGTH = 2;
    final String directoryPath =
        "H:\\AllFolders\\MHA_NLP\\UnigramBigramAnalysis\\InputFiles\\TP\\Sentences";
    final String outputDirectorName = "Output";
    final String assignedLabel = "TPSentences";
    Matcher nonAlphanumericMatcher = Pattern.compile("^\\W").matcher("");

    SwingUtilities.invokeLater(new Runnable() {
      public Hashtable<String, RaptatDocument> getDocuments() {

        File fileDirectory = new File(directoryPath);
        if (!fileDirectory.exists()) {
          fileDirectory =
              GeneralHelper.getDirectory("Select directory containing files for analysis");
        }
        File[] files = fileDirectory.listFiles(GeneralHelper.getFilenameFilter(".txt"));

        TextAnalyzer theAnalyzer = new TextAnalyzer();
        theAnalyzer.setTokenProcessingParameters(false, false, true, 7, ContextHandling.NONE);
        Hashtable<String, RaptatDocument> theDocuments = new Hashtable<>();

        int index = 1;
        for (File nextFile : files) {
          System.out.println("Processing " + index++ + " of " + files.length + " files");
          RaptatDocument inputDocument = theAnalyzer.processDocument(nextFile.getAbsolutePath());
          inputDocument.activateBaseSentences();
          theDocuments.put(nextFile.getName(), inputDocument);
        }

        return theDocuments;
      }


      @Override
      public void run() {
        WordSequenceFrequency theTester = new WordSequenceFrequency();
        Hashtable<String, RaptatDocument> testDocuments = getDocuments();
        UnlabeledHashTree phrases = theTester.buildTree(testDocuments, NGRAM_LENGTH);
        String outputPath = directoryPath + File.separator + outputDirectorName + File.separator
            + assignedLabel + "_Output_PhraseLength_" + NGRAM_LENGTH + "_"
            + GeneralHelper.getTimeStamp() + ".txt";
        try {
          PrintWriter pw = new PrintWriter(outputPath);
          Map<Long, List<String>> phraseOccurrenceMap =
              getPhraseOccurrenceMap(phrases, NGRAM_LENGTH);

          List<Long> occurrenceList = new ArrayList<>(phraseOccurrenceMap.keySet());
          Collections.sort(occurrenceList, Collections.reverseOrder());

          for (Long listKey : occurrenceList) {
            List<String> phraseList = phraseOccurrenceMap.get(listKey);
            Collections.sort(phraseList);
            for (String phrase : phraseList) {
              // Add apostrophe (') so excel will read unusual, non-alphanumeric characters
              // properly
              String prepend = nonAlphanumericMatcher.reset(phrase).find() ? "'" : "";
              String outputString = prepend + phrase + "\t" + listKey;
              System.out.println(outputString);
              pw.println(outputString);
              pw.flush();
            }
          }
          pw.close();
          System.exit(0);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
          System.exit(-1);
        }

      }


      private Map<Long, List<String>> getPhraseOccurrenceMap(UnlabeledHashTree phrases,
          int phraseLength) {

        List<String> phraseList = null;
        Map<Long, List<String>> phraseOccurrenceMap = new HashMap<>();
        for (HashTreeFields phrase : phrases) {
          UnlabeledHashTreeFields unlabeledPhrase = (UnlabeledHashTreeFields) phrase;
          if (phraseLength < 1 || unlabeledPhrase.stringSequence.size() == phraseLength) {
            Iterator<String> sequenceIterator = unlabeledPhrase.stringSequence.iterator();
            StringBuilder sb = new StringBuilder(sequenceIterator.next());
            while (sequenceIterator.hasNext()) {
              sb.append(" ").append(sequenceIterator.next());
            }
            Long occurrences = unlabeledPhrase.unlabeled / 967680;
            if ((phraseList = phraseOccurrenceMap.get(occurrences)) == null) {
              phraseList = new ArrayList<>();
              phraseOccurrenceMap.put(occurrences, phraseList);
            }
            phraseList.add(sb.toString());
          }
        }
        return phraseOccurrenceMap;
      }

    });

  }
}
