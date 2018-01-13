package com.seikomi.janus.commands;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.properties.JanusServerProperties;

public class CommandFactoryTest {

	private JanusServer server;
	private JanusProperties serverProperties;

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		Path serverPropertiesPath = Paths.get(temporaryFolder.getRoot().getPath() + "serverTest.properties");
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

	@Test(expected = InvocationTargetException.class)
	public void testPrivateConstructor() throws Exception {
		Constructor<CommandsFactory> constructor = CommandsFactory.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testAddCommandWithNoArgs() {
		final String commandTestName = "#CMD_TEST";
		final String[] commandTestReturns = new String[] { "TEST" };

		CommandsFactory.addCommand(new JanusCommand() {

			@Override
			public String[] apply(String[] args) {
				return commandTestReturns;
			}
		}, commandTestName, server);

		String[] returns = CommandsFactory.executeCommand(commandTestName);
		assertArrayEquals(commandTestReturns, returns);

	}

	@Test
	public void testAddCommandWithArgs() {
		final String commandTestName = "#CMD_TEST2";
		final String arg1 = "hello";
		final String arg2 = "world";
		final String[] commandTestReturns = new String[] { "TEST2", arg1, arg2 };

		CommandsFactory.addCommand(new JanusCommand() {

			@Override
			public String[] apply(String[] args) {
				assertEquals(2, args.length);
				return new String[] { commandTestReturns[0], arg1, arg2 };
			}
		}, commandTestName, server);

		String[] returns = CommandsFactory.executeCommand(commandTestName + " " + arg1 + " " + arg2);
		assertArrayEquals(commandTestReturns, returns);

	}

}
