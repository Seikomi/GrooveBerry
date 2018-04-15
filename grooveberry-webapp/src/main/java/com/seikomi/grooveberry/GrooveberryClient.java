package com.seikomi.grooveberry;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.seikomi.grooveberry.properties.GrooveberryClientProperties;
import com.seikomi.grooveberry.websocket.WebSocketController;
import com.seikomi.janus.net.JanusClient;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.utils.Utils;

@Scope(value = "singleton")
@Component
public class GrooveberryClient implements Observer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrooveberryClient.class);

	private JanusClient client;
	private boolean lock;
	private String response;
	
	@Autowired 
	WebSocketController webSocketController;

	@Autowired
	public GrooveberryClient(GrooveberryClientProperties clientProperties) throws IOException {
		JanusProperties janusClientProperties = new JanusProperties(
				Utils.transformStringPath(clientProperties.getProperties()));
		this.client = new JanusClient(janusClientProperties);
		this.client.start();

		this.client.getAskConnectionTask().addObserver(this);
	}

	public String executeCommand(String command) throws IOException, InterruptedException {
		lock = true;

		client.executeCommand(command);
		LOGGER.info("Execute command : {}", command);
		synchronized (this) {
			while (lock) {
				this.wait();
			}
		}
		return response;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (((String) arg).equals("#UPDATE")) {
			LOGGER.info("Receiving update");
			LOGGER.info(webSocketController.sendNotification());
		} else {
			lock = false;
			response = (String) arg;
		}
	}

	public JanusClient getClient() {
		return client;
	}
	
	

}
