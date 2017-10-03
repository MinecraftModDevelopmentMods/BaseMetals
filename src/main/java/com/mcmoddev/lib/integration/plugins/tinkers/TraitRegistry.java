package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerTraits;

public class TraitRegistry {

	private static final Map<String, ITrait> registeredTraits = new HashMap<>();

	private TraitRegistry() {
		throw new IllegalAccessError("Not an instantiable class");
	}

	public static void addTrait(@Nonnull final String name, @Nonnull final ITrait trait) {
		registeredTraits.put(name, trait);
	}

	public static boolean hasTrait(@Nonnull final String name) {
		return registeredTraits.keySet().contains(name);
	}

	public static ITrait getTrait(@Nonnull final String name) {
		if( registeredTraits.isEmpty() ) {
			initTraits();
			initTiCTraits();
		}
		
		if( registeredTraits.containsKey(name) ) {
			return registeredTraits.get(name);
		} else {
			// assume it's a core TiC trait
			return TinkerRegistry.getTrait(name);
		}
	}

	public static ITrait get(@Nonnull final String name) {
		return getTrait(name);
	}

    public static void initTiCTraits() {
        // hate having to do it this way, but it seems the only way
        registeredTraits.put("alien", TinkerTraits.alien);
        registeredTraits.put("aquadynamic", TinkerTraits.aquadynamic);
        registeredTraits.put("aridiculous", TinkerTraits.aridiculous);
        registeredTraits.put("autosmelt", TinkerTraits.autosmelt);
        registeredTraits.put("baconlicious", TinkerTraits.baconlicious);
        registeredTraits.put("cheap", TinkerTraits.cheap);
        registeredTraits.put("cheapskate", TinkerTraits.cheapskate);
        registeredTraits.put("coldblooded", TinkerTraits.coldblooded);
        registeredTraits.put("crude", TinkerTraits.crude);
        registeredTraits.put("crude2", TinkerTraits.crude2);
        registeredTraits.put("crumbling", TinkerTraits.crumbling);
        registeredTraits.put("dense", TinkerTraits.dense);
        registeredTraits.put("depthdigger", TinkerTraits.depthdigger);
        registeredTraits.put("duritos", TinkerTraits.duritos);
        registeredTraits.put("ecological", TinkerTraits.ecological);
        registeredTraits.put("enderference", TinkerTraits.enderference);
        registeredTraits.put("established", TinkerTraits.established);
        registeredTraits.put("flammable", TinkerTraits.flammable);
        registeredTraits.put("fractured", TinkerTraits.fractured);
        registeredTraits.put("hellish", TinkerTraits.hellish);
        registeredTraits.put("holy", TinkerTraits.holy);
        registeredTraits.put("insatiable", TinkerTraits.insatiable);
        registeredTraits.put("jagged", TinkerTraits.jagged);
        registeredTraits.put("lightweight", TinkerTraits.lightweight);
        registeredTraits.put("magnetic", TinkerTraits.magnetic);
        registeredTraits.put("magnetic2", TinkerTraits.magnetic2);
        registeredTraits.put("momentum", TinkerTraits.momentum);
        registeredTraits.put("petramor", TinkerTraits.petramor);
        registeredTraits.put("poisonous", TinkerTraits.poisonous);
        registeredTraits.put("prickly", TinkerTraits.prickly);
        registeredTraits.put("sharp", TinkerTraits.sharp);
        registeredTraits.put("shocking", TinkerTraits.shocking);
        registeredTraits.put("slimeyGreen", TinkerTraits.slimeyGreen);
        registeredTraits.put("slimeyBlue", TinkerTraits.slimeyBlue);
        registeredTraits.put("spiky", TinkerTraits.spiky);
        registeredTraits.put("splintering", TinkerTraits.splintering);
        registeredTraits.put("splinters", TinkerTraits.splinters);
        registeredTraits.put("squeaky", TinkerTraits.squeaky);
        registeredTraits.put("superheat", TinkerTraits.superheat);
        registeredTraits.put("stiff", TinkerTraits.stiff);
        registeredTraits.put("stonebound", TinkerTraits.stonebound);
        registeredTraits.put("tasty", TinkerTraits.tasty);
        registeredTraits.put("unnatural", TinkerTraits.unnatural);
        registeredTraits.put("writable", TinkerTraits.writable);
        registeredTraits.put("writable2", TinkerTraits.writable2);

          // arrow shaft traits
        registeredTraits.put("breakable", TinkerTraits.breakable);
        registeredTraits.put("endspeed", TinkerTraits.endspeed);
        registeredTraits.put("freezing", TinkerTraits.freezing);
        registeredTraits.put("hovering", TinkerTraits.hovering);
        registeredTraits.put("splitting", TinkerTraits.splitting);
    }
    
	public static void initTraits() {
		registeredTraits.put("soft", MMDTraits.soft);
		registeredTraits.put("sparkly", MMDTraits.sparkly);
		registeredTraits.put("heavy", MMDTraits.heavy);
		registeredTraits.put("brittle", MMDTraits.brittle);
		registeredTraits.put("toxic", MMDTraits.toxic);
		registeredTraits.put("radioactive", MMDTraits.radioactive);
		registeredTraits.put("reactive", MMDTraits.reactive);
	}

	public static void dumpRegistry() {
		for (final Entry<String, ITrait> e : registeredTraits.entrySet()) {
			final String t = String.format("BaseMetals-TCon> Trait: %s - class %s", e.getKey(), e.getValue().getClass().getName());
			BaseMetals.logger.info(t);
		}
	}
	
	public static void registerTraits() {
		// not needed anymore
	}
}
