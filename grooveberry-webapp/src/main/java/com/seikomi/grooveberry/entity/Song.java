package com.seikomi.grooveberry.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This class contains song informations : the file location of the song and is
 * associated {@link SongTag}
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "song")
public class Song {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long songId;
	
	@Column
	@NotBlank
	private String path;
	
	@OneToOne
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
