/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import java.util.List;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IntegerPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IntegerPairComparer;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;

/********************************************************
 *
 *
 * @author Glenn Gobbel - Apr 27, 2012
 *******************************************************/
public class IntegerPairComparatorTest {
  @Test
  public void getLesserOrEqualValueTest() {
    List<IntegerPair> testList = new Vector<>();

    testList.add(new IntegerPair(2, 0));
    testList.add(new IntegerPair(128, 0));
    testList.add(new IntegerPair(256, 0));
    testList.add(new IntegerPair(32, 0));
    testList.add(new IntegerPair(16, 0));
    testList.add(new IntegerPair(64, 0));
    testList.add(new IntegerPair(8, 0));
    testList.add(new IntegerPair(4, 0));

    IntegerPairComparer ipcTree = new IntegerPairComparer(testList);

    Assert.assertEquals(new RaptatPair<>("left", 2), ipcTree.getSideAndDepth(testList.get(0).left));
    Assert.assertEquals(new RaptatPair<>("left", 1), ipcTree.getSideAndDepth(testList.get(1).left));
    Assert.assertEquals(new RaptatPair<>("right", 2),
        ipcTree.getSideAndDepth(testList.get(2).left));
    Assert.assertEquals(new RaptatPair<>("root", 0), ipcTree.getSideAndDepth(testList.get(3).left));
    Assert.assertEquals(new RaptatPair<>("left", 2), ipcTree.getSideAndDepth(testList.get(4).left));
    Assert.assertEquals(new RaptatPair<>("right", 1),
        ipcTree.getSideAndDepth(testList.get(5).left));
    Assert.assertEquals(new RaptatPair<>("right", 2),
        ipcTree.getSideAndDepth(testList.get(6).left));
    Assert.assertEquals(new RaptatPair<>("right", 3),
        ipcTree.getSideAndDepth(testList.get(7).left));

    Assert.assertNull(ipcTree.getSideAndDepth(1));
    Assert.assertNull(ipcTree.getClosestLesserOrEqualObject(1));
    Assert.assertEquals(testList.get(0), ipcTree.getClosestLesserOrEqualObject(3));
    Assert.assertEquals(testList.get(1), ipcTree.getClosestLesserOrEqualObject(5));
    Assert.assertEquals(testList.get(2), ipcTree.getClosestLesserOrEqualObject(9));
    Assert.assertEquals(testList.get(3), ipcTree.getClosestLesserOrEqualObject(17));
    Assert.assertEquals(testList.get(4), ipcTree.getClosestLesserOrEqualObject(33));
    Assert.assertEquals(testList.get(5), ipcTree.getClosestLesserOrEqualObject(65));
    Assert.assertEquals(testList.get(6), ipcTree.getClosestLesserOrEqualObject(129));
    Assert.assertEquals(testList.get(7), ipcTree.getClosestLesserOrEqualObject(256));

    testList.add(new IntegerPair(512, 0));
    ipcTree = new IntegerPairComparer(testList);
    Assert.assertEquals(new RaptatPair<>("left", 2), ipcTree.getSideAndDepth(testList.get(0).left));
    Assert.assertEquals(new RaptatPair<>("left", 1), ipcTree.getSideAndDepth(testList.get(1).left));
    Assert.assertEquals(new RaptatPair<>("right", 2),
        ipcTree.getSideAndDepth(testList.get(2).left));
    Assert.assertEquals(new RaptatPair<>("right", 3),
        ipcTree.getSideAndDepth(testList.get(3).left));
    Assert.assertEquals(new RaptatPair<>("root", 0), ipcTree.getSideAndDepth(testList.get(4).left));
    Assert.assertEquals(new RaptatPair<>("left", 2), ipcTree.getSideAndDepth(testList.get(5).left));
    Assert.assertEquals(new RaptatPair<>("right", 1),
        ipcTree.getSideAndDepth(testList.get(6).left));
    Assert.assertEquals(new RaptatPair<>("right", 2),
        ipcTree.getSideAndDepth(testList.get(7).left));
    Assert.assertEquals(new RaptatPair<>("right", 3),
        ipcTree.getSideAndDepth(testList.get(8).left));

    Assert.assertNull(ipcTree.getSideAndDepth(1));
    Assert.assertNull(ipcTree.getClosestLesserOrEqualObject(1));
    Assert.assertEquals(testList.get(0), ipcTree.getClosestLesserOrEqualObject(3));
    Assert.assertEquals(testList.get(1), ipcTree.getClosestLesserOrEqualObject(5));
    Assert.assertEquals(testList.get(2), ipcTree.getClosestLesserOrEqualObject(9));
    Assert.assertEquals(testList.get(3), ipcTree.getClosestLesserOrEqualObject(17));
    Assert.assertEquals(testList.get(4), ipcTree.getClosestLesserOrEqualObject(33));
    Assert.assertEquals(testList.get(5), ipcTree.getClosestLesserOrEqualObject(65));
    Assert.assertEquals(testList.get(6), ipcTree.getClosestLesserOrEqualObject(129));
    Assert.assertEquals(testList.get(7), ipcTree.getClosestLesserOrEqualObject(256));

  }
}
