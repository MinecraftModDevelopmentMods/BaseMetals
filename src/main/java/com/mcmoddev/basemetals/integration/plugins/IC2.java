package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends com.mcmoddev.lib.integration.plugins.IC2Base implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled.get("ic2")) {
			return;
		}

		// registerVanillaRecipes(Materials.adamantine);
		// addMaceratorRecipes(Materials.adamantine);

		initDone = true;
	}
}
