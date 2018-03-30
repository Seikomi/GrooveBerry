package com.seikomi.grooveberry.utils;

import java.io.IOException;
import java.nio.file.Path;

import com.seikomi.janus.net.properties.JanusDefaultProperties;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

public class GrooveberryPropertiesFileGenerator extends JanusPropertiesFileGenerator {
	
	public static JanusProperties createServerPropertiesFile(Path serverPropertiesFilePath) throws IOException {
//		JanusProperties serverProperties = JanusPropertiesFileGenerator
//				.createServerPropertiesFile(serverPropertiesFilePath, JanusDefaultProperties.COMMAND_PORT.getPropertyValueAsInt(), 3009);
		JanusProperties serverProperties = new JanusProperties(serverPropertiesFilePath);
		serverProperties.getProperties().put("database.url","jdbc:h2:mem:db");
		serverProperties.getProperties().put("database.user", "sa");
		serverProperties.getProperties().put("database.pasword", "");

		return serverProperties;
	}

}
