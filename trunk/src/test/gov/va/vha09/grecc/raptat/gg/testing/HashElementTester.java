package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.util.Arrays;
import java.util.Hashtable;

public class HashElementTester {
  public static void main(String[] args) {
    Integer[] myInts1 = {3, 8, 79};
    Integer[] myInts2 = {4, 16, 26};

    Hashtable<String, Integer[]> myHash = new Hashtable<>();

    myHash.put("a", myInts1);
    myHash.put("b", myInts2);

    System.out.println("a: " + Arrays.toString(myHash.get("a")));
    Integer[] changeInts = myHash.get("a");

    changeInts[0] = 35;

    System.out.println("a: " + Arrays.toString(myHash.get("a")));
  }
}
