package com.seikomi.grooveberry.bo;

import java.util.Collections;
import java.util.List;

/**
 * This class is the representation of a grooveberry user. A user has a name and
 * the playlist that he made.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class User {

	private long userId;
	private String name;
	private List<Playlist> playlists;

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the playlists associated with this user.
	 *
	 * @return the playlists
	 */
	public List<Playlist> getPlaylists() {
		return Collections.unmodifiableList(playlists);
	}

}
