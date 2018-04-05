package com.seikomi.grooveberry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.seikomi.grooveberry.database.ConnectionMySQLDatabase;

/**
 * This abstract class gives the basic CRUD operations to implements in each DAO
 * for the object {@code T}.
 *
 * @param <T>
 *            the type of the object associated with the DAO
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public abstract class DAO<T> {
	protected static final String SQL_TRACE_FORMAT = "SQL: {}";
	
	/** The H2 database connector. */
	protected static Connection connection = ConnectionMySQLDatabase.getConnection();

	/**
	 * Finds the object {@code T} in the database with his id.
	 * 
	 * @param id
	 *            the id of the object to find
	 * @return the found object or {@code null} if he doesn't exist
	 */
	public abstract T find(long id);

	/**
	 * Creates the object {@code T} in the database, that method create all
	 * associated object with the object {@code T}.
	 * 
	 * @param object
	 *            the object to create
	 * @return the new object or {@code null} if he cannot be create
	 */
	public abstract T create(T object);

	/**
	 * Updates the object {@code T} in the database, that method updates all
	 * associated object with the object {@code T}.
	 * 
	 * @param object
	 *            the object to update
	 * @return the updated object or {@code null} if he cannot be update
	 */
	public abstract T update(T object);

	/**
	 * Delete the object {@code T} in the database, that method delete all
	 * associated object with the object {@code T}.
	 * 
	 * @param object
	 *            the object to delete
	 * @return the deleted object or {@code null} if he cannot be delete
	 */
	public abstract void delete(T object);
	
	/**
	 * Find the last key id created by the prepared statement
	 * 
	 * @param preparedStatement
	 *            the prepare statement
	 * @return the last key id created by the prepared statement or {@code 0} if there is none 
	 * @throws SQLException
	 *             if a database access error occurs or this method is called on a
	 *             closed result set
	 */
	protected static long findLastIdCreated(PreparedStatement preparedStatement) throws SQLException {
		try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
			if (generatedKeys.first()) {
				return generatedKeys.getLong(1);
			}
		}
		return 0;
	}
}
