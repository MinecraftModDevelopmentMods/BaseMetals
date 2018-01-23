package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = DenseOres.PLUGIN_MODID)
public class DenseOres extends com.mcmoddev.lib.integration.plugins.DenseOresBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		registerOres();
	}

	/**
	 * Register all ores that are currently known by the materials registry
	 * 
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	private static void registerOres() {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.LEAD,
				MaterialNames.MERCURY, MaterialNames.NICKEL, MaterialNames.PLATINUM, MaterialNames.SILVER,
				MaterialNames.TIN, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).equals(Materials.emptyMaterial))
				.forEach(materialName -> {
					String baseMaterial;
					switch (materialName) {
						case MaterialNames.ADAMANTINE:
						case MaterialNames.COLDIRON:
							baseMaterial = Oredicts.NETHERRACK;
							break;
						default:
							baseMaterial = Oredicts.STONE;
					}
					registerOre(String.format("%s_%s", materialName, Oredicts.ORE), baseMaterial, 0);
				});
	}
}
