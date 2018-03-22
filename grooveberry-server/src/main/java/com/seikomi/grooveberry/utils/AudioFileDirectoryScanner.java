package com.seikomi.grooveberry.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.seikomi.grooveberry.bo.AudioFile;
import com.seikomi.grooveberry.bo.Song;

//TODO recursive research
public class AudioFileDirectoryScanner {
	private List<AudioFile> audioFiles;
	private List<Song> songs;

	public AudioFileDirectoryScanner(Path directoryFilePath) throws IOException {
		this.audioFiles = new ArrayList<>();
		this.songs = new ArrayList<>();

		DirectoryStream<Path> stream = Files.newDirectoryStream(directoryFilePath);
		try {
			for (Path path : stream) {
				String fileName = path.getFileName().toString();
				String[] tokens = fileName.split("\\.");
				String fileExtension = tokens[tokens.length - 1];
				if (fileExtension.equals("mp3") || fileExtension.equals("wav")) {
					audioFiles.add(new AudioFile(path.toString()));
					
					Song song = new Song();
					song.setPath(path.toString());
					songs.add(song);
				}
			}
		} finally {
			stream.close();
		}

	}
	
	public List<Song> getSongList() {
		return songs;
	}
	
	public List<AudioFile> getAudioFileList() {
		return this.audioFiles;
	}

}
