package com.mcmoddev.lib.integration;

/**
 * Core of the MMDPlugin system, this interface acts as the base of all plugin code.
 * 
 * @author J. Iwanek
 * @author D. Hazelton
 *
 */
@FunctionalInterface
public interface IIntegration {

	/*
	 * Need a function for getting the callbacks - should run after init() - is the first thing
	 * done. "init()" should probably go away in favor of a constructor with defined parameters.
	 */
	/**
	 * This is called to properly initialize the plugin code.
	 */
	void init();
}
