package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.AudioFile;
import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.bo.Song;

public class ReadingQueueDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadingQueueDAO.class);
	private static final String SQL_TRACE_FORMAT = "SQL: {}";

	private static final String SQL_QUERY_UPDATE_SONG = "UPDATE reading_queue SET current_track_song_id = ?";

	public void update() {
		try (PreparedStatement preparedStatement = DAO.connection.prepareStatement(SQL_QUERY_UPDATE_SONG)) {
			AudioFile currentTrack = ReadingQueue.getInstance().getCurrentTrack();
			SongDAO songDAO = new SongDAO();
			Song song = songDAO.findByPath(currentTrack.getAbsolutePath());
			
			preparedStatement.setLong(1, song.getSongId());
			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the reading queue object", e);
		}
	}

}
