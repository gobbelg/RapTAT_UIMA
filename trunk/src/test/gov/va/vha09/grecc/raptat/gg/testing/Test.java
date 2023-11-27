package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;

/************************************************************
 * Test -
 *
 * @author Glenn Gobbel, Jan 12, 2011
 *
 ***********************************************************/
public class Test {
  static int optionsValue = 0; // Stores the settings for the 4 options using

  // a 4-bit integer
  final static int stemmingMask = 1;
  final static int posMask = 2;
  final static int stopWordsMask = 4;
  final static int tokenSequenceMask = 8;


  public static void main(String[] args) throws IOException {
    Test testCode = new Test();
    // Boolean testBoolean = true;
    //
    // System.out.println( "testBoolean before:" + testBoolean );
    // testCode.changeBoolean( testBoolean );
    // System.out.println( "testBoolean after:" + testBoolean );

    String last = "\'";
    String first = "\\'";

    if (last.equals(first)) {
      System.out.println("Equal!");
    } else {
      System.out.println("Not equal");
    }

    // testCode.insertPhraseStrings( null, "/Users/glenn/Desktop/test.txt"
    // );

  }

  public boolean useStems = false, usePOS = false, removeStopWords = false,
      invertTokenSequence = false;


  private void changeBoolean(Boolean testBoolean) {
    testBoolean = !testBoolean;
    System.out.println("testBoolean inside:" + testBoolean);

  }


  private void insertPhraseStrings(List<AnnotatedPhrase> allAnnData, String pathToText) {
    BufferedReader br;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(pathToText), "UTF-8"));

      for (int i = 0; i < 1000; i++) {
        char[] phrase = new char[10];
        br.read(phrase);
        System.out.println(br.toString() + " - End Reached\n");
      }

      // for(AnnotatedPhrase curPhrase : allAnnData)
      // {
      // startOffset = Integer.parseInt(curPhrase.getStartOff());
      // endOffset = Integer.parseInt(curPhrase.getEndOff());
      // char [] phrase = new char[endOffset - startOffset];
      // br.skip(startOffset - curStreamPosition);
      // br.read(phrase);
      // curPhrase.setPhrase(phrase.toString());
      // }
    } catch (FileNotFoundException e) {
      System.out.println(e);
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    }
  }


  /*************************************************************
   * Uses bit values to determine option settings for running the training and testing during
   * bootstrapping
   *
   * @author Glenn Gobbel - Jun 18, 2012
   *************************************************************/
  private void setNextOptions() {
    this.useStems = (Test.optionsValue & Test.stemmingMask) > 0 ? true : false;
    this.usePOS = (Test.optionsValue & Test.posMask) > 0 ? true : false;
    this.removeStopWords = (Test.optionsValue & Test.stopWordsMask) > 0 ? true : false;
    this.invertTokenSequence = (Test.optionsValue & Test.tokenSequenceMask) > 0 ? true : false;

    ++Test.optionsValue;
    Test.optionsValue %= 16;
  }
}
