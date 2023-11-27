/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/********************************************************
 *
 *
 * @author Glenn Gobbel - May 30, 2012
 *******************************************************/
public class RuntimeTester {

  /*************************************************************
   * @param args
   *
   * @author Glenn Gobbel - May 30, 2012
   *************************************************************/
  public static void main(String[] args) {

    Process myProcess = null;
    // String [] cmdArray = {"pwd"};
    // String [] cmdArray2 = {"cd", "/"};
    String[] cmdArray = {"ls", "-al", "/Users/glenn/01ShortcutToRaptatTrainingTests"};

    try {
      // Runtime.getRuntime().exec(cmdArray2);
      myProcess = Runtime.getRuntime().exec(cmdArray);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    }
    InputStream stdin = myProcess.getInputStream();
    InputStreamReader isr = new InputStreamReader(stdin);
    BufferedReader br = new BufferedReader(isr);
    String line = null;
    System.out.println("<OUTPUT>");
    try {
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(e);
      e.printStackTrace();
    }
    System.out.println(Arrays.toString(cmdArray));

  }

}
