package src.test.gov.va.vha09.grecc.raptat.gg.LexicalTools;

import org.junit.Test;
import src.main.gov.va.vha09.grecc.raptat.gg.lexicaltools.Lemmatiser;

public class LemmatiserTest {

  @Test
  public void testLemmatize() {

    Lemmatiser root = new Lemmatiser();
    try {
      String[] words = null;

      root.lvgConnection(true);
      String phrase1 = "will be gone";
      phrase1.trim();
      // First remove the stop words,etc from string, then run the
      // Lemmatiser on the output.
      words = phrase1.split(" ");
      // String PhraseWithoutStops=root.preProcess(phrase1);
      // String[]
      // normalized=root.lemmatize(PhraseWithoutStops.split(" "));
      // String[] phraseWithoutStops=root.preProcess(words,false);
      String[] normalized = root.lemmatizePhrase(words);

      // Printing the normalized/stemmed words
      for (int i = 0; i < normalized.length; i++) {
        if (normalized[i] != null) {
          System.out.println(normalized[i]);
        }

      }
      root.printStemsMap();
      // get the pos from the gate module for the words in the phrase

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      root.lvgCleanup();
    }
  }

}
