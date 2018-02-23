package com.seikomi.grooveberry;

import java.io.IOException;
import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Launch the GrooveBerry server.
 */
public class Launcher {
	
	/**
	 * Launch the GrooveBerry server.
	 * 
	 * @param args
	 *            no args
	 */
	public static void main(String[] args) throws IOException {
		JanusServerProperties serverProperties = new JanusServerProperties(Paths.get("server.properties"));
		new GrooveberryServer(serverProperties).start();
	}
}
