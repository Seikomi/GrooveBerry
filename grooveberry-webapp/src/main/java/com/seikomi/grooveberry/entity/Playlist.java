package com.seikomi.grooveberry.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * This class contains playlist informations : the playlist name and the songs
 * into.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
@Entity
@Table(name = "playlist")
public class Playlist {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long playlistId;
	
	@Column
	@NotBlank
	private String name;

	@OneToMany
	private List<Song> songs;

	/**
	 * Create a new empty playlist.
	 */
	public Playlist() {
		this.songs = new ArrayList<>();
	}

	/**
	 * Gets the playlist id.
	 * 
	 * @return the playlist id
	 */
	public long getPlaylistId() {
		return playlistId;
	}

	/**
	 * Sets the playlist id.<br>
	 * <b>BE CAREFUL</b> changing id without concern can create database errors.
	 * 
	 * @param playlistId
	 *            the playlist id
	 */
	public void setPlaylistId(long playlistId) {
		this.playlistId = playlistId;
	}

	/**
	 * Gets the playlist name.
	 * 
	 * @return the playlist name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the playlist name.
	 * 
	 * @param name
	 *            the playlist name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the song in the playlist at the index.
	 * 
	 * @param index
	 *            the index of the song to get
	 * @return the song in the playlist at the index
	 */
	public Song getSong(int index) {
		return songs.get(index);
	}

	/**
	 * Gets all the songs in the playlist.
	 * 
	 * @return all the songs in the playlist
	 */
	public List<Song> getSongs() {
		return Collections.unmodifiableList(songs);
	}

	/**
	 * Adds the song in the playlist.
	 * 
	 * @param song
	 *            the song to add
	 */
	public void addSong(Song song) {
		songs.add(song);
	}

	/**
	 * Sets the songs list of this playlist.
	 * 
	 * @param songs
	 *            the songs list to add
	 */
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

}
