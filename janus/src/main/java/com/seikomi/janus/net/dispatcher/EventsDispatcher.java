package com.seikomi.janus.net.dispatcher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsDispatcher {
	static final Logger LOGGER = LoggerFactory.getLogger(EventsDispatcher.class);

	private DataOutputStream out;

	public EventsDispatcher(Socket commandSocket) throws IOException {
		this.out = new DataOutputStream(commandSocket.getOutputStream());
	}

	public void sendEvent() throws IOException {
		out.writeUTF("#UPDATE");
		out.flush();

		LOGGER.trace("The #UPDATE events has been broadcast on the port.");
	}

}
