package com.seikomi.grooveberry.bo;

import java.util.List;

public class Library {
	
	private List<AudioFile> songs;
	
	public void addSong(AudioFile audioFile) {
		songs.add(audioFile);
	}
	public void addSongs(List<AudioFile> audioFiles) {
		songs.addAll(audioFiles);
	}
	public void clear() {
		songs.clear();
	}

}
