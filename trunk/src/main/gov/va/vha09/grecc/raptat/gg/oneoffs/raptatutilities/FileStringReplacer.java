/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.raptatutilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

/**
 * Utility class to replace all the strings in a given text file using a set of regular expression
 * to match the string to and the strings to replace any matches. The search and replace is
 * automatically case insensitive.
 *
 * @author gobbelgt
 *
 */
public class FileStringReplacer {

  public static final String REGEX_FILEPATH =
      "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\CanaryEpsilonTest_190423\\CanaryEpsilonModel_v01_190424\\CliticFileRaptat_v04_190417.txt";
  public static final String TEXT_FILEPATH =
      "P:\\ORD_Virani_201706002D\\NLP\\Glenn\\CanaryDevelopmentAndTesting\\CanaryEpsilonTest_190423\\CanaryEpsilonModel_v01_190424\\NotesFolder\\Group_04_181128_v03.txt";

  public static final String ALLERGY_LIST_START = "^( |\\t)*allerg(y|ies|\\.)*.*:";
  public static final Pattern ALLERGY_LIST_PATTERN =
      Pattern.compile(ALLERGY_LIST_START, Pattern.CASE_INSENSITIVE);


  public File concatenateLinesAndReplaceRegexes(String textFilePath, String regexFilePath) {

    File replacementFile = null;
    File textFile = new File(textFilePath);

    if (!textFile.exists()) {
      textFile = GeneralHelper.getFile("Select text file containing text to be replaced");
    }

    File regexFile = new File(regexFilePath);
    if (!regexFile.exists()) {
      regexFile = GeneralHelper.getFile(
          "Select text file containing regular expressions and strings to be used for replacement.\n"
              + "Each line should contain the regular expression to locate followed by a tab followed by the string to use for replacement. ");
    }

    PrintWriter pw = null;
    try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
      replacementFile = getReplacementFile(textFile);
      Map<Integer, RaptatPair<String, String>> regexAndStringReplacements =
          getReplacementRegexesAndStrings(regexFile);
      String textLine;
      int lineIndex = 0;

      pw = new PrintWriter(replacementFile);
      while ((textLine = br.readLine()) != null) {
        System.out.println("TextLine " + lineIndex++ + ":" + textLine);

        if (ALLERGY_LIST_PATTERN.matcher(textLine).find()) {
          textLine = concatenateAllergyLine(textLine, br);
        }
        String replacementLine = getReplacementLine(textLine, regexAndStringReplacements);
        System.out.println("ReplacementLine:" + replacementLine);
        pw.println(replacementLine);
        pw.flush();
      }

      pw.close();
    } catch (IOException exception) {
      GeneralHelper.errorWriter(exception.getLocalizedMessage());
      System.err.println(exception.getLocalizedMessage());
      exception.printStackTrace();
    } finally {
      if (pw != null) {
        pw.close();
      }
    }

    System.out.println("\n\n---------------------------------------------\n"
        + "Regular expression replaceement complete"
        + "\n---------------------------------------------");

    return replacementFile;
  }


  private String concatenateAllergyLine(String textLine, BufferedReader br) throws IOException {
    String nextLine;

    textLine = textLine.trim();
    textLine = System.lineSeparator() + textLine;

    while ((nextLine = br.readLine()) != null) {
      nextLine = nextLine.trim();
      if (Pattern.matches(".*\\p{Alpha}+.*", nextLine) && !nextLine.contains(":")
          && !nextLine.endsWith(".")) {
        if (!textLine.endsWith(",") && !textLine.endsWith(":")) {
          textLine += ", ";
        }
        textLine += nextLine;
      } else {
        textLine += System.lineSeparator();
        textLine += System.lineSeparator();
        textLine += nextLine;
        break;
      }
    }

    return textLine;
  }


  private File getReplacementFile(File textFile) throws IOException {
    String parentDirectoryPath = textFile.getParent();
    String replacementDirectoryPath = parentDirectoryPath + File.separator + "Revised";
    FileUtils.forceMkdir(new File(replacementDirectoryPath));
    String textFileNameOld = textFile.getName();
    String[] textFileNameOnly = textFileNameOld.split("\\.");
    return new File(replacementDirectoryPath, textFileNameOnly[0] + "_Replaced.txt");
  }


  private String getReplacementLine(String textLine,
      Map<Integer, RaptatPair<String, String>> regexAndStringReplacements) {

    for (Integer mapKey : regexAndStringReplacements.keySet()) {
      RaptatPair<String, String> regexAndReplacement = regexAndStringReplacements.get(mapKey);
      textLine = textLine.replaceAll(regexAndReplacement.left, regexAndReplacement.right);
    }
    return textLine;
  }


  /*
   * Generates an ordered map of regular expression replacements. The ordering will either be in the
   * order in the file, or, if an index is provided as the first element in the file according to
   * the index. If an index occurs more than once, the index will be incremented by one until a free
   * index is identified.
   */
  private Map<Integer, RaptatPair<String, String>> getReplacementRegexesAndStrings(File regexFile) {
    Map<Integer, RaptatPair<String, String>> resultMap = new TreeMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(regexFile))) {
      String textLine;
      int line = 0;
      while ((textLine = br.readLine()) != null) {
        String[] regexReplacementPair = textLine.split("\t");
        int replacedStringIndex = 0;
        Integer regexIndex = 0;
        if (regexReplacementPair.length == 2) {
          regexIndex = line;
        } else if (regexReplacementPair.length == 3) {
          regexIndex = Integer.parseInt(regexReplacementPair[0]);
          ++replacedStringIndex;
        } else {
          GeneralHelper.errorWriter(
              "Incorrect formatting on line " + line + " of regex string replacement file ");
          System.exit(-1);
        }
        while (resultMap.containsKey(regexIndex)) {
          ++regexIndex;
        }
        resultMap.put(regexIndex,
            new RaptatPair<>("(?i)" + regexReplacementPair[replacedStringIndex],
                regexReplacementPair[replacedStringIndex + 1]));

        ++line;

      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return resultMap;
  }


  /*
   * Call main with args[0] as the text filepath and args[1] the regex filepath or the final static
   * strings variables will be used
   */
  public static void main(String[] args) {

    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        FileStringReplacer fsr = new FileStringReplacer();
        File resultFile = fsr.concatenateLinesAndReplaceRegexes(TEXT_FILEPATH, REGEX_FILEPATH);

        int exitStatus = resultFile == null ? -1 : 0;
        System.exit(exitStatus);
      }

    });
  }
}
