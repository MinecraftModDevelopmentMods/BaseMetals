package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;

@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.Mekanism implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableMekanism) {
			return;
		}

		MetalMaterial material;

		if (Options.enableAdamantine) {
			material = Materials.adamantine;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableAntimony) {
			material = Materials.antimony;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableBismuth) {
			material = Materials.bismuth;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableColdIron) {
			material = Materials.coldiron;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enablePlatinum) {
			material = Materials.platinum;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableNickel) {
			material = Materials.nickel;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableStarSteel) {
			material = Materials.starsteel;
			addOreMultiplicationRecipes(material);
		}

		if (Options.enableZinc) {
			material = Materials.zinc;
			addOreMultiplicationRecipes(material);
		}

		initDone = true;
	}
}
