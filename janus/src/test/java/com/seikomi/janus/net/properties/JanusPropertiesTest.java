package com.seikomi.janus.net.properties;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

/** TODO reactivate **/
public class JanusPropertiesTest {

//	private final static URL PROPERTIES_URL = TestUtils.getServerPropertiesURL();
	private final static URL PROPERTIES_MALFORMED_01_URL = TestUtils.getURL(JanusPropertiesTest.class,
			"serverTestMalformed.properties");

//	private static final int DATA_PORT_EXPECTED = JanusDefaultProperties.DATA_PORT.getPropertyValueAsInt();
//	private static final int COMMAND_PORT_EXPECTED = JanusDefaultProperties.COMMAND_PORT.getPropertyValueAsInt();
//
//	@Rule
//	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		// Nothing to initialize before test, each test has it's own
		// serverProperties
	}

//	@Test
//	public void testJanusPropertiesWithNoExistingPropertiesFile() throws Exception {
//		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "testCreateProperties.properties");
//		JanusProperties serverProperties = new JanusProperties(serverPropertiesPath);
//
//		assertEquals(COMMAND_PORT_EXPECTED, serverProperties.getCommandPort());
//		assertEquals(DATA_PORT_EXPECTED, serverProperties.getDataPort());
//
//		assertTrue(serverPropertiesPath.toFile().exists());
//
//	}
//
//	@Test
//	public void testJanusPropertiesWithExistingPropertiesFile() throws Exception {
//		Path serverPropertiesPath = Paths.get(PROPERTIES_URL.toURI());
//		JanusProperties serverProperties = new JanusProperties(serverPropertiesPath);
//
//		assertEquals(COMMAND_PORT_EXPECTED, serverProperties.getProperties().getProperty(""));
//		assertEquals(DATA_PORT_EXPECTED, serverProperties.getDataPort());
//
//	}

	@Test(expected = IOException.class)
	public void testMalFormedJanusPropertiesWithValueNotValid() throws Exception {
		Path serverPropertiesPath = Paths.get(PROPERTIES_MALFORMED_01_URL.toURI());
		new JanusProperties(serverPropertiesPath);
	}

}
