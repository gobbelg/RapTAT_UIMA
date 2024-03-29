package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common.ComparableSpanHelper;


/**
 * Class that encapsulates the vertical column sweep algorithm. The invoke method returns a list of
 * lists, which in effect establishes an ordered column space for token membership.
 *
 * @author westerd
 *
 */
public class VerticalColumnAlgorithm {
  /** The document token phrases. */
  private List<RaptatTokenPhrase> __document_token_phrases;

  /** The tree set. */
  Supplier<TreeMap<LineNumber, RaptatTokenPhrase>> tree_map =
      () -> new TreeMap<LineNumber, RaptatTokenPhrase>();

  /** The offsets. */
  private TreeSet<Integer> __offsets;

  /** The current phrases. */
  private List<RaptatTokenPhrase> __current_phrases;

  /** The next phrases. */
  private List<RaptatTokenPhrase> __next_phrases;

  /** The added phrase. */
  private boolean __added_phrase;

  private List<List<RaptatTokenPhrase>> __labeled_list;

  private boolean __do_log;


  /**
   * 1. Put all token phrases into a collection, documentTokenPhrases. 2. Put all the offsets, start
   * and end, of all tokens, into an ordered set, offsets, so that the values go from smallest to
   * largest and anyduplicates are eliminated. 3. Create two empty collections for temporarily
   * storing token phrases that overlap, currentPhrases and nextPhrases. 4. Create a boolean,
   * addedPhrase, and set it to false.
   *
   * @param token_phrases the token phrases
   */
  public VerticalColumnAlgorithm(List<RaptatTokenPhrase> token_phrases) {
    this(token_phrases, true);
  }


  public VerticalColumnAlgorithm(List<RaptatTokenPhrase> token_phrases, boolean do_log) {
    this.__do_log = do_log;

    set_document_token_phrases(token_phrases);

    store_offsets(token_phrases);

    this.__current_phrases = new ArrayList<RaptatTokenPhrase>();

    this.__next_phrases = new ArrayList<RaptatTokenPhrase>();

    this.__labeled_list = new ArrayList<List<RaptatTokenPhrase>>();

    this.__added_phrase = false;
  }


  /**
   * 6. Using documentTokenPhrases, copy all token phrases that overlap the offset from offsetIndex
   * to offsetIndex +1 into nextPhrases
   *
   * @param index the index
   * @param get_next_offset_after the get next offset after
   */
  private void copy_all_overlapping_phrases_from_current_to_next_offset_into_next_phrases(
      int current) {
    for (RaptatTokenPhrase phrase : this.__document_token_phrases) {
      if (phrase.is_in_column_index(current)) {
        if (this.__next_phrases.contains(phrase) == false) {
          this.__next_phrases.add(phrase);
        }
      }
    }
  }


  /**
   * 7.a. move the token phrases in current set to a list sorted by line number.
   *
   * @return
   */
  private List<RaptatTokenPhrase> copy_current_phrases_to_list_sorted_by_line_number() {
    List<RaptatTokenPhrase> sorted_list = new ArrayList<>(this.__current_phrases);
    sorted_list.sort((lhs, rhs) -> ComparableSpanHelper.compare_lines(lhs, rhs));
    return sorted_list;

  }


  private Set<Integer> get_offsets() {
    return this.__offsets;
  }


  /**
   * Gets the token phrase lists for labeling.
   *
   * @return the token phrase lists for labeling
   */
  private List<List<RaptatTokenPhrase>> get_token_phrase_lists_for_labeling() {
    return this.__labeled_list;
  }


  private void log_labeled_list(String title, List<List<RaptatTokenPhrase>> labeled_list) {
    set_title(title);

    int index = 0;
    for (List<RaptatTokenPhrase> list : labeled_list) {
      System.out.println("List # " + index++);
      print_list(list);
    }

  }


  private void log_variable(String title, boolean added_phrase) {
    set_title(title + ": " + added_phrase);

  }


  private void log_variable(String title, List<RaptatTokenPhrase> current_phrases) {
    set_title(title);
    print_list(current_phrases);
  }


  private void log_variables(String info, Integer index) {
    if (this.__do_log == false) {
      return;
    }
    System.out.println("\n--- " + info + ", Index: " + index + " "
        + new String(new char[80]).replace("\0", "-") + "\n");
    this.log_variable("__document_token_phrases", this.__document_token_phrases);
    this.log_variable("__current_phrases", this.__current_phrases);
    this.log_variable("__next_phrases", this.__next_phrases);
    this.log_variable("__added_phrase", this.__added_phrase);
    log_labeled_list("__labeled_list", this.__labeled_list);
  }


  /**
   * 7. If there are token phrase objects in currentPhrases that are not in present in nextPhrases
   * and addedPhrase is true
   *
   * @return true, if successful
   */
  private boolean phrases_exist_in_current_phrases_but_not_in_next_or_added() {
    for (RaptatTokenPhrase phrase : this.__current_phrases) {
      boolean next_contains = this.__next_phrases.contains(phrase);
      if (next_contains == false && this.__added_phrase) {
        return true;
      }

    }
    return false;
  }


  /**
   * 8. If there are phrases in nextPhrases that are not in currentPhrases , set addedPhrase to
   * true.
   */
  private void phrases_in_next_but_not_in_current_then_added_phrase_set_true() {
    for (RaptatTokenPhrase phrase : this.__next_phrases) {
      if (this.__current_phrases.contains(phrase) == false) {
        this.__added_phrase = true;
        break;
      }
    }

  }


  private void print_list(List<RaptatTokenPhrase> list) {
    for (RaptatTokenPhrase phrase : list) {
      System.out.println(phrase);
    }
  }


  /**
   * 7.c. set addedPhrase to false
   */
  private void set_added_phrase_to_false() {
    this.__added_phrase = false;

  }


  /**
   * 9. Set currentPhrases equal to nextPhrases
   */
  private void set_current_to_next() {
    this.__current_phrases = new ArrayList<>(this.__next_phrases);
    this.__next_phrases.clear();

  }


  /**
   * Sets the document token phrases.
   *
   * @param token_phrases the new document token phrases
   */
  private void set_document_token_phrases(List<RaptatTokenPhrase> token_phrases) {
    this.__document_token_phrases = new ArrayList<RaptatTokenPhrase>(token_phrases);
  }


  private void set_title(String title) {
    System.out.println("\n" + title + "\n");
  }


  /**
   * Put all the o sets, start and end, of all tokens, into an ordered set, offsets, so that the
   * values go from smallest to largest and any duplicates are eliminated.
   *
   * @param token_phrases the token phrases
   */
  private void store_offsets(List<RaptatTokenPhrase> token_phrases) {
    this.__offsets = new TreeSet<Integer>();
    for (RaptatTokenPhrase phrase : token_phrases) {
      this.__offsets.add(phrase.get_start_offset_relative_to_line());
      this.__offsets.add(phrase.get_end_offset_relative_to_line() - 1);
    }
    this.__offsets.add(this.__offsets.last() + 1);

  }


  /**
   * 7.b. store that list into a list of lists for returning to the user at the end of the method,
   * and
   *
   * @param sorted_list
   */
  private void store_sorted_list_in_label_list(List<RaptatTokenPhrase> sorted_list) {
    this.__labeled_list.add(sorted_list);

    /**
     * Dax - 5/20/19 Adding ability for each RaptatTokenPhrase to have any awareness of its column
     * membership
     */
    for (RaptatTokenPhrase raptatTokenPhrase : sorted_list) {
      raptatTokenPhrase.add_sorted_column_list(sorted_list);
    }
  }


  /**
   * Invoke.
   *
   * @param get_token_phrases the get token phrases
   * @return the list
   */
  public static List<List<RaptatTokenPhrase>> invoke(List<RaptatTokenPhrase> token_phrases) {
    return invoke(token_phrases, true);
  }


  public static List<List<RaptatTokenPhrase>> invoke(List<RaptatTokenPhrase> tokenPhrases,
      boolean do_log) {
    /**
     * Step 1
     */
    /**
     * Step 2
     */
    /**
     * Step 3
     */
    /**
     * Step 4
     */
    VerticalColumnAlgorithm vca = new VerticalColumnAlgorithm(tokenPhrases, do_log);
    vca.log_variables("Step 1-4", 0);

    /**
     * Step 5
     */

    Set<Integer> offsets = vca.get_offsets();

    for (Integer integer : offsets) {

      /**
       * Step 6
       */
      vca.copy_all_overlapping_phrases_from_current_to_next_offset_into_next_phrases(integer);
      vca.log_variables("Step 6", integer);

      /**
       * Step 7
       */
      if (vca.phrases_exist_in_current_phrases_but_not_in_next_or_added()) {
        /**
         * Step 7a
         */
        List<RaptatTokenPhrase> sorted_list =
            vca.copy_current_phrases_to_list_sorted_by_line_number();
        vca.log_variables("Step 7a", integer);
        /**
         * Step 7b
         */
        vca.store_sorted_list_in_label_list(sorted_list);
        vca.log_variables("Step 7b", integer);
        /**
         * Step 7c
         */
        vca.set_added_phrase_to_false();
        vca.log_variables("Step 7c", integer);
      }
      /**
       * Step 8
       */
      vca.phrases_in_next_but_not_in_current_then_added_phrase_set_true();
      vca.log_variables("Step 8", integer);

      /**
       * Step 9
       */
      vca.set_current_to_next();
      vca.log_variables("Step 9", integer);
    }

    return vca.get_token_phrase_lists_for_labeling();
  }

}
