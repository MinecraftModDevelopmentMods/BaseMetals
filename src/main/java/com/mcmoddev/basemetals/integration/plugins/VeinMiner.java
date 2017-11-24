package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;

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
		if (initDone || !Options.isModEnabled(VeinMiner.PLUGIN_MODID)) {
			return;
		}

		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.AQUARIUM, MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE,
				MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.DIAMOND,
				MaterialNames.ELECTRUM, MaterialNames.GOLD, MaterialNames.INVAR, MaterialNames.IRON, MaterialNames.LEAD,
				MaterialNames.MITHRIL, MaterialNames.NICKEL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.SILVER, MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN,
				MaterialNames.WOOD, MaterialNames.ZINC };

		for (int i = 0; i < baseNames.length; i++) {
			final String material = baseNames[i];
			addToolsForMaterial(material);
		}

		initDone = true;
	}
}
