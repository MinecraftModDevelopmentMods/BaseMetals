package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.util.ConfigBase.Options;
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
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID, postInitCallback = "doSecondPass")
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(TinkersConstruct.PLUGIN_MODID)) {
			return;
		}

		TraitRegistry.initTiCTraits();
		TraitRegistry.initMetalsTraits();
		ModifierRegistry.initModifiers();

		registerMaterial(Options.isMaterialEnabled(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, TraitNames.AQUADYNAMIC, TraitNames.JAGGED, TraitLocations.HEAD, TraitNames.AQUADYNAMIC, TraitLocations.HEAD);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, true, false, TraitNames.DENSE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, TraitNames.FREEZING);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, TraitNames.HOLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, TraitNames.SOFT);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, TraitNames.ENDERFERENCE, TraitLocations.HEAD, TraitNames.SPARKLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, true, false);

		registerAlloys();

		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
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
		if ((Options.isMaterialEnabled(MaterialNames.LEAD)) && (Options.isThingEnabled("Plate"))) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			registry.registerFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 144);
			if (Options.isThingEnabled("Basics")) {
				registerModifierItem("toxic", Materials.getMaterialByName(MaterialNames.MERCURY).getItem(Names.POWDER));
			}
		}

		if (Options.isMaterialEnabled(MaterialNames.SILVER)) {
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
		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM) && Options.isMaterialEnabled(MaterialNames.COPPER) && Options.isMaterialEnabled(MaterialNames.ZINC)) {
			registry.registerAlloy(MaterialNames.AQUARIUM, Materials.getMaterialByName(MaterialNames.AQUARIUM).getFluid(), 3, MaterialNames.COPPER, 2, MaterialNames.ZINC, 1, MaterialNames.PRISMARINE, 3);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL) && Options.isMaterialEnabled(MaterialNames.COPPER) && Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.CUPRONICKEL, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getFluid(), 4, MaterialNames.COPPER, 3, MaterialNames.NICKEL, 1);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR) && Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.INVAR, Materials.getMaterialByName(MaterialNames.INVAR).getFluid(), 3, MaterialNames.IRON, 2, MaterialNames.NICKEL, 1);
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL) && Options.isMaterialEnabled(MaterialNames.COLDIRON) && Options.isMaterialEnabled(MaterialNames.SILVER) && Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			registry.registerAlloy(MaterialNames.MITHRIL, Materials.getMaterialByName(MaterialNames.MITHRIL).getFluid(), 3, MaterialNames.SILVER, 2, MaterialNames.COLDIRON, 1, MaterialNames.MERCURY, 1);
		}

		if (Options.isMaterialEnabled(MaterialNames.PEWTER) && Options.isMaterialEnabled(MaterialNames.LEAD) && Options.isMaterialEnabled(MaterialNames.COPPER) && Options.isMaterialEnabled(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy(MaterialNames.PEWTER, Materials.getMaterialByName(MaterialNames.PEWTER).getFluid(), 144, MaterialNames.TIN, 137, MaterialNames.COPPER, 2, MaterialNames.LEAD, 5);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			registry.registerAlloy(MaterialNames.STEEL, Materials.getMaterialByName(MaterialNames.STEEL).getFluid(), 8, MaterialNames.IRON, 8, MaterialNames.COAL, 1);
		}
	}

	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
