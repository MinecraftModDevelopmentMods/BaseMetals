package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = ThermalExpansion.PLUGIN_MODID)
public class ThermalExpansion extends com.mcmoddev.lib.integration.plugins.ThermalExpansionBase
		implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void regShit(RegistryEvent.Register<IRecipe> event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.AQUARIUM, MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.COLDIRON,
				MaterialNames.CUPRONICKEL, MaterialNames.PEWTER, MaterialNames.STARSTEEL, MaterialNames.ZINC,
				MaterialNames.MERCURY);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					addFurnace(materialName);
					addCrucible(materialName);
					addPlatePress(materialName);
					addPressStorage(materialName);
				});

		final MMDMaterial brass = Materials.getMaterialByName(MaterialNames.BRASS);
		final MMDMaterial copper = Materials.getMaterialByName(MaterialNames.COPPER);
		final MMDMaterial cupronickel = Materials.getMaterialByName(MaterialNames.CUPRONICKEL);
		final MMDMaterial nickel = Materials.getMaterialByName(MaterialNames.NICKEL);
		final MMDMaterial zinc = Materials.getMaterialByName(MaterialNames.ZINC);

		if (Materials.hasMaterial(MaterialNames.COPPER) && Materials.hasMaterial(MaterialNames.ZINC)
				&& Materials.hasMaterial(MaterialNames.BRASS)) {
			if ((copper.hasItem(Names.INGOT)) && (zinc.hasItem(Names.INGOT)) && (brass.hasItem(Names.INGOT))) {
				ThermalExpansionHelper.addSmelterRecipe(4000, copper.getItemStack(Names.INGOT, 2),
						zinc.getItemStack(Names.INGOT, 1), brass.getItemStack(Names.INGOT, 3));
			}
		}

		// TODO: Recently fixed for intent, We may also want bronze here
		if (Materials.hasMaterial(MaterialNames.COPPER) && Materials.hasMaterial(MaterialNames.NICKEL)
				&& Materials.hasMaterial(MaterialNames.CUPRONICKEL)) {
			if ((copper.hasItem(Names.INGOT)) && (nickel.hasItem(Names.INGOT)) && (cupronickel.hasItem(Names.INGOT))) {
				ThermalExpansionHelper.addSmelterRecipe(4000, copper.getItemStack(Names.INGOT, 3),
						nickel.getItemStack(Names.INGOT, 1), cupronickel.getItemStack(Names.INGOT, 4));
			}
		}
	}
}
