package com.seikomi.grooveberry.bo;

/**
 * This class represent the tags of a song. It mostly based on ID3 metadata
 * container.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class SongTag {

	private long songTagId;
	private String title;
	private String artistName;
	private String albumName;
	private int year;
	private String comment;
	private int trackNumber;
	private Genre genre;

	/**
	 * Gets the song tag id.
	 *
	 * @return the song tag id
	 */
	public long getSongTagId() {
		return songTagId;
	}

	/**
	 * Sets the song tag id.
	 *
	 * @param songTagId
	 *            the new song tag id
	 */
	public void setSongTagId(long songTagId) {
		this.songTagId = songTagId;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the artist name.
	 *
	 * @return the artist name
	 */
	public String getArtistName() {
		return artistName;
	}

	/**
	 * Sets the artist name.
	 *
	 * @param artistName
	 *            the new artist name
	 */
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	/**
	 * Gets the album name.
	 *
	 * @return the album name
	 */
	public String getAlbumName() {
		return albumName;
	}

	/**
	 * Sets the album name.
	 *
	 * @param albumName
	 *            the new album name
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets the year.
	 *
	 * @param year
	 *            the new year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 *
	 * @param comment
	 *            the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Gets the track number.
	 *
	 * @return the track number
	 */
	public int getTrackNumber() {
		return trackNumber;
	}

	/**
	 * Sets the track number.
	 *
	 * @param trackNumber
	 *            the new track number
	 */
	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	/**
	 * Gets the genre.
	 *
	 * @return the genre
	 */
	public Genre getGenre() {
		return genre;
	}

	/**
	 * Sets the genre.
	 *
	 * @param genre
	 *            the new genre
	 */
	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}
