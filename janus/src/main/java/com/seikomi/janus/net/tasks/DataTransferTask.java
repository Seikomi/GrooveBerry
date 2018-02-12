package com.seikomi.janus.net.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.utils.Utils;
import com.seikomi.janus.utils.Utils.Pair;

/**
 * Janus task to handle files transmission between a server and a client. This
 * task open the data port, sends and receives files and close the data port
 * when the task is finish.
 * 
 * This task use a Deque structure read in FIFO to handle addition of file to
 * receive or send during a transfer already in progress.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class DataTransferTask extends JanusTask {
	static final Logger LOGGER = LoggerFactory.getLogger(ConnectTask.class);

	private static final int BUFFER_SIZE = 1024; // 1 Kio Buffer

	private ServerSocket dataServerSocket;

	private BufferedOutputStream out;
	private BufferedInputStream in;
	
	private String receptionDirectory;
	private int dataPort;

	private Deque<Pair<String, Boolean>> filesDeque;

	/**
	 * Construct a data transfert task associated with the Janus server to send
	 * the files referenced by their file names if {@code isDownloadTransfert}
	 * is {@code true}, receive the files otherwise.
	 * 
	 * @param server
	 *            the Janus server
	 * @param fileNames
	 *            an array of file names of files to transmit
	 * @param isDownloadTransfert
	 *            flag to indicate the direction of the data transfer :
	 *            {@code true} for sending and {@code false} to receiving.
	 */
	public DataTransferTask(String[] fileNames, String receptionDirectory, int dataPort, boolean isDownloadTransfert) {
		this.receptionDirectory = receptionDirectory;
		this.dataPort = dataPort;
		this.filesDeque = new ArrayDeque<>();

		if (fileNames == null || fileNames.length == 0) {
			LOGGER.debug("No files to send are defined.");
		} else {
			for (int i = 0; i < fileNames.length; i++) {
				this.filesDeque.push(new Pair<String, Boolean>(fileNames[i], isDownloadTransfert));
			}
		}
	}

	/**
	 * Open the data port and wait for connection.
	 */
	@Override
	protected void beforeLoop() {
		try {
			dataServerSocket = new ServerSocket(dataPort);
			Socket dataSocket = dataServerSocket.accept();
			out = new BufferedOutputStream(dataSocket.getOutputStream(), BUFFER_SIZE);
			in = new BufferedInputStream(dataSocket.getInputStream(), BUFFER_SIZE);
		} catch (IOException e) {
			LOGGER.error("An error occurs during initialization of data transmission", e);
		}
	}

	/**
	 * Sends each files if they exists on file system or receive files from a
	 * client.
	 * @throws IOException 
	 */
	@Override
	protected void loop() {
		if (!filesDeque.isEmpty()) {
			Pair<String, Boolean> file = filesDeque.pop();
			if (file.getRight()) {
				File fileToSend = new File(file.getLeft());
				if (fileToSend.exists()) {
					sendFile(fileToSend);
				} else {
					LOGGER.debug(file.getLeft() + " not found");
				}
			} else {
				try {
					receiveFile(file.getLeft());
				} catch (IOException e) {
					LOGGER.debug(file.getLeft() + " not found");
				}
			}
		} else {
			endLoop();
		}
	}

	/**
	 * Close the data port when all files has been send or receive.
	 */
	@Override
	protected void afterLoop() {
		try {
			out.close();
			dataServerSocket.close();
		} catch (IOException e) {
			LOGGER.error("An error occurs during the closing of data transmission pipe", e);
		}
	}

	/**
	 * Sends the file locate to {@code path} on file system on data port channel
	 * in binary with the following format :
	 * {@code [filelength on 8 bytes (LONG)] + [data]}.
	 * 
	 * @param fileToSend
	 *            the file path
	 */
	private void sendFile(File fileToSend) {
		try (final BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(fileToSend),
				BUFFER_SIZE)) {
			// Header (file length)
			byte[] header = Utils.longToBytes(fileToSend.length());
			out.write(header);

			// Data
			byte[] bytes = new byte[BUFFER_SIZE];
			int numberOfByteRead;
			do {
				numberOfByteRead = fileInputStream.read(bytes);
				if (numberOfByteRead != -1) {
					out.write(bytes, 0, numberOfByteRead);
				}
			} while (numberOfByteRead != -1);

			out.flush();
		} catch (IOException e) {
			LOGGER.error("An error occurs during the sending of data", e);
		}
	}

	/**
	 * Receives the file from a client and write it on the file system in the
	 * directory referenced by the {@code serverFileRootDirectory} property. The
	 * data stream from the client must be on the following format :
	 * {@code [filelength on 8 bytes (LONG)] + [data]}.
	 * 
	 * @param path
	 *            the file path
	 * @throws IOException 
	 */
	private void receiveFile(String path) throws IOException {;
		File file = new File(receptionDirectory + "\\" + path);

		try (final BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(file),
				BUFFER_SIZE)) {
			// Header (file length)
			byte[] header = new byte[Long.BYTES];
			in.read(header, 0, header.length);
			long fileLenth = Utils.bytesToLong(header);

			// Data
			byte[] bytes = new byte[BUFFER_SIZE];

			long numberOfByteRead = 0;
			do {
				if (numberOfByteRead + BUFFER_SIZE <= fileLenth) {
					in.read(bytes, 0, BUFFER_SIZE);
					numberOfByteRead += BUFFER_SIZE;
				} else {
					int numberOfBytesToReadRemaining = Math.toIntExact(fileLenth - numberOfByteRead);
					in.read(bytes, 0, numberOfBytesToReadRemaining);
					numberOfByteRead += numberOfBytesToReadRemaining;
				}
				fileOutputStream.write(bytes);
				fileOutputStream.flush();
			} while (numberOfByteRead < fileLenth);
		} catch (IOException e) {
			LOGGER.error("An error occurs during the reception of data", e);
		}
	}

	/**
	 * Adds files in transfer queue.
	 * 
	 * @param fileNames
	 *            the files names
	 * @param isDownloadTransfert
	 *            {@code true} to dowload files, {@code false} to upload files
	 */
	public void addFiles(String[] fileNames, boolean isDownloadTransfert) {
		for (int i = 0; i < fileNames.length; i++) {
			filesDeque.push(new Pair<String, Boolean>(fileNames[i], isDownloadTransfert));
		}
	}
}
