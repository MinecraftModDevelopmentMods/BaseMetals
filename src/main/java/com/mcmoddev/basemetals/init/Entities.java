package com.mcmoddev.basemetals.init;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Entities {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		initDone = true;
	}
}
