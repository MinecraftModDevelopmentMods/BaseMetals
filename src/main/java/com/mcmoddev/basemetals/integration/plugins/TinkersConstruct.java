package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMetalMaterial;

import slimeknights.tconstruct.tools.traits.*;
import slimeknights.tconstruct.library.materials.MaterialTypes;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstruct implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}

		if (Options.enableAdamantine) {
			TCMetalMaterial adamantTC = new TCMetalMaterial(Materials.adamantine);
			adamantTC.craftable = false;
			adamantTC.addTrait(new TraitColdblooded());
			adamantTC.addTrait(new TraitInsatiable(), MaterialTypes.HEAD);
			registerMaterial(adamantTC);
		}

		if (Options.enableAntimony) {
			registerMaterial(Materials.antimony.getName(), false, true);
		}

		if (Options.enableAquarium) {
			registerMaterial(Materials.aquarium, false, true, new TraitAquadynamic());
			// registerAlloy(Materials.aquarium.getName(), 3,
			//		new String[] { "copper", "zinc", "prismarine" },
			//		new int[] { 2, 1, 3}); // Not possible currently
		}

		if (Options.enableBismuth) {
			registerMaterial(Materials.bismuth.getName(), false, true);
		}

		if (Options.enableBrass) {
			registerMaterial(Materials.brass.getName(), false, true);
			// registerAlloy(Materials.brass.getName(), 3,
			//		new String[] { "copper", "zinc" },
			//		new int[] { 2, 1 }); // TCon already has Brass alloy
		}

		/*
		if (Options.ENABLE_BRONZE) {
			registerMaterial(Materials.bronze.getName(), false, true);
			registerAlloy(Materials.bronze.getName(), 4,
					new String[] { "copper", "tin" },
					new int[] { 3, 1 }); // TCon already has Bronze alloy
		}
		*/

		if (Options.enableCoal) {
			registerFluid(Materials.vanilla_coal, 144);
		}
		
		if (Options.enableColdIron) {
			registerMaterial(Materials.coldiron, false, true, new TraitFreezing());
		}

		/*
		if (Options.ENABLE_COPPER) {
			registerMaterial(Materials.copper.getName(), false, true);
		}
		*/

		if (Options.enableCupronickel) {
			registerMaterial(Materials.cupronickel.getName(), false, true);
			registerAlloy(Materials.cupronickel.getName(), 4,
					new String[] { "copper", "nickel" },
					new int[] { 3, 1 });
		}

		/*
		if (Options.ENABLE_ELECTRUM) {
			registerMaterial(Materials.electrum.getName(), false, true);
			registerAlloy(Materials.electrum.getName(), 2,
					new String[] { "silver", "gold" },
					new int[] { 2, 1 }); // TCon already has Electrum alloy
		}
		*/

		if (Options.enableInvar) {
			registerMaterial(Materials.invar.getName(), false, true);
			// registerAlloy(Materials.invar.getName(), 3,
			//		new String[] { "iron", "nickel" },
			//		new int[] { 2, 1 }); // TCon already has Invar alloy
		}

		/*
		if (Options.ENABLE_LEAD) {
			registerTinkerMaterial(Materials.lead.getName(), false, true);
		}
		*/

		if (Options.enableMercury) {
			registerFluid(Materials.mercury, 144);
		}

		if (Options.enableMithril) {
			registerMaterial(Materials.mithril, false, true, new TraitHoly());
			registerAlloy(Materials.mithril.getName(), 3,
					new String[] { "silver", "coldiron", "mercury" },
					new int[] { 2, 1, 1});
		}

		if (Options.enableNickel) {
			 registerMaterial(Materials.nickel.getName(), false, true);
		}

		if (Options.enablePewter) {
			registerMaterial(Materials.pewter.getName(), false, true);
		}

		if (Options.enablePlatinum) {
			registerMaterial(Materials.platinum.getName(), false, true);
		}

		if (Options.enableSteel) {
			registerAlloy(Materials.steel.fluid.getName(), 8, 
					new String[] { "iron","coal" },
					new int[]{8,1});
		}

		if (Options.enableStarSteel) {
			registerMaterial(Materials.starsteel.getName(), false, true);
		}

		if (Options.enableTin) {
			registerMaterial(Materials.tin.getName(), false, true);
		}

		if (Options.enableZinc) {
			registerMaterial(Materials.zinc.getName(), false, true);
		}

		initDone = true;
	}
}
