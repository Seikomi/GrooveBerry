package com.seikomi.janus.net.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.janus.net.JanusClient;
import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusClientProperties;
import com.seikomi.janus.net.properties.JanusDefaultProperties;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.properties.JanusServerProperties;
import com.seikomi.janus.utils.JanusPropertiesFileGenerator;

/**
 * Functional test.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
public class DataTransfertTaskTest {
	private JanusServer server;
	private JanusClient client;

	private File serverFolder;
	private File clientFolder;

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
		Path clientPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "clientTest.properties");

		serverFolder = Paths.get(temporaryFolder.getRoot().getPath() + "/serverFiles/").toFile();
		clientFolder = Paths.get(temporaryFolder.getRoot().getPath() + "/clientFile/").toFile();

		serverFolder.mkdirs();
		clientFolder.mkdirs();

		JanusProperties[] propertiesFiles = JanusPropertiesFileGenerator.createJanusProperties(serverPropertiesPath,
				clientPropertiesPath);
		propertiesFiles[0].getProperties().setProperty(JanusDefaultProperties.RECEPTION_DIRECTORY.getPropertyName(),
				serverFolder.getAbsolutePath());
		propertiesFiles[1].getProperties().setProperty(JanusDefaultProperties.RECEPTION_DIRECTORY.getPropertyName(),
				clientFolder.getAbsolutePath());

		JanusServerProperties serverProperties = (JanusServerProperties) propertiesFiles[0];
		server = new JanusServer(serverProperties) {
			@Override
			protected void loadContext() {
				// Nothing to do
			}

		};
		server.start();

		JanusClientProperties clientProperties = (JanusClientProperties) propertiesFiles[1];
		client = new JanusClient(clientProperties);
		client.start();
	}

	@After
	public void tearDown() throws Exception {
		client.stop();
		server.stop();

		client = null;
		server = null;

		serverFolder = null;
		clientFolder = null;
	}

	@Test
	public void testDownloadTransfertBeetweenClientAndServer() throws InterruptedException {
		await().atMost(Duration.ONE_SECOND).until(testDownload());
	}

	private Callable<Boolean> testDownload() {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				client.executeCommand("#DOWNLOAD LICENSE");
				try {
					DirectoryStream<Path> stream = Files.newDirectoryStream(clientFolder.toPath());
					Iterator<Path> iterator = stream.iterator();
					assertTrue(iterator.hasNext());
					assertEquals("LICENSE", iterator.next().getFileName().toString());
					return true;
				} catch (IOException e) {
					fail(e.getMessage());
					return false;
				}
			}
		};
	}

	@Test
	public void testUploadTransfertBeetweenClientAndServer() throws InterruptedException {
		Thread.sleep(1000);
		client.executeCommand("#UPLOAD LICENSE");
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(serverFolder.toPath());
			Iterator<Path> iterator = stream.iterator();
			assertTrue(iterator.hasNext());
			assertEquals("LICENSE", iterator.next().getFileName().toString());
//			return true;
		} catch (IOException e) {
			fail(e.getMessage());
//			return false;
		}
	}

	private Callable<Boolean> testUpload() {
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				client.executeCommand("#UPLOAD LICENSE");
				try {
					DirectoryStream<Path> stream = Files.newDirectoryStream(serverFolder.toPath());
					Iterator<Path> iterator = stream.iterator();
					assertTrue(iterator.hasNext());
					assertEquals("LICENSE", iterator.next().getFileName().toString());
					return true;
				} catch (IOException e) {
					fail(e.getMessage());
					return false;
				}

			}
		};

	}

}
