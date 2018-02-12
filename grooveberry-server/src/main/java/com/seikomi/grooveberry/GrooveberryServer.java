package com.seikomi.grooveberry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.AudioFile;
import com.seikomi.grooveberry.commands.Next;
import com.seikomi.grooveberry.commands.Pause;
import com.seikomi.grooveberry.commands.Play;
import com.seikomi.grooveberry.commands.Prev;
import com.seikomi.grooveberry.commands.VolumeDown;
import com.seikomi.grooveberry.commands.VolumeUp;
import com.seikomi.grooveberry.commands.WhatIsTheReadingQueue;
import com.seikomi.grooveberry.commands.WhatIsThisSong;
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
		initReadingQueue();
		
		super.start();
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

		try {
			if (!mainDirectory.exists()) {
				boolean isCreate = mainDirectory.mkdir();
				if (!isCreate) {
					throw new IOException();
				} else {
					LOGGER.info("Create the directory : " + mainDirectoryPath);
				}
			}
			if (!serverProperties.exists()) {
				boolean isCreate = serverProperties.createNewFile();
				if (!isCreate) {
					throw new IOException();
				} else {
					LOGGER.info("Create the file :" + serverPropertiesPath);
				}
			}
			if (!serverLibraryDirectory.exists()) {
				boolean isCreate = serverLibraryDirectory.mkdir();
				if (!isCreate) {
					throw new IOException();
				} else {
					LOGGER.info("Create the directory :" + serverLibraryDirectoryPath);
				}
			}
		} catch (IOException e) {
			LOGGER.error("An I/O exception occurs durring the creation of the Grooveberry server files", e);
		}
	}

	/**
	 * initialize the reading queue with all the file in the library.
	 */
	private void initReadingQueue() {
		try {
			Path directoryPath = Paths.get(USER_HOME_PATH + "/.grooveberry/library/");

			if (directoryPath.toFile().exists()) {
				LOGGER.debug("Scanning audio files in directory : " + directoryPath.toAbsolutePath());
				AudioFileDirectoryScanner directoryScanner = new AudioFileDirectoryScanner(directoryPath);

				LOGGER.debug("Loading audio files in reading queue");
				ArrayList<AudioFile> audioFileList = directoryScanner.getAudioFileList();
				if (!audioFileList.isEmpty()) {
					Locator.getService(ReadingQueueService.class, this).addToReadingQueue(audioFileList);
				} else {
					LOGGER.debug("No audio files in " + USER_HOME_PATH + "\\library\\");
				}
			}
		} catch (IOException e) {
			LOGGER.error("Directory scanning error", e);
		}

	}

}
