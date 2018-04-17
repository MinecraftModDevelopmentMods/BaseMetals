package com.mcmoddev.lib.integration;

@FunctionalInterface
public interface IIntegration {
	/*
	 * Need a function for getting the callbacks - should run after init()
	 * - is the first thing done. "init()" should probably go away in favor of a
	 * constructor with defined parameters.
	 */
	void init();
}
