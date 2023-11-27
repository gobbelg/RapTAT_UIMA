package src.test.gov.va.vha09.grecc.raptat.gg.datastructures;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTree;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTreeInputStream;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees.UnlabeledHashTreeOutputStream;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

public class UnlabeledHashTreeOutstreamTest {

  // @Test
  public void standardOutstreamTest() {
    // Create and print out some UnlabeledHashTreeInstances
    UnlabeledHashTree testTree1 = UnlabeledHashTreeTest.generateTestTree1();
    UnlabeledHashTree testTree2 = UnlabeledHashTreeTest.generateTestTree2();
    UnlabeledHashTree testTree3 = UnlabeledHashTreeTest.generateTestTree3();
    ArrayList<UnlabeledHashTree> treeList = new ArrayList<>(10);
    treeList.add(testTree1);
    treeList.add(testTree2);
    treeList.add(testTree3);
    treeList.add(testTree1);

    String pathToFile =
        GeneralHelper.getNewFileName("Select directory to store uhts", "UHT_Test_", ".rpt");
    ObjectOutputStream outStream = null;
    try {
      outStream = new ObjectOutputStream(new FileOutputStream(pathToFile));
      for (UnlabeledHashTree curTree : treeList) {
        outStream.writeObject(curTree);
      }
      outStream.flush();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    } finally {
      if (outStream != null) {
        try {
          outStream.flush();
          outStream.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          System.out.println(e);
          e.printStackTrace();
        }
      }

    }

    ObjectInputStream inStream = null;
    UnlabeledHashTree testTree;
    try {
      inStream = new ObjectInputStream(new FileInputStream(pathToFile));
      while (true) {
        testTree = (UnlabeledHashTree) inStream.readObject();
        System.out.println("***************************************************");
        testTree.printUnlabeledSequences();
      }

    } catch (FileNotFoundException e) {
      System.out.println(e);
      e.printStackTrace();
    } catch (EOFException e) {
      System.out.println("EOF Found");
    } catch (IOException e) {
      System.out.println(e);
    } catch (ClassNotFoundException e) {
      System.out.println(e);
      e.printStackTrace();
    }

    finally {
      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          System.out.println(e);
          e.printStackTrace();
        }
      }

    }

  }


  @Test
  public void TestAppendingUnlabeledTrees() {
    // Create and print out some UnlabeledHashTreeInstances
    UnlabeledHashTree testTree1 = UnlabeledHashTreeTest.generateTestTree1();
    UnlabeledHashTree testTree2 = UnlabeledHashTreeTest.generateTestTree2();
    UnlabeledHashTree testTree3 = UnlabeledHashTreeTest.generateTestTree3();

    {
      ArrayList<UnlabeledHashTree> treeList = new ArrayList<>(10);
      treeList.add(testTree1);
      treeList.add(testTree2);
      treeList.add(testTree3);
      treeList.add(testTree2);

      UnlabeledHashTreeOutputStream myListStream;
      myListStream = new UnlabeledHashTreeOutputStream();
      File theListFile = myListStream.getFile();
      myListStream.write(treeList);

      treeList.add(testTree3);
      myListStream.append(treeList);

      // Read in the saved UnlabeledHashTree instances
      // and print them out
      UnlabeledHashTreeInputStream myInList = null;
      try {
        myInList = new UnlabeledHashTreeInputStream(theListFile.getCanonicalPath());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println(e);
        e.printStackTrace();
      }
      int i = 1;
      for (UnlabeledHashTree aTree : myInList) {
        System.out.println("Printing unlabeledHashTree List Item " + i++);
        aTree.printUnlabeledSequences();
        System.out.println("*****************************************");
      }

    }

    {
      // Write the instances to a file on disk
      UnlabeledHashTreeOutputStream myOutStream;
      myOutStream = new UnlabeledHashTreeOutputStream();
      File theOutFile = myOutStream.getFile();
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
      UnlabeledHashTreeInputStream myInStream = null;
      try {
        myInStream = new UnlabeledHashTreeInputStream(theOutFile.getCanonicalPath());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        System.out.println(e);
        e.printStackTrace();
      }
      for (UnlabeledHashTree aTree : myInStream) {
        System.out.println("Printing unlabeledHashTree #" + i++);
        aTree.printUnlabeledSequences();
        System.out.println("*****************************************");
      }
    }

  }

}
