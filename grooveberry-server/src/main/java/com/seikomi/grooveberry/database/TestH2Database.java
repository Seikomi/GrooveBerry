package com.seikomi.grooveberry.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.bo.SongTag;
import com.seikomi.grooveberry.dao.SongDAO;

public class TestH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestH2Database.class);
	
	public static void main(String[] args) throws SQLException {
		Statement statement = ConnectionH2Database.getInstance().createStatement();
		statement.executeUpdate("RUNSCRIPT FROM '" + new File("C:/Users/Nicolas/git/GrooveBerry/grooveberry-server/src/main/resources/sql/init.sql") + "'");
		
		SongDAO songDAO = new SongDAO();
		
		Song song = new Song();
		song.setPath("C:\\Users\\Nicolas\\.grooveberry\\library\\Pensées et lucidité v2.mp3");
		Song songCreated = songDAO.create(song);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("        ID          " + songCreated.getSongId());
		stringBuilder.append("\n        PATH        " + songCreated.getPath());
		stringBuilder.append("\n        FILENAME    " + songCreated.getFileName());
		stringBuilder.append("\n        TAG OBJECT  " + songCreated.getSongTag());
		LOGGER.info("Object song created :\n" + stringBuilder);
		
		song = new Song();
		song.setPath("C:\\Users\\Nicolas\\.grooveberry\\library\\Pensées et lucidité v2.mp3");
		song.setSongTag(new SongTag());
		songCreated = songDAO.create(song);
		stringBuilder = new StringBuilder();
		stringBuilder.append("        ID          " + songCreated.getSongId());
		stringBuilder.append("\n        PATH        " + songCreated.getPath());
		stringBuilder.append("\n        FILENAME    " + songCreated.getFileName());
		stringBuilder.append("\n        TAG OBJECT  " + songCreated.getSongTag());
		LOGGER.info("Object song created :\n" + stringBuilder);

	}
}
