package com.seikomi.janus.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.seikomi.janus.net.NetworkApp;

/**
 * This class is the factory of commands use by Janus server. She stores a set
 * of commands and execute the command ask by a client. This factory need to be
 * initialize before use it by the call of {@code init()} method.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class CommandsFactory {
	private static final  Map<String, JanusCommand> commands = new HashMap<>();

	private CommandsFactory() {
		// Hide the public constructor
		throw new UnsupportedOperationException();
	}

	/**
	 * Add an external command in the command factory.
	 * 
	 * @param name
	 *            the associated name of the command
	 * @param command
	 *            the command
	 */
	public static void addCommand(JanusCommand command, String name, NetworkApp associatedNetworkApp) {
		command.name = name;
		command.networkApp = associatedNetworkApp;
		
		commands.put(name, command);
	}

	/**
	 * Execute the command of name equals to the {@code commandString} pass in
	 * arguments.
	 * 
	 * @param commandString
	 *            the command string name
	 * @return the result of the command like an array of strings
	 */
	public static String[] executeCommand(String commandString) {
		String[] commandResult = null;

		StringTokenizer stringTokenizer = new StringTokenizer(commandString);
		if (stringTokenizer.hasMoreTokens()) {
			String commandHeader = stringTokenizer.nextToken();
			if (commands.containsKey(commandHeader)) {
				if (stringTokenizer.hasMoreTokens()) {
					String[] args = new String[stringTokenizer.countTokens()];
					for (int i = 0; i < args.length; i++) {
						args[i] = stringTokenizer.nextToken();
					}
					commandResult = commands.get(commandHeader).apply(args);
				} else {
					commandResult = commands.get(commandHeader).apply(null);
				}
			}
		}

		return commandResult;
	}

	/**
	 * Gets the commands list handle by this factory.
	 * 
	 * @return the commands list (unmodifiable)
	 */
	public static Map<String, JanusCommand> getCommands() {
		return Collections.unmodifiableMap(commands);
	}

	/**
	 * Clear all commands handle by this factory.
	 */
	public static void clear() {
		commands.clear();
	}

}
