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
import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.services.Locator;

//TODO Use test property
public class PauseTest {
	
	private GrooveberryServer server;
	private JanusServerProperties serverProperties;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		Path fileDatabasePath = Paths.get(temporaryFolder.getRoot().getPath() + "dbPauseTest");
		serverProperties = GrooveberryPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesPath, fileDatabasePath.toString());
		server = new GrooveberryServer(serverProperties) {
			@Override
			protected void loadContext() {
				CommandsFactory.addCommand(new Pause(), "#PAUSE", this);
				Locator.load(new ReadingQueueService(this));
			}
		};
		server.start();
		
		ReadingQueue.getInstance();
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
	public void testPauseCommandWithNoArgs() {
		String[] returns = CommandsFactory.executeCommand("#PAUSE");
		assertArrayEquals(new String[] { "#PAUSE OK" }, returns);
	}

}
