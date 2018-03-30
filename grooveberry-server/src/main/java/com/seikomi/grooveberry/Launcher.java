package com.seikomi.grooveberry;

import java.io.IOException;
import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusProperties;

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
		JanusProperties serverProperties = new JanusProperties(Paths.get("server.properties"));
		new GrooveberryServer(serverProperties).start();
	}
}
