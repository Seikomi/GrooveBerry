package com.seikomi.janus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.JanusServer;
import com.seikomi.janus.net.tasks.FileTransferTask;
import com.seikomi.janus.net.tasks.ObjectTransferTask;

/**
 * Service to handle data transfer between the client and the server.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class DataTranferService extends JanusService {
	static final Logger LOGGER = LoggerFactory.getLogger(DataTranferService.class);
	
	private FileTransferTask fileTransfertTask;
	private ObjectTransferTask objectTransferTask;
	
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
	public void sendFiles(String[] fileNames) {
		if (fileTransfertTask == null) {
			try {
				int dataPort = Integer.parseInt(networkApp.getProperties("server.ports.data"));
				String receptionDirectory = networkApp.getProperties("server.directories.files");
				fileTransfertTask = new FileTransferTask(fileNames, true, dataPort, receptionDirectory);
				startFileTranferTask();
			} catch (NumberFormatException e) {
				LOGGER.error("Cannot send files : data port properties malformed", e);
			}
		} else {
			fileTransfertTask.addFiles(fileNames, true);
		}
	}
	
	public void sendObject(Object object) {
		if (objectTransferTask == null) {
			try {
				int dataPort = Integer.parseInt(networkApp.getProperties("server.ports.data"));
				objectTransferTask = new ObjectTransferTask(dataPort);
				startObjectTranferTask();
			} catch (NumberFormatException e) {
				LOGGER.error("Cannot send objectq : data port properties malformed", e);
			}
		} else {
			objectTransferTask.addObject(object);
		}
	}
	
	private void startObjectTranferTask() {
		Thread objectTransferThread = new Thread(objectTransferTask, "objectTransferThread");
		objectTransferThread.start();
	}

	/**
	 * Sends the files identified on the file system by the array of filenames.
	 * 
	 * @param fileNames
	 *            the array of filenames of files to send.
	 */
	public void receiveFiles(String[] fileNames) {
		if (fileTransfertTask == null) {
			try {
				int dataPort = Integer.parseInt(networkApp.getProperties("server.ports.data"));
				String receptionDirectory = networkApp.getProperties("server.directories.files");
				fileTransfertTask = new FileTransferTask(fileNames, false, dataPort, receptionDirectory);
				startFileTranferTask();
			} catch (NumberFormatException e) {
				LOGGER.error("Cannot receive files : data port properties malformed", e);
			}
		} else {
			fileTransfertTask.addFiles(fileNames, false);
		}
	}
	
	/**
	 * Starts the data transfert task.
	 */
	private void startFileTranferTask() {
		Thread fileTranfertThread = new Thread(fileTransfertTask, "fileTransfertThread");
		fileTranfertThread.start();
	}

}
