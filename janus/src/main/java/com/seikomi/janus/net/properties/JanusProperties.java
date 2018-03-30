package com.seikomi.janus.net.properties;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Properties;

import com.seikomi.janus.utils.Utils;

/**
 * This class is the main set of properties use to initialize a Janus
 * application. It is link to a {@code Properties} class, provide automatic
 * properties file creation on file system and check if the properties set in
 * the file are the one expected by the server and give direct access on
 * property with a type conversion.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class JanusProperties {

	/** The {@code Properties} instance use to store application properties. */
	protected Properties properties;

	/**
	 * Create a new instance of Janus application properties file with the
	 * properties set in the file at the {@code propertiesFilePath}. If it
	 * doesn't exist, create a new file and set the properties to her default
	 * values described in JanusDefaultProperties.
	 * 
	 * @param propertiesFilePath
	 *            the properties file path
	 * @throws IOException
	 *             if an error occurs at the creation or at the reading of the
	 *             read the property file properties
	 */
	public JanusProperties(Path propertiesFilePath) throws IOException {
		properties = new Properties();

		File propertiesFile = propertiesFilePath.toFile();
		if (!propertiesFile.exists()) {
			createDefaultPropertiesFile(propertiesFile);
		}

		readProperties(propertiesFile);
	}

	/**
	 * Create the properties file on file system with default values.
	 * 
	 * @param propertiesFile
	 *            the properties file to create
	 * @throws IOException
	 *             if an error occurs at the creation the property file
	 *             properties
	 */
	private void createDefaultPropertiesFile(File propertiesFile) throws IOException {
		Writer writer = new FileWriter(propertiesFile);

		Properties defaultProperties = new Properties();
		for (JanusDefaultProperties defaultProperty : JanusDefaultProperties.values()) {
			defaultProperties.setProperty(defaultProperty.getPropertyName(), defaultProperty.getPropertyValue());
		}
		defaultProperties.store(writer, "Application properties file");
	}

	/**
	 * Read the properties in the properties file.
	 * 
	 * @param propertiesFile
	 *            the properties file
	 * @throws IOException
	 *             if an error occurs at the reading of the read the property
	 *             file properties
	 */
	private void readProperties(File propertiesFile) throws IOException {
		Reader reader = new FileReader(propertiesFile);
		properties.load(reader);

		if (!isValid()) {
			throw new IOException("one or many properties are malformed");
		}

	}

	/**
	 * Check if the properties file is well formed.
	 * 
	 * @return {@code true} if all properties is well formed, {@code false}
	 *         otherwise.
	 */
	private boolean isValid() {
		boolean valid = true;

		for (JanusDefaultProperties defaultProperty : JanusDefaultProperties.values()) {
			String propertyName = defaultProperty.getPropertyName();
			String property = properties.getProperty(propertyName);
			
			if (property == null) {
				valid &= false;
				break;
			}

			boolean containsKeys = properties.containsKey(propertyName);
			boolean isInteger = Utils.isInteger(property);

			switch (defaultProperty) {
			case COMMAND_PORT:
			case DATA_PORT:
				valid &= containsKeys && isInteger;
				break;

			default:
				// Nothing to do
				break;
			}

		}

		return valid;
	}

	/**
	 * Gets the properties file
	 * 
	 * @return the properties file
	 */
	public Properties getProperties() {
		return properties;
	}

}
