package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.data.Names;
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
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled.get("tinkersconstruct")) {
			return;
		}

		TraitRegistry.initTiCTraits();
		TraitRegistry.initMetalsTraits();
		ModifierRegistry.initModifiers();

		registerMaterial(Options.materialEnabled.get(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, "coldblooded", "insatiable" );
		registerMaterial(Options.materialEnabled.get(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, "aquadynamic", "jagged", TraitLocations.HEAD, "aquadynamic", TraitLocations.HEAD);
		registerMaterial(Options.materialEnabled.get(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.BRASS), MaterialNames.BRASS, true, false, "dense");
		//registerMaterial(Options.materials.get(MaterialNames.BRONZE), MaterialNames.BRONZE, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, "freezing");
		registerMaterial(Options.materialEnabled.get(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, "holy");
		registerMaterial(Options.materialEnabled.get(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, "soft");
		registerMaterial(Options.materialEnabled.get(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, "enderference", TraitLocations.HEAD, "sparkly");
		registerMaterial(Options.materialEnabled.get(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Options.materialEnabled.get(MaterialNames.ZINC), MaterialNames.ZINC, true, false);

		registerAlloys();
		
		if (Options.materialEnabled.get(MaterialNames.COAL)) {
			registerFluid(Materials.getMaterialByName(MaterialNames.COAL), 144);
		}


		// As much as we'd like to, we cannot do this like this.
		// At some point between this code running and it getting
		// up to the point of registration, the state changes and
		// lead is suddenly registered.
		// There will have to be another means of making this work...
		// There is an event TiC has that covers material registration
		// we could hook that...

		// Lead itself is added by TiC
		if ((Options.materialEnabled.get(MaterialNames.LEAD)) && (Options.thingEnabled.get("Plate"))) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}

		if (Options.materialEnabled.get(MaterialNames.MERCURY)) {
			registry.registerFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 144);
			if (Options.thingEnabled.get("Basics")) {
				registerModifierItem("toxic", Materials.getMaterialByName(MaterialNames.MERCURY).getItem(Names.POWDER));
			}
		}



		if (Options.materialEnabled.get(MaterialNames.SILVER)) {
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
		if (Options.materialEnabled.get(MaterialNames.AQUARIUM) && Options.materialEnabled.get(MaterialNames.COPPER) && Options.materialEnabled.get(MaterialNames.ZINC)) {
			registry.registerAlloy(MaterialNames.AQUARIUM, Materials.getMaterialByName(MaterialNames.AQUARIUM).getFluid(), 3, MaterialNames.COPPER, 2, MaterialNames.ZINC, 1, MaterialNames.PRISMARINE, 3);
		}

		if (Options.materialEnabled.get(MaterialNames.CUPRONICKEL) && Options.materialEnabled.get(MaterialNames.COPPER) && Options.materialEnabled.get(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.CUPRONICKEL, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getFluid(), 4, MaterialNames.COPPER, 3, MaterialNames.NICKEL, 1 );
		}

		if (Options.materialEnabled.get(MaterialNames.INVAR) && Options.materialEnabled.get(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.INVAR, Materials.getMaterialByName(MaterialNames.INVAR).getFluid(), 3, MaterialNames.IRON, 2, MaterialNames.NICKEL, 1);
		}

		if (Options.materialEnabled.get(MaterialNames.MITHRIL) && Options.materialEnabled.get(MaterialNames.COLDIRON) && Options.materialEnabled.get(MaterialNames.SILVER) && Options.materialEnabled.get(MaterialNames.MERCURY)) {
			registry.registerAlloy(MaterialNames.MITHRIL, Materials.getMaterialByName(MaterialNames.MITHRIL).getFluid(), 3, MaterialNames.SILVER, 2, MaterialNames.COLDIRON, 1, MaterialNames.MERCURY, 1);
		}

		if (Options.materialEnabled.get(MaterialNames.PEWTER) && Options.materialEnabled.get(MaterialNames.LEAD) && Options.materialEnabled.get(MaterialNames.COPPER) && Options.materialEnabled.get(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy(MaterialNames.PEWTER, Materials.getMaterialByName(MaterialNames.PEWTER).getFluid(), 144, MaterialNames.TIN, 137, MaterialNames.COPPER, 2, MaterialNames.LEAD, 5);
		}

		if (Options.materialEnabled.get(MaterialNames.STEEL)) {
			registry.registerAlloy(MaterialNames.STEEL, Materials.getMaterialByName(MaterialNames.STEEL).getFluid(), 8, MaterialNames.IRON, 8, MaterialNames.COAL, 1);
		}
	}
	
	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
