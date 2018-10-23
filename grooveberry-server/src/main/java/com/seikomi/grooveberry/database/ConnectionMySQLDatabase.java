//package com.seikomi.grooveberry.database;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ConnectionMySQLDatabase {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionMySQLDatabase.class);
//
//	private static Connection connection;
//
//	private ConnectionMySQLDatabase() {
//		// Hide the public constructor
//	}
//
//	public static Connection getConnection(String url, String user, String password) {
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			LOGGER.trace("MySQL Driver found");
//		} catch (ClassNotFoundException e) {
//			LOGGER.error("MySQL Driver not found", e);
//		}
//
//		try {
//			connection = DriverManager.getConnection(url, user, password);
//			LOGGER.debug("Connection with the MySQL database established : {}", url);
//		} catch (SQLException e) {
//			LOGGER.error("Unable to establish connection with the MySQL database", e);
//		}
//		return connection;
//	}
//
//	public static void closeConnection() {
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			LOGGER.error("A database access error occurs", e);
//		}
//	}
//
//	public static Connection getConnection() {
//		return connection;
//	}
//
//}
