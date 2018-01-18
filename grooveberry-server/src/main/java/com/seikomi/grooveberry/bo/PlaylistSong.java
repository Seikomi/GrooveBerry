package com.seikomi.grooveberry.bo;

/**
 * This class represent an association between a playlist and a song.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class PlaylistSong {
	private long playlistId;
	private long songId;

	/**
	 * Gets the playlist id.
	 * 
	 * @return the playlist id
	 */
	public long getPlaylistId() {
		return playlistId;
	}

	/**
	 * Sets the playlist id.
	 * 
	 * @param playlistId
	 *            the playlist id to set
	 */
	public void setPlaylistId(long playlistId) {
		this.playlistId = playlistId;
	}

	/**
	 * Gets the song id.
	 * 
	 * @return the song id
	 */
	public long getSongId() {
		return songId;
	}

	/**
	 * Sets the song id.
	 * 
	 * @param songId
	 *            the song id to set
	 */
	public void setSongId(long songId) {
		this.songId = songId;
	}

}
