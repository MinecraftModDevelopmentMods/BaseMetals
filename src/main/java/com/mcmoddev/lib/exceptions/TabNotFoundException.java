package com.mcmoddev.lib.exceptions;

public class TabNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TabNotFoundException(String tabName) {
		super(tabName + " was not found in the tab array");
	}

}