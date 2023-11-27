package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.util.Scanner;

public class doublePrint {
  public static void main(String args[]) {
    Scanner keyboard = new Scanner(System.in);
    String aString = "";

    while (true) {
      aString = keyboard.next();
      System.out.println(aString);
    }
  }
}
