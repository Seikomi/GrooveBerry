package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class Randomise extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Randomising reading queue");
		Locator.getService(ReadingQueueService.class, networkApp).randomise();
		return new String[] {"#RANDOMISE OK"};
	}
    
}
