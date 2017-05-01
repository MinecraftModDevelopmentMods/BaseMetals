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

		if (Options.materialEnabled.get(MaterialNames.ADAMANTINE)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
		}

		if (Options.materialEnabled.get(MaterialNames.ANTIMONY)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ANTIMONY));
		}

		if (Options.materialEnabled.get(MaterialNames.BISMUTH)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.BISMUTH));
		}

		if (Options.materialEnabled.get(MaterialNames.COLDIRON)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.COLDIRON));
		}

		if (Options.materialEnabled.get(MaterialNames.PLATINUM)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.PLATINUM));
		}

		if (Options.materialEnabled.get(MaterialNames.NICKEL)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.NICKEL));
		}

		if (Options.materialEnabled.get(MaterialNames.STARSTEEL)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.STARSTEEL));
		}

		if (Options.materialEnabled.get(MaterialNames.ZINC)) {
			addOreMultiplicationRecipes(Materials.getMaterialByName(MaterialNames.ZINC));
		}

		initDone = true;
	}
}
