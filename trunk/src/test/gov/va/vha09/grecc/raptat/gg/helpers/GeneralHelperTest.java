package src.test.gov.va.vha09.grecc.raptat.gg.helpers;

import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

/************************************************************
 * GeneralHelperTest - Tester for GeneralHelper.java class
 *
 * @author Glenn Gobbel, Jan 25, 2011
 *
 ***********************************************************/
public class GeneralHelperTest {
  // @Test
  // public void getFileNameTest()
  // {
  // String result = GeneralHelper.getFilePath("Get the file to open",
  // new KnowtatorProjectFileFilter());
  // Assert.assertNotSame(result,"abracadabra");
  // System.out.println(result + System.getProperty("line.separator"));
  // }
  //
  //
  // @Test
  // public void getFileDirectoryTest()
  // {
  // JFileChooser fc = new JFileChooser();
  // fc.setDialogTitle("Pick Test File");
  // fc.setFileHidingEnabled(true);
  // int fileChosen = fc.showOpenDialog(null);
  // if (fileChosen == JFileChooser.APPROVE_OPTION)
  // {
  // String filePath = fc.getSelectedFile().getAbsolutePath();
  // String theDir = GeneralHelper.getFileDirectory(filePath);
  // String thefile = fc.getName();
  // int i= 3;
  // Assert.assertEquals(thefile.lastIndexOf(File.separator),filePath.length()
  // -
  // thefile.length() - File.separator.length());
  // }
  //
  // }

  // @Test
  // public void countFileLinesTest()
  // {
  // JFileChooser fc = new JFileChooser();
  // fc.setDialogTitle("Pick Test File");
  // fc.setFileHidingEnabled(true);
  // int fileChosen = fc.showOpenDialog(null);
  // File chosenFile;
  // int numLines = 0;
  // if (fileChosen == JFileChooser.APPROVE_OPTION)
  // {
  // chosenFile = fc.getSelectedFile();
  // try
  // {
  // System.out.println("Number of lines is " +
  // GeneralHelper.countNonEmptyFileLines(chosenFile));
  // }
  // catch (IOException e)
  // {
  // e.printStackTrace();
  // }
  // }
  // }
  //
  // @Test
  // public void invertNonNullTokensTest()
  // {
  //
  // String [] testSequence = {"alpha", "bravo", "gamma",NULL_ELEMENT,
  // "delta", NULL_ELEMENT};
  // String [] expectedResult = {"gamma", "bravo", "alpha", NULL_ELEMENT,
  // "delta", NULL_ELEMENT};
  // GeneralHelper.reverseNonNulls(testSequence);
  // System.out.println(GeneralHelper.arrayToString(testSequence));
  // assertArrayEquals(testSequence,expectedResult);
  //
  //
  // String [] testSequence2 = {"alpha", "bravo", "gamma"};
  // String [] expectedResult2 = {"gamma", "bravo", "alpha"};
  // GeneralHelper.reverseNonNulls(testSequence2);
  // System.out.println(GeneralHelper.arrayToString(testSequence2));
  // assertArrayEquals(testSequence2,expectedResult2);
  //
  // String [] testSequence3 = {NULL_ELEMENT,NULL_ELEMENT,NULL_ELEMENT};
  // String [] expectedResult3 = {NULL_ELEMENT,NULL_ELEMENT,NULL_ELEMENT};
  // GeneralHelper.reverseNonNulls(testSequence3);
  // System.out.println( GeneralHelper.arrayToString(testSequence3) );
  // assertArrayEquals(testSequence3,expectedResult3);
  //
  // }

  // @Test
  // public void concatenateArraysTest()
  // {
  //
  // String [] testSequence = {"alpha", "bravo", "gamma",NULL_ELEMENT,
  // "delta", NULL_ELEMENT};
  // String [] expectedResult = {"gamma", "bravo", "alpha", NULL_ELEMENT,
  // "delta", NULL_ELEMENT};
  // GeneralHelper.concatenateArrays(testSequence, expectedResult, "_");
  // System.out.println(GeneralHelper.arrayToString(testSequence));
  // assertArrayEquals(testSequence,expectedResult);
  //
  //
  // String [] testSequence2 = {"alpha", "bravo", "gamma"};
  // String [] expectedResult2 = {"gamma", "bravo", "alpha"};
  // GeneralHelper.reverseNonNulls(testSequence2);
  // System.out.println(GeneralHelper.arrayToString(testSequence2));
  // assertArrayEquals(testSequence2,expectedResult2);
  //
  // String [] testSequence3 = {NULL_ELEMENT,NULL_ELEMENT,NULL_ELEMENT};
  // String [] expectedResult3 = {NULL_ELEMENT,NULL_ELEMENT,NULL_ELEMENT};
  // GeneralHelper.reverseNonNulls(testSequence3);
  // System.out.println( GeneralHelper.arrayToString(testSequence3) );
  // assertArrayEquals(testSequence3,expectedResult3);

  // @Test
  // public void sameStringsTest()
  // {
  // String [] startSeq = {"alpha", "bravo", "gamma",NULL_ELEMENT, "delta",
  // NULL_ELEMENT};
  // String [] testSeq = {"alpha", "gamma", NULL_ELEMENT};
  // System.out.println(GeneralHelper.intArrayToString(GeneralHelper.sameStrings(startSeq,
  // testSeq)));
  //
  // String [] testSeq2 = {};
  // System.out.println(GeneralHelper.intArrayToString(GeneralHelper.sameStrings(startSeq,
  // testSeq2)));
  // }

  @Test
  public void getOverlapTest() {
    String s1 = "frank";
    String s2;

    s2 = "ank";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s2 = "an";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s2 = "ankle";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s2 = "stink";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s2 = "able";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s2 = "tom frank";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

    s1 = "the talking d";
    s2 = "dog";
    System.out.println(GeneralHelper.getOverlap(s1, s2));

  }

}
