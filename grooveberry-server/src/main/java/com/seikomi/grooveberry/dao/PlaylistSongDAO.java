package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.PlaylistSong;

public class PlaylistSongDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistSongDAO.class);

	private static final String SQL_QUERY_FIND_PLAYLIST_SONG = "SELECT * FROM playlist_songs WHERE playlist_playlist_id = ? AND songs_song_id = ?";
	private static final String SQL_QUERY_CREATE_PLAYLIST_SONG = "INSERT INTO playlist_songs VALUES(?, ?)";
	private static final String SQL_QUERY_DELETE_PLAYLIST_SONG = "DELETE FROM playlist_songs WHERE playlist_playlist_id = ? AND songs_song_id = ?";

	public PlaylistSong find(long playlistId, long songId) {
		PlaylistSong playlistSong = null;
		try (PreparedStatement preparedStatement = DAO.connection.prepareStatement(SQL_QUERY_FIND_PLAYLIST_SONG)) {
			preparedStatement.setLong(1, playlistId);
			preparedStatement.setLong(2, songId);

			playlistSong = executeFindQuery(preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the playlist song tag with the id = {}, {}", playlistId, songId, e);
		}
		return playlistSong;
	}

	private PlaylistSong executeFindQuery(PreparedStatement preparedStatement)
			throws SQLException {
		PlaylistSong playlistSong = null;
		try (ResultSet result = preparedStatement.executeQuery()) {
			LOGGER.trace(DAO.SQL_TRACE_FORMAT, preparedStatement);
			if (result.first()) {
				playlistSong = new PlaylistSong();
				playlistSong.setPlaylistId(result.getLong("playlist_playlist_id"));
				playlistSong.setSongId(result.getLong("songs_song_id"));
			} else {
				LOGGER.trace(DAO.SQL_TRACE_FORMAT, "no result");
			}
		}
		return playlistSong;
	}

	public PlaylistSong create(PlaylistSong playlistSong) {
		PlaylistSong playlistSongCreated = null;
		try (PreparedStatement preparedStatement = DAO.connection.prepareStatement(SQL_QUERY_CREATE_PLAYLIST_SONG)) {
			preparedStatement.setLong(1, playlistSong.getPlaylistId());
			preparedStatement.setLong(2, playlistSong.getSongId());

			preparedStatement.executeUpdate();
			LOGGER.trace(DAO.SQL_TRACE_FORMAT, preparedStatement);

			playlistSongCreated = find(playlistSong.getPlaylistId(), playlistSong.getSongId());
		} catch (SQLException e) {
			LOGGER.error("Unable to create the playlist song object", e);
		}
		return playlistSongCreated;
	}

	public void delete(PlaylistSong playlistSong) {
		try (PreparedStatement preparedStatement = DAO.connection.prepareStatement(SQL_QUERY_DELETE_PLAYLIST_SONG)) {
			preparedStatement.setLong(1, playlistSong.getPlaylistId());
			preparedStatement.setLong(2, playlistSong.getSongId());

			preparedStatement.executeUpdate();
			LOGGER.trace(DAO.SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = {}, {}", playlistSong.getPlaylistId(),
					playlistSong.getSongId(), e);
		}

	}

}
