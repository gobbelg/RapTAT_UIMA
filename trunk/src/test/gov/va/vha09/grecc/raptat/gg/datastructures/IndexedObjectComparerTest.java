/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import java.util.List;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.IndexedTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;

/********************************************************
 *
 *
 * @author Glenn Gobbel - Apr 27, 2012
 *******************************************************/
public class IndexedObjectComparerTest {
  @Test
  public void getLesserOrEqualValueTest() {
    List<IndexedObject<Integer>> testList = new Vector<>();

    testList.add(new IndexedObject<>(2, 0));
    testList.add(new IndexedObject<>(128, 0));
    testList.add(new IndexedObject<>(256, 0));
    testList.add(new IndexedObject<>(32, 0));
    testList.add(new IndexedObject<>(16, 0));
    testList.add(new IndexedObject<>(64, 0));
    testList.add(new IndexedObject<>(8, 0));
    testList.add(new IndexedObject<>(4, 0));

    IndexedTree<IndexedObject<Integer>> sbtTree = new IndexedTree<>(testList);

    Assert.assertEquals(new RaptatPair<>("left", 2), sbtTree.getSideAndDepth(testList.get(0)));
    Assert.assertEquals(new RaptatPair<>("left", 1), sbtTree.getSideAndDepth(testList.get(1)));
    Assert.assertEquals(new RaptatPair<>("right", 2), sbtTree.getSideAndDepth(testList.get(2)));
    Assert.assertEquals(new RaptatPair<>("root", 0), sbtTree.getSideAndDepth(testList.get(3)));
    Assert.assertEquals(new RaptatPair<>("left", 2), sbtTree.getSideAndDepth(testList.get(4)));
    Assert.assertEquals(new RaptatPair<>("right", 1), sbtTree.getSideAndDepth(testList.get(5)));
    Assert.assertEquals(new RaptatPair<>("right", 2), sbtTree.getSideAndDepth(testList.get(6)));
    Assert.assertEquals(new RaptatPair<>("right", 3), sbtTree.getSideAndDepth(testList.get(7)));

    IndexedObject<Integer> testPair = new IndexedObject<>(1, 0);
    Assert.assertNull(sbtTree.getSideAndDepth(testPair));
    Assert.assertNull(sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(3, 0);
    Assert.assertEquals(testList.get(0),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(1), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(5, 0);
    Assert.assertEquals(testList.get(1),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(2), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(9, 0);
    Assert.assertEquals(testList.get(2),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(3), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(17, 0);
    Assert.assertEquals(testList.get(3),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(4), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(33, 0);
    Assert.assertEquals(testList.get(4),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(5), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(65, 0);
    Assert.assertEquals(testList.get(5),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(6), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(129, 0);
    Assert.assertEquals(testList.get(6),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(7), sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testPair = new IndexedObject<>(256, 0);
    Assert.assertEquals(testList.get(7),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertNull(sbtTree.getClosestGreaterObject(testPair.getIndex()));

    testList.add(new IndexedObject<>(512, 0));
    sbtTree = new IndexedTree<>(testList);
    Assert.assertEquals(new RaptatPair<>("left", 2), sbtTree.getSideAndDepth(testList.get(0)));
    Assert.assertEquals(new RaptatPair<>("left", 1), sbtTree.getSideAndDepth(testList.get(1)));
    Assert.assertEquals(new RaptatPair<>("right", 2), sbtTree.getSideAndDepth(testList.get(2)));
    Assert.assertEquals(new RaptatPair<>("right", 3), sbtTree.getSideAndDepth(testList.get(3)));
    Assert.assertEquals(new RaptatPair<>("root", 0), sbtTree.getSideAndDepth(testList.get(4)));
    Assert.assertEquals(new RaptatPair<>("left", 2), sbtTree.getSideAndDepth(testList.get(5)));
    Assert.assertEquals(new RaptatPair<>("right", 1), sbtTree.getSideAndDepth(testList.get(6)));
    Assert.assertEquals(new RaptatPair<>("right", 2), sbtTree.getSideAndDepth(testList.get(7)));
    Assert.assertEquals(new RaptatPair<>("right", 3), sbtTree.getSideAndDepth(testList.get(8)));

    testPair = new IndexedObject<>(1, 0);
    Assert.assertNull(sbtTree.getSideAndDepth(testPair));
    Assert.assertNull(sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(3, 0);
    Assert.assertEquals(testList.get(0),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(5, 0);
    Assert.assertEquals(testList.get(1),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(9, 0);
    Assert.assertEquals(testList.get(2),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(17, 0);
    Assert.assertEquals(testList.get(3),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(33, 0);
    Assert.assertEquals(testList.get(4),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(65, 0);
    Assert.assertEquals(testList.get(5),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(129, 0);
    Assert.assertEquals(testList.get(6),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));

    testPair = new IndexedObject<>(256, 0);
    Assert.assertEquals(testList.get(7),
        sbtTree.getClosestLesserOrEqualObject(testPair.getIndex()));
    Assert.assertEquals(testList.get(8), sbtTree.getClosestGreaterObject(testPair.getIndex()));

  }
}
