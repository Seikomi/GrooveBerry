package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class VolumeUp extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Turn Up the volume by 10%!");
		Locator.getService(ReadingQueueService.class, networkApp).volumeUp();
		return new String[] {"#VOLUP OK"};
	}

}
