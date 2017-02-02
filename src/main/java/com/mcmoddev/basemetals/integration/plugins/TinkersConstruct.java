package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.init.Fluids;
import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;

import cyano.basemetals.init.Materials;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(TinkersConstruct.PLUGIN_MODID)
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstruct implements IIntegration {

//	public static final String PLUGIN_MODID = "tconstruct";

//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_TINKERS_CONSTRUCT) {
			return;
		}

		if (Options.ENABLE_ADAMANTINE) {
			registerMaterial(Materials.adamantine.getName(), false, true);
		}

		if (Options.ENABLE_ANTIMONY) {
			registerMaterial(Materials.antimony.getName(), false, true);
		}

		if (Options.ENABLE_AQUARIUM) {
			registerMaterial(Materials.aquarium.getName(), false, true);
			// registerAlloy(Materials.aquarium.getName(), 3, new String[] { "copper", "zinc", "prismarine" }, new int[] { 2, 1, 3}); // Not possible currently
		}

		if (Options.ENABLE_BISMUTH) {
			registerMaterial(Materials.bismuth.getName(), false, true);
		}

		if (Options.ENABLE_BRASS) {
			registerMaterial(Materials.brass.getName(), false, true);
			// registerAlloy(Materials.brass.getName(), 3, new String[] { "copper", "zinc" }, new int[] { 2, 1 }); // TCon already has Brass alloy
		}

		//if (Options.ENABLE_BRONZE) {
			// registerMaterial(Materials.bronze.getName(), false, true);
			// registerAlloy(Materials.bronze.getName(), 4, new String[] { "copper", "tin" }, new int[] { 3, 1 }); // TCon already has Bronze alloy
		//}

		if (Options.ENABLE_COLDIRON) {
			registerMaterial(Materials.coldiron.getName(), false, true);
		}

		//if (Options.ENABLE_COPPER) {
			// registerMaterial(Materials.copper.getName(), false, true);
		//}

		if (Options.ENABLE_CUPRONICKEL) {
			registerMaterial(Materials.cupronickel.getName(), false, true);
			registerAlloy(Materials.cupronickel.getName(), 4, new String[] { "copper", "nickel" }, new int[] { 3, 1 });
		}

		//if (Options.ENABLE_ELECTRUM) {
			// registerMaterial(Materials.electrum.getName(), false, true);
		// registerAlloy(Materials.electrum.getName(), 2, new String[] { "silver", "gold" }, new int[] { 2, 1 }); // TCon already has Electrum alloy
		//}

		if (Options.ENABLE_INVAR) {
			registerMaterial(Materials.invar.getName(), false, true);
			// registerAlloy(Materials.invar.getName(), 3, new String[] { "iron", "nickel" }, new int[] { 2, 1 }); // TCon already has Invar alloy
		}

		//if (Options.ENABLE_LEAD) {
			// registerTinkerMaterial(Materials.lead.getName(), false, true);
		//}

		if (Options.ENABLE_MERCURY) {
			//registerMaterial(Materials.mercury.getName(), false, true); // Crashes
			registerFluid(Fluids.fluidMercury, false);
		}

		if (Options.ENABLE_MITHRIL) {
			registerMaterial(Materials.mithril.getName(), false, true);
			registerAlloy(Materials.mithril.getName(), 3, new String[] { "silver", "coldiron", "mercury" }, new int[] { 2, 1, 1});
		}

		if (Options.ENABLE_NICKEL) {
			 registerMaterial(Materials.nickel.getName(), false, true);
		}

		if (Options.ENABLE_PEWTER) {
			registerMaterial(Materials.pewter.getName(), false, true);
		}

		if (Options.ENABLE_PLATINUM) {
			registerMaterial(Materials.platinum.getName(), false, true);
		}

		//if (Options.ENABLE_SILVER) {
			// registerMaterial(Materials.silver.getName(), false, true);
		//}

		if (Options.ENABLE_STARSTEEL) {
			registerMaterial(Materials.starsteel.getName(), false, true);
		}

		//if (Options.ENABLE_STEEL) {
			// registerMaterial(Materials.steel.getName(), false, true);
			// registerAlloy(Materials.steel.getName(), 8, new String[] { "iron", "coal" }, new int[] { 8, 1 }); // TCon already has Steel alloy?
		//}

		if (Options.ENABLE_TIN) {
			registerMaterial(Materials.tin.getName(), false, true);
		}

		if (Options.ENABLE_ZINC) {
			registerMaterial(Materials.zinc.getName(), false, true);
		}

		initDone = true;
	}
}
