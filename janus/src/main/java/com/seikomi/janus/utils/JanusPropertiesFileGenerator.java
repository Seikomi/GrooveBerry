package com.seikomi.janus.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Properties;

import com.seikomi.janus.net.properties.JanusClientProperties;
import com.seikomi.janus.net.properties.JanusDefaultProperties;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Static class to generate the server and client properties files with command
 * and data port pick in available ports.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public final class JanusPropertiesFileGenerator {

	private static String commandPort;
	private static String dataPort;

	private JanusPropertiesFileGenerator() {
		// Hide the public constructor.
	}

	/**
	 * Create a new {@code server.propertie} file with command and data port
	 * pick in available ports.
	 * 
	 * @param serverPropertiesFilePath
	 * @throws IOException
	 */
	public static JanusServerProperties createServerPropertiesFile(Path serverPropertiesFilePath) throws IOException {
		pickFreePort();
		
		JanusServerProperties.loadProperties(serverPropertiesFilePath);

		useActualPickedPort(JanusServerProperties.readProperties().getProperties());

		return JanusServerProperties.readProperties();
	}

	/**
	 * Create a new {@code client.propertie} file with command and data port
	 * pick in available ports.
	 * 
	 * @param clientPropertiesFilePath
	 * @throws IOException
	 */
	public static JanusClientProperties createClientPropertiesFile(Path clientPropertiesFilePath) throws IOException {
		pickFreePort();
		
		JanusClientProperties janusClientPropertiesFile = new JanusClientProperties(clientPropertiesFilePath);

		useActualPickedPort(janusClientPropertiesFile.getProperties());

		return janusClientPropertiesFile;
	}

	public static JanusProperties[] createJanusProperties(Path serverPropertiesFilePath, Path clientPropertiesFilePath) throws IOException {
		pickFreePort();
		
		JanusServerProperties.loadProperties(serverPropertiesFilePath);
		useActualPickedPort(JanusServerProperties.readProperties().getProperties());

		JanusClientProperties janusClientPropertiesFile = new JanusClientProperties(clientPropertiesFilePath);
		useActualPickedPort(janusClientPropertiesFile.getProperties());
		
		return new JanusProperties[] { JanusServerProperties.readProperties(), janusClientPropertiesFile };
	}
	
	private static void useActualPickedPort(Properties propertiesFile) throws IOException {
		propertiesFile.setProperty(JanusDefaultProperties.COMMAND_PORT.getPropertyName(), commandPort);
		propertiesFile.setProperty(JanusDefaultProperties.DATA_PORT.getPropertyName(), dataPort);
	}
	
	private static void pickFreePort() throws IOException {
		commandPort = findFreePort();
		dataPort = findFreePort();
	}

	/**
	 * Returns a free port number on localhost.
	 * 
	 * @return a free port number on localhost
	 * @throws IOException
	 * @throws IllegalStateException
	 *             if unable to find a free port
	 */
	private static String findFreePort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			socket.setReuseAddress(true);
			return String.valueOf(socket.getLocalPort());
		} catch (IOException e) {
			throw e;
		}
	}

}
