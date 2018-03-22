package com.seikomi.grooveberry.properties;

public enum GrooveberryDefaultProperties {
	WELCOME_MESSAGE("welcomeMessage", "Welcome to GrooveBerry Server (0.1)"),
	GROOVEBERRY_DIRECTORY("grooveBerryDirectory", ".grooveberry/"),
	LIBRARY_DIRECTORY("libraryDirectory", "library/"),
	DATABASE_URL("database.url", "jdbc:h2:mem:db"),
	DATABASE_USER("database.user", "sa"),
	DATABASE_PASSWORD("database.password", "");
	
	private String propertyName;
	private String propertyValue;

	/**
	 * Construct an item with two value : a property name and a property value
	 * 
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 */
	private GrooveberryDefaultProperties(String propertyName, String propertyValue) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	/**
	 * Gets the property name use in {@code JanusServerProperties} file.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Gets the defaults property value.
	 * 
	 * @return the defaults property value
	 */
	protected String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * Gets the defaults property value parsed in signed decimal integer.
	 * 
	 * @return the defaults property value as signed decimal integer.
	 * @throws NumberFormatException
	 *             if the string does not contain a parsable integer.
	 * @see {@link Integer#parseInt(String)}
	 */
	protected int getPropertyValueAsInt() {
		return Integer.parseInt(propertyValue);
	}

}
