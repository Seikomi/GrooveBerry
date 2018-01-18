package com.seikomi.grooveberry.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.GrooveberryServer;
import com.seikomi.grooveberry.bo.AudioFile;
import com.seikomi.grooveberry.bo.Library;
import com.seikomi.grooveberry.bo.Playlist;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.dao.PlaylistDAO;
import com.seikomi.grooveberry.dao.SongDAO;
import com.seikomi.grooveberry.utils.AudioFileDirectoryScanner;

public class TestH2Database {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestH2Database.class);
	private static SongDAO songDAO = new SongDAO();
	private static PlaylistDAO playlistDAO = new PlaylistDAO();
	
	public static void main(String[] args) throws SQLException {
		Statement statement = ConnectionH2Database.getInstance().createStatement();
		statement.executeUpdate("RUNSCRIPT FROM '" + new File("D:\\Perso\\workspace\\GrooveBerry\\grooveberry-server\\src\\main\\resources\\sql\\init.sql") + "'");
		
		scanLibraryDirectory();	
		
		List<Song> songs = Library.getInstance().getSongs();
		LOGGER.info("*** LIBRARY ***");
		printSongsInfo(songs);
		
		Playlist playlist = new Playlist();
		playlist.setName("My playlist = library");
		playlist.setSongs(songs);
		
		Playlist playlistCreated = playlistDAO.create(playlist);
		
		printPlaylistInfo(playlistCreated);

	}

	private static void scanLibraryDirectory() {
		try {
			Path directoryPath = Paths.get(GrooveberryServer.USER_HOME_PATH + "/.grooveberry/library/");

			if (directoryPath.toFile().exists()) {
				LOGGER.debug("Scanning audio files in directory : " + directoryPath.toAbsolutePath());
				AudioFileDirectoryScanner directoryScanner = new AudioFileDirectoryScanner(directoryPath);

				LOGGER.debug("Loading audio files in database");
				ArrayList<AudioFile> audioFileList = directoryScanner.getAudioFileList();
				if (audioFileList.isEmpty()) {
					LOGGER.debug("No audio files in " + GrooveberryServer.USER_HOME_PATH + "\\.grooveberry\\library\\");
				}
				for (AudioFile audioFile : audioFileList) {
					Song song = new Song();
					song.setPath(audioFile.getPath());
					songDAO.create(song);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Directory scanning error", e);
		}
	}

	private static void printSongsInfo(List<Song> songs) {
		for (Song song : songs) {
			printSongInfo(song);
		}		
	}

	private static void printPlaylistInfo(Playlist playlist) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("        ID          " + playlist.getPlaylistId());
		stringBuilder.append("\n        NAME        " + playlist.getName());
		stringBuilder.append("\n        SONGS    ");
		
		LOGGER.info("Playlist object infos :\n" + stringBuilder);
		printSongsInfo(playlist.getSongs());
	}

	private static void printSongInfo(Song song) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("        ID          " + song.getSongId());
		stringBuilder.append("\n        PATH        " + song.getPath());
		stringBuilder.append("\n        FILENAME    " + song.getFileName());
		stringBuilder.append("\n        TAG OBJECT  " + song.getSongTag());
		LOGGER.info("Song object infos :\n" + stringBuilder);
	}
}
