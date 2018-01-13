package com.seikomi.janus.net.properties;

import java.net.URL;

import com.seikomi.janus.net.JanusServerTest;

public class TestUtils {
	
	public static URL getServerPropertiesURL() {
		return JanusServerTest.class.getResource("serverTest.properties");
	}
	
	public static <T> URL getURL(Class<T> targetedClass, String filename ) {
		return targetedClass.getResource(filename);
	}

}
