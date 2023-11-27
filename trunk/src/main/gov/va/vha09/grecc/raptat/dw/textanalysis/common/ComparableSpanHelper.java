package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.common;

import src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.RaptatTokenPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;


/**
 * The Class ComparableSpanHelper.
 */
public class ComparableSpanHelper {

  /**
   * Compare end offsets.
   *
   * @param lhs the lhs
   * @param rhs the rhs
   * @return the int
   */
  public static int compare_end_offsets(RaptatTokenPhrase lhs, RaptatTokenPhrase rhs) {
    return lhs.get_end_offset_as_int() - rhs.get_end_offset_as_int();
  }

  /**
   * Not 100% how I want this defined, so for now it accepts start and end offset in principle, but
   * it only calculates using start.
   *
   * @param lhs
   * @param rhs
   * @return
   */
  public static int compare_for_inclusive_intersection(RaptatTokenPhrase lhs,
      RaptatTokenPhrase rhs) {
    int start_offset_comparison = compare_start_offsets(lhs, rhs);
    int end_offset_comparison = compare_end_offsets(lhs, rhs);
    int rv = 0;

    if (start_offset_comparison > 0 && end_offset_comparison < 0) {
      rv = 0;
    } else if (start_offset_comparison < 0) {
      rv = -1;
    } else if (end_offset_comparison > 0) {
      rv = 1;
    }

    return rv;
  }

  /**
   * Compare lines.
   *
   * @param lhs the lhs
   * @param rhs the rhs
   * @return the int
   */
  public static int compare_lines(RaptatTokenPhrase lhs, RaptatTokenPhrase rhs) {
    return lhs.get_line_index() - rhs.get_line_index();
  }

  /**
   * Compare start offsets.
   *
   * @param lhs the lhs
   * @param rhs the rhs
   * @return the int
   */
  public static int compare_start_offsets(IComparableSpan lhs, IComparableSpan rhs) {
    return lhs.get_start_offset_as_int() - rhs.get_start_offset_as_int();
  }

  public static int compare_start_offsets(RaptatToken lhs, RaptatToken rhs) {
    return lhs.get_start_offset_as_int() - rhs.get_start_offset_as_int();
  }
}
