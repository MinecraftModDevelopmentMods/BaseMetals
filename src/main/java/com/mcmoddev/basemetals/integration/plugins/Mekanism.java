package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;

@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.Mekanism implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableMekanism) {
			return;
		}

		if (Options.enableAdamantine) {
			addOreMultiplicationRecipes(Materials.adamantine);
		}

		if (Options.enableAntimony) {
			addOreMultiplicationRecipes(Materials.antimony);
		}

		if (Options.enableBismuth) {
			addOreMultiplicationRecipes(Materials.bismuth);
		}

		if (Options.enableColdIron) {
			addOreMultiplicationRecipes(Materials.coldiron);
		}

		if (Options.enablePlatinum) {
			addOreMultiplicationRecipes(Materials.platinum);
		}

		if (Options.enableNickel) {
			addOreMultiplicationRecipes(Materials.nickel);
		}

		if (Options.enableStarSteel) {
			addOreMultiplicationRecipes(Materials.starsteel);
		}

		if (Options.enableZinc) {
			addOreMultiplicationRecipes(Materials.zinc);
		}

		initDone = true;
	}
}
