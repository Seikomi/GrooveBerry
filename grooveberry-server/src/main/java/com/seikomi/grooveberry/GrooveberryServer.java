package com.seikomi.grooveberry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.commands.Next;
import com.seikomi.grooveberry.commands.Pause;
import com.seikomi.grooveberry.commands.Play;
import com.seikomi.grooveberry.commands.Prev;
import com.seikomi.grooveberry.commands.VolumeDown;
import com.seikomi.grooveberry.commands.VolumeUp;
import com.seikomi.grooveberry.commands.WhatIsTheReadingQueue;
import com.seikomi.grooveberry.commands.WhatIsThisSong;
import com.seikomi.grooveberry.dao.SongDAO;
import com.seikomi.grooveberry.database.ConnectionH2Database;
import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.grooveberry.utils.AudioFileDirectoryScanner;
import com.seikomi.janus.commands.CommandsFactory;
import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.services.Locator;

public class GrooveberryServer extends JanusServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrooveberryServer.class);
	public static final String USER_HOME_PATH = System.getProperty("user.home");
	
	public GrooveberryServer(JanusServerProperties serverProperties) {
		super(serverProperties);
	}

	@Override
	public void start() {
		initServerFiles();
		initDatabase();
		initReadingQueue();

		super.start();
	}	

	@Override
	public void stop() {
		Connection connection = ConnectionH2Database.getConnection();
		try (Statement statement = connection.createStatement()){
			ClassLoader classLoader = getClass().getClassLoader();
			File initDatabaseFile = new File(classLoader.getResource("sql/dropTables.sql").getFile());
			statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
		} catch (SQLException e) {
			LOGGER.error("Unable to build the database", e);
		}
		
		ConnectionH2Database.closeConnection();
		ReadingQueue.getInstance().clearQueue();
		super.stop();
	}

	@Override
	protected void loadContext() {
		CommandsFactory.addCommand(new Play(), "#PLAY", this);
		CommandsFactory.addCommand(new Next(), "#NEXT", this);
		CommandsFactory.addCommand(new Prev(), "#PREV", this);
		CommandsFactory.addCommand(new Pause(), "#PAUSE", this);
		CommandsFactory.addCommand(new VolumeDown(), "#VOLDOWN", this);
		CommandsFactory.addCommand(new VolumeUp(), "#VOLUP", this);
		CommandsFactory.addCommand(new WhatIsTheReadingQueue(), "#LIST", this);
		CommandsFactory.addCommand(new WhatIsThisSong(), "#SONG", this);

		Locator.load(new ReadingQueueService(this));
	}

	/**
	 * Initialize library directory and the .properties file.
	 */
	private void initServerFiles() {
		Path mainDirectoryPath = Paths.get(USER_HOME_PATH + "/.grooveberry/");
		Path serverPropertiesPath = Paths.get(USER_HOME_PATH + "/.grooveberry/grooveberry.properties");
		Path serverLibraryDirectoryPath = Paths.get(USER_HOME_PATH + "/.grooveberry/library/");

		File mainDirectory = mainDirectoryPath.toFile();
		File serverProperties = serverPropertiesPath.toFile();
		File serverLibraryDirectory = serverLibraryDirectoryPath.toFile();
		
		createFile(mainDirectory, true);
		createFile(serverProperties, false);
		createFile(serverLibraryDirectory, true);

	}

	private void createFile(File file, boolean isDirectory) {
		if (!file.exists()) {
			try {
				boolean isCreate = isDirectory ? file.mkdir() : file.createNewFile();
				if (!isCreate) {
					throw new IOException();
				} else {
					String message = "Create the {} at {}";
					LOGGER.info(message, (isDirectory ? "directory" : "file"), file.getPath());
				}
			} catch (IOException e) {
				String message = "An I/O exception occurs durring the creation of the Grooveberry server {} : {}";
				LOGGER.error(message, (isDirectory ? "directory" : "file"), file.getPath(), e);
			}
		}
	}
	
	private void initDatabase() {
		String url = getServerProperties().getProperty("database.url");
		String user = getServerProperties().getProperty("database.user");
		String password = getServerProperties().getProperty("database.password");
		Connection connection = ConnectionH2Database.getConnection(url, user, password);
		
		try (Statement statement = connection.createStatement()){
			ClassLoader classLoader = getClass().getClassLoader();
			File initDatabaseFile = new File(classLoader.getResource("sql/init.sql").getFile());
			statement.executeUpdate(String.format("RUNSCRIPT FROM '%s'", initDatabaseFile));
		} catch (SQLException e) {
			LOGGER.error("Unable to build the database", e);
		}
	}

	/**
	 * initialize the reading queue with all the file in the library.
	 */
	private void initReadingQueue() {
		try {
			Path directoryPath = Paths.get(USER_HOME_PATH + "/.grooveberry/library/");

			if (directoryPath.toFile().exists()) {
				SongDAO songDAO = new SongDAO();
				
				LOGGER.debug("Scanning audio files in directory : {}", directoryPath.toAbsolutePath());
				AudioFileDirectoryScanner directoryScanner = new AudioFileDirectoryScanner(directoryPath);
				
				LOGGER.debug("Populate the database");
				for (Song song : directoryScanner.getSongList()) {
					songDAO.create(song);
					LOGGER.debug("Loading song : {} in the database", song.getFileName());
				}

				LOGGER.debug("Loading audio files in reading queue");
				List<Song> audioFileList = songDAO.findAll();
				if (!audioFileList.isEmpty()) {
					Locator.getService(ReadingQueueService.class, this).addToReadingQueue(audioFileList);
				} else {
					LOGGER.debug("No audio files in {}\\.grooveberry\\library\\", USER_HOME_PATH);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Directory scanning error", e);
		}

	}

}
