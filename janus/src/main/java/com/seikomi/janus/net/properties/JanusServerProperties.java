package com.seikomi.janus.net.properties;

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
	public JanusServerProperties(Path propertiesFilePath) throws IOException {
		super(propertiesFilePath);
	}

}
