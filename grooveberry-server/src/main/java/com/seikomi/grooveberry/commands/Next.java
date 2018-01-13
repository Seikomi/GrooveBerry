package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;


public class Next extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Switch to next track in reading queue");
		Locator.getService(ReadingQueueService.class, networkApp).next();
		return new String[] {"#NEXT OK"};
	}

}
