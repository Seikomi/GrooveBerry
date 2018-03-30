package com.seikomi.grooveberry.commands;

import static org.junit.Assert.assertArrayEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.grooveberry.GrooveberryServer;
import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.grooveberry.utils.GrooveberryPropertiesFileGenerator;
import com.seikomi.janus.commands.CommandsFactory;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.services.Locator;

//TODO Use test property
public class PlayTest {
	
	private GrooveberryServer server;
	private JanusProperties serverProperties;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		serverProperties = GrooveberryPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesPath);
		server = new GrooveberryServer(serverProperties) {
			@Override
			protected void loadContext() {
				CommandsFactory.addCommand(new Play(), "#PLAY", this);
				Locator.load(new ReadingQueueService(this));
			}
		};
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		CommandsFactory.clear();
		Locator.clear();
		ReadingQueue.getInstance().clearQueue();
		
//		server.stop();
		server = null;
		serverProperties = null;
	}

	@Test
	public void testPlayCommandWithNoArgs() {
		String[] returns = CommandsFactory.executeCommand("#PLAY");
		assertArrayEquals(new String[] { "#PLAY OK" }, returns);
	}

}
