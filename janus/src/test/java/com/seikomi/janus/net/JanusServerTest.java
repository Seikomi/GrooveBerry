package com.seikomi.janus.net;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;
/**
 * Functional tests ? or just change ports number ? that is the question.
 */
public class JanusServerTest {

	private JanusServer server;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		JanusProperties serverProperties = JanusPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesPath);
		server = new JanusServer(serverProperties) {
			@Override
			protected void loadContext() {
				//Nothing to do
			}
			
		};

		server.start();
	}
	
	@After
	public void tearDown() throws Exception {
		server.stop();
		server = null;
	}

	@Test
	public void testRestart() {
		server.restart();
	}

}
