package com.seikomi.janus.net.tasks;

import java.util.Observable;

/**
 * Abstract class of Janus task, witch is a thread that run the three following
 * abstract methods :
 * <ul>
 * <li>{@code beforeLoop()}, executes before the {@code loop()} method ;</li>
 * <li>{@code Loop()}, executes until the current thread is interrupted,
 * <i>require a blocking instruction to not be an infinite loop</i> ;</li>
 * <li>{@code afterLoop()}, executes after the {@code loop()} method, when the
 * current thread is interrupt.</li>
 * </ul>
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public abstract class JanusTask extends Observable implements Runnable {

	private volatile Thread currentThread;

	@Override
	public void run() {
		currentThread = Thread.currentThread();

		beforeLoop();
		while (!currentThread.isInterrupted()) {
			loop();
		}
		afterLoop();
	}

	/**
	 * Method executes before the {@code loop()} method.
	 */
	protected abstract void beforeLoop();

	/**
	 * Method executes after the {@code loop()} method, when the current thread
	 * is interrupt.
	 */
	protected abstract void afterLoop();

	/**
	 * Method that repeat until the current thread is interrupted, note that
	 * method can be an infinite loop if no blocking method is used inside.
	 * @throws Exception 
	 */
	protected abstract void loop();

	/** End the {@code loop()} method in interrupting the current thread. */
	protected void endLoop() {
		if (currentThread != null) {
			currentThread.interrupt();
		}
	}

	/** Inform the observer of the changes of state in Janus task. */
	protected void informObservers(Object arg) {
		setChanged();
		notifyObservers(arg);
	}

}
