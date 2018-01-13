package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class Play extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Playing reading queue");
		Locator.getService(ReadingQueueService.class, networkApp).play();
		return new String[] {"#PLAY OK"};
	}

}
