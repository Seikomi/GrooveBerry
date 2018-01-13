package com.seikomi.janus.net.tasks;

import java.util.Observable;
import java.util.Observer;

class ClientObserver implements Observer {

	
	public ClientObserver() {
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("Hello");
		
	}
	
}