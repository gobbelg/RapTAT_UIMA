package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;

public class LabeledHashTreeTest {
  public static LabeledHashTree generateTestTree1() {
    LabeledHashTree myTestTree = new LabeledHashTree();

    String[] testPhrase2 = {"alpha"};
    myTestTree.addUnlabeledPhrase(testPhrase2);

    String[] testPhrase3 = {"hello", "again", "my", "friend"};
    myTestTree.addLabeledPhrase(testPhrase3);

    String[] testPhrase = {"hello", "it's", "me"};
    myTestTree.addLabeledPhrase(testPhrase);

    String[] newPhrase = {"my", "friend", "is", "here", "to", "stay"};
    myTestTree.addUnlabeledPhrase(newPhrase);
    myTestTree.addUnlabeledPhrase(newPhrase);

    String[] myPhrase = {"my", "dog"};
    myTestTree.addLabeledPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);
    myTestTree.addUnlabeledPhrase(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "fast"};
    myTestTree.addLabeledPhrase(newPhrase2);

    String[] newPhrase3 = {"my", "dog", "runs", "fast", "without", "stopping"};
    myTestTree.addLabeledPhrase(newPhrase3);

    String[] newPhrase4 = {"my", "dog", "runs", "whenever", "he", "can"};
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);

    myTestTree.updateProbabilities();

    // myTestTree.print();
    // System.out.println("Testing iterator");
    // myTestTree.printLabeledSequences();

    return myTestTree;
  }


  public static LabeledHashTree generateTestTree2() {
    LabeledHashTree myTestTree = new LabeledHashTree();

    String[] testPhrase2 = {"beta"};
    myTestTree.addUnlabeledPhrase(testPhrase2);

    String[] myPhrase = {"my", "car"};
    myTestTree.addLabeledPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "very", "far"};
    myTestTree.addLabeledPhrase(newPhrase2);
    myTestTree.addLabeledPhrase(newPhrase2);

    String[] newPhrase4 = {"the", "man", "is", "going", "home"};
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);

    myTestTree.updateProbabilities();

    // myTestTree.print();
    // System.out.println("Testing iterator");
    // myTestTree.printLabeledSequences();

    return myTestTree;
  }


  // Very slight difference from testTree2
  // (one letter change of one token)
  public static LabeledHashTree generateTestTree4() {
    LabeledHashTree myTestTree = new LabeledHashTree();

    String[] testPhrase2 = {"beta"};
    myTestTree.addUnlabeledPhrase(testPhrase2);

    String[] myPhrase = {"my", "car"};
    myTestTree.addLabeledPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "very", "far"};
    myTestTree.addLabeledPhrase(newPhrase2);
    myTestTree.addLabeledPhrase(newPhrase2);

    String[] newPhrase4 = {"the", "man", "is", "going", "homes"};
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);

    myTestTree.updateProbabilities();

    // myTestTree.print();
    // System.out.println("Testing iterator");
    // myTestTree.printLabeledSequences();

    return myTestTree;
  }


  @Test
  public void addPhraseLabelFromIndexTest() {
    LabeledHashTree theTree = new LabeledHashTree();
    String[] seq1 = {"A", "B", "C", "D", "E"};
    String[] seq2 = {"A", "B", "Z", "Y", "X", "W"};
    String[] seq3 = {"G", "H", "I", "J"};
    String[] seq4 = {"H", "I", "K"};
    String[] seq5 = {};

    theTree.addPhraseLabelFromIndex(this.convertToTokenList(seq1), 0);
    theTree.addPhraseLabelFromIndex(this.convertToTokenList(seq2), 2);
    theTree.addPhraseLabelFromIndex(this.convertToTokenList(seq3), 3);
    theTree.addPhraseLabelFromIndex(this.convertToTokenList(seq4), 2);
    theTree.addPhraseLabelFromIndex(this.convertToTokenList(seq5), 1);

    Iterator<RaptatPair<List<String>, LabeledHashTree>> treeIterator = theTree.iterator();
    while (treeIterator.hasNext()) {
      System.out.println(treeIterator.next().left);
    }
  }


  /*************************************************************
   * @param seq1
   *
   * @author Glenn Gobbel - Jun 24, 2012
   *************************************************************/
  private List<RaptatToken> convertToTokenList(String[] seq1) {
    List<RaptatToken> tokenList = new ArrayList<>(seq1.length);
    src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken curToken;
    for (String curString : seq1) {
      curToken = new RaptatToken(curString, curString, curString, curString, curString);
      tokenList.add(curToken);
    }
    return tokenList;
  }


  // @Test
  public void generateRandomTreeTest() {
    LabeledHashTree testTree = LabeledHashTree.generateRandomTree(8, 40);
    testTree.printLabeledSequences();
    // testTree.print();
  }


  // Very slight difference from testTree2
  // (one change in the number of unlabeled phrases)
  public LabeledHashTree generateTestTree3() {
    LabeledHashTree myTestTree = new LabeledHashTree();

    String[] testPhrase2 = {"beta"};
    myTestTree.addUnlabeledPhrase(testPhrase2);

    String[] myPhrase = {"my", "car"};
    myTestTree.addLabeledPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "very", "far"};
    myTestTree.addLabeledPhrase(newPhrase2);
    myTestTree.addLabeledPhrase(newPhrase2);

    String[] newPhrase4 = {"the", "man", "is", "going", "home"};
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);

    myTestTree.updateProbabilities();

    // myTestTree.print();
    // System.out.println("Testing iterator");
    // myTestTree.printLabeledSequences();

    return myTestTree;
  }


  // Generates tree identical to testTree4
  public LabeledHashTree generateTestTree5() {
    LabeledHashTree myTestTree = new LabeledHashTree();

    String[] testPhrase2 = {"beta"};
    myTestTree.addUnlabeledPhrase(testPhrase2);

    String[] myPhrase = {"my", "car"};
    myTestTree.addLabeledPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);
    myTestTree.addDeletedPhrase(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "very", "far"};
    myTestTree.addLabeledPhrase(newPhrase2);
    myTestTree.addLabeledPhrase(newPhrase2);

    String[] newPhrase4 = {"the", "man", "is", "going", "homes"};
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addLabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);
    myTestTree.addUnlabeledPhrase(newPhrase4);

    myTestTree.updateProbabilities();

    // myTestTree.print();
    // System.out.println("Testing iterator");
    // myTestTree.printLabeledSequences();

    return myTestTree;
  }


  // @Test
  public void testEquals() {
    boolean testResult;

    testResult = generateTestTree1().equals(generateTestTree1());
    System.out.println("Compare tree1 to tree1: " + testResult);
    assertTrue(testResult);

    testResult = generateTestTree2().equals(generateTestTree2());
    System.out.println("Compare tree2 to tree2: " + testResult);
    assertTrue(testResult);

    testResult = generateTestTree4().equals(this.generateTestTree5());
    System.out.println("Compare tree4 to tree5: " + testResult);
    assertTrue(testResult);

    testResult = generateTestTree1().equals(generateTestTree2());
    System.out.println("Compare tree1 to tree2: " + testResult);
    assertFalse(testResult);

    testResult = generateTestTree2().equals(generateTestTree1());
    System.out.println("Compare tree2 to tree1: " + testResult);
    assertFalse(testResult);

    testResult = generateTestTree2().equals(this.generateTestTree3());
    System.out.println("Compare tree2 to tree3: " + testResult);
    assertFalse(testResult);

    testResult = generateTestTree4().equals(generateTestTree2());
    System.out.println("Compare tree4 to tree2: " + testResult);
    assertFalse(testResult);
  }

}
