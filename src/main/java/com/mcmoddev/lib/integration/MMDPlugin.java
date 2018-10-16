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
	 * More specifically, this can take a semicolon separated list of modid\@version pairs, such as:
	 * tconstruct@[1.12.2-2.7.4.0,);conarm@[1.12.2-2.10.1.87,) and it will try to match all given mods
	 * and versions, rejecting if they are not present.
	 * 
	 * @return The required versions for this plugins needed mods
	 */
	String versions() default "";
	
	/**
	 * <p>
	 * In deference to the existing nature and use of the "versions" data, we introduce the "ordering" data - which serves
	 * a similar role, but for providing some ordering to init() and making sure we load the plugins in the correct order.
	 * <p>
	 * This gets a string, formatted a pair of key-value pairs separated by a semi-colon - such as: before:basemetals@thermalexpansion,
	 * basemetals@mekanism;after:basemetals@tconstruct and should result in the mod having its init() call made in that order.
	 * 
	 * @return The ordering dependency string
	 */
	String ordering() default "";
}
