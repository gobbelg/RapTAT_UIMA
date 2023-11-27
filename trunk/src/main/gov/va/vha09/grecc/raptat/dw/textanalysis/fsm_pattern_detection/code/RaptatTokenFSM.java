package src.main.gov.va.vha09.grecc.raptat.dw.textanalysis.fsm_pattern_detection.code;

import java.util.HashMap;
import java.util.Map;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;


/**
 * The Class ABIRaptatTokenExtended.
 */
public class RaptatTokenFSM extends RaptatToken {

  /**
   *
   */
  private static final long serialVersionUID = -4244625928869745364L;


  /** The features. */
  Map<String, FSMFeatureData> __features = new HashMap<>();

  /**
   * Instantiates a new ABI raptat token extended.
   *
   * @param input_string the input string
   */
  public RaptatTokenFSM(String input_string) {
    this(input_string, 0, input_string.length());
  }

  /**
   * Instantiates a new ABI raptat token extended.
   *
   * @param input_string the input string
   * @param startOffset the start offset
   * @param endOffset the end offset
   */
  public RaptatTokenFSM(String input_string, int startOffset, int endOffset) {
    super(input_string, startOffset, endOffset);
  }


  /**
   * Adds the feature.
   *
   * @param feature_type the feature type
   * @param feature_value the feature value
   */
  public void add_feature(String feature_type, String feature_value) {
    this.add_feature(feature_type, feature_value, null);
  }


  public String get_column_label_index_type() {
    return get_feature("column_label_index_type");
  }


  public String get_column_label_laterality() {
    return get_feature("column_label_laterality");
  }


  /**
   * Gets the fsm feature map.
   *
   * @return the fsm feature map
   */
  public Map<String, FSMFeatureData> get_fsm_feature_map() {
    return this.__features;
  }


  public String get_line_label_index_type() {
    return get_feature("line_label_index_type");
  }


  public String get_line_label_laterality() {
    return get_feature("line_label_laterality");
  }


  public String get_upstream_label_body_part() {
    return get_feature("upstream_label_body_part");
  }


  public String get_upstream_label_index_type() {
    return get_feature("upstream_label_index_type");
  }


  public String get_upstream_label_laterality() {
    return get_feature("upstream_label_laterality");
  }


  public void set_column_label_laterality(String _column_label_laterality) {
    this.add_feature("column_label_laterality", _column_label_laterality);
  }


  public void set_line_label_laterality(String line_label_laterality) {
    this.add_feature("line_label_laterality", line_label_laterality);
  }


  public void set_upstream_label_body_part(String upstream_label_body_part) {
    this.add_feature("upstream_label_body_part", upstream_label_body_part);
  }


  public void set_upstream_label_index_type(String upstream_label_index_type) {
    this.add_feature("upstream_label_index_type", upstream_label_index_type);
  }


  public void set_upstream_label_laterality(String upstream_label_laterality) {
    this.add_feature("upstream_label_laterality", upstream_label_laterality);
  }


  private void add_feature(String feature_type, String feature_value, String group_id) {
    if (this.__features.containsKey(feature_type)) {
      throw new IllegalArgumentException(
          "Feature " + feature_type + " already labeled for this token");
    }
    FSMFeatureData feature_data = new FSMFeatureData(feature_type, feature_value);
    this.__features.put(feature_type, feature_data);
  }


  private String get_feature(String string) {

    return null;
  }


  private void set_column_label_index_type(String index_type) {
    this.add_feature("column_label_index_type", index_type);
  }


  private void set_row_label_index_type(String index_type) {
    this.add_feature("line_label_index_type", index_type);
  }


  private void set_row_label_laterality(String laterality) {
    this.add_feature("line_label_laterality", laterality);
  }

}
