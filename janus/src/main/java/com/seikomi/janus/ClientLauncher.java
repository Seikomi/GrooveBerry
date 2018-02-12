package com.seikomi.janus;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.JanusClient;
import com.seikomi.janus.net.properties.JanusClientProperties;

/**
 * Main class of Janus client.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class ClientLauncher implements Observer {
	static final Logger LOGGER = LoggerFactory.getLogger(ClientLauncher.class);
	private JanusClient client;
	private Scanner scanner;

	private ClientLauncher() {
		Path propertiesFilePah = Paths.get("client.properties");
		try {
			JanusClientProperties clientProperties = new JanusClientProperties(propertiesFilePah);
			client = new JanusClient(clientProperties);
			client.start();
			LOGGER.info("Janus client started and connecting to the port " + client.getCommandPort() + " for commands");

			scanner = new Scanner(System.in);
			
			String command;
			do {
				command = scanner.nextLine();
				client.executeCommand(command);
			} while(!"#EXIT".equals(command));

		} catch (IOException e) {
			LOGGER.error("An unknown error occurs during the reading of Janus client properties file", e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		String command = scanner.nextLine();
		client.executeCommand(command);
	}

	/**
	 * Starts the Janus client.
	 * 
	 * @param args
	 *            the arguments : not require
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		new ClientLauncher();
	}

}
