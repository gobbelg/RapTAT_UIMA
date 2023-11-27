package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

/**
 * The Class DriverBase.
 */
public abstract class DriverBase {

  /** The driver. */
  private ISQLDriver driver;

  /**
   * Instantiates a new driver base.
   *
   * @param driver the driver
   */
  public DriverBase(ISQLDriver driver) {
    set_driver(driver);
  }


  /**
   * Gets the driver.
   *
   * @return the driver
   */
  public ISQLDriver getDriver() {
    return this.driver;
  }


  /**
   * Sets the driver.
   *
   * @param driver the new driver
   */
  private void set_driver(ISQLDriver driver) {
    this.driver = driver;
  }

}
