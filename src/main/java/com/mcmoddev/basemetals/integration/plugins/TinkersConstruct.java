package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitLocations;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;

import net.minecraft.item.Item;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID, initCallback = "doSecondPass")
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled("tinkersconstruct")) {
			return;
		}

		TraitRegistry.initTiCTraits();
		TraitRegistry.initMetalsTraits();
		ModifierRegistry.initModifiers();

		registerMaterial(Options.materialEnabled(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, "coldblooded", "insatiable");
		registerMaterial(Options.materialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, "aquadynamic", "jagged", TraitLocations.HEAD, "aquadynamic", TraitLocations.HEAD);
		registerMaterial(Options.materialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, true, false, "dense");
		//registerMaterial(Options.materialEnabled(MaterialNames.BRONZE), MaterialNames.BRONZE, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, "freezing");
		registerMaterial(Options.materialEnabled(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, "holy");
		registerMaterial(Options.materialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, "soft");
		registerMaterial(Options.materialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, "enderference", TraitLocations.HEAD, "sparkly");
		registerMaterial(Options.materialEnabled(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Options.materialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, true, false);

		registerAlloys();

		if (Options.materialEnabled(MaterialNames.COAL)) {
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
		if ((Options.materialEnabled(MaterialNames.LEAD)) && (Options.thingEnabled("Plate"))) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			registry.registerFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 144);
			if (Options.thingEnabled("Basics")) {
				registerModifierItem("toxic", Materials.getMaterialByName(MaterialNames.MERCURY).getItem(Names.POWDER));
			}
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			// Anything needed?
		}

		registry.registerAll();
		initDone = true;
	}

	private boolean isTraitLoc(String loc) {
		switch (loc) {
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
		while (i < traits.length) {
			if (i + 1 >= traits.length) {
				mat.addTrait(traits[i]);
				i++;
				continue;
			}

			if (isTraitLoc(traits[i + 1])) {
				mat.addTrait(traits[i], traits[i + 1]);
				i++;
			} else {
				mat.addTrait(traits[i]);
			}
			i++;
		}
	}

	private void registerMaterial(boolean enabled, String name, boolean castable, boolean craftable, String... traits) {
		if (enabled) {
			TCMaterial mat = registry.getMaterial(name, Materials.getMaterialByName(name)).setCastable(castable).setCraftable(craftable);
			if (traits.length > 0) {
				addTraits(mat, traits);
			}
			mat.settle();
		}
	}

	private void registerAlloys() {
		if (Options.materialEnabled(MaterialNames.AQUARIUM) && Options.materialEnabled(MaterialNames.COPPER) && Options.materialEnabled(MaterialNames.ZINC)) {
			registry.registerAlloy(MaterialNames.AQUARIUM, Materials.getMaterialByName(MaterialNames.AQUARIUM).getFluid(), 3, MaterialNames.COPPER, 2, MaterialNames.ZINC, 1, MaterialNames.PRISMARINE, 3);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL) && Options.materialEnabled(MaterialNames.COPPER) && Options.materialEnabled(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.CUPRONICKEL, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getFluid(), 4, MaterialNames.COPPER, 3, MaterialNames.NICKEL, 1);
		}

		if (Options.materialEnabled(MaterialNames.INVAR) && Options.materialEnabled(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.INVAR, Materials.getMaterialByName(MaterialNames.INVAR).getFluid(), 3, MaterialNames.IRON, 2, MaterialNames.NICKEL, 1);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL) && Options.materialEnabled(MaterialNames.COLDIRON) && Options.materialEnabled(MaterialNames.SILVER) && Options.materialEnabled(MaterialNames.MERCURY)) {
			registry.registerAlloy(MaterialNames.MITHRIL, Materials.getMaterialByName(MaterialNames.MITHRIL).getFluid(), 3, MaterialNames.SILVER, 2, MaterialNames.COLDIRON, 1, MaterialNames.MERCURY, 1);
		}

		if (Options.materialEnabled(MaterialNames.PEWTER) && Options.materialEnabled(MaterialNames.LEAD) && Options.materialEnabled(MaterialNames.COPPER) && Options.materialEnabled(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy(MaterialNames.PEWTER, Materials.getMaterialByName(MaterialNames.PEWTER).getFluid(), 144, MaterialNames.TIN, 137, MaterialNames.COPPER, 2, MaterialNames.LEAD, 5);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			registry.registerAlloy(MaterialNames.STEEL, Materials.getMaterialByName(MaterialNames.STEEL).getFluid(), 8, MaterialNames.IRON, 8, MaterialNames.COAL, 1);
		}
	}

	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
