package com.seikomi.grooveberry.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionH2Database.class);

	private static Connection connection;

	private ConnectionH2Database() {
		// Hide the public constructor
	}

	public static Connection getConnection(String url, String user, String password) {
		try {
			Class.forName("org.h2.Driver");
			LOGGER.trace("H2 Driver found");
		} catch (ClassNotFoundException e) {
			LOGGER.error("H2 Driver not found", e);
		}

		try {
			connection = DriverManager.getConnection(url, user, password);
			LOGGER.debug("Connection with the H2 database established : {}", url);
		} catch (SQLException e) {
			LOGGER.error("Unable to establish connection with the H2 database", e);
		}
		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.error("A database access error occurs", e);
		}
	}

	public static Connection getConnection() {
		return connection;
	}

}
