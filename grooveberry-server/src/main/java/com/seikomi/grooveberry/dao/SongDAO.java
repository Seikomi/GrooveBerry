package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private static final String SQL_QUERY_FIND_ALL_SONGS = "SELECT * FROM Song";
	private static final String SQL_QUERY_CREATE_SONG = "INSERT INTO Song(path, songTagId) VALUES (?, ?)";
	private static final String SQL_QUERY_UPDATE_SONG = "UPDATE Song SET path = ?, songTagId = ? WHERE songId = ?";
	private static final String SQL_QUERY_DELETE_SONG = "DELETE FROM Song WHERE songId = ?";

	private static final String SQL_QUERY_FIND_SONGS_BY_PLAYLIST_ID = "SELECT * FROM Song WHERE songId IN (SELECT songId FROM PlaylistSong WHERE playlistId = ?)";

	private DAO<SongTag> songTagDAO;

	/**
	 * Create a new song DAO.
	 */
	public SongDAO() {
		this.songTagDAO = new SongTagDAO();
	}

	@Override
	public Song find(long songId) {
		Song song = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_SONG)) {
			preparedStatement.setLong(1, songId);

			song = executeFindQuery(songId, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the song object with the id = {}", songId, e);
		}
		return song;
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
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);

			long songCreatedId = findLastIdCreated(preparedStatement);
			LOGGER.trace(SQL_TRACE_FORMAT, "get the last generated key");
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
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = {}", song.getSongId(), e);
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
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the song object with the id = {}", song.getSongId(), e);
		}
	}

	/**
	 * Finds all songs in database.
	 * 
	 * @return the list of all songs
	 */
	public List<Song> findAll() {
		List<Song> songs = new ArrayList<>();
		try (Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(SQL_QUERY_FIND_ALL_SONGS)) {
			LOGGER.trace(SQL_TRACE_FORMAT, SQL_QUERY_FIND_ALL_SONGS);
			songs = createSongList(result);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the songs objects", e);
		}
		return songs;
	}

	/**
	 * Finds all the songs associated with the playlist id in the database.
	 * 
	 * @param playlistId
	 *            the playlist id
	 * @return the list of the songs in the playlist
	 */
	public List<Song> findByPlaylistId(long playlistId) {
		List<Song> songs = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_SONGS_BY_PLAYLIST_ID)) {
			preparedStatement.setLong(1, playlistId);
			songs = executeFindByPlaylistIdQuery(preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the songs objects for the playlist id = {}", playlistId, e);
		}
		return songs;
	}

	/**
	 * Executes a find query on the song with his id and create the associated song
	 * object.
	 * 
	 * @param songId
	 *            the song id
	 * @param preparedStatement
	 *            the prepared statement to execute
	 * @return the song found
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return a
	 *             ResultSet object
	 */
	private Song executeFindQuery(long songId, PreparedStatement preparedStatement) throws SQLException {
		Song song = null;
		try (ResultSet result = preparedStatement.executeQuery()) {
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
			if (result.first()) {
				song = new Song();
				song.setSongId(songId);
				song.setPath(result.getString("path"));
				
				SongTag songTag = findAssociatedSongTag(result);
				if (songTag !=null) {
					song.setSongTag(songTag);
				}				
			} else {
				LOGGER.trace(SQL_TRACE_FORMAT, "empty ResultSet");
			}
		}
		return song;
	}

	/**
	 * Executes a find query on the songs to find these associated with a playlist.
	 * 
	 * @param preparedStatement
	 *            the prepared statement to execute
	 * @return the list of the songs in the playlist
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return a
	 *             ResultSet object
	 */
	private List<Song> executeFindByPlaylistIdQuery(PreparedStatement preparedStatement) throws SQLException {
		List<Song> songs = new ArrayList<>();
		try (ResultSet result = preparedStatement.executeQuery()) {
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
			songs = createSongList(result);
		}
		return songs;
	}

	/**
	 * Create the songs list according to the result of a query.
	 * 
	 * @param result
	 *            the result of a query
	 * @return the songs list
	 * @throws SQLException
	 *             if the song tag id label is not valid; if a database access error
	 *             occurs or this method is called on a closed result set
	 */
	private List<Song> createSongList(ResultSet result) throws SQLException {
		List<Song> songs = new ArrayList<>();
		while (result.next()) {
			Song song = new Song();
			song.setSongId(result.getLong("songId"));
			song.setPath(result.getString("path"));
			
			SongTag songTag = findAssociatedSongTag(result);
			if (songTag != null) {
				song.setSongTag(songTag);
			}

			songs.add(song);
		}
		if (songs.isEmpty()) {
			LOGGER.trace(SQL_TRACE_FORMAT, "empty ResultSet");
		}
		return songs;
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
		if (result.getInt("songTagId") == 0) {
			return null;
		}
		return songTagDAO.find(result.getInt("songTagId"));
	}

}
