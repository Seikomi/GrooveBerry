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
import com.seikomi.janus.net.properties.JanusProperties;

public class DownloadTest {

	private JanusServer server;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		JanusProperties serverProperties = new JanusProperties(serverPropertiesPath);
		server = new JanusServer(serverProperties) {
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
	}

	@Test
	public void testDownloadCommandWithNoArgs() {
		CommandsFactory.addCommand(new Download(), "#DOWNLOAD", server);
		String[] returns = CommandsFactory.executeCommand("#DOWNLOAD");
		assertArrayEquals(new String[] { "#DOWNLOAD NO FILES TO SEND" }, returns);

	}
	
	@Test
	public void testDownloadCommandWithArgs() {
		CommandsFactory.addCommand(new Download(), "#DOWNLOAD", server);
		String[] returns = CommandsFactory.executeCommand("#DOWNLOAD testFile");
		assertArrayEquals(new String[] { "#DOWNLOAD STARTED" }, returns);
	}

}
