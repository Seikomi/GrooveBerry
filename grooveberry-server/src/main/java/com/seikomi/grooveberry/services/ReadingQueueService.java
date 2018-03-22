package com.seikomi.grooveberry.services;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.services.JanusService;
import com.seikomi.grooveberry.bo.AudioFile;
import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.bo.Song;
import com.seikomi.grooveberry.utils.AudioUtility;

public class ReadingQueueService extends JanusService implements Observer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadingQueueService.class);
	private ReadingQueue readingQueue = ReadingQueue.getInstance();
	public final static char DATA_SEPARATOR = ';';

	public ReadingQueueService(JanusServer server) {
		super(server);
	}

	/**
	 * Passer au morceau suivant dans le fil de lecture
	 */
	public void next() {
		readingQueue.getCurrentTrack().deleteObservers();

		int trackIndex = readingQueue.getCurrentTrackPosition();
		if ((trackIndex + 1 < readingQueue.size()) || readingQueue.isRandomised()) {
			changeTrack(true);
		} else {
			readingQueue.setCurrentTrackPostion(0);
		}
		readingQueue.getCurrentTrack().addObserver(this);
		readingQueue.getCurrentTrack().play();

	}

	/**
	 * Passer au morceau précédent dans le fil de lecture
	 */
	public void prev() {
		readingQueue.getCurrentTrack().deleteObservers();

		int trackIndex = readingQueue.getCurrentTrackPosition();
		if (trackIndex - 1 >= 0) {
			changeTrack(false);
			readingQueue.getCurrentTrack().addObserver(this);
			readingQueue.getCurrentTrack().play();
		}
	}

	/**
	 * Activer/désactiver le passage aléatoire à un morceau
	 */
	public void play() {
		readingQueue.getCurrentTrack().deleteObservers();
		ReadingQueue.getInstance().getCurrentTrack().addObserver(this);
		readingQueue.getCurrentTrack().play();

	}

	public void pause() {
		readingQueue.getCurrentTrack().pause();

	}

	public void randomise() {
		if (readingQueue.isRandomised()) {
			readingQueue.setRandomised(false);
		} else {
			readingQueue.setRandomised(true);
		}
	}

	public void volumeUp() {
		Float volume = AudioUtility.getMasterOutputVolume();
		if (volume >= 0f && volume <= 0.9f) {
			AudioUtility.setMasterOutputVolume(volume + 0.1f);
		} else if (volume > 0.9 && volume < 1f) {
			AudioUtility.setMasterOutputVolume(1f);
		}

	}

	public void volumeDown() {
		Float volume = AudioUtility.getMasterOutputVolume();
		if (volume >= 0.1f && volume <= 1f) {
			AudioUtility.setMasterOutputVolume(volume - 0.1f);
		} else if (volume < 0.1f && volume > 0f) {
			AudioUtility.setMasterOutputVolume(0f);
		}

	}

	public String whatIsThisSong() {
		return readingQueue.getCurrentTrack().getName();

	}

	public String whatIsTheReadingQueue() {
		StringBuilder stringBuilder = new StringBuilder();
		int position = 0;
		for (Song audioFile : readingQueue.getAudioFileList()) 
		{	//Format NOM_FICHIER;bool_isCurrentTrack
			stringBuilder.append(audioFile.getFileName());
			if (readingQueue.getCurrentTrackPosition() == position) {
				stringBuilder.append(DATA_SEPARATOR + "1");
			} else {
				stringBuilder.append(DATA_SEPARATOR + "0");
			}
			stringBuilder.append("\n");
			position++;
		}
		return stringBuilder.toString();
	}

	/**
	 * Mettre fin au morceau en cours de lecture en fonction de
	 * <code>trackFlags</code>.
	 * 
	 * @param trackFlags
	 *            Le statut d'un fichier audio.
	 * 
	 * @see AudioFile, AudioFile.TrackFlags
	 */
	private void endCurrentTrack(TrackFlags trackFlags) {
		if (trackFlags.played) {
			if (trackFlags.paused) {
				readingQueue.getCurrentTrack().pause();
			}
			readingQueue.getCurrentTrack().stop();
		}
		if (trackFlags.muted) {
			readingQueue.getCurrentTrack().mute();
		}
		if (trackFlags.looped) {
			readingQueue.getCurrentTrack().loop();
		}
	}

	/**
	 * Changer le morceau selon <code>forward</code>, <code>isRandomised</code>
	 * et <code>trackFlags</code>.<br/>
	 * 
	 * @param forward
	 *            Si <code>forward = true</code> alors passe au morceau suivant,
	 *            sinon passe au morceau précédent.
	 * @param trackFlags
	 *            Le statut d'un fichier audio.
	 * 
	 * @see AudioFile, AudioFile.TrackFlags
	 */
	private void changeCurrentTrack(boolean forward) {
		int shiftInt;
		if (readingQueue.isRandomised()) {
			Random rand = new Random();
			shiftInt = rand.nextInt(readingQueue.size() - 1);
		} else if (forward) {
			shiftInt = readingQueue.getCurrentTrackPosition() + 1;
		} else {
			shiftInt = readingQueue.getCurrentTrackPosition() - 1;
		}
		readingQueue.setCurrentTrackPostion(shiftInt);
	}

	/**
	 * Changer le statut du morceau en cours de lecture en fonction de
	 * <code>trackFlags</code>.
	 * 
	 * @param trackFlags
	 *            Le statut d'un fichier audio.
	 * 
	 * @see AudioFile, AudioFile.TrackFlags
	 */
	private void changeCurrentTrackStatus(TrackFlags trackFlags) {
		if (trackFlags.muted) {
			readingQueue.getCurrentTrack().mute();
		}
		if (trackFlags.looped) {
			readingQueue.getCurrentTrack().loop();
		}

	}

	/**
	 * Changer le morceau selon <code>forward</code>.
	 * 
	 * @param forward
	 *            Si <code>forward = true</code> alors passe au morceau suivant,
	 *            sinon passe au morceau précedent.
	 */
	private void changeTrack(boolean forward) {
		TrackFlags previousTrackFlags = new TrackFlags(readingQueue.getCurrentTrack());

		endCurrentTrack(previousTrackFlags);
		changeCurrentTrack(forward);
		changeCurrentTrackStatus(previousTrackFlags);
	}

	/**
	 * Structure de données represantant l'etat d'un fichier audio.
	 * 
	 * @see AudioFile
	 * 
	 * @author Nicolas Symphorien
	 * @version 1.0
	 */
	private class TrackFlags {
		private boolean muted;
		private boolean looped;
		private boolean played;
		private boolean paused;

		/**
		 * Construire une structure de données représantant le fichier audio
		 * <code>track</code>.
		 * 
		 * @param track
		 *            le fichier audio a analyser.
		 * 
		 * @see AudioFile
		 */
		public TrackFlags(AudioFile track) {
			muted = track.isMuted();
			looped = track.isLooping();
			played = track.isPlaying();
			paused = track.isPaused();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof AudioFile) {
			String state = (String) arg;
			if ("EndOfPlay".equals(state)) {
				endOfPlay();
			} else if ("StopOfPlay".equals(state)) {
				stopOfPlay();
			}
		}

	}

	/**
	 * Événement qui survient à la fin de la lecture d'un morceau dans le fil de
	 * lecture.
	 */
	private void endOfPlay() {
		next();

		LOGGER.info("End of Play event : switch to next track");

	}

	/**
	 * Événement qui survient à la mise sur stop d'un morceau dans le fil de
	 * lecture.
	 */
	private void stopOfPlay() {
		LOGGER.info("Stop of Play event");
	}

	public void addToReadingQueue(List<Song> audioFileList) {
		readingQueue.addList(audioFileList);

	}

}
