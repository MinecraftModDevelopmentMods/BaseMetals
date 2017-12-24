package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.crafting.IRecipe;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = ThermalExpansion.PLUGIN_MODID)
public class ThermalExpansion extends com.mcmoddev.lib.integration.plugins.ThermalExpansionBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(ThermalExpansion.PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);
		initDone = true;
	}
	
	@SubscribeEvent
	public void regShit(RegistryEvent.Register<IRecipe> event) {
		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.AQUARIUM, MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.COLDIRON,
				MaterialNames.CUPRONICKEL, MaterialNames.PEWTER, MaterialNames.STARSTEEL, MaterialNames.ZINC,
				MaterialNames.MERCURY };

		for (final String materialName : baseNames) {
			if (Materials.hasMaterial(materialName)) {
				addFurnace(Materials.hasMaterial(materialName), materialName);
				addCrucible(Materials.hasMaterial(materialName), materialName);
				addPlatePress(Materials.hasMaterial(materialName), materialName);
				addPressStorage(Materials.hasMaterial(materialName), materialName);
			}
		}

		final MMDMaterial brass = Materials.getMaterialByName(MaterialNames.BRASS);
		final MMDMaterial copper = Materials.getMaterialByName(MaterialNames.COPPER);
		final MMDMaterial cupronickel = Materials.getMaterialByName(MaterialNames.CUPRONICKEL);
		final MMDMaterial nickel = Materials.getMaterialByName(MaterialNames.NICKEL);
		final MMDMaterial zinc = Materials.getMaterialByName(MaterialNames.ZINC);

		if (Materials.hasMaterial(MaterialNames.COPPER) && Materials.hasMaterial(MaterialNames.ZINC) && Materials.hasMaterial(MaterialNames.BRASS)) {
			if ((copper.hasItem(Names.INGOT)) && (zinc.hasItem(Names.INGOT)) && (brass.hasItem(Names.INGOT))) {
				ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(copper.getItem(Names.INGOT), 2), new ItemStack(zinc.getItem(Names.INGOT), 1), new ItemStack(brass.getItem(Names.INGOT), 3));
			}
		}

		// TODO: Recently fixed for intent, We may also want bronze here
		if (Materials.hasMaterial(MaterialNames.COPPER) && Materials.hasMaterial(MaterialNames.NICKEL) && Materials.hasMaterial(MaterialNames.CUPRONICKEL)) {
			if ((copper.hasItem(Names.INGOT)) && (nickel.hasItem(Names.INGOT)) && (cupronickel.hasItem(Names.INGOT))) {
				ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(copper.getItem(Names.INGOT), 3), new ItemStack(nickel.getItem(Names.INGOT), 1), new ItemStack(cupronickel.getItem(Names.INGOT), 4));
			}
		}
	}
}
