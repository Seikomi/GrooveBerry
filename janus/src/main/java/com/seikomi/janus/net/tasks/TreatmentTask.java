package com.seikomi.janus.net.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.commands.CommandsFactory;

/**
 * Janus task that handle client commands send on the command socket. Send a
 * welcome message when a client at initailization.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class TreatmentTask extends JanusTask {
	static final Logger LOGGER = LoggerFactory.getLogger(TreatmentTask.class);

	public static final String WELCOME_MESSAGE = "Welcome to Janus Server (0.1)";

	private Socket commandSocket;

	private DataInputStream in;
	private DataOutputStream out;

	/**
	 * Construct a treatment task associate with a Janus server and link with the
	 * command socket of a client. Initialize the I/O stream to communicate with
	 * the client.
	 * 
	 * @param server
	 *            the Janus server
	 * @param commandSocket
	 *            the command socket of the client
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public TreatmentTask(Socket commandSocket) throws IOException {
		this.commandSocket = commandSocket;
		this.in = new DataInputStream(commandSocket.getInputStream());
		this.out = new DataOutputStream(commandSocket.getOutputStream());
	}

	/**
	 * Send a welcome message before handle client commands.
	 */
	@Override
	protected void beforeLoop() {
		sendMessage(WELCOME_MESSAGE);
	}

	/**
	 * Treats the command receive on the command socket.
	 */
	@Override
	protected void loop() {
		try {
			String receivingMessage = in.readUTF();
			if (receivingMessage == null) {
				endLoop();
			}

			commandeExecute(receivingMessage);
			LOGGER.trace("Command receive : " + receivingMessage);
		} catch (IOException e) {
			LOGGER.info("One client has been logged out");
			LOGGER.trace("IOException handle, see this stack trace for details: ", e);
			endLoop();
		}
	}

	/**
	 * Close the command socket after the thread is interrupted.
	 */
	@Override
	protected void afterLoop() {
		try {
			out.close();
			commandSocket.close();
		} catch (IOException e) {
			LOGGER.error("An error occurs during command socket closing", e);
		}
	}

	/**
	 * Execute the command and send the response. If the command response is
	 * {@code #EXIT_OK} special return state, end the current thread and end the
	 * treatment task.
	 * 
	 * @param receivingMessage
	 *            the command message to execute
	 */
	private void commandeExecute(String receivingMessage) {
		String[] commandReturnState = CommandsFactory.executeCommand(receivingMessage);
		if (commandReturnState == null) {
			sendMessage(receivingMessage);
			LOGGER.trace("Message send (echo) : " + receivingMessage);
		} else if ("#EXIT OK".equals(commandReturnState[0])) {
			endLoop();
		} else {
			sendMessage(commandReturnState[0]);
			LOGGER.trace("Message send (command result) : " + commandReturnState[0]);
		}
	}

	/**
	 * Sends a message encode in UTF to the client.
	 * 
	 * @param message
	 *            the message to send
	 */
	public void sendMessage(String message) {
		try {
			out.writeUTF(message);
			out.flush();
		} catch (IOException e) {
			LOGGER.error("An error occurs during the sending of a message to a client", e);
		}
	}

}
