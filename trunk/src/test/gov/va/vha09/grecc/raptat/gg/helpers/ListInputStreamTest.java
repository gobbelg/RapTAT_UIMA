package src.test.gov.va.vha09.grecc.raptat.gg.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.LabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.ListInputStream;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.ListOutputStream;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.NotListOutputStreamFile;
import src.test.gov.va.vha09.grecc.raptat.gg.datastructures.LabeledHashTreeTest;
import src.test.gov.va.vha09.grecc.raptat.gg.datastructures.UnlabeledHashTreeTest;

public class ListInputStreamTest {
  @Test
  public void ManyTreeTest() {
    int listSize1 = 50;

    ArrayList<LabeledHashTree> treeList1 = new ArrayList<>(listSize1);

    for (int i = 0; i < listSize1; ++i) {
      treeList1.add(LabeledHashTree.generateRandomTree(8, 40));
    }

    ListOutputStream<LabeledHashTree> myListStream;
    myListStream = new ListOutputStream<>();
    String theListFile = myListStream.getFileName();
    myListStream.write(treeList1);

    int listSize2 = 10;
    ArrayList<LabeledHashTree> treeList2 = new ArrayList<>(listSize2);
    for (int i = 0; i < listSize2; ++i) {
      treeList2.add(LabeledHashTree.generateRandomTree(10, 8));
    }
    myListStream.append(treeList2);

    treeList1.addAll(treeList2);

    ListInputStream<LabeledHashTree> myInList;
    try {
      myInList = new ListInputStream<>(theListFile);
      int i = 1;
      for (LabeledHashTree aTree : myInList) {
        System.out.println("Printing LabeledHashTree List Item " + i++);
        aTree.printLabeledSequences();
        System.out.println("*****************************************");
        assertEquals(treeList1.get(i - 2), aTree);
      }
    } catch (NotListOutputStreamFile e) {
      System.out.println(e);
      e.printStackTrace();
      assertTrue(false);
    }
  }


  // @Test
  public void TestAppendingLabeledTrees() {
    // Create and print out some UnlabeledHashTreeInstances
    LabeledHashTree testTree1 = LabeledHashTreeTest.generateTestTree1();
    LabeledHashTree testTree2 = LabeledHashTreeTest.generateTestTree1();
    LabeledHashTree testTree3 = LabeledHashTreeTest.generateTestTree1();

    {
      ArrayList<LabeledHashTree> treeList = new ArrayList<>(10);
      treeList.add(testTree1);
      treeList.add(testTree2);
      treeList.add(testTree3);
      treeList.add(testTree2);

      ListOutputStream<LabeledHashTree> myListStream;
      myListStream = new ListOutputStream<>();
      String theListFile = myListStream.getFileName();
      myListStream.write(treeList);

      treeList.add(testTree3);
      myListStream.append(testTree3);

      myListStream.append(treeList);
      treeList.addAll(treeList);

      System.out.println("Tree list length: " + treeList.size());

      // Read in the saved UnlabeledHashTree instances
      // and print them out
      ListInputStream<LabeledHashTree> myInList;
      try {
        myInList = new ListInputStream<>(theListFile);
        int i = 1;
        for (LabeledHashTree aTree : myInList) {
          System.out.println("Printing LabeledHashTree List Item " + i++);
          aTree.printLabeledSequences();
          System.out.println("*****************************************");
          assertEquals(treeList.get(i - 2), aTree);
        }
      } catch (NotListOutputStreamFile e) {
        System.out.println(e);
        e.printStackTrace();
        assertTrue(false);
      }

    }

    {
      // Write the instances to a file on disk
      ListOutputStream<LabeledHashTree> myOutStream;
      myOutStream = new ListOutputStream<>();
      String theOutFile = myOutStream.getFileName();
      ArrayList<LabeledHashTree> myTreeList = new ArrayList<>();
      myOutStream.append(testTree1);
      myTreeList.add(testTree1);
      myOutStream.append(testTree3);
      myTreeList.add(testTree3);
      myOutStream.append(testTree2);
      myTreeList.add(testTree2);
      myOutStream.append(testTree3);
      myTreeList.add(testTree3);
      myOutStream.append(testTree2);
      myTreeList.add(testTree2);
      myOutStream.append(testTree1);
      myTreeList.add(testTree1);
      myOutStream.append(testTree3);
      myTreeList.add(testTree3);
      myOutStream.append(testTree3);
      myTreeList.add(testTree3);

      // Read in the saved UnlabeledHashTree instances
      // and print them out
      int i = 1;
      ListInputStream<LabeledHashTree> myInStream;
      try {
        myInStream = new ListInputStream<>(theOutFile);
        for (LabeledHashTree aTree : myInStream) {
          System.out.println("Printing LabeledHashTree #" + i++);
          aTree.printLabeledSequences();
          System.out.println("*****************************************");
          assertEquals(myTreeList.get(i - 2), aTree);
        }
      } catch (NotListOutputStreamFile e) {
        // TODO Auto-generated catch block
        System.out.println(e);
        e.printStackTrace();
      }

    }

  }


  // @Test
  public void TestAppendingUnlabeledTrees() {
    // Create and print out some UnlabeledHashTreeInstances
    UnlabeledHashTree testTree1 = UnlabeledHashTreeTest.generateTestTree1();
    UnlabeledHashTree testTree2 = UnlabeledHashTreeTest.generateTestTree1();
    UnlabeledHashTree testTree3 = UnlabeledHashTreeTest.generateTestTree1();

    {
      ArrayList<UnlabeledHashTree> treeList = new ArrayList<>(10);
      treeList.add(testTree1);
      treeList.add(testTree2);
      treeList.add(testTree3);
      treeList.add(testTree2);

      ListOutputStream<UnlabeledHashTree> myListStream;
      myListStream = new ListOutputStream<>();
      String theListFile = myListStream.getFileName();
      myListStream.write(treeList);

      myListStream.append(testTree3);
      treeList.add(testTree3);

      myListStream.append(treeList);
      treeList.addAll(treeList);

      // Read in the saved UnlabeledHashTree instances
      // and print them out
      ListInputStream<UnlabeledHashTree> myInList;
      try {
        myInList = new ListInputStream<>(theListFile);
        int i = 1;
        for (UnlabeledHashTree aTree : myInList) {
          System.out.println("Printing unlabeledHashTree List Item " + i++);
          aTree.printUnlabeledSequences();
          System.out.println("*****************************************");
        }
      } catch (NotListOutputStreamFile e) {
        System.out.println(e);
        e.printStackTrace();
        assertTrue(false);
      }

    }

    {
      // Write the instances to a file on disk
      ListOutputStream<UnlabeledHashTree> myOutStream;
      myOutStream = new ListOutputStream<>();
      String theOutFile = myOutStream.getFileName();
      myOutStream.append(testTree1);
      myOutStream.append(testTree3);
      myOutStream.append(testTree2);
      myOutStream.append(testTree3);
      myOutStream.append(testTree2);
      myOutStream.append(testTree1);
      myOutStream.append(testTree3);
      myOutStream.append(testTree3);

      // Read in the saved UnlabeledHashTree instances
      // and print them out
      int i = 1;
      ListInputStream<UnlabeledHashTree> myInStream;
      try {
        myInStream = new ListInputStream<>(theOutFile);
        for (UnlabeledHashTree aTree : myInStream) {
          System.out.println("Printing unlabeledHashTree #" + i++);
          aTree.printUnlabeledSequences();
          System.out.println("*****************************************");
        }
      } catch (NotListOutputStreamFile e) {
        // TODO Auto-generated catch block
        System.out.println(e);
        e.printStackTrace();
      }

    }

  }
}
