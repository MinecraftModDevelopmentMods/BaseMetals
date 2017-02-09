package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

@BaseMetalsPlugin(IC2.PLUGIN_MODID)
public class IC2 extends com.mcmoddev.lib.integration.plugins.IC2 implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableIC2) {
			return;
		}

//		registerVanillaRecipes(Materials.adamantine);
//		addMaceratorRecipes(Materials.adamantine);

		initDone = true;
	}
}
