package com.buzzerbeater;

public class Version {
	public static String getVersion() {
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + BUILD_VERSION;
	}
	
	private static final String MAJOR_VERSION = "3";
	private static final String MINOR_VERSION = "4";
	private static final String BUILD_VERSION = "1";
}
