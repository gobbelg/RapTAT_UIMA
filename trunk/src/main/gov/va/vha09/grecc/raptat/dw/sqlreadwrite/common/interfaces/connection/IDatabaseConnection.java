package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection;

import java.sql.Connection;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.exception.DataSourceException;

/**
 * Interface to describe connection to MSSQL DB.
 *
 * @author westerd
 */
public interface IDatabaseConnection {

  /**
   * Connect to the database
   *
   * @return the boolean
   * @throws DataSourceException
   */
  Connection connect() throws DataSourceException;


  /**
   * Gets the data source url in order to create a JDBC connection
   *
   * @return the data source url
   */
  String getDataSourceUrl();


  /**
   * Checks if there exists a connection to the database.
   *
   * @return the boolean
   */
  Boolean isConnected();

}
