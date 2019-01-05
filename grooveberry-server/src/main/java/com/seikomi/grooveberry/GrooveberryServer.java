package com.seikomi.grooveberry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.commands.Get;
import com.seikomi.grooveberry.commands.Next;
import com.seikomi.grooveberry.commands.Pause;
import com.seikomi.grooveberry.commands.Play;
import com.seikomi.grooveberry.commands.Prev;
import com.seikomi.grooveberry.commands.VolumeDown;
import com.seikomi.grooveberry.commands.VolumeUp;
import com.seikomi.grooveberry.commands.WhatIsTheReadingQueue;
import com.seikomi.grooveberry.commands.WhatIsThisSong;
import com.seikomi.grooveberry.commands.YoutubeDownload;
import com.seikomi.grooveberry.dao.SongDAO;
import com.seikomi.grooveberry.database.ConnectionH2Database;
import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.grooveberry.services.YoutubeTransfertService;
import com.seikomi.grooveberry.utils.AudioFileDirectoryScanner;
import com.seikomi.grooveberry.utils.ScriptRunner;
import com.seikomi.janus.commands.CommandsFactory;
import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.dispatcher.EventsDispatcher;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.services.Locator;
import com.seikomi.janus.utils.Utils;

public class GrooveberryServer extends JanusServer implements Observer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrooveberryServer.class);
	public static final String USER_HOME_PATH = System.getProperty("user.home");

	public GrooveberryServer(JanusProperties serverProperties) {
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
		runScript(connection, Utils.transformStringPath("sql/dropTables.sql").toString());

		ConnectionH2Database.closeConnection();
		ReadingQueue.getInstance().clearQueue();
		super.stop();
	}

	@Override
	protected void loadContext() {
		String url = getProperties("database.url");
		String user = getProperties("database.user");
		String password = getProperties("database.password");
		ConnectionH2Database.getConnection(url, user, password);

		CommandsFactory.addCommand(new Play(), "#PLAY", this);
		CommandsFactory.addCommand(new Next(), "#NEXT", this);
		CommandsFactory.addCommand(new Prev(), "#PREV", this);
		CommandsFactory.addCommand(new Pause(), "#PAUSE", this);
		CommandsFactory.addCommand(new VolumeDown(), "#VOLDOWN", this);
		CommandsFactory.addCommand(new VolumeUp(), "#VOLUP", this);
		CommandsFactory.addCommand(new WhatIsTheReadingQueue(), "#LIST", this);
		CommandsFactory.addCommand(new WhatIsThisSong(), "#SONG", this);
		CommandsFactory.addCommand(new Get(), "#GET", this);
		CommandsFactory.addCommand(new YoutubeDownload(), "#Y_DOWNLOAD", this);

		Locator.load(new ReadingQueueService(this));
		Locator.load(new YoutubeTransfertService(this));

		Locator.getService(ReadingQueueService.class, this).addObserver(this);
	}

	/**
	 * Initialize library directory and the .properties file.
	 */
	private void initServerFiles() {
		Path mainDirectoryPath = Utils.transformStringPath(getProperties("server.directories.root"));
		Path serverLibraryDirectoryPath = Utils.transformStringPath(getProperties("server.directories.library"));
		Path serverPropertiesPath = Utils
				.transformStringPath(getProperties("server.directories.root") + "grooveberry.properties");

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

		Connection connection = ConnectionH2Database.getConnection();
		runScript(connection, Utils.transformStringPath("sql/init.sql").toString());
	}

	private void runScript(Connection connection, String filePath) {
		String file = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			ScriptRunner runner = new ScriptRunner(connection, false, false);
			file = classLoader.getResource(filePath).getFile();
			runner.runScript(new BufferedReader(new FileReader(file)));
		} catch (IOException | SQLException e) {
			LOGGER.error("Failed to execute the SQL script file {}", file, e);
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

	@Override
	public void update(Observable o, Object arg) {
		List<EventsDispatcher> eventsDispatchers = getEventsDispatchers();
		if (eventsDispatchers != null) {
			LOGGER.debug("Send incoming events to events dispatchers :");
			for (EventsDispatcher eventsDispatchTask : eventsDispatchers) {
				try {
					eventsDispatchTask.sendEvent();
				} catch (IOException e) {
					LOGGER.error("An error occurs during the broadcasting of events", e);
				}
			}
		}
	}
}
