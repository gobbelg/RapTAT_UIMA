package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Interface ISQLDriver.
 */
public interface ISQLDriver {

  /**
   * Execute insert.
   *
   * @param cols the cols
   * @param line the line
   * @param schema the schema
   * @param string the string
   * @throws NoConnectionException the no connection exception
   */
  void executeInsert(String cols, List<String> line, String schema, String string)
      throws NoConnectionException;


  /**
   * Execute select.
   *
   * @param query the query
   * @return the result set
   * @throws NoConnectionException the no connection exception
   */
  ResultSet executeSelect(String query) throws NoConnectionException;


  Connection getConnection();


  /**
   * Gets the last row attribute ID.
   *
   * @param dbSchemaName the schema
   * @param string the string
   * @return the last row attribute ID
   * @throws NoConnectionException the no connection exception
   */
  String getLastRowAttributeID(String dbSchemaName, String string) throws NoConnectionException;


  /**
   * Gets the last row ID.
   *
   * @param dbSchemaName the schema
   * @param string the string
   * @return the last row ID
   * @throws NoConnectionException the no connection exception
   */
  long getLastRowID(String dbSchemaName, String string) throws NoConnectionException;


  /**
   * Prepare statement.
   *
   * @param query the query
   * @return the prepared statement
   */
  PreparedStatement prepareStatement(String query) throws SQLException;

}
