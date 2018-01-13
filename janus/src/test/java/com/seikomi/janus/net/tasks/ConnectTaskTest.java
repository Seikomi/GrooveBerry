package com.seikomi.janus.net.tasks;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

public class ConnectTaskTest {

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
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();

		serverProperties = null;
		server = null;
	}

	@Test
	public void testOneClientConnection() throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(InetAddress.getLocalHost(), JanusServerProperties.readProperties().getCommandPort());
		boolean isConnected = clientSocket.isConnected() && clientSocket.isBound();

		assertTrue(isConnected);

		clientSocket.close();
	}
	
	@Test
	public void testMultiClientConnection() throws UnknownHostException, IOException, InterruptedException {
		
		final int NUMBER_OF_CLIENT = 5;
		Socket[] clientsSockets = new Socket[NUMBER_OF_CLIENT];
		for (int i = 0; i < clientsSockets.length; i++) {
			clientsSockets[i] = new Socket(InetAddress.getLocalHost(), JanusServerProperties.readProperties().getCommandPort());
			boolean isConnected = clientsSockets[i].isConnected() && clientsSockets[i].isBound();
			assertTrue(isConnected);
		}
		for (Socket socket : clientsSockets) {
			socket.close();
		}
	}

}
