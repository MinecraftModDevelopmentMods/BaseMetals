package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(EnderIO.PLUGIN_MODID)
public class EnderIO extends com.mcmoddev.lib.integration.plugins.EnderIO implements IIntegration {

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableEnderIO) {
			return;
		}

		// TODO: Verify blocks, Slabs, Ingots, Oreblocks
		addSagMillRecipe("adamantine", null, 3600);
		addSagMillRecipe("antimony", null, 3600);
		addSagMillRecipe("aquarium", null, 3600);
		addSagMillRecipe("bismuth", null, 3600);
		addSagMillRecipe("brass", null, 3600);
		addSagMillRecipe("bronze", null, 3600);
		addSagMillRecipe("coldiron", null, 3600);
		addSagMillRecipe("copper", null, 3600);
		addSagMillRecipe("cupronickel", null, 3600);
		addSagMillRecipe("electrum", null, 3600);
		addSagMillRecipe("invar", null, 3600);
		addSagMillRecipe("lead", null, 3600);
		addSagMillRecipe("mithril", null, 3600);
		addSagMillRecipe("nickel", null, 3600);
		addSagMillRecipe("pewter", null, 3600);
		addSagMillRecipe("platinum", null, 3600);
		addSagMillRecipe("silver", null, 3600);
		addSagMillRecipe("starsteel", null, 3600);
		addSagMillRecipe("steel", null, 3600);
		addSagMillRecipe("tin", null, 3600);
		addSagMillRecipe("zinc", null, 3600);

		initDone = true;
	}


}
