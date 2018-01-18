package com.seikomi.grooveberry.bo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.seikomi.grooveberry.dao.SongDAO;

/**
 * This class represent the library of the songs played in the grooveberry
 * server.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class Library {
	private static Library instance;

	private List<Song> songs;
	private Map<User, List<Playlist>> playlistsByUsers;

	private SongDAO songDAO;

	/**
	 * Gets the unique instance of the library.
	 * 
	 * @return the unique instance of the library
	 */
	public static synchronized Library getInstance() {
		if (instance == null) {
			return new Library();
		}
		return instance;
	}

	/**
	 * Construct the library with all the songs in the database.
	 */
	private Library() {
		this.songDAO = new SongDAO();
		this.songs = this.songDAO.findAll();
	}

	/**
	 * Gets the song at the index.
	 * 
	 * @param index
	 *            the index of the song to get
	 * @return the song at the index
	 */
	public Song getSong(int index) {
		return songs.get(index);
	}

	/**
	 * Gets all the songs in the library.
	 * 
	 * @return all the songs
	 */
	public List<Song> getSongs() {
		return Collections.unmodifiableList(songs);
	}

	/**
	 * Adds a song to the library. This addition is propagate into the database.
	 * 
	 * @param song
	 *            the song to add
	 */
	public void addSong(Song song) {
		songDAO.create(song);
		songs.add(song);
	}

	/**
	 * Gets all the playlists associated with a user.
	 * 
	 * @param user
	 *            the user
	 * @return all the playlists associated with a user
	 */
	public List<Playlist> getPlaylists(User user) {
		return playlistsByUsers.get(user);
	}

}
