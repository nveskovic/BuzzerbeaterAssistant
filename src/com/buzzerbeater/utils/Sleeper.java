package com.buzzerbeater.utils;

public class Sleeper {
	
	private static void simpleSleeperInMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch(Exception e){}
	}
	
	public static void sleepTightInMillis(long millis) {
		simpleSleeperInMillis(millis);
	}

	public static void sleepTightInMinutes(int minutes) {
		long l = (long)minutes;
		simpleSleeperInMillis(l * 60 * 1000);
	}

	public static void sleepTightInSeconds(int seconds) {
		long l = (long)seconds;
		simpleSleeperInMillis(l * 1000);
	}
}
