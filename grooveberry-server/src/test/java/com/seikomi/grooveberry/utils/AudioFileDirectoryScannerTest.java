package com.seikomi.grooveberry.utils;

import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.seikomi.grooveberry.bo.AudioFile;

public class AudioFileDirectoryScannerTest {
	private AudioFileDirectoryScanner audioFileDirectoryScanner;

	@Before
	public void setUp() throws Exception {
		Path audioFileDirectoryPath = Paths.get("src/test/resources/com/seikomi/grooveberry/bo/");
		audioFileDirectoryScanner = new AudioFileDirectoryScanner(audioFileDirectoryPath);
	}

	@After
	public void tearDown() throws Exception {
		audioFileDirectoryScanner = null;
	}

	@Test
	public void testScanDirectory() {
		List<AudioFile> audioFileList = audioFileDirectoryScanner.getAudioFileList();
		Set<String> audioFileSet = new HashSet<String>();
		for (AudioFile audioFile : audioFileList) {
			audioFileSet.add(audioFile.getName());
		}

		assertTrue(audioFileSet.contains("Born Again.mp3"));
		assertTrue(audioFileSet.contains("9.wav"));
		assertTrue(audioFileSet.contains("aol.wav"));
		assertTrue(audioFileSet.contains("free.wav"));
	}

}