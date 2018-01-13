package com.seikomi.grooveberry;

import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Hello world!
 *
 */
public class Launcher 
{
    public static void main(String[] args) {
    	JanusServerProperties.loadProperties(Paths.get("server.properties"));
		new GrooveberryServer().start();
    }
}
