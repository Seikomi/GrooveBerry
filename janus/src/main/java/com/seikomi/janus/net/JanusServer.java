package com.seikomi.janus.net;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.commands.CommandsFactory;
import com.seikomi.janus.commands.Download;
import com.seikomi.janus.commands.Exit;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.commands.Upload;
import com.seikomi.janus.net.dispatcher.EventsDispatcher;
import com.seikomi.janus.net.properties.JanusProperties;
import com.seikomi.janus.net.tasks.ConnectTask;
import com.seikomi.janus.net.tasks.TreatmentTask;
import com.seikomi.janus.services.DataTranferService;
import com.seikomi.janus.services.JanusService;
import com.seikomi.janus.services.Locator;
import com.seikomi.janus.utils.Utils;

/**
 * Implementation of a socket server. With the usage of two distinct ports (like
 * an FTP server) : one for the commands and the other for the data transfer.
 * His configuration is store in a {@code .properties} file of type
 * {@link JanusServerProperties}.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public abstract class JanusServer extends Observable implements NetworkApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(JanusServer.class);

	private JanusProperties serverProperties;

	private ConnectTask connectTask;
	private List<TreatmentTask> treatmentTasks = null;
	private List<EventsDispatcher> eventsDispatchers = null;

	/**
	 * Create a new instance of Janus server and configure it with the
	 * {@code .properties} file pass in argument.
	 * 
	 * @param janusProperties
	 *            the server {@code .properties} file
	 */
	public JanusServer(JanusProperties janusProperties) {
		this.serverProperties = janusProperties;

		loadContext();
		loadJanusContext();

		LOGGER.debug("Janus context loaded.");
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Commands :");
			for (Entry<String, JanusCommand> entry : CommandsFactory.getCommands().entrySet()) {
				LOGGER.trace("\t" + entry.getKey() + " : " + entry.getValue().getClass().getSimpleName());
			}
			LOGGER.trace("Service :");
			for (Entry<String, JanusService> entry : Locator.getServices().entrySet()) {
				LOGGER.trace("\t" + entry.getKey() + " : " + entry.getValue().getClass().getSimpleName());
			}

		}
	}

	/**
	 * Load defaults commands and services needed to run properly the Janus server.
	 */
	private void loadJanusContext() {
		CommandsFactory.addCommand(new Exit(), "#EXIT", this);
		CommandsFactory.addCommand(new Download(), "#DOWNLOAD", this);
		CommandsFactory.addCommand(new Upload(), "#UPLOAD", this);

		Locator.load(new DataTranferService(this));
	}

	/**
	 * Start the Janus server and wait for client connections.
	 */
	@Override
	public void start() {
		try {
			connectTask = new ConnectTask(Utils.convertStringToInt(getProperties("server.ports.command")));

			Thread connectTread = new Thread(connectTask, "ConnectThread");
			connectTread.start();

			treatmentTasks = connectTask.getTreatmentTasks();
			eventsDispatchers = connectTask.getEventsDispatchTasks();

			LOGGER.debug("Janus server start on port {} for command and on port {} for data",
					getProperties("server.ports.command"), getProperties("server.ports.data"));
		} catch (IOException e) {
			LOGGER.error("An unknown error occurs during the starting of Janus server", e);
		}

	}

	/**
	 * Restart the Janus server.
	 */
	@Override
	public void restart() {
		if (connectTask != null) {
			connectTask.restart();
			LOGGER.info("Janus server has been restart.");
		} else {
			LOGGER.error("Connect task not running : start first before restart");
		}
	}

	/**
	 * Stop the Janus server, close all associated sockets.
	 */
	@Override
	public void stop() {
		if (connectTask != null) {
			connectTask.stop();
			LOGGER.info("Janus server has been stop.");
		} else {
			LOGGER.error("Connect task not running : start first before stop");
		}
		CommandsFactory.clear();
		Locator.clear();
	}

	/**
	 * This method must be override to load the context of the Janus server.
	 * Basically, it must be contain all commands and services you want to reach
	 * from any part of the application.
	 */
	protected abstract void loadContext();

	public boolean isStarted() {
		return connectTask.isWaiting();
	}

	@Override
	public String getProperties(String propertieName) {
		return serverProperties.getProperties().getProperty(propertieName, null);
	}

	public List<TreatmentTask> getTreatmentTasks() {
		return treatmentTasks;
	}

	public List<EventsDispatcher> getEventsDispatchers() {
		return eventsDispatchers;
	}

}
