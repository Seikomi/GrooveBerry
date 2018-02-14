package com.seikomi.grooveberry.utils;

import java.io.IOException;
import java.nio.file.Path;

import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

public class GrooveberryPropertiesFileGenerator extends JanusPropertiesFileGenerator {
	
	public static JanusServerProperties createServerPropertiesFile(Path serverPropertiesFilePath, String filePath) throws IOException {
		JanusServerProperties serverProperties = JanusPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesFilePath);
		serverProperties.getProperties().put("database.url","jdbc:h2:file:" + filePath);
		serverProperties.getProperties().put("database.user", "sa");
		serverProperties.getProperties().put("database.pasword", "");

		return serverProperties;
	}

}
