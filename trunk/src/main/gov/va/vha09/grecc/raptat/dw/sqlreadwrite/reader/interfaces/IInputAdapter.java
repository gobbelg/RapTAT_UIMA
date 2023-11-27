package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.reader.interfaces;

import java.util.Iterator;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.ItemToProcess;

/**
 * Interface to describe the reader methods for pulling documents from a MSSQL database
 */
public interface IInputAdapter extends Iterator<ItemToProcess> {

  void halt();

}
