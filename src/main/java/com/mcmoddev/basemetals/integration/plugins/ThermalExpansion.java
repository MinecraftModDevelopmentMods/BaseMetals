package com.mcmoddev.basemetals.integration.plugins;

import java.util.HashMap;
import java.util.Map;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.basemetals.data.MaterialNames;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.ItemStack;

@MMDPlugin( addonId = BaseMetals.MODID, pluginId = ThermalExpansion.PLUGIN_MODID )
public class ThermalExpansion extends com.mcmoddev.lib.integration.plugins.ThermalExpansionBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableThermalExpansion) {
			return;
		}

		Map<String, Boolean> materials = new HashMap<>();

		materials.put("Adamantine", Options.enableAdamantine);
		materials.put("Antimony", Options.enableAntimony);
		materials.put("Aquarium", Options.enableAquarium);
		materials.put("Bismuth", Options.enableBismuth);
		materials.put("Brass", Options.enableBrass);
		materials.put("Bronze", Options.enableBronze);
		materials.put("ColdIron", Options.enableColdIron);
		materials.put("Cupronickel", Options.enableCupronickel);
		materials.put("Invar", Options.enableInvar);
		materials.put("Mithril", Options.enableMithril);
		materials.put("Nickel", Options.enableNickel);
		materials.put("Pewter", Options.enablePewter);
		materials.put("Platinum", Options.enablePlatinum);
		materials.put("StarSteel", Options.enableStarSteel);
		materials.put("Tin", Options.enableTin);
		materials.put("Zinc", Options.enableZinc);
		materials.put("Lead", Options.enableLead);
		materials.put("Mercury", Options.enableMercury);
		materials.put("Silver", Options.enableSilver);

		for( Map.Entry<String, Boolean> e : materials.entrySet() ) {
			addFurnace(e.getValue(), e.getKey());
			addCrucible(e.getValue(), e.getKey());
			addPlatePress(e.getValue(), e.getKey());
			addPressStorage(e.getValue(), e.getKey());
		}

		ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Materials.copper.getItem(Names.INGOT), 2), new ItemStack(Materials.zinc.getItem(Names.INGOT), 1), new ItemStack(Materials.brass.getItem(Names.INGOT), 3));
		ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Materials.copper.getItem(Names.INGOT), 3), new ItemStack(Materials.nickel.getItem(Names.INGOT), 1), new ItemStack(Materials.cupronickel.getItem(Names.INGOT), 4));
		initDone = true;
	}
}
