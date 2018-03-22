package com.seikomi.grooveberry.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.bo.ReadingQueueTest;
import com.seikomi.grooveberry.bo.Song;

public class ReadingQueueServiceTest {
	
	private ReadingQueueService readingQueueService;
	
	private Song leNeuf;
	private Song free;
	private Song aol;
	private Song bornAgain;
	
	private static final ReadingQueue readingQueue = ReadingQueue.getInstance();
	
	@Before
	public void setUp() throws Exception {
		readingQueueService = new ReadingQueueService(null);
		
		bornAgain = new Song();
		bornAgain.setPath(Paths.get(ReadingQueueTest.class.getResource("Born Again.mp3").toURI()).toString());

		free = new Song();
		free.setPath(Paths.get(ReadingQueueTest.class.getResource("free.wav").toURI()).toString());

		leNeuf = new Song();
		leNeuf.setPath(Paths.get(ReadingQueueTest.class.getResource("9.wav").toURI()).toString());

		aol = new Song();
		aol.setPath(Paths.get(ReadingQueueTest.class.getResource("aol.wav").toURI()).toString());
		
		// Initialize Reading queue
		readingQueue.addLast(leNeuf);
		readingQueue.addLast(free);
		readingQueue.addLast(aol);
		readingQueue.addLast(bornAgain);
	}
	
	@After
	public void tearDown() throws Exception {
		readingQueueService = null;
		
		this.leNeuf = null;
		this.free = null;
		this.aol = null;
		this.bornAgain = null;
		
		// Clean Reading queue
		readingQueue.clearQueue();
		readingQueue.setRandomised(false);
	}
	
	@Test
	public void testNextSongNoListeningTrack() {
		readingQueueService.next();
		
		assertEquals(free.getPath(), readingQueue.getCurrentTrack().getPath());
		assertEquals(1, readingQueue.getCurrentTrackPosition());
		assertTrue(readingQueue.getCurrentTrack().isPlaying());
	}
	
	@Test
	public void testNextSongWithListeningTrack() {
		readingQueueService.play();
		readingQueueService.next();
		
		assertEquals(free.getPath(), readingQueue.getCurrentTrack().getPath());
		assertEquals(1, readingQueue.getCurrentTrackPosition());
		assertTrue(readingQueue.getCurrentTrack().isPlaying());
	}
	
	private boolean areIdentical(List<Integer[]> list) {
		Integer[] firstTrackIndexTab = list.get(0);
		for (Integer[] trackIndexTabIterator : list) {
			for (int i = 0; i < trackIndexTabIterator.length; i++) {
				if (!firstTrackIndexTab[i].equals(trackIndexTabIterator[i])) {
					return false;
				}
			}
		}
		return true;
	}
	
	private List<Integer[]> generateDistributionList(int numberOfTry) {
		List<Integer[]> tryList = new ArrayList<>();
		
		int tryIndex = 0;
		while (tryIndex < numberOfTry) {
			Integer[] trackIndexTab = new Integer[readingQueue.size()];
			for (int i = 0; i < readingQueue.size(); i++) {
				readingQueueService.next();
				trackIndexTab[i] = readingQueue.getCurrentTrackPosition();
			}
			tryList.add(trackIndexTab);
			tryIndex++;
		}
		
		return tryList;
	}

	@Test
	public void testRandomiseReadingQueue() {
		readingQueueService.play();
		readingQueueService.randomise(); // On rand
		
		List<Integer[]> tryList = generateDistributionList(10);
		assertFalse(areIdentical(tryList));
		
		readingQueueService.randomise(); // Off rand
		
		tryList = generateDistributionList(10);
		assertTrue(areIdentical(tryList));		
	}
	
	@Test
	public void testNextSongDistributionWithNonRandomReadingQueue() {
		List<Integer[]> tryList = new ArrayList<Integer[]>();
		
		readingQueueService.play();
		
		final int NUMBER_OF_TRY = 10; 
		int tryIndex = 0;
		while(tryIndex < NUMBER_OF_TRY) {
			Integer[] trackIndexTab = new Integer[readingQueue.size()];
			for (int i = 0; i < readingQueue.size(); i++) {
				readingQueueService.next();
				trackIndexTab[i] = readingQueue.getCurrentTrackPosition();
			}
			tryList.add(trackIndexTab);
			tryIndex++;
		}
		assertTrue(areIdentical(tryList));
		
	}
	
	@Test
	public void testNextSongToTheEndOfReadingQueue() {
		readingQueue.setCurrentTrackPostion(readingQueue.size() - 1);
		readingQueueService.next();
		
		assertEquals(leNeuf.getPath(), readingQueue.getCurrentTrack().getPath());
		assertEquals(0, readingQueue.getCurrentTrackPosition());
		assertTrue(readingQueue.getCurrentTrack().isPlaying());
	}
	
	@Test
	public void testPrevSongNoListeningTrack() {
		readingQueueService.prev();
		
		assertEquals(leNeuf.getPath(), readingQueue.getCurrentTrack().getPath());
		assertEquals(0, readingQueue.getCurrentTrackPosition());
	}
	
	@Test
	public void testPrevSongWithListeningTrack() {
		readingQueue.setCurrentTrackPostion(1);
		readingQueueService.play();
		readingQueueService.prev();
		
		assertEquals(leNeuf.getPath(), readingQueue.getCurrentTrack().getPath());
		assertEquals(0, readingQueue.getCurrentTrackPosition());
	}
	
	
	@Test
	public void testPauseReadingQueue() {
		readingQueueService.play();
		assertTrue(readingQueue.getCurrentTrack().isPlaying());
		assertFalse(readingQueue.getCurrentTrack().isPaused());
		
		readingQueueService.pause();
		assertTrue(readingQueue.getCurrentTrack().isPlaying());
		assertTrue(readingQueue.getCurrentTrack().isPaused());
	}

}
