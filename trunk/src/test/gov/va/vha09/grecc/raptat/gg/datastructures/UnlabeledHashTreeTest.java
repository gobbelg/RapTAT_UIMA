package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;

public class UnlabeledHashTreeTest {
  public static UnlabeledHashTree generateTestTree1() {
    UnlabeledHashTree myTestTree = new UnlabeledHashTree();

    String[] testPhrase2 = {"alpha"};
    myTestTree.addUnlabeledSequence(testPhrase2);

    String[] testPhrase3 = {"hello", "again", "my", "friend"};
    myTestTree.addUnlabeledSequence(testPhrase3);

    String[] testPhrase = {"hello", "it's", "me"};
    myTestTree.addUnlabeledSequence(testPhrase);

    String[] newPhrase = {"my", "friend", "is", "here", "to", "stay"};
    myTestTree.addUnlabeledSequence(newPhrase);
    myTestTree.addUnlabeledSequence(newPhrase);

    String[] myPhrase = {"my", "dog"};
    myTestTree.addUnlabeledSequence(myPhrase);
    myTestTree.addUnlabeledSequence(myPhrase);
    myTestTree.addUnlabeledSequence(myPhrase);

    String[] newPhrase2 = {"my", "dog", "runs", "fast"};
    myTestTree.addUnlabeledSequence(newPhrase2);

    String[] newPhrase3 = {"my", "dog", "runs", "fast", "without", "stopping"};
    myTestTree.addUnlabeledSequence(newPhrase3);

    String[] newPhrase4 = {"my", "dog", "runs", "whenever", "he", "can"};
    myTestTree.addUnlabeledSequence(newPhrase4);
    myTestTree.addUnlabeledSequence(newPhrase4);
    myTestTree.addUnlabeledSequence(newPhrase4);
    myTestTree.addUnlabeledSequence(newPhrase4);
    myTestTree.addUnlabeledSequence(newPhrase4);

    return myTestTree;
  }


  public static UnlabeledHashTree generateTestTree2() {
    UnlabeledHashTree myTestTree = new UnlabeledHashTree();
    String[] newPhrase5 = {"Jack", "and", "Jill"};
    myTestTree.addUnlabeledSequence(newPhrase5);
    String[] newPhrase6 = {"Jack", "and", "Tom", " are", "gone"};
    myTestTree.addUnlabeledSequence(newPhrase6);

    return myTestTree;
  }


  public static UnlabeledHashTree generateTestTree3() {
    UnlabeledHashTree myTestTree = new UnlabeledHashTree();

    String[] testPhrase1 = {"to", "the", "moon", "and", "beyond"};
    String[] testPhrase2 = {"to", "the", "sun", "and", "back"};
    String[] testPhrase3 = {"a", "brand", "new", "car"};

    myTestTree.addUnlabeledSequence(testPhrase1);
    myTestTree.addUnlabeledSequence(testPhrase1);
    myTestTree.addUnlabeledSequence(testPhrase1);
    myTestTree.addUnlabeledSequence(testPhrase2);
    myTestTree.addUnlabeledSequence(testPhrase2);
    myTestTree.addUnlabeledSequence(testPhrase3);

    return myTestTree;
  }


  @Test
  public void equalsTest() {
    boolean testResult;

    testResult = generateTestTree1().equals(generateTestTree1());
    System.out.println("Compare tree1 to tree1: " + testResult);
    assertTrue(testResult);

    testResult = generateTestTree1().equals(generateTestTree2());
    System.out.println("Compare tree1 to tree2: " + testResult);
    assertFalse(testResult);

    testResult = generateTestTree2().equals(generateTestTree1());
    System.out.println("Compare tree2 to tree1: " + testResult);
    assertFalse(testResult);

    testResult = generateTestTree2().equals(generateTestTree2());
    System.out.println("Compare tree2 to tree2: " + testResult);
    assertTrue(testResult);

    testResult = generateTestTree2().equals(generateTestTree3());
    System.out.println("Compare tree2 to tree3: " + testResult);
    assertFalse(testResult);

  }

}
