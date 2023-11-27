package src.main.gov.va.vha09.grecc.raptat.gg.core.annotation;

import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotatorConfiguration.
 */
public final class AnnotatorConfiguration {

  /**
   * The Enum From.
   */
  public enum From {

    /** The Database. */
    Database,

    /** The File. */
    File
  }

  /**
   * Gets the db config file path.
   *
   * @return the db config file path
   */
  public static String getDbConfigFilePath() {
    return Constants.DB_CONFIG_FILE_PATH;
  }


  /**
   * Gets the schema file path.
   *
   * @return the schema file path
   */
  public static String getSchemaFilePath() {
    return Constants.SCHEMA_FILE_PATH;
  }


  /**
   * Checks if is correct offsets.
   *
   * @return true, if is correct offsets
   */
  public static boolean isCorrectOffsets() {
    return true;
  }


  /**
   * Checks if is insert phrase strings.
   *
   * @return true, if is insert phrase strings
   */
  public static boolean isInsertPhraseStrings() {
    return true;
  }


  /**
   * Read from.
   *
   * @return the from
   */
  public static From read_from() {
    return From.Database;
  }


  /**
   * Use sql exporter.
   *
   * @return true, if successful
   */
  public static boolean useSqlExporter() {
    return Constants.DIRECT_DATABASE_WRITE;
  }
}
