package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class Prev extends JanusCommand {
	
	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Switch to previous track in reading queue");
		Locator.getService(ReadingQueueService.class, networkApp).prev();
		return new String[] {"#PREV OK"};
	}

}
