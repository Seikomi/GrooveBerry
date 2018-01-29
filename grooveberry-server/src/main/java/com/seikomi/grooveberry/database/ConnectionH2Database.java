package com.seikomi.grooveberry.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionH2Database.class);

	private static Connection instance;

	private ConnectionH2Database() {
		// Hide the public constructor
	}

	public static Connection getInstance() {
		if (instance == null) {
			try {
				Class.forName("org.h2.Driver");
				LOGGER.trace("H2 Driver found");
			} catch (ClassNotFoundException e) {
				LOGGER.error("H2 Driver not found", e);
			}

			try {
				Connection connection = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "");
				LOGGER.debug("Connection with the H2 database established");
				instance = connection;
			} catch (SQLException e) {
				LOGGER.error("Unable to establish connection with the H2 database", e);
			}
		}
		return instance;
	}
	
	public static void closeConnection() {
		try {
			instance.rollback();
			instance.close();
		} catch (SQLException e) {
			LOGGER.error("A database access error occurs", e);
		}
	}

}
