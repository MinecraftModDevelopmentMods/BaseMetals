package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.EnderIOBase;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = EnderIO.PLUGIN_MODID)
public class EnderIO extends EnderIOBase implements IIntegration {

	/**
	 *
	 */
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM, MaterialNames.INVAR,
				MaterialNames.MITHRIL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> addSagMillRecipe(materialName, 3600));

		addSagMillRecipe(MaterialNames.COPPER, 2, MaterialNames.GOLD, 1, 3600);
		addSagMillRecipe(MaterialNames.LEAD, 2, MaterialNames.SILVER, 1, 3600);
		addSagMillRecipe(MaterialNames.NICKEL, 2, MaterialNames.PLATINUM, 1, 3600);
		addSagMillRecipe(MaterialNames.SILVER, 2, MaterialNames.LEAD, 1, 3600);
		addSagMillRecipe(MaterialNames.IRON, 2, MaterialNames.NICKEL, 1, 3600);
	}
}
