package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.bo.SongTag;

public class SongDAO extends DAO<Song> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SongDAO.class);

	private static final String SQL_QUERY_FIND_SONG = "SELECT path, songTagId FROM Song";
	private static final String SQL_QUERY_CREATE_SONG = "INSERT INTO Song(path, songTagId) VALUES (?, ?)";
	private static final String SQL_QUERY_UPDATE_SONG = "UPDATE Song SET path = ?, songTagId = ? WHERE songId = ?";
	private static final String SQL_QUERY_DELETE_SONG = "DELETE FROM Song WHERE songId = ?";

	@Override
	public Song find(long songId) {
		Song song = new Song();
		LOGGER.trace("SQL : " + SQL_QUERY_FIND_SONG);
		try (ResultSet result = connection.createStatement().executeQuery(SQL_QUERY_FIND_SONG)) {
			if (result.first()) {
				DAO<SongTag> songTagDao = new SongTagDAO();
				SongTag songTag = songTagDao.find(result.getInt("songTagId"));
				
				song.setSongId(songId);
				song.setPath(result.getString("path"));
				song.setSongTag(songTag);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to find the song object with the id = " + songId, e);
		}
		return song;
	}

	@Override
	public Song create(Song song) {
		Song songCreated = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_SONG)) {
			preparedStatement.setString(1, song.getPath());
			preparedStatement.setLong(2, song.getSongTag().getSongTagId());
			
			LOGGER.trace("SQL : " + SQL_QUERY_CREATE_SONG);
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.first()) {
				long songId = generatedKeys.getLong(1);
				songCreated = find(songId);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to create the song object", e);
		}
		return songCreated;
	}

	@Override
	public Song update(Song song) {		
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_UPDATE_SONG)) {
			preparedStatement.setString(1, song.getPath());
			if (song.getSongTag() != null) {
				preparedStatement.setLong(2, song.getSongTag().getSongTagId());
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}
			preparedStatement.setLong(3, song.getSongId());
			
			LOGGER.trace("SQL : " + SQL_QUERY_UPDATE_SONG);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = " + song.getSongId() , e);
		}
		return find(song.getSongId());
	}

	@Override
	public void delete(Song song) {
		if (song.getSongTag() != null) {
			DAO<SongTag> songTagDAO = new SongTagDAO();
			songTagDAO.delete(song.getSongTag());
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_SONG)) {
			preparedStatement.setLong(1, song.getSongId());
			
			LOGGER.trace("SQL : " + SQL_QUERY_DELETE_SONG);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = " + song.getSongId() , e);
		}
	}

}
