/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.SequenceHashTree;

/********************************************************
 *
 *
 * @author Glenn Gobbel - Jun 24, 2012
 *******************************************************/
public class SequenceHashTreeTester {
  /*************************************************************
   * @param seq1
   *
   * @author Glenn Gobbel - Jun 24, 2012
   *************************************************************/
  private List<RaptatToken> convertToTokenList(String[] seq1) {
    List<RaptatToken> tokenList = new ArrayList<>(seq1.length);
    RaptatToken curToken;
    for (String curString : seq1) {
      curToken = new RaptatToken(curString, curString, curString, curString, curString);
      tokenList.add(curToken);
    }
    return tokenList;
  }


  @Test
  public void SequenceHashIteratorTester() {
    SequenceHashTree theTree = new SequenceHashTree();
    // String [] seq1 = {"A"};
    String[] seq1 = {"A", "B", "C", "D", "E"};
    String[] seq2 = {"A", "B", "Z", "Y", "X", "W"};
    String[] seq3 = {"G", "H", "I", "J"};
    String[] seq4 = {"H", "I", "K"};
    String[] seq5 = {};

    theTree.addSequence(this.convertToTokenList(seq1));
    theTree.addSequence(this.convertToTokenList(seq2));
    theTree.addSequence(this.convertToTokenList(seq3));
    theTree.addSequence(this.convertToTokenList(seq4));
    theTree.addSequence(this.convertToTokenList(seq5));

    Iterator<RaptatPair<List<String>, LabeledHashTree>> treeIterator = theTree.iterator();
    while (treeIterator.hasNext()) {
      System.out.println(treeIterator.next().left.toString());
    }
  }
}
