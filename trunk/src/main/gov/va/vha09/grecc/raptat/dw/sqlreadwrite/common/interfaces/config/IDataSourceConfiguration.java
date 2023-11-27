package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.AuditLogId;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.TableDefinition;

// TODO: Auto-generated Javadoc
/**
 * The Interface IDataSourceConfiguration.
 */
public interface IDataSourceConfiguration {

  /**
   * Gets the application app; EHOST("eHOST"), KNOWTATOR("Knowtator").
   *
   * @return the application app
   */
  String getApplicationApp();


  /**
   * Gets the audit log definition.
   *
   * @return the audit log definition
   */
  TableDefinition getAuditLogDefinition();


  /**
   * Gets the audit update query.
   *
   * @param auditLogId
   *
   * @return the audit update query
   */
  String getAuditUpdateQuery(AuditLogId auditLogId);


  /**
   * Gets the correct offsets.
   *
   * @return the correct offsets
   */
  boolean getCorrectOffsets();


  /**
   * Gets the database.
   *
   * @return the database
   */
  String getDatabase();


  /**
   * Gets the db driver, e.g. com.microsoft.sqlserver.jdbc.SQLServerDriver
   *
   * @return the db driver
   */
  String getDbDriverString();


  /**
   * Gets the SQL string to get the document count of the reader.
   *
   * @return the document count query
   */
  String getDocumentCountQuery();


  /**
   * Get the target schema.
   *
   * @return the export schema
   */
  String getSqlExportSchemaName();


  /**
   * Gets the absolute file path to the concepts to be annotated with the supplied solution file
   *
   * @return The path to a text file, which should reserve the first line for a header and each
   *         subsequent line one of the included concepts for annotation
   */
  String getIncludedConceptsFilePath();


  /**
   * Gets the insert phrase string.
   *
   * @return the insert phrase string
   */
  boolean getInsertPhraseString();


  /**
   * Gets the SQL string to get the next document from the reader.
   *
   * @return SQL string for next document query
   */
  String getNextDocumentQuery();


  /**
   * Gets the password for the database connection; can be empty of trusted connection.
   *
   * @return the password
   */
  String getPassword();


  /**
   * Gets the port for the database connection.
   *
   * @return the port
   */
  String getPort();


  /**
   * Get the definition of the processing queue.
   *
   * @return the processing queue table definition
   */
  TableDefinition getProcessingQueueTableDefinition();


  /**
   * Gets the schema file.
   *
   * @return the schema file
   */
  String getAnnotationSchemaFilePath();


  /**
   * Gets the server for the database connection.
   *
   * @return the server
   */
  String getServer();


  /**
   * Gets the absolute file path to the trained RapTAT solution file used for annotation
   *
   * @return The path to the RapTAT solution file
   */
  String getSolutionFilePath();


  /**
   * Gets the source id.
   *
   * @return the source id
   */
  String getSourceId();

  /**
   * Gets the user name for the database connection; can be empty of trusted connection.
   *
   * @return the user name
   */
  String getUserName();

  /**
   * Checks if is trusted connection.
   *
   * @return true, if is trusted connection
   */
  boolean isTrustedConnection();

}
