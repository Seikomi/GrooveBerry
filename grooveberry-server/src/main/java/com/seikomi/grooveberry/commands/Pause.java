package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class Pause extends JanusCommand{

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Pausing reading queue");
		Locator.getService(ReadingQueueService.class, networkApp).pause();
		return new String[] {"#PAUSE OK"};
	}

}
