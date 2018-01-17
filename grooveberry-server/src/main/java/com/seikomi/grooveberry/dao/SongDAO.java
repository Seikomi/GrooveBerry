package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.bo.SongTag;

/**
 * This class is the DAO of the {@link Song} object
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class SongDAO extends DAO<Song> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SongDAO.class);

	private static final String SQL_QUERY_FIND_SONG = "SELECT path, songTagId FROM Song WHERE songId = ?";
	private static final String SQL_QUERY_FIND_All_SONGS = "SELECT * FROM Song";
	private static final String SQL_QUERY_CREATE_SONG = "INSERT INTO Song(path, songTagId) VALUES (?, ?)";
	private static final String SQL_QUERY_UPDATE_SONG = "UPDATE Song SET path = ?, songTagId = ? WHERE songId = ?";
	private static final String SQL_QUERY_DELETE_SONG = "DELETE FROM Song WHERE songId = ?";

	private DAO<SongTag> songTagDAO;

	/**
	 * Create a new song DAO.
	 */
	public SongDAO() {
		this.songTagDAO = new SongTagDAO();
	}

	@Override
	public Song find(long songId) {
		Song song = new Song();
		
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_SONG)) {
			preparedStatement.setLong(1, songId);
			
			ResultSet result = preparedStatement.executeQuery();
			LOGGER.trace("SQL : " + SQL_QUERY_FIND_SONG);
			if (result.first()) {
				SongTag songTag = findAssociatedSongTag(result);

				song.setSongId(songId);
				song.setPath(result.getString("path"));
				song.setSongTag(songTag);
			} else {
				LOGGER.trace("SQL : empty ResultSet");
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to find the song object with the id = " + songId, e);
		}
		return song;
	}

	/**
	 * Finds all songs in database.
	 * 
	 * @return the list of all songs
	 */
	public List<Song> findAll() {
		List<Song> songs = new ArrayList<>();
		try (ResultSet result = connection.createStatement().executeQuery(SQL_QUERY_FIND_All_SONGS)) {
			LOGGER.trace("SQL : " + SQL_QUERY_FIND_All_SONGS);
			while (result.next()) {
				SongTag songTag = findAssociatedSongTag(result);

				Song song = new Song();
				song.setSongId(result.getLong("songId"));
				song.setPath(result.getString("path"));
				song.setSongTag(songTag);

				songs.add(song);
			}
			if (songs.isEmpty()) {
				LOGGER.trace("SQL : empty ResultSet");
			}
		} catch (SQLException e) {
			LOGGER.error("Unable to find the songs objects", e);
		}
		return songs;
	}

	@Override
	public Song create(Song song) {
		Song songCreated = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_SONG)) {
			preparedStatement.setString(1, song.getPath());
			if (song.getSongTag() != null) {
				SongTag songtagToCreate = song.getSongTag();
				SongTag songTagInDatabase = songTagDAO.find(songtagToCreate.getSongTagId());
				if (songTagInDatabase != null) {
					preparedStatement.setLong(2, songtagToCreate.getSongTagId());
				} else {
					SongTag createdSongTag = songTagDAO.create(song.getSongTag());
					preparedStatement.setLong(2, createdSongTag.getSongTagId());
				}
			} else {
				preparedStatement.setNull(2, Types.INTEGER);
			}

			preparedStatement.executeUpdate();
			LOGGER.trace("SQL : " + SQL_QUERY_CREATE_SONG);

			long songCreatedId = findLastIdCreated(preparedStatement);
			LOGGER.trace("SQL : get the last generated key");
			songCreated = find(songCreatedId);
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

			preparedStatement.executeUpdate();
			LOGGER.trace("SQL : " + SQL_QUERY_UPDATE_SONG);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = " + song.getSongId(), e);
		}
		return find(song.getSongId());
	}

	@Override
	public void delete(Song song) {
		if (song.getSongTag() != null) {
			songTagDAO.delete(song.getSongTag());
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_SONG)) {
			preparedStatement.setLong(1, song.getSongId());

			preparedStatement.executeUpdate();
			LOGGER.trace("SQL : " + SQL_QUERY_DELETE_SONG);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = " + song.getSongId(), e);
		}
	}

	/**
	 * Find the associate song tag of the song return in the result set.
	 * 
	 * @param result
	 *            the result set where to find the song tab id
	 * @return the associate song tag
	 * @throws SQLException
	 *             if the song tag id label is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private SongTag findAssociatedSongTag(ResultSet result) throws SQLException {
		return songTagDAO.find(result.getInt("songTagId"));
	}

}
