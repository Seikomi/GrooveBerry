package com.seikomi.grooveberry.bo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Library {

	private static Library instance;
	private List<Song> songs;
	private Map<User, List<Playlist>> playlistsByUsers;
	
	public static Library GetInstance() {
		if (instance == null) {
			return new Library();
		}
		return instance;
	}

	private Library() {
		// Hide the public one
	}

	public Song getSong(int index) {
		return songs.get(index);
	}

	public List<Song> getSongs() {
		return Collections.unmodifiableList(songs);
	}

	public void addSong(Song song) {
		songs.add(song);
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	public List<Playlist> getPlaylists(User user) {
		return playlistsByUsers.get(user);
	}

}
