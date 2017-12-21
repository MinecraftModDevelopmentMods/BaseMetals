package com.mcmoddev.lib.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MMDPlugin {

	/**
	 * 
	 * @return The id of the addon (ModID requesting service)
	 */
	String addonId() default "";

	/**
	 * 
	 * @return The id of the plugin (ModID it supports)
	 */
	String pluginId() default "";

	/**
	 * 
	 * @return The preInit phase callback name for the plugin
	 */
	String preInitCallback() default "";

	/**
	 * 
	 * @return The init phase callback name for the plugin
	 */
	String initCallback() default "";

	/**
	 * 
	 * @return The postInit phase callback name for the plugin
	 */
	String postInitCallback() default "";
}
