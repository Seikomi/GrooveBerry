package com.seikomi.grooveberry;

import java.io.IOException;
import java.nio.file.Paths;

import com.seikomi.janus.net.properties.JanusProperties;

/**
 * Hello world!
 *
 */
public class Launcher 
{
    public static void main(String[] args) throws IOException {
		new GrooveberryServer(new JanusProperties(Paths.get("server.properties"))).start();
    }
}
