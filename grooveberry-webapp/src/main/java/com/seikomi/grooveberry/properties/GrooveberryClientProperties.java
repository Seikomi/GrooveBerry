package com.seikomi.grooveberry.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("grooveberry.client")
public class GrooveberryClientProperties {
	
	private String properties = "client.properties";

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

}
