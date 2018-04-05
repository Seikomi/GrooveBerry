package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Genre;
import com.seikomi.grooveberry.bo.SongTag;

/**
 * This class is the DAO of the {@link SongTag} object
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class SongTagDAO extends DAO<SongTag> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SongTagDAO.class);

	private static final String SQL_QUERY_FIND_SONG_TAG = "SELECT title, artist_name, album_name, year, comment, track_number, genre_id FROM song_tag WHERE song_tag_id = ?";
	private static final String SQL_QUERY_CREATE_SONG_TAG = "INSERT INTO song_tag(title, artist_name, album_name, year, comment, track_number, genre_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_QUERY_UPDATE_SONG_TAG = "UPDATE song_tag SET title = ?, artist_name = ?, album_name = ?, year = ?, comment = ?, track_number = ?, genre_id = ? WHERE song_tag_id = ?";
	private static final String SQL_QUERY_DELETE_SONG_TAG = "DELETE FROM song_tag WHERE song_tag_id = ?";

	@Override
	public SongTag find(long songTagId) {
		SongTag songTag = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_SONG_TAG)) {
			preparedStatement.setLong(1, songTagId);

			songTag = executeFindQuery(songTagId, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the song tag object with the id = {}", songTagId, e);
		}
		return songTag;
	}

	private SongTag executeFindQuery(long songTagId, PreparedStatement preparedStatement)
			throws SQLException {
		SongTag songTag = null;
		try (ResultSet result = preparedStatement.executeQuery()) {
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
			if (result.first()) {
				songTag = new SongTag();
				songTag.setSongTagId(songTagId);
				songTag.setTitle(result.getString("title"));
				songTag.setArtistName(result.getString("artist_name"));
				songTag.setAlbumName(result.getString("album_name"));
				songTag.setYear(result.getInt("year"));
				songTag.setComment(result.getString("comment"));
				songTag.setTrackNumber(result.getInt("track_number"));
				int genreId = result.getInt("genre_id");
				songTag.setGenre(Genre.getGenre(genreId));
			} else {
				LOGGER.trace(SQL_TRACE_FORMAT, "no result");
			}
		}
		return songTag;
	}

	@Override
	public SongTag create(SongTag songTag) {
		SongTag songTagCreated = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_SONG_TAG, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, songTag.getTitle());
			preparedStatement.setString(2, songTag.getArtistName());
			preparedStatement.setString(3, songTag.getAlbumName());
			preparedStatement.setInt(4, songTag.getYear());
			preparedStatement.setString(5, songTag.getComment());
			preparedStatement.setInt(6, songTag.getTrackNumber());
			if (songTag.getGenre() != null) {
				preparedStatement.setInt(7, songTag.getGenre().getIndex());
			} else {
				preparedStatement.setNull(7, Types.INTEGER);
			}

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);

			long songTagCreatedId = findLastIdCreated(preparedStatement);
			LOGGER.trace(SQL_TRACE_FORMAT, "get the last generated key");
			songTagCreated = find(songTagCreatedId);
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

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song tag object with the id = {}", songTag.getSongTagId(), e);
		}
		return find(songTag.getSongTagId());
	}

	@Override
	public void delete(SongTag songTag) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_SONG_TAG)) {
			preparedStatement.setLong(1, songTag.getSongTagId());

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song tag object with the id = {}", songTag.getSongTagId(), e);
		}
	}

}
