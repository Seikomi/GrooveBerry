package com.seikomi.janus.commands;

import static org.junit.Assert.assertArrayEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

public class UploadTest {
	
	private JanusServer server;
	private JanusServerProperties serverProperties;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		serverProperties = JanusPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesPath);
		JanusServerProperties.loadProperties(serverPropertiesPath);
		server = new JanusServer() {
			@Override
			protected void loadContext() {
				//Nothing to do
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
		CommandsFactory.clear();
		server = null;
		serverProperties = null;
	}

	@Test
	public void testDownloadCommandWithNoArgs() {
		CommandsFactory.addCommand(new Upload(), "#UPLOAD", server);
		String[] returns = CommandsFactory.executeCommand("#UPLOAD");
		assertArrayEquals(new String[] { "#UPLOAD NO FILES TO RECEIVE" }, returns);

	}
	
	@Test
	public void testDownloadCommandWithArgs() {
		CommandsFactory.addCommand(new Upload(), "#UPLOAD", server);
		String[] returns = CommandsFactory.executeCommand("#UPLOAD testFile");
		assertArrayEquals(new String[] { "#UPLOAD STARTED" }, returns);
		//TODO test transfert OK
	}

}
