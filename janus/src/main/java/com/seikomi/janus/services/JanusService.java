package com.seikomi.janus.services;

import java.util.Observable;

import com.seikomi.janus.net.NetworkApp;

/**
 * Abstract class of Janus services. A Janus service must be registered in the
 * {@code Locator} to be accessible and must reference the Janus server instance
 * where it will be apply.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public abstract class JanusService extends Observable {

	protected NetworkApp networkApp;

	/**
	 * Constructor to link a Janus service with an instance of a Janus serer.
	 * 
	 * @param server
	 *            the Janus server
	 */
	public JanusService(NetworkApp networkApp) {
		this.networkApp = networkApp;
	}

}
