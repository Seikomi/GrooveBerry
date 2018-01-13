package com.seikomi.janus.commands;

/**
 * Exit command. Just return exit message interpret by the server like
 * interrupting flag.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class Exit extends JanusCommand {


	@Override
	public String[] apply(String[] args) {
		return new String[] { "#EXIT OK" };
	}

}
