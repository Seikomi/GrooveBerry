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

	public long getSongTagId() {
		return songTagId;
	}

	public void setSongTagId(long songTagId) {
		this.songTagId = songTagId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}
