package com.seikomi.grooveberry.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionH2Database.class);

	private static Connection connection;
	private static Server h2TcpServer;

	private ConnectionH2Database() {
		// Hide the public constructor
	}

	public static Connection getConnection(String url, String user, String password) {
		if (connection == null) {
			try {
				h2TcpServer = Server.createTcpServer("-tcpPort", "9090", "-tcpAllowOthers").start();
				LOGGER.trace("H2 database in-memory TCP server launch");
			} catch (SQLException e) {
				LOGGER.error("Cannot create H2 database TCP server", e);
			}
		}
		
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
			LOGGER.trace("Connection with the H2 database has been stopped");
		} catch (SQLException e) {
			LOGGER.error("A database access error occurs", e);
		}
		
		h2TcpServer.stop();
		LOGGER.trace("H2 database in-memory TCP server has been stopped");
	}

	public static Connection getConnection() {
		return connection;
	}

}
