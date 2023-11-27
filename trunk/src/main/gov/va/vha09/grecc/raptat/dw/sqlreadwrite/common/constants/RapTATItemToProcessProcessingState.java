package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants;

import java.sql.Timestamp;

/**
 * The processing state of the RapTATItemToProcess
 */
public enum RapTATItemToProcessProcessingState {

  /** The Success. */
  Success("SUCCESS"),

  /** The Error. */
  Error("ERROR");

  /** The state. */
  private String state;

  /**
   * Instantiates a new processing state.
   *
   * @param state the state
   */
  RapTATItemToProcessProcessingState(String state) {
    this.state = state;
  }


  /**
   * Gets the state string
   *
   * @return the string
   */
  public String get() {
    return this.state;
  }


  public Timestamp getEndDate() {
    // TODO Auto-generated method stub
    return null;
  }


  public Timestamp getStartDate() {
    // TODO Auto-generated method stub
    return null;
  }


  public void rollbackRequest() {
    // TODO Auto-generated method stub

  }


  public void setProcessingStateError() {
    // TODO Auto-generated method stub

  }

}
