package com.seikomi.janus.net.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class TreatmentTaskTest {

	private JanusServer server;
	private JanusServerProperties serverProperties;

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		serverProperties = new JanusServerProperties(serverPropertiesPath);
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

		serverProperties = null;
		server = null;
	}

	@Test
	public void testReceiveWelcomeMessageFromServer() throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(InetAddress.getLocalHost(), serverProperties.getCommandPort());
		boolean isConnected = clientSocket.isConnected() && clientSocket.isBound();

		if (isConnected) {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String serverResponce = in.readUTF();
			assertEquals(TreatmentTask.WELCOME_MESSAGE, serverResponce);
		}

		clientSocket.close();
	}

	@Test
	public void testSendMessageToServer() throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(InetAddress.getLocalHost(), serverProperties.getCommandPort());
		boolean isConnected = clientSocket.isConnected() && clientSocket.isBound();

		if (isConnected) {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			String serverResponce = in.readUTF();
			assertEquals(TreatmentTask.WELCOME_MESSAGE, serverResponce);

			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			String request = "TEST";
			out.writeUTF(request);

			serverResponce = in.readUTF();
			assertNotNull(serverResponce);

		}

		clientSocket.close();
	}

}
