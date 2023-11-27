/*
 *
 */
package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.impl;

import java.sql.Connection;

import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;

/**
 * The Class SQLDriverWrapper.
 */
public class SQLDriverWrapper extends SQLDriver
{

	/** The connection. */
	private Connection connection;

	/**
	 * Instantiates a new SQL driver wrapper.
	 *
	 * @param connection
	 *            the connection
	 * @throws SQLDriverException
	 *             the SQL driver exception
	 */
	public SQLDriverWrapper(Connection connection) throws SQLDriverException
	{
		super( null, null, null, null );
		this.connection = connection;
	}


	/**
	 * Connect.
	 *
	 * @return the connection
	 */
	@Override
	public Connection connect()
	{
		return this.connection;
	}

}
