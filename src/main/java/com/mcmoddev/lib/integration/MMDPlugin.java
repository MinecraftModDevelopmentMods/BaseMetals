package com.mcmoddev.lib.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation that all MMDPlugins provide. This is used by the {@link IntegrationManager} code to
 * locate and load the plugins.
 * 
 * @author SYSTEM
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MMDPlugin {

	/**
	 * What is the ModID of the mod providing this plugin ?
	 * 
	 * @return The id of the addon (ModID requesting service)
	 */
	String addonId() default "";

	/**
	 * What is the ModID of the mod this plugin provides integration with ?
	 * 
	 * @return The id of the plugin (ModID it supports)
	 */
	String pluginId() default "";

	/**
	 * <p>
	 * What versions of the "pluginId" mod does this code work with ?
	 * <p>
	 * This takes the form of a limited Maven Artifact Version. It is not, yet, capable of handling
	 * SemVer. It must also start with an open parentheses or square bracket, contain one or more
	 * sets of version numbers and end with a closing parentheses or square bracket.
	 * 
	 * @return The required versions for this plugins needed mods
	 */
	String versions() default "";
}
