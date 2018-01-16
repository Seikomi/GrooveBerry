package com.seikomi.grooveberry.bo;

import java.util.Collections;
import java.util.List;

public class Playlist {

	private long playlistId;

	private List<Song> songs;

	public long getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(long playlistId) {
		this.playlistId = playlistId;
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

}
