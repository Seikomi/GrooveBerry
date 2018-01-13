package com.seikomi.janus.commands;

import com.seikomi.janus.services.DataTranferService;
import com.seikomi.janus.services.Locator;

/**
 * Upload command. Download on the client the files passed in arguments. Return
 * {@code #DOWNLOAD STARTED} when the upload begin.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class Upload extends JanusCommand {

	@Override
	public String[] apply(String[] args) {
		String[] responce;

		DataTranferService uploadService = Locator.getService(DataTranferService.class, networkApp);

		if (args == null || args.length == 0) {
			responce = new String[] { "#UPLOAD NO FILES TO RECEIVE" };
		} else {
			uploadService.receive(args);
			responce = new String[] { "#UPLOAD STARTED" };
		}

		return responce;
	}

}
