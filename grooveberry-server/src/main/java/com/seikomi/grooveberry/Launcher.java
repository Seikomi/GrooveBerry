package com.seikomi.grooveberry;

import java.io.IOException;
import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusServerProperties;

/**
 * Hello world!
 *
 */
public class Launcher 
{
    public static void main(String[] args) throws IOException {
    	JanusServerProperties serverProperties = new JanusServerProperties(Paths.get("server.properties"));
		new GrooveberryServer(serverProperties).start();
    }
}
