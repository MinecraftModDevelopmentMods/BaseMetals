package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitLocations;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

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
		if (initDone || !Options.isModEnabled(TinkersConstruct.PLUGIN_MODID)) {
			return;
		}

		TraitRegistry.initTiCTraits();
		TraitRegistry.initMetalsTraits();
		ModifierRegistry.initModifiers();

		registerMaterial(Materials.hasMaterial(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(Materials.hasMaterial(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, TraitNames.AQUADYNAMIC, TraitNames.JAGGED, TraitLocations.HEAD, TraitNames.AQUADYNAMIC, TraitLocations.HEAD);
		registerMaterial(Materials.hasMaterial(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.BRASS), MaterialNames.BRASS, true, false, TraitNames.DENSE);
		// registerMaterial(Materials.hasMaterial(MaterialNames.BRONZE), MaterialNames.BRONZE, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, TraitNames.FREEZING);
		registerMaterial(Materials.hasMaterial(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, TraitNames.HOLY);
		registerMaterial(Materials.hasMaterial(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, TraitNames.SOFT);
		registerMaterial(Materials.hasMaterial(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, TraitNames.ENDERFERENCE, TraitLocations.HEAD, TraitNames.SPARKLY);
		registerMaterial(Materials.hasMaterial(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Materials.hasMaterial(MaterialNames.ZINC), MaterialNames.ZINC, true, false);

		registerAlloys();

		if (Materials.hasMaterial(MaterialNames.COAL)) {
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
		if ((Materials.hasMaterial(MaterialNames.LEAD)) && (Materials.getMaterialByName(MaterialNames.LEAD).hasBlock(Names.PLATE))) {

			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			registry.registerFluid(mercury, 144);
			if (Options.isThingEnabled("Basics")) {
				registerModifierItem("toxic", mercury.getItem(Names.POWDER));
			}
		}

		if (Materials.hasMaterial(MaterialNames.SILVER)) {
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
			registerMaterial(name, castable, craftable, traits);
		}
	}

	private void registerMaterial(String name, boolean castable, boolean craftable, String... traits) {
		final MMDMaterial mmdMat = Materials.getMaterialByName(name);
		final TCMaterial mat = registry.getMaterial(name, mmdMat);

		mat.setCastable(castable);
		mat.setCraftable(craftable);

		if (traits.length > 0) {
			addTraits(mat, traits);
		}

		mat.settle();
	}

	private void registerAlloys() {
		if (Materials.hasMaterial(MaterialNames.AQUARIUM) &&
				Materials.hasMaterial(MaterialNames.COPPER) && Materials.hasMaterial(MaterialNames.ZINC)) {
			registry.registerAlloy(MaterialNames.AQUARIUM, Materials.getMaterialByName(MaterialNames.AQUARIUM).getFluid(), 3, MaterialNames.COPPER, 2, MaterialNames.ZINC, 1, MaterialNames.PRISMARINE, 3);
		}

		if (Materials.hasMaterial(MaterialNames.CUPRONICKEL) &&
				Materials.hasMaterial(MaterialNames.COPPER) &&
				Materials.hasMaterial(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.CUPRONICKEL, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getFluid(), 4, MaterialNames.COPPER, 3, MaterialNames.NICKEL, 1);
		}

		if (Materials.hasMaterial(MaterialNames.INVAR) &&
				Materials.hasMaterial(MaterialNames.NICKEL)) {
			registry.registerAlloy(MaterialNames.INVAR, Materials.getMaterialByName(MaterialNames.INVAR).getFluid(), 3, MaterialNames.IRON, 2, MaterialNames.NICKEL, 1);
		}

		if (Materials.hasMaterial(MaterialNames.MITHRIL) &&
				Materials.hasMaterial(MaterialNames.COLDIRON) &&
				Materials.hasMaterial(MaterialNames.SILVER) &&
				Materials.hasMaterial(MaterialNames.MERCURY)) {
			registry.registerAlloy(MaterialNames.MITHRIL, Materials.getMaterialByName(MaterialNames.MITHRIL).getFluid(), 3, MaterialNames.SILVER, 2, MaterialNames.COLDIRON, 1, MaterialNames.MERCURY, 1);
		}

		if (Materials.hasMaterial(MaterialNames.PEWTER) &&
				Materials.hasMaterial(MaterialNames.LEAD) &&
				Materials.hasMaterial(MaterialNames.COPPER) &&
				Materials.hasMaterial(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy(MaterialNames.PEWTER, Materials.getMaterialByName(MaterialNames.PEWTER).getFluid(), 144, MaterialNames.TIN, 137, MaterialNames.COPPER, 2, MaterialNames.LEAD, 5);
		}

		if (Materials.hasMaterial(MaterialNames.STEEL)) {
			registry.registerAlloy(MaterialNames.STEEL, Materials.getMaterialByName(MaterialNames.STEEL).getFluid(), 8, MaterialNames.IRON, 8, MaterialNames.COAL, 1);
		}
	}

	public void doSecondPass() {
		// make the second-pass MaterialIntegration call
		registry.integrateRecipes();
	}
}
