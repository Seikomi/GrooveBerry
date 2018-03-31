package com.seikomi.grooveberry;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.seikomi.janus.net.JanusClient;
import com.seikomi.janus.net.properties.JanusProperties;

@Scope(value = "singleton")
@Component
public class GrooveberryClient implements Observer {
	private static final Logger LOGGER = LoggerFactory.getLogger(GrooveberryServer.class);

	private JanusClient client;
	private boolean lock;
	private String response;

	public GrooveberryClient() throws IOException {
		Path propertiePath = Paths.get("C:\\Users\\Nicolas\\git\\GrooveBerry\\janus\\client.properties");
		JanusProperties janusClientProperties = new JanusProperties(propertiePath);
		this.client = new JanusClient(janusClientProperties);
		this.client.addObserver(this);
		this.client.start();
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
		lock = false;
		response = (String) arg;
	}
}
