package com.seikomi.grooveberry.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestH2Database.class);
	
	public static void main(String[] args) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.error("H2 Driver not found", e);
		}
		
		try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:db", "sa", "")){
			// add application code here
		} catch (SQLException e) {
			LOGGER.error("Unable to establish connection with the H", e);
		} 

	}
}
