package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.Config.Options;

/**
 * VeinMiner Integration Plugin.
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = VeinMiner.PLUGIN_MODID)
public final class VeinMiner extends com.mcmoddev.lib.integration.plugins.VeinMinerBase
		implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.DIAMOND,
				MaterialNames.ELECTRUM, MaterialNames.GOLD, MaterialNames.INVAR, MaterialNames.IRON,
				MaterialNames.LEAD, MaterialNames.MITHRIL, MaterialNames.NICKEL,
				MaterialNames.PEWTER, MaterialNames.PLATINUM, MaterialNames.SILVER,
				MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN, MaterialNames.WOOD,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial).filter(VeinMiner::isMaterialNotEmpty)
				.forEach(com.mcmoddev.lib.integration.plugins.VeinMinerBase::addToolsForMaterial);
	}

	private static boolean isMaterialNotEmpty(@Nonnull final String materialName) {
		return !Materials.getMaterialByName(materialName).isEmpty();
	}
}
