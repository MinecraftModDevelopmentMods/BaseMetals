package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.ItemStack;

@MMDPlugin( addonId = BaseMetals.MODID, pluginId = ThermalExpansion.PLUGIN_MODID )
public class ThermalExpansion extends com.mcmoddev.lib.integration.plugins.ThermalExpansionBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.isModEnabled(ThermalExpansion.PLUGIN_MODID)) {
			return;
		}

		final String[] baseNames = new String[] {
			MaterialNames.ADAMANTINE,
			MaterialNames.ANTIMONY,
			MaterialNames.AQUARIUM,
			MaterialNames.BISMUTH,
			MaterialNames.BRASS,
			MaterialNames.COLDIRON,
			MaterialNames.CUPRONICKEL,
			MaterialNames.PEWTER,
			MaterialNames.STARSTEEL,
			MaterialNames.ZINC,
			MaterialNames.MERCURY
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String ore = baseNames[i];
			if (Options.isMaterialEnabled(ore)) {
				addFurnace(Options.isMaterialEnabled(ore), ore);
				addCrucible(Options.isMaterialEnabled(ore), ore);
				addPlatePress(Options.isMaterialEnabled(ore), ore);
				addPressStorage(Options.isMaterialEnabled(ore), ore);
			}
		}

		ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Materials.getMaterialByName(MaterialNames.COPPER).getItem(Names.INGOT), 2), new ItemStack(Materials.getMaterialByName(MaterialNames.ZINC).getItem(Names.INGOT), 1), new ItemStack(Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.INGOT), 3));
		ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Materials.getMaterialByName(MaterialNames.COPPER).getItem(Names.INGOT), 3), new ItemStack(Materials.getMaterialByName(MaterialNames.NICKEL).getItem(Names.INGOT), 1), new ItemStack(Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getItem(Names.INGOT), 4));
		initDone = true;
	}
}
