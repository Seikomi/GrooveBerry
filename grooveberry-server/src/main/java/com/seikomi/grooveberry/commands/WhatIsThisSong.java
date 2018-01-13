package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.services.ReadingQueueService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class WhatIsThisSong extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Questioning inference data base !");
		return new String[] {"#SONG " + Locator.getService(ReadingQueueService.class, networkApp).whatIsThisSong()};
	}

}
