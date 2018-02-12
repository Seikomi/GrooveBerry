package com.seikomi.janus.net.properties;

/**
 * Enumeration of all defaults properties. The fist argument is the name of the
 * property and the second is this default value (only accessible by
 * {@code JanusServerPropertie} for initialization purpose). Bring access to the
 * constant name of properties and use to set up the server at the first start.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 *
 */
public enum JanusDefaultProperties {
	/** Command port property representation : {@code commandPort=3008} */
	COMMAND_PORT("commandPort", "3008"),
	/** Data port property representation : {@code dataPort=3009} */
	DATA_PORT("dataPort", "3009"),
	/** Reception directory for uploaded files : {@code receptionDirectory=files/} */
	RECEPTION_DIRECTORY("receptionDirectory", "files/");

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
	private JanusDefaultProperties(String propertyName, String propertyValue) {
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
