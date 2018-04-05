package com.seikomi.grooveberry.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.grooveberry.bo.Playlist;
import com.seikomi.grooveberry.bo.PlaylistSong;
import com.seikomi.grooveberry.bo.Song;

/**
 * This class is the DAO of the {@link Playlist} object
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class PlaylistDAO extends DAO<Playlist> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistDAO.class);

	private static final String SQL_QUERY_FIND_PLAYLIST = "SELECT name FROM playlist WHERE playlist_id = ?";
	private static final String SQL_QUERY_CREATE_PLAYLIST = "INSERT INTO playlist(name) VALUES(?)";
	private static final String SQL_QUERY_UPDATE_PLAYLIST = "UPDATE playlist SET name = ? WHERE playlist_id = ?";
	private static final String SQL_QUERY_DELETE_PLAYLIST = "DELETE FROM playlist WHERE playlist_id = ?";

	private SongDAO songDAO;
	private PlaylistSongDAO playlistSongDAO;

	/**
	 * Creates a new playlist DAO.
	 */
	public PlaylistDAO() {
		this.songDAO = new SongDAO();
		this.playlistSongDAO = new PlaylistSongDAO();
	}

	@Override
	public Playlist find(long playlistId) {
		Playlist playlist = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_FIND_PLAYLIST)) {
			preparedStatement.setLong(1, playlistId);

			playlist = executeFindQuery(playlistId, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to find the playlist object with the id = {}", playlistId, e);
		}
		return playlist;
	}

	@Override
	public Playlist create(Playlist playlist) {
		Playlist playlistCreated = null;
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_PLAYLIST,
				Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, playlist.getName());

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);

			long playlistCreatedId = findLastIdCreated(preparedStatement);
			LOGGER.trace(SQL_TRACE_FORMAT, "get the last generated key");

			if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
				List<Song> songsToCreate = playlist.getSongs();
				for (Song songToCreate : songsToCreate) {
					Song songInDatabase = songDAO.find(songToCreate.getSongId());
					PlaylistSong playlistSong = new PlaylistSong();
					if (songInDatabase != null) {
						playlistSong.setPlaylistId(playlistCreatedId);
						playlistSong.setSongId(songInDatabase.getSongId());
					} else {
						Song createdSong = songDAO.create(songToCreate);

						playlistSong.setPlaylistId(playlistCreatedId);
						playlistSong.setSongId(createdSong.getSongId());
					}
					playlistSongDAO.create(playlistSong);
				}
			}

			playlistCreated = find(playlistCreatedId);
		} catch (SQLException e) {
			LOGGER.error("Unable to create the playlist object", e);
		}
		return playlistCreated;
	}

	@Override
	public Playlist update(Playlist playlist) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_UPDATE_PLAYLIST)) {
			preparedStatement.setString(1, playlist.getName());

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);

			if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
				updatePlaylistSongs(playlist);
			}

		} catch (SQLException e) {
			LOGGER.error("Unable to update the playlist object with the id = {}", playlist.getPlaylistId(), e);
		}
		return find(playlist.getPlaylistId());
	}

	@Override
	public void delete(Playlist playlist) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_PLAYLIST)) {
			preparedStatement.setLong(1, playlist.getPlaylistId());

			preparedStatement.executeUpdate();
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
		} catch (SQLException e) {
			LOGGER.error("Unable to update the playlist object with the id = {}", playlist.getPlaylistId(), e);
		}

	}

	/**
	 * Executes the found playlist query and creates the playlist object according
	 * to the database response.
	 * 
	 * @param playlistId
	 *            the playlist id
	 * @param preparedStatement
	 *            the prepared statetement to execute
	 * @return the playlist object
	 * @throws SQLException
	 *             if a database access error occurs; this method is called on a
	 *             closed PreparedStatement or the SQL statement does not return a
	 *             ResultSet object
	 */
	private Playlist executeFindQuery(long playlistId, PreparedStatement preparedStatement) throws SQLException {
		Playlist playlist = null;
		try (ResultSet result = preparedStatement.executeQuery()) {
			LOGGER.trace(SQL_TRACE_FORMAT, preparedStatement);
			if (result.first()) {
				List<Song> songs = findAssociatedSongs(playlistId);

				playlist = new Playlist();
				playlist.setPlaylistId(playlistId);
				playlist.setName(result.getString("name"));
				playlist.setSongs(songs);
			} else {
				LOGGER.trace(SQL_TRACE_FORMAT, "empty ResultSet");
			}
		}
		return playlist;
	}

	/**
	 * Find the songs list associated with the playlist with the id.
	 * 
	 * @param playlistId
	 *            the playlist id
	 * @return the songs list
	 */
	private List<Song> findAssociatedSongs(long playlistId) {
		return songDAO.findByPlaylistId(playlistId);
	}

	/**
	 * Update the playlist songs to match with the new one. This methods check the
	 * changes between the playlist update and the playlist in database and perform
	 * addition/suppression.
	 * 
	 * @param playlist
	 *            the playlist update
	 */
	private void updatePlaylistSongs(Playlist playlist) {
		List<Song> songsToUpdate = playlist.getSongs();
		List<Song> songsInPlaylistDatabase = findAssociatedSongs(playlist.getPlaylistId());

		for (Song songToUpdate : songsToUpdate) {
			if (!songsInPlaylistDatabase.contains(songToUpdate)) {
				Song song = songDAO.create(songToUpdate);

				PlaylistSong playlistSong = new PlaylistSong();
				playlistSong.setPlaylistId(playlist.getPlaylistId());
				playlistSong.setSongId(song.getSongId());

				playlistSongDAO.create(playlistSong);
			}
		}

		for (Song songInPlaylistDatabase : songsInPlaylistDatabase) {
			if (!songsToUpdate.contains(songInPlaylistDatabase)) {
				songDAO.delete(songInPlaylistDatabase);

				PlaylistSong playlistSong = new PlaylistSong();
				playlistSong.setPlaylistId(playlist.getPlaylistId());
				playlistSong.setSongId(songInPlaylistDatabase.getSongId());

				playlistSongDAO.delete(playlistSong);
			}
		}
	}

}
