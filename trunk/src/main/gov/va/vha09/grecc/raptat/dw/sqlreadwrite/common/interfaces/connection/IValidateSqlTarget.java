package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants.SqlTargetState;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;

/**
 *
 * Interface to describe methods on how to validate a SQL target is available and that the expected
 * schema exists
 *
 * @author westerd
 *
 */
public interface IValidateSqlTarget {

  /**
   * Call to check if the MSSQL target for this implementation is valid.
   *
   * @return true, if the MSSQL target is valid
   * @throws DataSourceException
   */
  SqlTargetState isSqlTargetValid() throws DataSourceException;

}
