package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.interfaces;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.BaseException;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.RaptatResult;

/**
 * Interface to describe the writer methods for pulling documents from a MSSQL database
 */
public interface IOutputAdapter {

  void writeResult(RaptatResult processedItem) throws BaseException;

}
