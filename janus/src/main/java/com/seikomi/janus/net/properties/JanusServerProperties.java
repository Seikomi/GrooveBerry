package com.seikomi.janus.net.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This class is the main set of properties use to initialize the Janus server.
 * It is link to a {@code Properties} class, provide automatic properties file
 * creation on file system and check if the properties set in the file are the
 * one expected by the server and give direct access on property with a type
 * conversion.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class JanusServerProperties extends JanusProperties {

	private static JanusServerProperties instance;
	private static Path propertiesFilePath;

	/**
	 * Create a new instance of Janus server properties file with the properties set
	 * in the file at the {@code propertiesFilePath}. If it doesn't exist, create a
	 * new file and set the properties to her default values described in
	 * {@link JanusDefaultProperties}.
	 * 
	 * @param propertiesFilePath
	 *            the properties file path
	 * @throws IOException
	 *             if an error occurs at the creation or at the reading of the read
	 *             the property file properties
	 */
	private JanusServerProperties(Path propertiesFilePath) throws IOException {
		super(propertiesFilePath);
	}

	public static void loadProperties(Path propertiesFilePath) {
		if (!propertiesFilePath.equals(JanusServerProperties.propertiesFilePath)) {
			JanusServerProperties.propertiesFilePath = propertiesFilePath;
		}
	}

	public static JanusServerProperties readProperties() throws IOException {
		if (propertiesFilePath == null) {
			throw new FileNotFoundException("A properties file must be load with the loadProperties() method"
					+ " of the JanusServerProperties singleton before calling this method");
		}
		if (instance == null) {
			instance = new JanusServerProperties(propertiesFilePath);
		}
		return instance;
	}
}
