package com.mcmoddev.basemetals.init;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Entities {

	private static boolean initDone = false;

	private Entities() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		initDone = true;
	}
}
