package src.main.gov.va.vha09.grecc.raptat.gg.textanalysis;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.RaptatTokenPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;

/**
 * Stores the lines making up a text document including offset from the top, start offset in terms
 * of number of characters of the first character in the line, the end offset in terms of number of
 * characters of the last character in the line before a return, linefeed, or newline character, the
 * text within the line, and the tokens within the line (stored as an ordered list from first, i.e.,
 * leftmost, to last, i.e., rightmost).
 *
 * @author Glenn Gobbel, created on April 24, 2019
 *
 */
public class TextDocumentLine implements Comparable<TextDocumentLine> {

  public final int startOffset;
  public final int endOffset;
  public final String lineText;
  private final LinkedList<RaptatToken> tokenList = new LinkedList<RaptatToken>();
  private final Set<RaptatToken> tokenSet = new HashSet<>();
  private boolean tokenListSorted = false;

  /*
   * Create both hashsets and lists of tokens since the downstream algorithms may need one or the
   * other. We will create only a setLineTokens method to change what is in these two fields for a
   * bit of safety. For now, we will allow access to the two fields without copying, which exposes
   * them to changes by the users but reduces the time required for access.
   *
   */
  private final LinkedList<RaptatTokenPhrase> tokenPhraseList = new LinkedList<>();

  private final Set<RaptatTokenPhrase> tokenPhraseSet = new HashSet<>();
  private int __line_index;

  public TextDocumentLine(int startOffset, int endOffset, String lineText) {
    super();
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.lineText = lineText;
  }

  public void addTokenToEnd(RaptatToken token) {
    this.tokenListSorted = false;
    this.tokenList.addLast(token);
  }

  public void addTokenToStart(RaptatToken token) {
    this.tokenListSorted = false;
    this.tokenList.addFirst(token);
  }

  /**
   * DAX - 5/7/19
   *
   * Added to compare for use in TreeMap or TreeSet
   */
  @Override
  public int compareTo(TextDocumentLine rhs) {
    return this.endOffset - rhs.endOffset;
  }

  public int get_line_index() {
    return this.__line_index;
  }

  public List<RaptatToken> getTokenList() {
    return this.tokenList;
  }

  /**
   * @return the tokenPhraseList
   */
  public LinkedList<RaptatTokenPhrase> getTokenPhraseList() {
    if (!this.tokenListSorted) {
      Collections.sort(this.tokenPhraseList);
      this.tokenListSorted = true;
    }
    return this.tokenPhraseList;
  }

  /**
   * @return the tokenPhraseSet
   */
  public Set<RaptatTokenPhrase> getTokenPhraseSet() {
    return this.tokenPhraseSet;
  }

  public boolean isTokenListSorted() {
    return this.tokenListSorted;
  }

  public void set_line_index(int line_index) {
    this.__line_index = line_index;
  }

  public void setLineTokens(List<RaptatTokenPhrase> inputTokenPhrases) {

    /*
     * TODO - DAX - 5/7/2019
     *
     * Here is where the discussion on phrases that span multiple line should enjoin. Consider a
     * phrase two tokens long which starts on one line and continues on another
     *
     * There is a <blue dog> in the hallway.
     *
     * The idea under review is to make sure that <blue dog> can be used on each line. This likely
     * will involve going back to the TokenPhraseMaker and ensuring that the phrase is picked up
     * while reviewing one long line, then checking phrase start offset against line start offset to
     * ensure on same line, if not then make appropriate adjustments to ensure counted on both.
     */

    this.tokenPhraseList.clear();
    this.tokenList.clear();
    this.tokenListSorted = false;

    this.tokenPhraseSet.clear();
    this.tokenSet.clear();

    this.tokenPhraseList.addAll(inputTokenPhrases);

    for (RaptatTokenPhrase tokenPhrase : inputTokenPhrases) {
      this.tokenList.addAll(tokenPhrase.getTokensAsList());
    }

    this.tokenPhraseSet.addAll(inputTokenPhrases);
  }

  @Override
  public String toString() {
    return this.lineText;
  }

}
