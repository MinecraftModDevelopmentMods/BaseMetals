package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends com.mcmoddev.lib.integration.plugins.IC2Base implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.isModEnabled(IC2.PLUGIN_MODID)) {
			return;
		}

		// registerVanillaRecipes(MaterialNames.ADAMANTINE);
		// addMaceratorRecipes(MaterialNames.ADAMANTINE);

		initDone = true;
	}
}
