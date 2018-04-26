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
	 * @return The required versions for this plugins needed mods
	 */
	String versions() default "";
}
