package com.seikomi.janus.net.tasks;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.utils.Utils.Pair;

public class ObjectTransferTask extends JanusTask {
	static final Logger LOGGER = LoggerFactory.getLogger(ObjectTransferTask.class);
	
	private ServerSocket dataServerSocket;
	
	private ObjectOutputStream out;
	
	private Deque<Pair<Object, Boolean>> objectDeque;
	
	private int dataPort;
	
	public ObjectTransferTask(int dataPort) {
		this.objectDeque = new ArrayDeque<>();
		this.dataPort = dataPort;
	}

	@Override
	protected void beforeLoop() {
		try {
			dataServerSocket = new ServerSocket(dataPort);
			Socket dataSocket = dataServerSocket.accept();
			out = new ObjectOutputStream(dataSocket.getOutputStream());
		} catch (IOException e) {
			LOGGER.error("An error occurs during initialization of data transmission", e);
		}

	}

	@Override
	protected void afterLoop() {
		try {
			out.close();
			dataServerSocket.close();
		} catch (IOException e) {
			LOGGER.error("An error occurs during the closing of data transmission pipe", e);
		}

	}

	@Override
	protected void loop() {
		if (!objectDeque.isEmpty()) {
			Pair<Object, Boolean> object = objectDeque.pop();
			if (object.getRight()) {
				Object objectToSend = object.getLeft();
				if (objectToSend != null) {
					sendObject(objectToSend);
				} else {
					LOGGER.debug("object not found");
				}
			} else {
				//TODO Receive object
			}
		} else {
			endLoop();
		}

	}

	private void sendObject(Object objectToSend) {
		try {
			out.writeObject(objectToSend);
		} catch (IOException e) {
			LOGGER.error("An error occurs during the sending of object", e);
		}
	}

	public void addObject(Object object) {
		objectDeque.add(new Pair<Object, Boolean>(object, true));
	}

}
