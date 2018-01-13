package com.seikomi.janus.net.tasks;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.janus.net.JanusClient;
import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusClientProperties;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

/**
 * TODO reactivate
 * 
 * Functional test.
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 * 
 *
 */
@Ignore
public class DataTransfertTaskTest {

	private JanusServer server;
	private JanusServerProperties serverProperties;

	private JanusClient client;
	private JanusClientProperties clientProperties;

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		Path clientPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "clientTest.properties");
		JanusProperties[] propertiesFiles = JanusPropertiesFileGenerator.createJanusProperties(serverPropertiesPath,
				clientPropertiesPath);

		serverProperties = (JanusServerProperties) propertiesFiles[0];
		JanusServerProperties.loadProperties(serverPropertiesPath);
		server = new JanusServer() {
			@Override
			protected void loadContext() {
				//Nothing to do
			}
			
		};
		server.start();
		
		clientProperties = (JanusClientProperties) propertiesFiles[1];
		client = new JanusClient(clientProperties);
		client.start();
		
		client.addObserver(new ClientObserver());
	}

	@After
	public void tearDown() throws Exception {
		client.stop();
		server.stop();
		
		client = null;
		server = null;
	}

	@Test
	public void testDownloadTransfertBeetweenClientAndServer() throws InterruptedException {
		client.executeCommand("#Test LICENSE");
	}
	
	@Test
	public void testUploadTransfertBeetweenClientAndServer() throws InterruptedException {
		client.executeCommand("#UPLOAD LICENSE");
	}

}
