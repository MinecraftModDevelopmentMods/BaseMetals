package com.mcmoddev.lib.fuels;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FuelRegistry {
	// maps an oreDict to a burnTime
	private static Map<String,Integer> fuelDicts = new HashMap<>();
	private static boolean initialized = false;
	private static Map<Item,Integer> fuels = new HashMap<>();
	
	private FuelRegistry() {
		throw new IllegalAccessError("This class cannot be instantiated!");
	}
	
	public static void init() {
		if( !initialized ) {
			initialized = true;
		}
		
	}

	public static void register() {
		for( Map.Entry<String, Integer> ent : fuelDicts.entrySet() ) {
			for( ItemStack item : OreDictionary.getOres(ent.getKey())) {
				if( !fuels.containsKey(item.getItem()) ) { 
					fuels.put(item.getItem(), ent.getValue());
				}
			}
		}
		GameRegistry.registerFuelHandler( new MMDFuelHandler() );
	}
	
	public static Map<Item,Integer> getFuels() {
		return Collections.unmodifiableMap(fuels);
	}
	
	public static void addFuel(String oreDict, int burnTime) {
		if( !fuelDicts.containsKey(oreDict) ) {
			fuelDicts.put(oreDict, burnTime);
		}
	}
}

