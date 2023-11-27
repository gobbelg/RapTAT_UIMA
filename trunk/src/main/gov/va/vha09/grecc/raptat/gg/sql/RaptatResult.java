/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.sql;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.RapTATItemProcessingState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;

/**
 * @author VHATVHGOBBEG
 *
 */
public abstract class RaptatResult {

  /** The result. */
  public RapTATItemProcessingState result;

  private RapTATItemToProcess raptatItemToProcess;

  public RaptatResult(RapTATItemToProcess item) {
    result = RapTATItemProcessingState.Success;
    raptatItemToProcess = item;
  }

  public RapTATItemToProcess getItem() {
    return raptatItemToProcess;
  }

}
