package com.seikomi.grooveberry.commands;

import com.seikomi.grooveberry.bo.ReadingQueue;
import com.seikomi.janus.commands.JanusCommand;
import com.seikomi.janus.services.DataTranferService;
import com.seikomi.janus.services.Locator;

public class Get extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		ReadingQueue readingQueue = ReadingQueue.getInstance();

		int trackPosition;
		String response;

		if (args != null && args.length >= 2 && "--at".equals(args[0])) {
			try {
				trackPosition = Integer.parseInt(args[1]);
				if (trackPosition > 0 && trackPosition < readingQueue.getAudioFileList().size()) {
					response = readingQueue.getAudioFileList().get(trackPosition).getPath();
				} else {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Bad parameter for the command #GET", e);
				response = "Command error.\n USAGE : #GET [--at pos] where pos is a integer";
			}

		} else {
			response = readingQueue.getCurrentTrack().getPath();
			Locator.getService(DataTranferService.class, networkApp).sendObject(new String("GG Seikomi"));;
		}
		LOGGER.info("Getting the current track");
		return new String[] { response };
	}

}
