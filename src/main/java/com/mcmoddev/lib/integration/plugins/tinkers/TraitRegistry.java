package com.mcmoddev.lib.integration.plugins.tinkers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mcmoddev.basemetals.BaseMetals;

import slimeknights.tconstruct.library.traits.ITrait;

public class TraitRegistry {

	private static Map<String, ITrait> registeredTraits = new HashMap<>();
	
	public static void addTrait(String name, ITrait trait) {
		registeredTraits.put(name, trait);
	}
	
	public static boolean hasTrait(String name) {
		return registeredTraits.keySet().contains(name);
	}
	
	public static ITrait getTrait(String name) {
		return registeredTraits.get(name);
	}
	
	public static ITrait get(String name) {
		return getTrait(name);
	}
	
	public static void initTiCTraits() {
		Field[] fields = slimeknights.tconstruct.tools.TinkerTraits.class.getDeclaredFields();
		for( Field f : fields ) {
			Class<?> clazz= f.getType();
			try {
				registeredTraits.put(f.getName(), (ITrait)f.get(clazz));
			} catch (final Exception e) {
				// do nothing
			}
		}
	}
	
	public static void initMetalsTraits() {
		Field[] fields = com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits.class.getDeclaredFields();
		for( Field f : fields ) {
			Class<?> clazz = f.getType();
			try {
				registeredTraits.put(f.getName(), (ITrait)f.get(clazz));
			} catch( final Exception e ) {
				// do nothing
			}
		}
	}
	
	public static void dumpRegistry() {
		for( String s : registeredTraits.keySet()) {
			String t = String.format("BaseMetals-TCon> Trait: %s - class %s", s, registeredTraits.get(s).getClass().getName());
			BaseMetals.logger.info(t);
		}
	}
	
	public TraitRegistry() {
		throw new IllegalAccessError("Not an instantiable class");
	}
}
