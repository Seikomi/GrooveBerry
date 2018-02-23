package com.seikomi.grooveberry;

import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Launch the GrooveBerry server.
 */
public class Launcher {
	
	private Launcher() {
		// Nothing to do
	}
	
	/**
	 * Launch the GrooveBerry server.
	 * @param args no args 
	 */
    public static void main(String[] args) {
    	JanusServerProperties.loadProperties(Paths.get("server.properties"));
		new GrooveberryServer().start();
    }
}
