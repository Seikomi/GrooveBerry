package com.seikomi.janus.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.tasks.AskConnectionTask;
import com.seikomi.janus.utils.Utils;

/**
 * Implementation of a socket client. It provide basic access to a Janus Server.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class JanusClient implements NetworkApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(JanusClient.class);

	private static final int BUFFER_SIZE = 1024;

	private JanusProperties clientProperties;
	private AskConnectionTask askConnectionTask;

	/**
	 * Create a new instance of Janus client and configure it with the
	 * {@code .properties} file pass in argument.
	 * 
	 * @param clientProperties
	 *            the client {@code .properties} file
	 */
	public JanusClient(JanusProperties clientProperties) {
		this.clientProperties = clientProperties;
	}

	/**
	 * Start the Janus client and ask for connection to the server.
	 */
	@Override
	public void start() {
		askConnectionTask = new AskConnectionTask(Utils.convertStringToInt(getProperties("server.ports.command")));

		Thread askConnectionThread = new Thread(askConnectionTask, "AskConnectionThread");
		askConnectionThread.start();

		LOGGER.debug("Janus client start on port " + getProperties("server.ports.command") + " for command. ");
	}

	/**
	 * Restart the Janus client.
	 */
	@Override
	public void restart() {
		if (askConnectionTask != null) {
			askConnectionTask.restart();
			LOGGER.info("Ask connection task has been restart.");
		} else {
			LOGGER.error("Ask connection task not running : start first before restart");
		}
	}

	/**
	 * Stop the Janus client, close all associated sockets.
	 */
	@Override
	public void stop() {
		if (askConnectionTask != null) {
			askConnectionTask.stop();
			LOGGER.info("Janus client has been stop.");
		} else {
			LOGGER.error("Ask connection not running : start first before stop");
		}
	}

	public void executeCommand(String command) {
		try {
			DataOutputStream out = new DataOutputStream(askConnectionTask.getOutputStream());

			out.writeUTF(command);
			out.flush();

			switch (command) {
			case "#UPLOAD LICENSE":
				uploadLicenceFile();
				break;
			case "#DOWNLOAD LICENSE":
				downloadLicenceFile();
				break;
			default:
				break;
			}
		} catch (IOException e) {
			LOGGER.error("An error occur during the treatment of a send command", e);
		}
	}

	private void downloadLicenceFile() throws IOException {
		try (Socket dataSocket = new Socket("localhost", Utils.convertStringToInt(getProperties("server.ports.data")))) {
			boolean isConnected = dataSocket.isConnected() && dataSocket.isBound();
			if (isConnected) {
				BufferedInputStream in = new BufferedInputStream(dataSocket.getInputStream());
				File file = new File(getProperties("server.directories.files") + "\\LICENSE");

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
		}

	}

	private void uploadLicenceFile() throws IOException {
		try (Socket dataSocket = new Socket("localhost",
				Utils.convertStringToInt(getProperties("server.ports.data")))) {
			boolean isConnected = dataSocket.isConnected() && dataSocket.isBound();
			if (isConnected) {

				File fileToSend = Paths.get("LICENSE").toFile();
				final BufferedOutputStream dataOut = new BufferedOutputStream(dataSocket.getOutputStream());

				try (final BufferedInputStream fileInputStream = new BufferedInputStream(
						new FileInputStream(fileToSend), BUFFER_SIZE)) {
					// Header (file length)
					byte[] header = Utils.longToBytes(fileToSend.length());
					dataOut.write(header);

					// Data
					byte[] bytes = new byte[BUFFER_SIZE];
					int numberOfByteRead;
					do {
						numberOfByteRead = fileInputStream.read(bytes);
						if (numberOfByteRead != -1) {
							dataOut.write(bytes, 0, numberOfByteRead);
						}
					} while (numberOfByteRead != -1);

					dataOut.flush();
				} catch (IOException e) {
					LOGGER.error("An error occurs during the sending of data", e);
				}
			}
		}
	}

	public void sendCommand(String command) throws IOException {
		askConnectionTask.getOutputStream().writeUTF(command);
		askConnectionTask.getOutputStream().flush();
	}

	@Override
	public String getProperties(String propertieName) {
		return clientProperties.getProperties().getProperty(propertieName, null);
	}

	public AskConnectionTask getAskConnectionTask() {
		return askConnectionTask;
	}
	
	


}
