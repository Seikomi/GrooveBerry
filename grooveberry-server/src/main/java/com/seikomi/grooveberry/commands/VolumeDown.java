package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class VolumeDown extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Turn Down the volume by 10%!");
		Locator.getService(ReadingQueueService.class, networkApp).volumeDown();
		return new String[] {"#VOLDOWN OK"};
	}

}
