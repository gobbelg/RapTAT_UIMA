package src.main.gov.va.vha09.grecc.raptat.gg.sql;

/**
 * The Class DbConnectionObject.
 */
/*
 * Implement DbConnectionObject to store and/or retrieve the information for connecting to the
 * database with annotations of interest
 */
public class DbConnectionObject {

  /** The database. */
  final String database;

  /** The password. */
  final String password;

  /** The server. */
  final String server;

  /** The username. */
  final String username;


  /**
   * Instantiates a new db connection object.
   *
   * @param server the server
   * @param database the database
   * @param username the username
   * @param password the password
   */
  public DbConnectionObject(String server, String database, String username, String password) {
    super();
    this.server = server;
    this.database = database;
    this.username = username;
    this.password = password;
  }

  /**
   * Gets the database.
   *
   * @return the database
   */
  public String get_database() {
    return this.database;
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public String get_password() {
    return this.password;
  }

  /**
   * Gets the server.
   *
   * @return the server
   */
  public String get_server() {
    return this.server;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String get_username() {
    return this.username;
  }

}
