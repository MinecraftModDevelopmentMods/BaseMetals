package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.IIntegration;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableMekanism) {
			return;
		}

		if (Options.enableAdamantine) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
		}

		if (Options.enableAntimony) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ANTIMONY));
		}

		if (Options.enableBismuth) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.BISMUTH));
		}

		if (Options.enableColdIron) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.COLDIRON));
		}

		if (Options.enablePlatinum) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.PLATINUM));
		}

		if (Options.enableNickel) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.NICKEL));
		}

		if (Options.enableStarSteel) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.STARSTEEL));
		}

		if (Options.enableZinc) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ZINC));
		}

		initDone = true;
	}
}
