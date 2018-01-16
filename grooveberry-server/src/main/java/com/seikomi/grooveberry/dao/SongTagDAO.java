package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Genre;
import com.seikomi.grooveberry.bo.SongTag;

public class SongTagDAO extends DAO<SongTag> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SongTagDAO.class);

	private static final String SQL_QUERY_FIND_SONG_TAG = "SELECT title, artistName, albumName, year, comment, trackNumber, genreId FROM SongTag";

	private static final String SQL_QUERY_CREATE_SONG_TAG = "INSERT INTO SongTag(title, artistName, albumName, year, comment, trackNumber, genreId) VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_QUERY_UPDATE_SONG_TAG = "UPDATE SongTag SET title = ?, artistName = ?, albumName = ?, year = ?, comment = ?, trackNumber = ?, genreId = ? WHERE songTagId = ?";

	private static final String SQL_QUERY_DELETE_SONG_TAG = "DELETE FROM SongTag WHERE songTagId = ?";

	@Override
	public SongTag find(long songTagId) {
		SongTag songTag = new SongTag();
		LOGGER.trace("SQL : " + SQL_QUERY_FIND_SONG_TAG);
		try (ResultSet result = connection.createStatement().executeQuery(SQL_QUERY_FIND_SONG_TAG)) {
			if (result.first()) {				
				songTag.setSongTagId(songTagId);
				songTag.setTitle(result.getString("title"));
				songTag.setArtistName(result.getString("artistName"));
				songTag.setAlbumName(result.getString("albumName"));
				songTag.setYear(result.getInt("year"));
				songTag.setComment(result.getString("comment"));
				songTag.setTrackNumber(result.getInt("trackNumber"));
				int genreId = result.getInt("genreId");
				songTag.setGenre(Genre.getGenre(genreId));
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to find the song tag object with the id = " + songTagId, e);
		}
		return songTag;
	}

	@Override
	public SongTag create(SongTag songTag) {
		SongTag songTagCreated = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_SONG_TAG)) {
			preparedStatement.setString(1, songTag.getTitle());
			preparedStatement.setString(2, songTag.getArtistName());
			preparedStatement.setString(3, songTag.getAlbumName());
			preparedStatement.setInt(4, songTag.getYear());
			preparedStatement.setString(5, songTag.getComment());
			preparedStatement.setInt(6, songTag.getTrackNumber());
			preparedStatement.setInt(7, songTag.getGenre().getIndex());
			
			LOGGER.trace("SQL : " + SQL_QUERY_CREATE_SONG_TAG);
			preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.first()) {
				long songTagId = generatedKeys.getLong(1);
				songTagCreated = find(songTagId);
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to create the song tag object", e);
		}
		return songTagCreated;
	}

	@Override
	public SongTag update(SongTag songTag) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_UPDATE_SONG_TAG)) {
			preparedStatement.setString(1, songTag.getTitle());
			preparedStatement.setString(2, songTag.getArtistName());
			preparedStatement.setString(3, songTag.getAlbumName());
			preparedStatement.setInt(4, songTag.getYear());
			preparedStatement.setString(5, songTag.getComment());
			preparedStatement.setInt(6, songTag.getTrackNumber());
			preparedStatement.setInt(7, songTag.getGenre().getIndex());
			preparedStatement.setLong(8, songTag.getSongTagId());
			
			LOGGER.trace("SQL : " + SQL_QUERY_UPDATE_SONG_TAG);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song tag object with the id = " + songTag.getSongTagId() , e);
		}
		return find(songTag.getSongTagId());
	}

	@Override
	public void delete(SongTag songTag) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_SONG_TAG)) {
			preparedStatement.setLong(1, songTag.getSongTagId());
			
			LOGGER.trace("SQL : " + SQL_QUERY_DELETE_SONG_TAG);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song tag object with the id = " + songTag.getSongTagId() , e);
		}
	}

}
