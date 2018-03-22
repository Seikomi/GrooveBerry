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
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

//TODO Use test property
public class NextTest {
	
	private GrooveberryServer server;
	private JanusServerProperties serverProperties;
	
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		Path fileDatabasePath = Paths.get(temporaryFolder.getRoot().getPath() + "dbNextTest");
		serverProperties = GrooveberryPropertiesFileGenerator.createServerPropertiesFile(serverPropertiesPath, fileDatabasePath.toString());
		server = new GrooveberryServer(serverProperties) {
			@Override
			protected void loadContext() {
				CommandsFactory.addCommand(new Next(), "#NEXT", this);
				Locator.load(new ReadingQueueService(this));
			}
		};
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		CommandsFactory.clear();
		ReadingQueue.getInstance().clearQueue();
		Locator.clear();
		ReadingQueue.getInstance().clearQueue();
		
//		server.stop();
		server = null;
		serverProperties = null;
	}

	@Test
	public void testNextCommandWithNoArgs() {
		String[] returns = CommandsFactory.executeCommand("#NEXT");
		assertArrayEquals(new String[] { "#NEXT OK" }, returns);
	}

}