package com.seikomi.grooveberry.commands;

import java.io.IOException;

import com.seikomi.grooveberry.services.YoutubeTransfertService;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.Locator;

public class YoutubeDownload extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		LOGGER.info("Downloading audiofile from youtube");
		try {
			Locator.getService(YoutubeTransfertService.class, networkApp).youtubeDownload(args[0]);
		} catch (IOException  e) {
			LOGGER.error("An errors occur during the execution of the youtube download command", e);
			return new String[] {"#Y_DOWNLOAD KO"};
		} 
		return new String[] {"#Y_DOWNLOAD OK"};
	}

}
