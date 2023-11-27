package src.test.gov.va.vha09.grecc.raptat.gg.oneoffs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTester {
  public static void main(String[] args) {
    List<String> testStrings = new ArrayList<>();
    Pattern nonAlphaPattern = Pattern.compile("[^'\\w\\s]|.*'.*'");
    Pattern nonWordCharacterSplitter =
        Pattern.compile("(?:(\\A\\w+(?:'\\w)*)(\\W+)*)|(?:(\\A\\W+)(\\w+(?:'\\w)*)*)");

    testStrings = Arrays.asList("a:ptsd", "test", "a:ptsd.345*765$#@", ":jkl$");

    for (String curString : testStrings) {
      System.out.print("\nString:" + curString);
      Matcher nonAlphaMatcher = nonAlphaPattern.matcher(curString);
      if (nonAlphaMatcher.find()) {
        Matcher matchSplitter = nonWordCharacterSplitter.matcher(curString);
        int j = 1;
        while (matchSplitter.find()) {
          System.out.println("Find:" + j++);
          int groupCount = matchSplitter.groupCount();
          System.out.println(", Groups:" + groupCount);
          for (int i = 0; i < groupCount; i++) {
            System.out.println("Group " + i + ":" + matchSplitter.group(i));
          }
        }

      }
    }
  }
}
