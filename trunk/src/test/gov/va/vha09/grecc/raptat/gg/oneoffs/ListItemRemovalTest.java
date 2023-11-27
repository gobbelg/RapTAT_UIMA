/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.oneoffs;

import java.util.ArrayList;
import java.util.Arrays;

/********************************************************
 *
 *
 * @author Glenn Gobbel - May 14, 2012
 *******************************************************/
public class ListItemRemovalTest {
  public static void main(String[] args) {
    String[] myArray = {"a", "b", "c", "d"};
    ArrayList<String> myList = new ArrayList<>();
    myList.addAll(Arrays.asList(myArray));

    for (int i = 0; i < myList.size(); i++) {
      if (myList.get(i).equals("b")) {
        myList.remove(i--);
      }
      System.out.println(i + ": " + myList.toString());
    }

  }
}
