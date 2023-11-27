package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;

/**
 * Provides the means to define a series of regular expressions which may be tested against a token
 * for matching.
 */
public class MatchPattern {

  public class MatchDetails<T extends RaptatToken> {
    private Integer __start = null;
    private Integer __end = null;
    private T __original_token = null;
    private String __found_text;

    public MatchDetails(int start, int end) {
      this.__start = start;
      this.__end = end;
    }

    public MatchDetails(Matcher matcher, T token) {
      this(matcher);
      this.__original_token = token;
    }

    protected MatchDetails(Matcher matcher) {
      this.__start = matcher.start();
      this.__end = matcher.end();
      this.__found_text = matcher.group();
    }

    public final Integer get_end() {
      return this.__end;
    }

    public String get_found_text() {
      return this.__found_text;
    }

    public T get_original_token() {
      return this.__original_token;
    }

    public final Integer get_start() {
      return this.__start;
    }

    @Override
    public String toString() {

      return String.format("[%s, %s) - %s", this.__start, this.__end, this.__found_text);
    }

  }

  public enum MatchType {
    Exact, Contains
  }

  /** The Constant None. */
  public static final MatchPattern None;

  static {
    None = new MatchPattern("None");
  }

  /** The match pattern name. */
  private String __match_pattern_name;


  /** The regex patterns. */
  private String[] __regex_pattern_strings;


  private Pattern[] __regex_patterns;


  /**
   * Instantiates a new match pattern.
   *
   * @param match_pattern_name the match pattern name
   * @param regex_patterns the regex patterns
   */
  public MatchPattern(String match_pattern_name, String[] regex_patterns) {
    this(match_pattern_name);
    this.__regex_pattern_strings = regex_patterns;
  }


  /**
   * Instantiates a new match pattern.
   */
  private MatchPattern() {}


  /**
   * Instantiates a new match pattern.
   *
   * @param match_pattern_name the match pattern name
   */
  private MatchPattern(String match_pattern_name) {
    this();
    this.__match_pattern_name = match_pattern_name;
  }

  /**
   * Gets the match pattern name.
   *
   * @return the match pattern name
   */
  public String get_match_pattern_name() {
    return this.__match_pattern_name;
  }

  public <T extends RaptatToken> MatchDetails<T> matches(T token,
      IRaptatTokenText token_text_callback, MatchType match_type) {
    // late-bind compile until needed; will happen once
    if (this.__regex_patterns == null && this.__regex_pattern_strings != null) {
      this.__regex_patterns = new Pattern[this.__regex_pattern_strings.length];
      for (int index = 0; index < this.__regex_pattern_strings.length; index++) {
        String string = this.__regex_pattern_strings[index];
        this.__regex_patterns[index] = Pattern.compile(string);
      }
    }

    MatchDetails<T> found = null;

    if (this.__regex_patterns != null) {
      // iterate through all the possible combinations of definitions for
      // this match
      for (Pattern pattern : this.__regex_patterns) {
        String token_text = token_text_callback.get(token);
        Matcher matcher = pattern.matcher(token_text);
        boolean find = matcher.find();
        if (find) {
          boolean exact_match = matcher.start() == 0
              && matcher.end() == token.get_end_offset_as_int() && match_type == MatchType.Exact;
          boolean partial_match = match_type == MatchType.Contains;
          if (exact_match || partial_match) {
            found = new MatchDetails<T>(matcher, token);
            break;
          }
        }
      }
    }

    return found;
  }


  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return this.__match_pattern_name;
  }

}
