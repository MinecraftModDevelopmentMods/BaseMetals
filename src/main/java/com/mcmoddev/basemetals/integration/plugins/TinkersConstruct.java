package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;
import com.mcmoddev.basemetals.data.MaterialNames;

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

		registerMaterial(Options.enableAdamantine, MaterialNames.ADAMANTINE, true, false, "coldblooded", "insatiable" );
		registerMaterial(Options.enableAntimony, MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.enableAquarium, MaterialNames.AQUARIUM, true, false, "aquadynamic", "jagged", TraitLocations.HEAD, "aquadynamic", TraitLocations.HEAD);
		registerMaterial(Options.enableBismuth, MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.enableBrass, MaterialNames.BRASS, true, false, "dense");
		//registerMaterial(Options.enableBronze, MaterialNames.BRONZE, true, false);
		registerMaterial(Options.enableColdIron, MaterialNames.COLDIRON, true, false, "freezing");
		registerMaterial(Options.enableCupronickel, MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.enableInvar, MaterialNames.INVAR, true, false);
		registerMaterial(Options.enableMithril, MaterialNames.MITHRIL, true, false, "holy");
		registerMaterial(Options.enableNickel, MaterialNames.NICKEL, true, false);
		registerMaterial(Options.enablePewter, MaterialNames.PEWTER, true, false, "soft");
		registerMaterial(Options.enablePlatinum, MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.enableStarSteel, MaterialNames.STARSTEEL, true, false, "enderference", TraitLocations.HEAD, "sparkly");
		registerMaterial(Options.enableTin, MaterialNames.TIN, true, false);
		registerMaterial(Options.enableZinc, MaterialNames.ZINC, true, false);
		
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
			registry.registerFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 144);
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
			registry.registerAlloy(MaterialNames.AQUARIUM, Materials.getMaterialByName(MaterialNames.AQUARIUM).fluid, 3, MaterialNames.COPPER, 2, MaterialNames.ZINC, 1, MaterialNames.PRISMARINE, 3);
		}

		if (Options.enableCupronickel) {
			registry.registerAlloy(MaterialNames.CUPRONICKEL, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).fluid, 4, MaterialNames.COPPER, 3, MaterialNames.NICKEL, 1 );
		}

		if (Options.enableInvar) {
			registry.registerAlloy(MaterialNames.INVAR, Materials.getMaterialByName(MaterialNames.INVAR).fluid, 3, MaterialNames.IRON, 2, MaterialNames.NICKEL, 1);
		}

		if (Options.enableMithril) {
			registry.registerAlloy(MaterialNames.MITHRIL, Materials.getMaterialByName(MaterialNames.MITHRIL).fluid, 3, MaterialNames.SILVER, 2, MaterialNames.COLDIRON, 1, MaterialNames.MERCURY, 1);
		}

		if (Options.enablePewter) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy(MaterialNames.PEWTER, Materials.getMaterialByName(MaterialNames.PEWTER).fluid, 144, MaterialNames.TIN, 137, MaterialNames.COPPER, 2, MaterialNames.LEAD, 5);
		}

		if (Options.enableSteel) {
			registry.registerAlloy(MaterialNames.STEEL, Materials.getMaterialByName(MaterialNames.STEEL).fluid, 8, MaterialNames.IRON, 8, MaterialNames.COAL, 1);
		}
	}
	
	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
