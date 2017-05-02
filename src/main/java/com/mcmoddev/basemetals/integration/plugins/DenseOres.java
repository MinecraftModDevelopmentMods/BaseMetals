package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = DenseOres.PLUGIN_MODID)
public class DenseOres extends com.mcmoddev.lib.integration.plugins.DenseOresBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled("denseores")) {
			return;
		}

		registerOres();

		initDone = true;
	}

	/**
	 * Register all ores that are currently known by the materials registry
	 * 
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	private static void registerOres() {
		final String[] baseNames = new String[] {
				MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH,
				MaterialNames.COLDIRON,
				MaterialNames.COPPER,
				MaterialNames.LEAD,
				MaterialNames.MERCURY,
				MaterialNames.NICKEL,
				MaterialNames.PLATINUM,
				MaterialNames.SILVER,
				MaterialNames.TIN,
				MaterialNames.ZINC
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String ore = baseNames[i];
			final MMDMaterial mat = Materials.getMaterialByName(ore);
			if (mat != null && Options.materialEnabled(ore)) {
				String baseMaterial;
				switch (ore) {
					case MaterialNames.ADAMANTINE:
					case MaterialNames.COLDIRON:
						baseMaterial = Oredicts.NETHERRACK;
						break;
					default:
						baseMaterial = Oredicts.STONE;
				}
				registerOre(String.format("%s_%s", ore, Oredicts.ORE), BaseMetals.MODID, baseMaterial, 0);
			}
		}
	}
}
