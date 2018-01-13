package com.seikomi.janus.net;

/**
 * Interface that provide basic services of a network application.
 * {@code start()} to start the application contains the code call at the
 * starting of the network service, {@code restart()} and {@code stop()}to
 * respectively restarting and stopping the network services.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public interface NetworkApp {
	
	/**
	 * Start the network application.
	 */
	public abstract void start();
	
	/**
	 * Restart the network application.
	 */
	public abstract void restart();
	
	/**
	 * Stop the network application.
	 */
	public abstract void stop();

}
