package com.seikomi.grooveberry.bo;

public class Song {

	private long songId;
	private String path;
	private SongTag songTag;

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public String getFileName() {
		return path.substring(path.lastIndexOf('\\') + 1); //TODO handle all type of path (unix or windows)
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public SongTag getSongTag() {
		return songTag;
	}

	public void setSongTag(SongTag songTag) {
		this.songTag = songTag;
	}
	
	

}
