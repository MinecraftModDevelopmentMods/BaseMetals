package com.mcmoddev.basemetals.init;

import com.mcmoddev.lib.data.SharedStrings;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Entities {

	private static boolean initDone = false;

	private Entities() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
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
