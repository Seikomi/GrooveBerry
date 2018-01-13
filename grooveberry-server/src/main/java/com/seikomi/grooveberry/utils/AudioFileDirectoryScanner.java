package com.seikomi.grooveberry.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.seikomi.grooveberry.bo.AudioFile;

//TODO recursive research
public class AudioFileDirectoryScanner {
	private ArrayList<AudioFile> audioFileList;

	public AudioFileDirectoryScanner(Path directoryFilePath) throws IOException {
		this.audioFileList = new ArrayList<>();

		DirectoryStream<Path> stream = Files.newDirectoryStream(directoryFilePath);
		try {
			for (Path path : stream) {
				String fileName = path.getFileName().toString();
				String[] tokens = fileName.split("\\.");
				String fileExtension = tokens[tokens.length - 1];
				if (fileExtension.equals("mp3") || fileExtension.equals("wav")) {
					this.audioFileList.add(new AudioFile(path.toString()));
				}
			}
		} finally {
			stream.close();
		}

	}

	public ArrayList<AudioFile> getAudioFileList() {
		return this.audioFileList;
	}

}
