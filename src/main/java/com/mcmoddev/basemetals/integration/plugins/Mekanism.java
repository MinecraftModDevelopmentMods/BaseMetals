package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public final class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase
		implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.BISMUTH, MaterialNames.COLDIRON,
				MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial).filter(Mekanism::isMaterialNotEmpty)
				.forEach(materialName -> {
					addGassesForMaterial(materialName);
					addOreMultiplicationRecipes(materialName);
				});
	}

	private static boolean isMaterialNotEmpty(@Nonnull final String materialName) {
		return !Materials.getMaterialByName(materialName).isEmpty();
	}
}
