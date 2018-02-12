package com.seikomi.janus.services;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.tasks.DataTransferTask;

/**
 * Service to handle data transfer between the client and the server.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class DataTranferService extends JanusService {
	private DataTransferTask dataTransfertTask;
	
	/**
	 * Create a new instance of the data transfer service by passing in arguments the
	 * targeted Janus server.
	 * 
	 * @param server
	 *            the targeted server
	 */
	public DataTranferService(JanusServer server) {
		super(server);
	}
	
	/**
	 * Sends the files identified on the file system by the array of filenames.
	 * 
	 * @param fileNames
	 *            the array of filenames of files to send.
	 */
	public void send(String[] fileNames) {
		if (dataTransfertTask == null) {
			String receptionDirectory = ((JanusServer) networkApp).getReceptionDirectory();
			int dataPort = ((JanusServer) networkApp).getDataPort();
			dataTransfertTask = new DataTransferTask(fileNames, receptionDirectory, dataPort, true);
			startTask();
		} else {
			dataTransfertTask.addFiles(fileNames, true);
		}
	}
	
	/**
	 * Sends the files identified on the file system by the array of filenames.
	 * 
	 * @param fileNames
	 *            the array of filenames of files to send.
	 */
	public void receive(String[] fileNames) {
		if (dataTransfertTask == null) {
			String receptionDirectory = ((JanusServer) networkApp).getReceptionDirectory();
			int dataPort = ((JanusServer) networkApp).getDataPort();
			dataTransfertTask = new DataTransferTask(fileNames, receptionDirectory, dataPort, false);
			startTask();
		} else {
			dataTransfertTask.addFiles(fileNames, false);
		}
	}
	
	/**
	 * Starts the data transfert task.
	 */
	private void startTask() {
		Thread downloadThread = new Thread(dataTransfertTask, "dataTransfertThread");
		downloadThread.start();
	}

}
