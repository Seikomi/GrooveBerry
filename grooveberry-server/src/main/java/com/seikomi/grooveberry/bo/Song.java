package com.seikomi.grooveberry.bo;

/**
 * This class contains song informations : the file location of the song and is
 * associated {@link SongTag}
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class Song {
	private long songId;
	private String path;
	private SongTag songTag;

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

	/**
	 * Gets the file name of this song.
	 * 
	 * @return the file name
	 */
	public String getFileName() {
		return path.substring(path.lastIndexOf('\\') + 1); // TODO handle all type of path (unix or windows)
	}

	/**
	 * Sets the path in the system of the song.
	 * 
	 * @param path
	 *            the path of the song to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the path in the system of the song.
	 * 
	 * @return the path of the song
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the SongTag object associated with this song.
	 * 
	 * @return the SongTag object or null if it doesn't exist
	 */
	public SongTag getSongTag() {
		return songTag;
	}

	/**
	 * Sets the SongTag object associated with this song.
	 * 
	 * @param songTag
	 *            the song tag to set
	 */
	public void setSongTag(SongTag songTag) {
		this.songTag = songTag;
	}

}
