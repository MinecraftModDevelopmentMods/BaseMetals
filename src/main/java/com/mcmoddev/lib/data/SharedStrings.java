package com.mcmoddev.lib.data;

public class SharedStrings {

	public static final String NOT_INSTANTIABLE = "Not a instantiable class";

	private SharedStrings() {
		throw new IllegalAccessError(NOT_INSTANTIABLE);
	}
}
