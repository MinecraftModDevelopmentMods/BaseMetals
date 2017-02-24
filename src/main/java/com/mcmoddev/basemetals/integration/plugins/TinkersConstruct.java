package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;

import net.minecraft.item.Item;

import com.mcmoddev.lib.integration.plugins.tinkers.TraitLocations;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID, initCallback="doSecondPass")
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstruct implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}

		TraitRegistry.initTiCTraits();
		TraitRegistry.initMetalsTraits();
		ModifierRegistry.initModifiers();

		registerMaterial(Options.enableAdamantine, "adamantine", true, false, "coldblooded", "insatiable" );
		registerMaterial(Options.enableAntimony, "antimony", true, false);
		registerMaterial(Options.enableAquarium, "aquarium", true, false, "aquadynamic", "jagged", TraitLocations.HEAD, "aquadynamic", TraitLocations.HEAD);
		registerMaterial(Options.enableBismuth,"bismuth", true, false);
		registerMaterial(Options.enableBrass, "brass", true, false, "dense");
		registerMaterial(Options.enableColdIron, "coldiron", true, false, "freezing");
		registerMaterial(Options.enableCupronickel, "cupronickel", true, false);
		registerMaterial(Options.enableInvar, "invar", true, false);
		registerMaterial(Options.enableMithril, "mithril", true, false, "holy");
		registerMaterial(Options.enableNickel, "nickel", true, false);
		registerMaterial(Options.enablePewter, "pewter", true, false, "soft");
		registerMaterial(Options.enablePlatinum, "platinum", true, false);
		registerMaterial(Options.enableStarSteel, "starsteel", true, false, "enderference", TraitLocations.HEAD, "sparkly");
		registerMaterial(Options.enableTin, "tin", true, false);
		registerMaterial(Options.enableZinc, "zinc", true, false);
		
		registerAlloys();
		
		if (Options.enableCoal) {
			registerFluid(Materials.vanilla_coal, 144);
		}


		// As much as we'd like to, we cannot do this like this.
		// At some point between this code running and it getting
		// up to the point of registration, the state changes and
		// lead is suddenly registered.
		// There will have to be another means of making this work...
		// There is an event TiC has that covers material registration
		// we could hook that...

		// Lead itself is added by TiC
		if ((Options.enableLead) && (Options.enablePlate)) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.lead.plate));
		}

		if (Options.enableMercury) {
			registry.registerFluid(Materials.getMaterialByName("mercury"), 144);
			if (Options.enableBasics) {
				registerModifierItem("toxic", Materials.mercury.powder);
			}
		}



		if (Options.enableSilver) {
			// Anything needed?
		}


		registry.registerAll();
		initDone = true;
	}

	private boolean isTraitLoc(String loc) {
		switch(loc) {
		case TraitLocations.BOW:
		case TraitLocations.BOWSTRING:
		case TraitLocations.EXTRA:
		case TraitLocations.FLETCHING:
		case TraitLocations.HANDLE:
		case TraitLocations.HEAD:
		case TraitLocations.PROJECTILE:
		case TraitLocations.SHAFT:
			return true;
		default:
			return false;
		}
	}
	
	private void addTraits(TCMaterial mat, String[] traits) {
		int i = 0;
		while( i < traits.length ) {
			if( i+1 >= traits.length ) {
				mat.addTrait(traits[i]);
				i++;
				continue;
			}

			if( isTraitLoc(traits[i+1]) ) {
				mat.addTrait( traits[i], traits[i+1] );
				i++;
			} else {
				mat.addTrait( traits[i] );
			}
			i++;
		}
	}
	
	private void registerMaterial(boolean enabled, String name, boolean castable, boolean craftable, String... traits) {
		if( enabled ) {
			TCMaterial mat = registry.getMaterial(name, Materials.getMaterialByName(name)).setCastable(castable).setCraftable(craftable);
			if( traits.length > 0 ) {
				addTraits(mat, traits);
			}
			mat.settle();
		}
	}

	private void registerAlloys() {
		if (Options.enableAquarium) {
			registry.registerAlloy("Aquarium", Materials.getMaterialByName("aquarium").fluid, 3, "copper", 2, "zinc", 1, "prismarine", 3);
		}

		if (Options.enableCupronickel) {
			registry.registerAlloy("cupronickel", Materials.getMaterialByName("cupronickel").fluid, 4, "copper", 3, "nickel", 1 );
		}

		if (Options.enableInvar) {
			registry.registerAlloy("invar", Materials.getMaterialByName("invar").fluid, 3, "iron", 2, "nickel", 1);
		}

		if (Options.enableMithril) {
			registry.registerAlloy("mithril", Materials.getMaterialByName("mithril").fluid, 3, "silver", 2, "coldiron", 1, "mercury", 1);
		}

		if (Options.enablePewter) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy("pewter", Materials.getMaterialByName("pewter").fluid, 144, "tin", 137, "copper", 2, "lead", 5);
		}

		if (Options.enableSteel) {
			registry.registerAlloy("steel", Materials.getMaterialByName("steel").fluid, 8, "iron", 8, "coal", 1);
		}
	}
	
	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
