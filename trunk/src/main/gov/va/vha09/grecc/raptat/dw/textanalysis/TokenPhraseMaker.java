package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.google.common.base.Joiner;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common.ComparableSpanHelper;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.FSMDefinition;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.FSMElementException;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.FSMFeatureData;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.FSMFeatureLabeler;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.IRaptatTokenText;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.MatchPattern;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.MatchPattern.MatchDetails;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.MatchPattern.MatchType;
import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code.RaptatTokenFSM;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;

/**
 * TokenPhraseMaker
 *
 * Class to take sentences containing their tokens and identifies and returns the token phrases
 * (RaptatTokenPhrase objects) within those sentences. It does this by being initialized with finite
 * state machines (FSM) that will match regular expressions that are patterned after strings made up
 * by one or more sequences of tokens.
 *
 * @author gtony
 * @date May 5, 2019
 *
 */
public class TokenPhraseMaker {

  private class RaptatTokenPhrases {
    Set<RaptatTokenPhrase> __raptat_token_phrase;


    public RaptatTokenPhrases() {
      this.__raptat_token_phrase = new TreeSet<>(
          (lhs, rhs) -> ComparableSpanHelper.compare_for_inclusive_intersection(lhs, rhs));
    }


    public void add(RaptatTokenPhrase token_phrase) {
      if (this.__raptat_token_phrase.contains(token_phrase) == false) {
        this.__raptat_token_phrase.add(token_phrase);
      }
    }


    public Collection<? extends RaptatTokenPhrase> get_phrases() {
      return this.__raptat_token_phrase;
    }


    @Override
    public String toString() {
      return Joiner.on("\n").join(this.__raptat_token_phrase);
    }
  }

  private static final int ESTIMATED_MAX_TOKEN_PHRASES_IN_DOC = 10000;


  private List<FSMFeatureLabeler> phraseLabelerList = new ArrayList<>();


  public TokenPhraseMaker(List<FSMFeatureLabeler> phraseLabelerList) {
    this.phraseLabelerList = phraseLabelerList;
  }

  /**
   * @param theSentences
   * @param index_range_fsm_defn
   * @return
   */
  public List<RaptatTokenPhrase> getTokenPhrases(List<AnnotatedPhrase> theSentences,
      FSMFeatureLabeler fsm_feature_labeler, FSMDefinition index_value_fsm_defn,
      FSMDefinition index_range_fsm_defn) {

    List<RaptatTokenPhrase> resultPhraseList =
        new ArrayList<RaptatTokenPhrase>(ESTIMATED_MAX_TOKEN_PHRASES_IN_DOC);

    // Question: should this be blown out into one long sentence to capture
    // spanning?
    // Current Approach: assuming no phrases spanning multiple lines.
    for (AnnotatedPhrase sentence : theSentences) {
      List<RaptatTokenPhrase> tokenPhrases = getTokenPhrasesFromAnnotatedPhrase(sentence,
          fsm_feature_labeler, index_value_fsm_defn, index_range_fsm_defn);
      resultPhraseList.addAll(tokenPhrases);
    }

    return resultPhraseList;
  }


  private void add_index_value_label(RaptatTokenPhrase foundTokenPhrase) {

    foundTokenPhrase.addPhraseLabel(new RaptatTokenPhrase.Label("IndexValue"));

  }


  private void add_matched_labels(RaptatTokenPhrase foundTokenPhrase,
      List<RaptatTokenPhrase.Label> labels) {
    foundTokenPhrase.addPhraseLabels(labels);
  }


  private void add_unassigned_label(RaptatTokenPhrase foundTokenPhrase) {
    foundTokenPhrase.addPhraseLabel(new RaptatTokenPhrase.Label("unassigned"));

  }


  /**
   * The object should have a set of finite state machines (see phraseLabelerList) assigned to it so
   * that it returns all labels corresponding to the phrase.
   *
   * @param fsm_feature_labeler
   *
   * @param phraseString)
   * @return
   */
  private List<RaptatTokenPhrase.Label> findLabelsForTokenPhrase(
      RaptatTokenPhrase found_token_phrase, IRaptatTokenText string_eval,
      FSMFeatureLabeler fsm_feature_labeler) {
    // /*
    // * Leaving in as place holder, doesn't actually do anything right now,
    // * but may be a useful mechanism later.
    // */
    // RaptatTokenFSM potential_phrase = RaptatToken.getStringFromTokens(
    // foundTokenPhrase, string_eval, x -> {
    // return x;
    // } );

    RaptatTokenFSM potential_phrase = new RaptatTokenFSM(found_token_phrase.get_text());

    if (fsm_feature_labeler != null) {
      try {
        fsm_feature_labeler.identify(potential_phrase, MatchType.Exact);
      } catch (FSMElementException e) {
        e.printStackTrace();
        System.out.println("Error attempting to identify: " + e.getMessage());
      }
    }

    List<RaptatTokenPhrase.Label> labels_found = new ArrayList<>();

    Map<String, FSMFeatureData> map = potential_phrase.get_fsm_feature_map();
    for (String key : map.keySet()) {
      FSMFeatureData label = map.get(key);
      if (label.get_feature_value() != MatchPattern.None.get_match_pattern_name()) {
        /*
         * e.g., IndexType, ABI
         */
        RaptatTokenPhrase.Label found_label =
            new RaptatTokenPhrase.Label(label.get_feature_type(), label.get_feature_value());
        labels_found.add(found_label);
      }
    }

    return labels_found;
  }


  /**
   * @param sentence
   * @param index_value_fsm_defn
   * @param fsm_feature_labeler
   * @param index_range_fsm_defn -- Element to ignore (e.g., 1.0-1.5 or 1.0 to 1.5)
   * @return
   */
  private List<RaptatTokenPhrase> getTokenPhrasesFromAnnotatedPhrase(AnnotatedPhrase sentence,
      FSMFeatureLabeler fsm_feature_labeler, FSMDefinition index_value_fsm_defn,
      FSMDefinition index_range_fsm_defn) {

    List<RaptatToken> tokens = sentence.getRawTokens();

    RaptatTokenPhrases resultPhrases = new RaptatTokenPhrases();

    /**
     * Locate the index values in the sentence
     */

    TreeMap<Integer, MatchDetails<RaptatTokenFSM>> start_offset_to_match = new TreeMap<>();

    if (index_value_fsm_defn != null) {
      List<MatchPattern> elements = index_value_fsm_defn.get_match_patterns("INDEX");
      List<MatchPattern> range_elements =
          index_range_fsm_defn.get_match_patterns("Index_Range_Pattern");

      /**
       * DAX - 5/16/19 - Putting in hard check to ensure that we don't include ranges
       */
      for (MatchPattern element : elements) {
        RaptatTokenFSM fsm_token = new RaptatTokenFSM(sentence.getPhraseStringUnprocessed());
        IRaptatTokenText token_text_callback = x -> x.getTokenStringAugmented();
        MatchDetails<RaptatTokenFSM> match_details =
            element.matches(fsm_token, token_text_callback, MatchType.Contains);
        if (match_details != null) {
          // String found_text = match_details.get_found_text();
          // String token_string = token_text_callback.get( fsm_token
          // );

          for (MatchPattern range_element : range_elements) {
            MatchDetails<RaptatTokenFSM> range_match_details =
                range_element.matches(fsm_token, token_text_callback, MatchType.Contains);
            if (range_match_details != null) {
              match_details = null;
              break;
            }
          }

          if (match_details != null) {
            start_offset_to_match.put(match_details.get_start(), match_details);
            break;
          }
        }

      }
    }

    /**
     * Loop through and attempt to detect minimal covering tokens that constitute a token phrase,
     * resulting in 1) a series of matched labels, 2) an IndexValue label, or 3) an "unassigned"
     * label.
     */
    for (int startIndex = 0; startIndex < tokens.size(); startIndex++) {

      int endIndex = tokens.size();
      if (startIndex + Constants.MAX_TOKENS_DEFAULT < endIndex) {
        endIndex = startIndex + Constants.MAX_TOKENS_DEFAULT;
      }

      while (endIndex > startIndex) {
        List<RaptatToken> candidates = tokens.subList(startIndex, endIndex);
        RaptatTokenPhrase foundTokenPhrase = new RaptatTokenPhrase(candidates);

        /*
         * x -> x.getTokenStringUnprocessed() is a lambda method defined in IRaptatTokenStringEval
         * that permits specification of which string method to use on the RapTAT token. Included
         * here to demonstrate how to generalize approach
         */
        IRaptatTokenText get_text = x -> x.getTokenStringAugmented();
        List<RaptatTokenPhrase.Label> labels =
            findLabelsForTokenPhrase(foundTokenPhrase, get_text, fsm_feature_labeler);
        if (labels != null && labels.isEmpty() == false) {
          add_matched_labels(foundTokenPhrase, labels);
          resultPhrases.add(foundTokenPhrase);
        } else {
          /*
           * Check to see if the current "token", e.g. what remains of the sentence, has a start
           * offset the same as in the list of index matches. If so, turn this into a phrase
           */
          RaptatToken raptatToken = candidates.get(0);
          int sentence_start_offset = Integer.parseInt(sentence.getRawTokensStartOff());
          int start_offset = raptatToken.get_start_offset_as_int() - sentence_start_offset;

          if (start_offset_to_match.containsKey(start_offset)) {
            MatchDetails<RaptatTokenFSM> matchDetails = start_offset_to_match.get(start_offset);
            RaptatToken current_candidate = candidates.get(candidates.size() - 1);
            int end_offset = current_candidate.get_end_offset_as_int() - sentence_start_offset;
            if (matchDetails.get_end() == end_offset) {
              add_index_value_label(foundTokenPhrase);
              resultPhrases.add(foundTokenPhrase);
            }

          }
        }

        endIndex--;
      }

      if (endIndex == startIndex) {
        List<RaptatToken> token_sublist = tokens.subList(startIndex, startIndex + 1);
        RaptatTokenPhrase foundTokenPhrase = new RaptatTokenPhrase(token_sublist);
        add_unassigned_label(foundTokenPhrase);
        resultPhrases.add(foundTokenPhrase);

      }
    }

    return new ArrayList<RaptatTokenPhrase>(resultPhrases.get_phrases());
  }

}
