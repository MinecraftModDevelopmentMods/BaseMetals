package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = EnderIO.PLUGIN_MODID)
public class EnderIO extends com.mcmoddev.lib.integration.plugins.EnderIOBase implements IIntegration {

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableEnderIO) {
			return;
		}

		// TODO: Verify blocks, Slabs, Ingots, Ore Blocks
		addSagMillRecipe(MaterialNames.ADAMANTINE);
		addSagMillRecipe(MaterialNames.ANTIMONY);
		addSagMillRecipe(MaterialNames.AQUARIUM);
		addSagMillRecipe(MaterialNames.BISMUTH);
		addSagMillRecipe(MaterialNames.BRASS);
		addSagMillRecipe(MaterialNames.BRONZE);
		addSagMillRecipe(MaterialNames.COLDIRON);
		addSagMillRecipe(MaterialNames.COPPER);
		addSagMillRecipe(MaterialNames.CUPRONICKEL);
		addSagMillRecipe(MaterialNames.ELECTRUM);
		addSagMillRecipe(MaterialNames.INVAR);
		addSagMillRecipe(MaterialNames.LEAD);
		addSagMillRecipe(MaterialNames.MITHRIL);
		addSagMillRecipe(MaterialNames.NICKEL);
		addSagMillRecipe(MaterialNames.PEWTER);
		addSagMillRecipe(MaterialNames.PLATINUM);
		addSagMillRecipe(MaterialNames.SILVER);
		addSagMillRecipe(MaterialNames.STARSTEEL);
		addSagMillRecipe(MaterialNames.STEEL);
		addSagMillRecipe(MaterialNames.TIN);
		addSagMillRecipe(MaterialNames.ZINC);

		initDone = true;
	}
}
