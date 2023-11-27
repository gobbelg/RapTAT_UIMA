package src.test.gov.va.vha09.grecc.raptat.gg.algorithms;

import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTreeOutputStream;

public class AlgorithmSolutionTest extends ProbabilisticTSFinderSolution {

  /**
   *
   */
  private static final long serialVersionUID = 5777162143984309791L;


  public void appendUnlabeledHashTree(String uhtPath) {
    String[] testPhrase1 = {"to", "the", "moon", "and", "beyond"};
    String[] testPhrase2 = {"to", "the", "sun", "and", "back"};
    String[] testPhrase3 = {"a", "brand", "new", "car"};

    UnlabeledHashTree myAppendedTree = new UnlabeledHashTree();
    myAppendedTree.addUnlabeledSequence(testPhrase1);
    myAppendedTree.addUnlabeledSequence(testPhrase1);
    myAppendedTree.addUnlabeledSequence(testPhrase1);
    myAppendedTree.addUnlabeledSequence(testPhrase2);
    myAppendedTree.addUnlabeledSequence(testPhrase2);
    myAppendedTree.addUnlabeledSequence(testPhrase3);

    System.out.println("Printing appended UnlabeledHashTree instance:");
    myAppendedTree.printUnlabeledSequences();

    UnlabeledHashTreeOutputStream outStream = new UnlabeledHashTreeOutputStream(uhtPath);
    outStream.append(myAppendedTree);

  }


  public LabeledHashTree getLabeledHashTree() {
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

    System.out.println("Printing initial LabeledHashTree");
    myTestTree.print();

    return myTestTree;
  }


  public String getUnlabledHashTreePath() {
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

    UnlabeledHashTree myTestTree2 = new UnlabeledHashTree();
    String[] newPhrase5 = {"Jack", "and", "Jill"};
    myTestTree2.addUnlabeledSequence(newPhrase5);
    String[] newPhrase6 = {"Jack", "and", "Tom", " are", "gone"};
    myTestTree2.addUnlabeledSequence(newPhrase6);

    UnlabeledHashTreeOutputStream myOutStream;
    myOutStream = new UnlabeledHashTreeOutputStream();
    myOutStream.write(myTestTree);
    myOutStream.append(myTestTree2);

    System.out.println("Printining initial UnlabeledHashTrees");
    System.out.println("myTestTree");
    myTestTree.printUnlabeledSequences();
    System.out.println("myTestTree2");
    myTestTree2.printUnlabeledSequences();

    return myOutStream.getFile().getAbsolutePath();
  }

}
