package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled(Mekanism.PLUGIN_MODID)) {
			return;
		}

		final String[] baseNames = new String[] {
				MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH,
				MaterialNames.COLDIRON,
				MaterialNames.PLATINUM,
				MaterialNames.NICKEL,
				MaterialNames.STARSTEEL,
				MaterialNames.ZINC
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String materialName = baseNames[i];
			if (Options.materialEnabled(materialName)) {
				addOreMultiplicationRecipes(materialName);
			}
		}

		initDone = true;
	}
}
