package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;

/**
 * VeinMiner Integration Plugin
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = VeinMiner.PLUGIN_MODID)
public class VeinMiner extends com.mcmoddev.lib.integration.plugins.VeinMinerBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled("veinminer")) {
			return;
		}

		addToolsForMaterial(MaterialNames.ADAMANTINE);
		addToolsForMaterial(MaterialNames.ANTIMONY);
		addToolsForMaterial(MaterialNames.AQUARIUM);
		addToolsForMaterial(MaterialNames.BISMUTH);
		addToolsForMaterial(MaterialNames.BRASS);
		addToolsForMaterial(MaterialNames.BRONZE);
		addToolsForMaterial(MaterialNames.COLDIRON);
		addToolsForMaterial(MaterialNames.COPPER);
		addToolsForMaterial(MaterialNames.CUPRONICKEL);
		addToolsForMaterial(MaterialNames.DIAMOND);
		addToolsForMaterial(MaterialNames.ELECTRUM);
		addToolsForMaterial(MaterialNames.GOLD);
		addToolsForMaterial(MaterialNames.INVAR);
		addToolsForMaterial(MaterialNames.IRON);
		addToolsForMaterial(MaterialNames.LEAD);
		addToolsForMaterial(MaterialNames.MITHRIL);
		addToolsForMaterial(MaterialNames.NICKEL);
		addToolsForMaterial(MaterialNames.PEWTER);
		addToolsForMaterial(MaterialNames.PLATINUM);
		addToolsForMaterial(MaterialNames.SILVER);
		addToolsForMaterial(MaterialNames.STARSTEEL);
		addToolsForMaterial(MaterialNames.STEEL);
		addToolsForMaterial(MaterialNames.TIN);
		addToolsForMaterial(MaterialNames.WOOD);
		addToolsForMaterial(MaterialNames.ZINC);

		initDone = true;
	}
}
