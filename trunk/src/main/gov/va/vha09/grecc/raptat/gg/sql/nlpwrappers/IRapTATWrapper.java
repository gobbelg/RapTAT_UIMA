package src.main.gov.va.vha09.grecc.raptat.gg.sql.nlpwrappers;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatResult;

public interface IRapTATWrapper {

  /**
   * Process document with RapTAT
   *
   * @param item the item
   * @return the rap TAT document result
   * @throws BaseException
   */
  RaptatResult processItem(RapTATItemToProcess item) throws BaseException;
}
