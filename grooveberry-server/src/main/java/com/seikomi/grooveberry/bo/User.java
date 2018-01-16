package com.seikomi.grooveberry.bo;

import java.util.Collections;
import java.util.List;

public class User {
	
	private long userId;
	private String name;
	private List<Playlist> playlists;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Playlist> getPlaylists() {
		return Collections.unmodifiableList(playlists);
	}

}
