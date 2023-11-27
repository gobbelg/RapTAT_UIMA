package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import com.google.common.base.Joiner;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common.ComparableSpanHelper;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common.IComparableSpan;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.TextDocumentLine;


/**
 * Class stub used in anticipation of upcoming RaptatTokenPhrase from RapTAT.
 *
 * @author westerd
 */
public class RaptatTokenPhrase implements IComparableSpan {

  /**
   * DAX 5/7/19
   *
   * Added to create a later common point for any logic dealing with Labels. At this point just
   * encapsulates a String
   *
   * @author westerd
   *
   */
  static public class Label implements Comparable<Label> {

    /** The feature search pattern. */
    final private String __name;

    /** The feature type. */
    final private String __value;


    public Label(String name) {
      this(name, name);
    }


    /**
     * Instantiates a new label.
     *
     * @param label the label
     * @param name the feature search pattern
     * @param value the feature type
     * @param parent_phrase the parent phrase
     * @param is_assigned the is assigned
     */
    public Label(String name, String value) {
      this.__name = name;
      this.__value = value;
    }


    /**
     * Compare to.
     *
     * @param label the label
     * @return the int
     */
    @Override
    public int compareTo(Label label) {
      if (equals(label)) {
        return 0;
      }
      return -1;
    }


    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof Label)) {
        return false;
      }

      Label lbl = (Label) obj;

      return new EqualsBuilder().append(this.__name, lbl.__name).append(this.__value, lbl.__value)
          .isEquals();
    }


    public String get_name() {
      return this.__name;
    }


    public String get_value() {
      return this.__value;
    }


    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37).append(this.__name).append(this.__value).toHashCode();
    }


    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
      return new StringBuilder().append("(").append(this.__name).append(": ").append(this.__value)
          .append(")").toString();
    }
  }


  private final static Label INDEX_VALUE = new Label("IndexValue");

  /** The start offset. */
  private int __start_offset;

  /** The end offset. */
  private int __end_offset;

  /** The tokens as list. */
  private final List<RaptatToken> tokensAsList = new ArrayList<>();

  /** The phrase labels. */
  /*
   * We create a set of phrase labels that store associations of the string indicated by the list of
   * tokens in the phrase. We may expand this in the future to handle multiple phrases.
   */
  private HashSet<Label> phraseLabels = new HashSet<>();

  /** The current line. */
  private TextDocumentLine __current_line;

  private Set<String> phraseFeatures;

  /**
   * DAX - 5/20/19 Added to create local reference to all column-space membership
   */
  List<List<RaptatTokenPhrase>> localColumnCache;


  /**
   * Instantiates a new raptat token phrase.
   *
   * @param tokens the tokens
   */
  public RaptatTokenPhrase(List<RaptatToken> tokens) {
    this.tokensAsList.addAll(tokens);
    Collections.sort(this.tokensAsList);
    this.__start_offset = this.tokensAsList.get(0).get_start_offset_as_int();
    this.__end_offset = this.tokensAsList.get(this.tokensAsList.size() - 1).get_end_offset_as_int();
    this.phraseFeatures = new HashSet<>();
    this.localColumnCache = new ArrayList<>();
  }


  /**
   * Instantiates a new raptat token phrase.
   *
   * @param token the token
   * @param label the label
   */
  public RaptatTokenPhrase(RaptatToken token, Label label) {
    this(new RaptatToken[] {token}, label);
  }


  /**
   * Instantiates a new raptat token phrase.
   *
   * @param raptatTokens the raptat tokens
   * @param labels the labels
   */
  public RaptatTokenPhrase(RaptatToken[] raptatTokens, Label... labels) {
    this(Arrays.asList(raptatTokens));
  }


  /**
   * DAX - 5/20/19 Added to create local reference to all column-space membership
   */
  public void add_sorted_column_list(List<RaptatTokenPhrase> sorted_list) {
    this.localColumnCache.add(sorted_list);
  }


  /**
   * Add an individual feature, usually one discovered from review of the immediate phrase's label
   *
   * @param phraseFeature
   */
  public void addPhraseFeature(String phraseFeature) {
    if (this.phraseFeatures.contains(phraseFeature) == false) {
      this.phraseFeatures.add(phraseFeature);
    }
  }


  /**
   * Add a List of features, usually already discovered from a row or column
   *
   * @param features the features
   */
  public void addPhraseFeatures(List<String> features) {
    if (features != null) {
      for (String feature : features) {
        addPhraseFeature(feature);
      }
    }
  }


  /**
   * Adds the phrase label.
   *
   * @param label the label
   */
  public void addPhraseLabel(Label label) {
    this.phraseLabels.add(label);
  }


  /**
   * Adds the phrase labels.
   *
   * @param labels the labels
   */
  public void addPhraseLabels(List<Label> labels) {
    this.phraseLabels.addAll(labels);

  }


  /**
   * Compare to.
   *
   * @param rhs the rhs
   * @return the int
   */
  @Override
  public int compareTo(IComparableSpan rhs) {
    IComparableSpan lhs = this;
    if (lhs.get_end_offset_as_int() > rhs.get_start_offset_as_int()) {
      if (lhs.get_start_offset_as_int() < rhs.get_end_offset_as_int()) {
        return 0;
      }
      // lhs comes after (in terms of character offset) rhs.
      return -1;
    }

    if (lhs.get_start_offset_as_int() < rhs.get_end_offset_as_int()) {
      if (lhs.get_end_offset_as_int() > rhs.get_start_offset_as_int()) {
        return 0;
      }
    }

    // lhs must come after rhs as lhs start >= rhs end
    return 1;
  }


  public String debug_tokenPhraseDetails() {
    return String.format("[%s, %s) - %s %s", this.__start_offset, this.__end_offset, get_text(),
        get_line_index() > -1 ? String.format("(line %s)", get_line_index()) : "");
  }


  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof RaptatTokenPhrase)) {
      return false;
    }
    RaptatTokenPhrase rtp = (RaptatTokenPhrase) obj;

    /*
     * Going to try this less complete definition of equals, discounting inner tokens for now, for
     * it should be reasonable that one-and-only-one phrase exists in the space defined by start,
     * end, line index, and token count.
     */
    return new EqualsBuilder().append(this.__start_offset, rtp.__start_offset)
        .append(this.__end_offset, rtp.__end_offset).append(get_line_index(), rtp.get_line_index())
        .append(this.tokensAsList.size(), rtp.tokensAsList.size()).isEquals();
  }


  /**
   * Gets the end offset as int.
   *
   * @return the end offset as int
   */
  @Override
  public int get_end_offset_as_int() {
    return this.__end_offset;
  }


  /**
   * Gets the end offset relative to line.
   *
   * @return the end offset relative to line
   */
  public int get_end_offset_relative_to_line() {
    return get_end_offset_as_int() - this.__current_line.startOffset;
  }


  /**
   * Gets the line index.
   *
   * @return the line index
   */
  @Override
  public int get_line_index() {
    return this.__current_line == null ? -1 : this.__current_line.get_line_index();
  }


  /**
   * Gets the parent line.
   *
   * @return the parent line
   */
  public TextDocumentLine get_parent_line() {
    return this.__current_line;
  }


  /**
   * DAX - 5/20/19 Added to create local reference to all column-space membership
   */
  public List<List<RaptatTokenPhrase>> get_sorted_column_lists() {
    return this.localColumnCache;
  }


  /**
   * Gets the start offset as int.
   *
   * @return the start offset as int
   */
  @Override
  public int get_start_offset_as_int() {
    return this.__start_offset;
  }


  /**
   * Gets the start offset relative to line.
   *
   * @return the start offset relative to line
   */
  public Integer get_start_offset_relative_to_line() {
    return get_start_offset_as_int() - this.__current_line.startOffset;
  }

  /**
   * Gets the text.
   *
   * @return the text
   */
  public String get_text() {
    String joinDelimeter = " ";
    if (this.phraseLabels.contains(INDEX_VALUE)) {
      joinDelimeter = "";
    }
    return Joiner.on(joinDelimeter)
        .join(this.tokensAsList.stream().map(x -> x.getTokenStringAugmented()).iterator());
  }


  /**
   * Find the relative distance (lhs on left, rhs on right) of two token phrases. Comparison across
   * lines results in an exception
   *
   * @param lhs the lhs
   * @param rhs the rhs
   * @return the token phrase distance
   * @throws RaptatTokenPhraseException the raptat token phrase exception
   */
  public int get_token_phrase_distance(RaptatTokenPhrase lhs, RaptatTokenPhrase rhs)
      throws RaptatTokenPhraseException {
    if (lhs.__current_line.equals(rhs.__current_line) == false) {
      throw new RaptatTokenPhraseException(String.format(
          "Cannot obtain token phrase distance from different lines: lhs = %s, rhs = %s", lhs,
          rhs));
    }
    int lhs_index = 0;
    int rhs_index = 0;
    for (int index = 0; index < this.__current_line.getTokenPhraseList().size(); index++) {
      RaptatTokenPhrase raptatTokenPhrase = this.__current_line.getTokenPhraseList().get(index);
      if (raptatTokenPhrase.equals(lhs)) {
        lhs_index = index;
      }
      if (raptatTokenPhrase.equals(rhs)) {
        rhs_index = index;
      }
    }
    return rhs_index - lhs_index;

  }


  public Set<String> getPhraseFeatures() {
    return this.phraseFeatures;
  }


  public Set<Label> getPhraseLabels() {
    return Collections.unmodifiableSet(this.phraseLabels);
  }


  /**
   * Gets the tokens as list.
   *
   * @return the tokens as list
   */
  public List<RaptatToken> getTokensAsList() {
    return this.tokensAsList;
  }


  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(this.__start_offset).append(this.__end_offset)
        .append(get_line_index()).append(this.tokensAsList.size()).toHashCode();
  }


  /**
   * Checks if is in column index.
   *
   * @param column_index the column index
   * @return true, if is in column index
   */
  @Override
  public boolean is_in_column_index(int column_index) {
    return get_start_offset_relative_to_line() <= column_index
        && column_index <= get_end_offset_relative_to_line() - 1;
  }


  /**
   * Sets the containing text document line.
   *
   * @param current_line the new containing text document line
   */
  public void set_containing_text_document_line(TextDocumentLine current_line) {
    this.__current_line = current_line;
  }


  public void setPhraseFeatures(Set<String> featureState) {


    this.phraseFeatures.addAll(featureState);

  }


  /**
   * Sets the token list.
   *
   * @param raptatTokens the new token list
   */
  public void setTokenList(List<RaptatToken> raptatTokens) {
    this.tokensAsList.addAll(raptatTokens);
    Collections.sort(this.tokensAsList);
  }


  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(debug_tokenPhraseDetails());
    sb.append("\nLabels: ");
    sb.append(this.phraseLabels);
    sb.append("\n");
    sb.append("Features: ");
    sb.append(this.phraseFeatures);
    sb.append("\n");

    return sb.toString();
  }


  /**
   * Sets the tokens.
   *
   * @param tokens the new tokens
   */
  @Deprecated
  private void setTokens(List<RaptatToken> tokens) {
    if (tokens == null) {
      throw new InvalidParameterException("A token phrase cannot be empty.");
    }

    TreeSet<RaptatToken> set =
        new TreeSet<>((lhs, rhs) -> ComparableSpanHelper.compare_start_offsets(lhs, rhs));
    set.addAll(tokens);

    this.__start_offset = set.first().get_start_offset_as_int();
    this.__end_offset = set.last().get_end_offset_as_int();

  }


  /**
   * Gets the all tokens.
   *
   * @param token_phrases the token phrases
   * @return the all tokens
   */
  public static List<RaptatToken> get_all_tokens(List<RaptatTokenPhrase> token_phrases) {
    List<RaptatToken> ret_val = new ArrayList<>();
    for (RaptatTokenPhrase raptat_token_phrase : token_phrases) {
      for (RaptatToken raptat_token : raptat_token_phrase.tokensAsList) {
        ret_val.add(raptat_token);
      }
    }
    return ret_val;
  }

}
