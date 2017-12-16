package com.mcmoddev.lib.exceptions;

public class MaterialNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MaterialNotFoundException(String materialName) {
		super(materialName + " was not found in the tab array");
	}

}