package src.test.gov.va.vha09.grecc.raptat.gg.nonnaivebayes;

import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.nonnaivebayes.ConditionalTree;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.nonnaivebayes.NonNBConceptLookupTable;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

/************************************************************
 * ConditionalTreeTest -
 *
 * @author Glenn Gobbel, Jan 14, 2011
 *
 ***********************************************************/
public class ConditionalTreeTest {
  @Test
  public void incrementTest() {
    ConditionalTree myTestTree;
    String[] concepts = {"35", "47", "35", "35"};

    myTestTree = new ConditionalTree(0);
    LinkedList<String[]> phraseList = new LinkedList<>();
    phraseList.add(new String[] {"my", "dog", "has", "fleas", "out", "the", "wazoo"});
    myTestTree.increment(phraseList.getLast(), concepts[0]);
    phraseList.add(new String[] {"my", "friend", "is", "here"});
    myTestTree.increment(phraseList.getLast(), concepts[1]);
    phraseList.add(new String[] {"alpha", "bet", "soup"});
    myTestTree.increment(phraseList.getLast(), concepts[2]);
    phraseList.add(new String[] {"my", "dog", "has", "fleas", "in"});
    myTestTree.increment(phraseList.getLast(), concepts[2]);

    int i, j, k;
    NonNBConceptLookupTable myTable;
    String[] subPhrase, curPhrase;
    k = 0;
    while (phraseList.size() > 0) {
      curPhrase = phraseList.removeFirst();
      for (i = 1; i <= curPhrase.length; ++i) {
        subPhrase = GeneralHelper.getSubArray(curPhrase, 0, i);
        for (j = 0; j < subPhrase.length; j++) {
          System.out.print(subPhrase[j] + " ");
        }
        System.out.println();
        myTable = myTestTree.getConceptTable(subPhrase);
        System.out.print(myTable);
        Assert.assertEquals(myTable.containsKey(concepts[k]), true);
      }
      ++k;
    }
  }
}
