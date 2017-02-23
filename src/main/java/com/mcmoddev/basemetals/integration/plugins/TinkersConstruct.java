package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMetalMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitRegistry;

import net.minecraft.item.Item;

import com.mcmoddev.lib.integration.plugins.tinkers.TraitLocations;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = TinkersConstruct.PLUGIN_MODID)
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

		if (Options.enableAdamantine) {
			/*
			 * Newly Suggested:
			 * Savage (ignore up to 10 points of damage)
			 * Petrarmor
			 */
			registry.getMaterial("adamant", Materials.getMaterialByName("adamantine")).setCastable(true).addTrait("coldblooded").addTrait("insatiable").settle();
		}

		if (Options.enableAntimony) {
			registry.getMaterial("antimony", Materials.getMaterialByName("antimony")).setCastable(true).settle();
		}

		if (Options.enableAquarium) {
			registry.getMaterial("aquarium", Materials.getMaterialByName("aquarium")).setCastable(true).addTrait("aquadynamic").addTrait("jagged",TraitLocations.HEAD).addTrait("aquadynamic",TraitLocations.HEAD).settle();
			registry.registerAlloy("Aquarium", Materials.getMaterialByName("aquarium").fluid, 3, "copper", 2, "zinc", 1, "prismarine", 3);
		}

		if (Options.enableBismuth) {
			registry.getMaterial("bismuth", Materials.getMaterialByName("bismuth")).setCastable(true).settle();
		}

		if (Options.enableBrass) {
			registry.getMaterial("brass", com.mcmoddev.lib.init.Materials.getMaterialByName("brass")).addTrait("dense").setCastable(true).settle();
		}

		if (Options.enableCoal) {
			registerFluid(Materials.vanilla_coal, 144);
		}

		if (Options.enableColdIron) {
			registry.getMaterial("coldiron", Materials.getMaterialByName("coldiron")).setCastable(true).addTrait("freezing").settle();
		}

		if (Options.enableCupronickel) {
			registry.getMaterial("cupronickel", com.mcmoddev.lib.init.Materials.getMaterialByName("cupronickel")).setCastable(true).settle();
			registry.registerAlloy("cupronickel", Materials.getMaterialByName("cupronickel").fluid, 4, "copper", 3, "nickel", 1 );
		}

		if (Options.enableInvar) {
			registry.getMaterial("invar", Materials.getMaterialByName("invar")).setCastable(true).settle();
			registry.registerAlloy("invar", Materials.getMaterialByName("invar").fluid, 3, "iron", 2, "nickel", 1);
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

		if (Options.enableMithril) {
			registry.getMaterial("mithril", Materials.getMaterialByName("mithril")).setCastable(true).addTrait("holy").settle();
			registry.registerAlloy("mithril", Materials.getMaterialByName("mithril").fluid, 3, "silver", 2, "coldiron", 1, "mercury", 1);
		}

		if (Options.enableNickel) {
			registry.getMaterial("nickel", Materials.getMaterialByName("nickel")).setCastable(true).settle();
		}

		if (Options.enablePewter) {
			registry.getMaterial("pewter", Materials.getMaterialByName("pewter")).setCastable(true).addTrait("soft").settle();
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			registry.registerAlloy("pewter", Materials.getMaterialByName("pewter").fluid, 144, "tin", 137, "copper", 2, "lead", 5);
		}

		if (Options.enablePlatinum) {
			registry.getMaterial("platinum", Materials.getMaterialByName("platinum")).setCastable(true).settle();
		}

		if (Options.enableSilver) {
			// Anything needed?
		}

		if (Options.enableSteel) {
			registry.registerAlloy("steel", Materials.getMaterialByName("steel").fluid, 8, "iron", 8, "coal", 1);
		}

		if (Options.enableStarSteel) {
			registry.getMaterial("starsteel", Materials.getMaterialByName("starsteel")).setCastable(true).addTrait("enderference", TraitLocations.HEAD).addTrait("sparkly").settle();
		}

		if (Options.enableTin) {
			registry.getMaterial("tin", Materials.getMaterialByName("tin")).setCastable(true).settle();
		}

		if (Options.enableZinc) {
			registry.getMaterial("zinc", Materials.getMaterialByName("zinc")).setCastable(true).settle();
		}

		initDone = true;
	}
}
