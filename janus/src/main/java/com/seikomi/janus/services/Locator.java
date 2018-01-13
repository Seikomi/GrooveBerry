package com.seikomi.janus.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seikomi.janus.net.NetworkApp;

/**
 * This static class is use to locate the service instance attach to a Janus
 * server from any part of the application. <i>Warning : </i> You must
 * registered services with the {@code load(JanusService service} method to be
 * able to call it with this services locator
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public class Locator {
	static final Logger LOGGER = LoggerFactory.getLogger(Locator.class);

	private static Map<String, JanusService> services = new HashMap<>();

	private Locator() {
		// Hide the public constructor
	}

	/**
	 * Load a service in the service locator. After that, this service must be
	 * call from any part of the application.
	 * 
	 * @param service
	 *            the service to load in the locator
	 */
	public static void load(JanusService service) {
		String serviceIdentifier = service.getClass().getSimpleName() + "@"
				+ Integer.toHexString(service.networkApp.hashCode());
		LOGGER.debug("Register the service in the locator : " + serviceIdentifier);
		services.put(serviceIdentifier, service);
	}

	/**
	 * Gets the service instance of the {@code serviceClass} class associated
	 * with a Janus server.
	 * 
	 * @param <T>
	 *            type extended JanusService
	 * @param serviceClass
	 *            the class of the service you want use
	 * @param associatedJanusServer
	 *            the associated server of the instance of the service you want
	 *            use
	 * @return the service associated with the Janus server
	 */
	public static <T extends JanusService> T getService(Class<T> serviceClass, NetworkApp associatedNetworkApp) {
		return serviceClass.cast(getService(serviceClass.getSimpleName(), associatedNetworkApp));
	}

	/**
	 * {@code fullServiceName} is an identifier with the following form :
	 * [serviceClassName]@[server instance hash code]
	 * 
	 * @param fullServiceName
	 *            the full name of the service you want use
	 * @return the service identified by the {@code fullServiceName}
	 */
	public static JanusService getService(String fullServiceName) {
		return services.get(fullServiceName);
	}

	/**
	 * Gets the service instance with the {@code serviceName} name associated
	 * with a Janus server.
	 * 
	 * @param serviceName
	 *            the service name of the service you want use
	 * @param associatedJanusServer
	 *            the associated server of the instance of the service you want
	 *            use
	 * @return the service associated with the Janus server
	 */
	public static JanusService getService(String serviceName, NetworkApp associatedNetworkApp) {
		return services.get(serviceName + "@" + Integer.toHexString(associatedNetworkApp.hashCode()));
	}

	/**
	 * Gets the services list handle by this locator.
	 * 
	 * @return the services list (unmodifiable)
	 */
	public static Map<String, JanusService> getServices() {
		return Collections.unmodifiableMap(services);
	}

	/**
	 * Clear all services instance registered in the service locator.
	 */
	public static void clear() {
		services.clear();
	}

}
