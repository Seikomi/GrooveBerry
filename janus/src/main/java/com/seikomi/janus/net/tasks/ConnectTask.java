package com.seikomi.janus.net.tasks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Janus task that handle all client connection, it is based on one-thread-per-client
 * design. At the instantiation this task create a server socket on command
 * port. Then, she wait for connection and each time a client
 * ask for connection, launch a treatment task associate with this client.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 * 
 */
public class ConnectTask extends JanusTask {
	static final Logger LOGGER = LoggerFactory.getLogger(ConnectTask.class);

	private ServerSocket serverCommandSocket;
	private List<TreatmentTask> treatmentTasks;

	private boolean isWaiting = false;

	/**
	 * Construct a connect task associated with the Janus server in arguments.
	 * Instantiate a server socket on command port.
	 * 
	 * @param server
	 *            the Janus server where the task is running
	 * @throws IOException
	 *             if an I/O errors occurs
	 */
	public ConnectTask(int commandPort) throws IOException {
		this.serverCommandSocket = new ServerSocket(commandPort);
		treatmentTasks = new ArrayList<>();
	}

	@Override
	protected void beforeLoop() {
		// Nothing to do
	}

	/**
	 * Wait for connection, and launch a treatment task for each client
	 * connection.
	 */
	@Override
	protected void loop() {
		try {
			LOGGER.info("Waiting for connect request...");
			isWaiting  = true;

			// The method blocks until a connection is made
			Socket commandSocket = serverCommandSocket.accept();
			LOGGER.info("One client is connect");
			isWaiting = false;

			TreatmentTask treatmentTask = new TreatmentTask(commandSocket);
			treatmentTasks.add(treatmentTask);
			Thread treatmentThread = new Thread(treatmentTask, "treatmentThread");
			treatmentThread.start();

		} catch (IOException e) {
			LOGGER.error("An error occurs during client connection", e);
		}
	}

	@Override
	protected void afterLoop() {
		// Nothing to do
	}

	/**
	 * Restarts all the treatments task associated with all the clients.
	 */
	public void restart() {
		stop();
		LOGGER.trace("Restart all tasks on this server instance.");
		for (TreatmentTask treatmentTask : treatmentTasks) {
			treatmentTask.run();
		}
	}

	/**
	 * Stop all the treatments tasks.
	 */
	public void stop() {
		LOGGER.trace("Stop all tasks running on this server instance.");
		for (TreatmentTask treatmentTask : treatmentTasks) {
			treatmentTask.endLoop();
		}
	}

	public boolean isWaiting() {
		return isWaiting;
	}
	
	

}
