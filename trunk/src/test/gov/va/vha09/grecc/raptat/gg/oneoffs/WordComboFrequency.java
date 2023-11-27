package src.test.gov.va.vha09.grecc.raptat.gg.oneoffs;

import static src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.MAX_TOKENS_DEFAULT;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.AlphaNumericTokenComparator;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.csv.CSVReader;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;

public class WordComboFrequency {
  public static void main(String[] args) {
    WordComboFrequency theTester = new WordComboFrequency();
    Hashtable<String, RaptatDocument> testDocuments = theTester.getDocuments();
    theTester.buildTree(testDocuments);
  }

  public RaptatToken tokenForInsert = new RaptatToken("$**NOTOKEN@@$");


  private void addPermutedSequenceAtIndex(UnlabeledHashTree theFrequencyTree,
      LinkedList<RaptatToken> sortedList, int startIndex) {
    while (startIndex < sortedList.size()) {
      RaptatToken replacedToken = sortedList.remove(startIndex);
      sortedList.add(this.tokenForInsert);
      Iterator<RaptatToken> tokenListIterator = sortedList.iterator();
      theFrequencyTree.addUnlabeledSequence(sortedList, tokenListIterator);

      this.addPermutedSequenceAtIndex(theFrequencyTree, sortedList, startIndex + 1);
      sortedList.removeLast();
      sortedList.add(startIndex, replacedToken);
      startIndex++;
    }
  }


  // To account for when one or more tokens do not match, we create all
  // possible sequences when any
  // one token is missing, when any two tokens are missing, when any 3 tokens
  // are missing, up to
  // k-1 where k is the number of tokens in the list. To do this, we simply
  // add a key at the end
  // to signal unmatched when 1 or more tokens are missing. So, if t tokens
  // are missing, the last
  // t tokens are the unmatched token.
  private void addSequencePermutations(UnlabeledHashTree theFrequencyTree,
      ArrayList<RaptatToken> sortedArrayList, LinkedList<RaptatToken> sortedLinkedList) {
    Iterator<RaptatToken> tokenListIterator = sortedLinkedList.iterator();
    theFrequencyTree.addUnlabeledSequence(sortedLinkedList, tokenListIterator);
    for (int i = sortedLinkedList.size(); i > 0; i--) {

    }
    this.addPermutedSequenceAtIndex(theFrequencyTree, sortedLinkedList, 0);
  }


  private void buildTree(Hashtable<String, RaptatDocument> testDocuments) {
    UnlabeledHashTree theFrequencyTree = new UnlabeledHashTree();
    Enumeration<String> documentNames = testDocuments.keys();
    AlphaNumericTokenComparator tokenComparator = new AlphaNumericTokenComparator();
    while (documentNames.hasMoreElements()) {
      String curDocumentName = documentNames.nextElement();
      System.out.println("Processing document:" + curDocumentName);

      RaptatDocument curDocument = testDocuments.get(curDocumentName);
      List<AnnotatedPhrase> docSentences = curDocument.getActiveSentences();
      for (AnnotatedPhrase curSentence : docSentences) {
        List<RaptatToken> sentenceTokens = curSentence.getProcessedTokens();
        int sentenceSize = sentenceTokens.size();
        for (int curTokenIndex = 0; curTokenIndex < sentenceSize; curTokenIndex++) {
          // We handle the first token differently as we don't deal
          // with permutations or missing values when there is only
          // one token
          int endToken = curTokenIndex + Constants.MAX_TOKENS_DEFAULT;
          if (endToken > sentenceTokens.size()) {
            endToken = sentenceTokens.size();
          }
          ArrayList<RaptatToken> sortedArrayList =
              new ArrayList<>(sentenceTokens.subList(curTokenIndex, endToken));
          Collections.sort(sortedArrayList, tokenComparator);
          LinkedList<RaptatToken> sortedLinkedList = new LinkedList<>(sortedArrayList);
          for (int offset = endToken; offset > 0; offset--) {
            this.addSequencePermutations(theFrequencyTree, sortedArrayList, sortedLinkedList);
            System.out.println("================================================");
            theFrequencyTree.print();
          }

          // // We handle the first token differently as we don't deal
          // // with permutations or missing values when there is only
          // one token
          // LinkedList<Token> sortingList = new LinkedList<Token>();
          // sortingList.add(sentenceTokens.get(0));
          // Iterator<Token> tokenListIterator =
          // sortingList.iterator();
          // theFrequencyTree.addUnlabeledSequence(sortingList,
          // tokenListIterator);
          // for(int offset = 1; offset < MAX_ELEMENTS_DEFAULT &&
          // curTokenIndex + offset <
          // sentenceSize ; offset++)
          // {
          // Token nextToken = sentenceTokens.get(curTokenIndex +
          // offset);
          // insertSorted(sortingList, nextToken);
          // tokenListIterator = sortingList.iterator();
          // theFrequencyTree.addUnlabeledSequence(sortingList,
          // tokenListIterator);
          // addPermutedSequence(theFrequencyTree, sortingList);
          // System.out.println("================================================");
          // theFrequencyTree.print();
          // }

        }
      }
    }
    theFrequencyTree.print();

  }


  public Hashtable<String, RaptatDocument> getDocuments() {
    TextAnalyzer theAnalyzer = new TextAnalyzer();
    String[] nextFile;
    CSVReader fileReader = new CSVReader("Select text file with concepts to be analyzed");
    String baseFileDirectory = fileReader.getInFileDirectory();
    Hashtable<String, RaptatDocument> theDocuments = new Hashtable<>();
    while ((nextFile = fileReader.getNextData()) != null) {
      String fileName = baseFileDirectory + File.separator + nextFile[0];
      RaptatDocument inputDocument = theAnalyzer.processDocument(fileName);
      theDocuments.put(fileName, inputDocument);
    }

    return theDocuments;
  }


  private int insertSorted(LinkedList<RaptatToken> sortingList, RaptatToken nextToken) {
    int i = 0;
    while (i < sortingList.size() && nextToken.getTokenStringAugmented()
        .compareTo(sortingList.get(i).getTokenStringAugmented()) > 0) {
      i++;
    }
    sortingList.add(i, nextToken);

    return i;
  }
}
