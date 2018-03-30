package com.seikomi.janus.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Properties;

import com.seikomi.janus.net.properties.JanusDefaultProperties;
import com.seikomi.janus.net.properties.JanusProperties;

/**
 * Static class to generate the server and client properties files with command
 * and data port pick in available ports.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class JanusPropertiesFileGenerator {

	private static String commandPort;
	private static String dataPort;

	protected JanusPropertiesFileGenerator() {
		// Hide the public constructor.
	}

	public static JanusProperties createServerPropertiesFile(Path serverPropertiesFilePath, int commandPort,
			int dataPort) throws IOException {
		JanusPropertiesFileGenerator.commandPort = Integer.toString(commandPort);
		JanusPropertiesFileGenerator.dataPort = Integer.toString(dataPort);
		return createServerPropertyFile(serverPropertiesFilePath);
	}

	private static JanusProperties createServerPropertyFile(Path serverPropertiesFilePath) throws IOException {
		JanusProperties serverProperties = new JanusProperties(serverPropertiesFilePath);
		useActualPickedPort(serverProperties.getProperties());
		return serverProperties;
	}

	/**
	 * Create a new {@code server.propertie} file with command and data port pick in
	 * available ports.
	 * 
	 * @param serverPropertiesFilePath
	 * @throws IOException
	 */
	public static JanusProperties createServerPropertiesFile(Path serverPropertiesFilePath) throws IOException {
		pickFreePort();
		return createServerPropertyFile(serverPropertiesFilePath);
	}

	/**
	 * Create a new {@code client.propertie} file with command and data port pick in
	 * available ports.
	 * 
	 * @param clientPropertiesFilePath
	 * @throws IOException
	 */
	public static JanusProperties createClientPropertiesFile(Path clientPropertiesFilePath) throws IOException {
		pickFreePort();
		return createServerPropertyFile(clientPropertiesFilePath);
	}

	public static JanusProperties[] createJanusProperties(Path serverPropertiesFilePath, Path clientPropertiesFilePath)
			throws IOException {
		pickFreePort();

		JanusProperties janusServerPropertiesFile = new JanusProperties(serverPropertiesFilePath);
		useActualPickedPort(janusServerPropertiesFile.getProperties());

		JanusProperties janusClientPropertiesFile = new JanusProperties(clientPropertiesFilePath);
		useActualPickedPort(janusClientPropertiesFile.getProperties());

		return new JanusProperties[] { janusServerPropertiesFile, janusClientPropertiesFile };
	}

	private static void useActualPickedPort(Properties propertiesFile) {
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
