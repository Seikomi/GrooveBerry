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
import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Main class of Janus client.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class ClientLauncher implements Observer {
	static final Logger LOGGER = LoggerFactory.getLogger(ClientLauncher.class);
	private static JanusClient client;
	private Scanner scanner;

	private ClientLauncher() throws InterruptedException {
		Path propertiesFilePah = Paths.get("client.properties");
		try {
			JanusClientProperties clientProperties = new JanusClientProperties(propertiesFilePah);
			JanusServerProperties.loadProperties(Paths.get("client.properties"));
			client = new JanusClient(clientProperties);
			client.start();
			LOGGER.info("Janus client started and connecting to the port " + client.getCommandPort() + " for commands");
			client.addObserver(this); //FIXME synchronization !!!

			scanner = new Scanner(System.in);

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
	public static void main(String[] args) throws InterruptedException {
		new ClientLauncher();
	}

}
