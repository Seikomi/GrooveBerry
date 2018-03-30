package com.seikomi.janus;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusProperties;

/**
 * Main class of Janus server.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class ServerLauncher {
	static final Logger LOGGER = LoggerFactory.getLogger(ServerLauncher.class);

	private JanusServer server;

	public ServerLauncher() {
		Path propertiesFilePath = Paths.get("server.properties");

		try {
			JanusProperties serverProperties = new JanusProperties(propertiesFilePath);
			server = new JanusServer(serverProperties) {

				@Override
				protected void loadContext() {
					// Nothing to load
				}

			};
			server.start();
			LOGGER.info("Janus server started and listening on ports : {} for commands and {} for data",
					server.getProperties("server.ports.command"), server.getProperties("server.ports.data"));
		} catch (IOException e) {
			LOGGER.error("An unknown error occurs during the reading of Janus server properties file", e);
		}
	}

	/**
	 * Starts the Janus server with the {@code server.properties} file.
	 * 
	 * @param args
	 *            the arguments : not require
	 */
	public static void main(String[] args) {
		new ServerLauncher();
	}
}
