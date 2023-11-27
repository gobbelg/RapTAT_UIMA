/*
 *
 */
package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.connection;

import java.text.MessageFormat;
import org.apache.log4j.Logger;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.config.IDataSourceConfiguration;
import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.interfaces.connection.IDatabaseConnection;

/**
 * The Class DataSourceConnection.
 */
public abstract class DataSourceConnection implements IDatabaseConnection {

  /**
   * The data source configuration
   */
  private IDataSourceConfiguration config;
  private Logger logger;

  /**
   * Instantiates a new data source connection.
   *
   * @param config configuration for this connection
   * @param auditAdapter
   * @param logger
   */
  protected DataSourceConnection(IDataSourceConfiguration config, Logger logger) {
    if (config == null) {
      getLogger().error(
          MessageFormat.format("Configuration for {0} not set", this.getClass().getSimpleName()));
    }

    this.config = config;
    this.logger = logger;
  }


  /**
   * Gets the configuration
   *
   * @return the configuration
   */
  protected IDataSourceConfiguration getConfig() {
    return this.config;
  }


  /**
   * Gets the logger.
   *
   * @return the logger
   */
  protected Logger getLogger() {
    return this.logger;
  }

}
