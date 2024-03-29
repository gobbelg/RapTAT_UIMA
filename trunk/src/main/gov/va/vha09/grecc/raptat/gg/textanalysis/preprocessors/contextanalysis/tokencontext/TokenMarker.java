package src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptPhraseModifier;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext.general.GeneralMarks;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext.general.GeneralBackwardLogicAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.preprocessors.contextanalysis.tokencontext.general.GeneralForwardLogicAnalyzer;

/**
 *
 * Instances of this class are a field within the TokenContextAnalyzer class instance. Members of
 * this class generate the tags the indicate contextual information about a token, such as whether
 * it is negated. It does this based on the boundary marks generated by an instance of the
 * BoundaryMarker class, which is another field within a TokenContextAnalyer class.
 *
 * @author Glenn Gobbel - Jul 17, 2014
 *
 */
public abstract class TokenMarker implements Serializable {
  protected static final long serialVersionUID = 8741921004650804483L;
  private static Logger logger = Logger.getLogger(TokenMarker.class);
  public int window;
  protected ForwardLogicAnalyzer forwardAnalyzer;
  protected BackwardLogicAnalyzer backwardAnalyzer;


  public TokenMarker() {}


  /**
   * Method Description - Generates an array of tags for the tokenStrings array parameter based on
   * the provided tokenBoundaryMarks.
   *
   * @param tokenStrings
   * @param tokenBoundaryMarks
   * @param tokenContextTag
   * @return TokenMarker String[]
   *
   * @author Glenn Gobbel - Jul 17, 2014
   */
  public String[] getContext(String[] tokenStrings, Mark[] tokenBoundaryMarks,
      String tokenContextTag) {
    String[] results = new String[tokenStrings.length];
    Arrays.fill(results, "");
    this.forwardAnalyzer.reset(tokenBoundaryMarks);

    for (int i = 0; i < tokenStrings.length; i++) {
      this.forwardAnalyzer.updateStatus();
      if (this.forwardAnalyzer.isMarking) {
        results[i] = tokenContextTag;
      }
    }

    int tokenIndex;
    if ((tokenIndex = findLastWordString(tokenStrings)) >= 0) {
      this.backwardAnalyzer.reset(tokenIndex, results);

      while (this.backwardAnalyzer.updateStatus() >= 0) {
        --tokenIndex;

        /*
         * Only mark if the location has not already been marked i.e., do not change existing mark
         */
        if (results[tokenIndex].equals("") && this.backwardAnalyzer.isMarking) {
          results[tokenIndex] = tokenContextTag;
        }
      }
    }
    return results;
  }


  /**
   * Determines whether the strings in an array, tokenStrings, are facilitated or blocked based on
   * the settings of tokenBoundaryMarks. The results for both facilitation and blocking are returned
   * in a part of String arrays.
   *
   * @param tokenStrings
   * @param facilitatorBlockerMarks
   * @param windows
   * @return
   */
  public RaptatPair<String[], String[]> getGeneralContext(String[] tokenStrings,
      Mark[] facilitatorBlockerMarks, ConceptPhraseModifier[] windows) {
    /*
     * It would probably be faster to make resultsFacilitator[] and resultsBlocker[] be arrays of
     * booleans, initially set to false. A setting of 'true' would indicate they blocked or
     * facilitated. However, this is difficult to do given the legacy code.
     */
    String[] facilitatedTags = new String[tokenStrings.length];
    String[] blockedTags = new String[tokenStrings.length];
    Arrays.fill(facilitatedTags, "");
    Arrays.fill(blockedTags, "");

    GeneralForwardLogicAnalyzer forwardAnalyzer = new GeneralForwardLogicAnalyzer(windows);

    forwardAnalyzer.reset(facilitatorBlockerMarks);
    for (int i = 0; i < tokenStrings.length; i++) {
      forwardAnalyzer.updateFacilitatingStatus();

      if (forwardAnalyzer.isMarking) {
        facilitatedTags[i] = Constants.FACILITATED_TAG;
      }
    }

    forwardAnalyzer.reset(facilitatorBlockerMarks);
    for (int i = 0; i < tokenStrings.length; i++) {
      forwardAnalyzer.updateBlockingStatus();

      if (forwardAnalyzer.isMarking) {
        blockedTags[i] = Constants.BLOCKED_TAG;
      }
    }

    int tokenIndex;
    GeneralBackwardLogicAnalyzer backwardAnalyzer = new GeneralBackwardLogicAnalyzer(windows);

    if ((tokenIndex = findLastWordString(tokenStrings)) >= 0) {
      backwardAnalyzer.reset(tokenIndex, facilitatedTags);

      // changes made to account the 0th token
      for (; tokenIndex > -1; tokenIndex--) {
        backwardAnalyzer.updateFacilitatingStatus();

        if (backwardAnalyzer.isMarking) {
          facilitatedTags[tokenIndex] = Constants.FACILITATED_TAG;
        }
      }
    }

    if ((tokenIndex = findLastWordString(tokenStrings)) >= 0) {
      backwardAnalyzer.reset(tokenIndex, facilitatedTags);

      for (; tokenIndex > -1; tokenIndex--) {
        backwardAnalyzer.updateBlockingStatus();

        if (backwardAnalyzer.isMarking) {
          blockedTags[tokenIndex] = Constants.BLOCKED_TAG;
        }
      }
    }

    return new RaptatPair<>(facilitatedTags, blockedTags);
  }


  public String[] markConcept(String[] tokenStrings, Mark[] tokenBoundaryMarks,
      String tokenContextTag, ConceptPhrase[] cncptPhrases, String[] resultsFacilitator,
      String[] resultsBlocker) {
    String[] result = new String[tokenStrings.length];
    Arrays.fill(result, "");
    ConceptPhrase cPhrase;

    for (int i = 0; i < tokenStrings.length; i++) {
      if (tokenBoundaryMarks[i] == GeneralMarks.ContextMark.CONCEPTWORD) {
        cPhrase = cncptPhrases[i];

        if (resultsBlocker[i].equals("BLOCK")) {
          if (!cPhrase.blockable) {
            result[i] = tokenContextTag;
          } else {
            result[i] = Constants.BLOCKED_TAG;
          }
        } else if (resultsFacilitator[i].equals(Constants.FACILITATED_TAG)) {
          result[i] = tokenContextTag;
        } else if (!cPhrase.requiresFacilitation) {
          result[i] = tokenContextTag;
        }
      } else if (resultsBlocker[i].equals("BLOCK")) {
        result[i] = Constants.BLOCKED_TAG;
      } else if (resultsFacilitator[i].equals(Constants.FACILITATED_TAG)) {
        result[i] = Constants.FACILITATED_TAG;
      }
    }

    return result;
  }


  protected int findLastWordString(String[] tokenStrings) {
    int curIndex = tokenStrings.length - 1;

    while (curIndex >= 0 && !tokenStrings[curIndex].matches("[A-Za-z0-9]+")) {
      --curIndex;
    }

    return curIndex;
  }


  protected int findLastWordToken(List<RaptatToken> sentenceTokens) {
    int lastIndex = sentenceTokens.size() - 1;
    ListIterator<RaptatToken> reverseIterator = sentenceTokens.listIterator(lastIndex);
    String testString = "";

    while (reverseIterator.hasPrevious() && !testString.matches("[A-Za-z0-9]+")) {
      --lastIndex;
      testString = reverseIterator.previous().getTokenStringPreprocessed();
    }
    return lastIndex;
  }

}
