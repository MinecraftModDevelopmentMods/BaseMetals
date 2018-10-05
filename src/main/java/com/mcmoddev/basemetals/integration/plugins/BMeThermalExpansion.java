package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.ThermalExpansion;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeThermalExpansion.PLUGIN_MODID,
           versions = BMeThermalExpansion.PLUGIN_MODID + "@(,5.3.12.17];")
public final class BMeThermalExpansion extends ThermalExpansion implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public void regShit(final RegistryEvent.Register<IRecipe> event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.COLDIRON, MaterialNames.CUPRONICKEL,
				MaterialNames.PEWTER, MaterialNames.STARSTEEL, MaterialNames.ZINC,
				MaterialNames.MERCURY, MaterialNames.REDSTONE);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					addFurnace(materialName);
					addCrucible(materialName);
					addPlatePress(materialName);
					addPressStorage(materialName);
					addPulverizer(materialName);
				});

		final MMDMaterial brass = Materials.getMaterialByName(MaterialNames.BRASS);
		final MMDMaterial copper = Materials.getMaterialByName(MaterialNames.COPPER);
		final MMDMaterial cupronickel = Materials.getMaterialByName(MaterialNames.CUPRONICKEL);
		final MMDMaterial nickel = Materials.getMaterialByName(MaterialNames.NICKEL);
		final MMDMaterial zinc = Materials.getMaterialByName(MaterialNames.ZINC);
		final MMDMaterial tin = Materials.getMaterialByName(MaterialNames.TIN);
		final MMDMaterial bronze = Materials.getMaterialByName(MaterialNames.BRONZE);

		if (hasMaterials(MaterialNames.COPPER, MaterialNames.ZINC, MaterialNames.BRASS)
				&& materialsHaveItems(Arrays.asList(MaterialNames.COPPER, MaterialNames.ZINC,
						MaterialNames.BRASS), Names.INGOT.toString())) {
			ThermalExpansionHelper.addSmelterRecipe(4000, copper.getItemStack(Names.INGOT, 2),
					zinc.getItemStack(Names.INGOT, 1), brass.getItemStack(Names.INGOT, 3));
		}

		if (hasMaterials(MaterialNames.COPPER, MaterialNames.NICKEL, MaterialNames.CUPRONICKEL)
				&& materialsHaveItems(Arrays.asList(MaterialNames.COPPER, MaterialNames.NICKEL,
						MaterialNames.CUPRONICKEL), Names.INGOT.toString())) {
			ThermalExpansionHelper.addSmelterRecipe(4000, copper.getItemStack(Names.INGOT, 3),
					nickel.getItemStack(Names.INGOT, 1), cupronickel.getItemStack(Names.INGOT, 4));
		}

		if (hasMaterials(MaterialNames.COPPER, MaterialNames.TIN, MaterialNames.BRONZE)
				&& materialsHaveItems(Arrays.asList(MaterialNames.COPPER, MaterialNames.TIN,
						MaterialNames.BRONZE), Names.INGOT.toString())) {
			ThermalExpansionHelper.addSmelterRecipe(4000, copper.getItemStack(Names.INGOT, 3),
					tin.getItemStack(Names.INGOT, 1), bronze.getItemStack(Names.INGOT, 3));
		}
	}

	private static boolean materialsHaveItems(final List<String> materialNames,
			final String... items) {
		for (final String item : items) {
			for (final String materialName : materialNames) {
				if (!Materials.getMaterialByName(materialName)
						.hasItem(item)) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean hasMaterials(final String... materials) {
		for (final String materialName : materials) {
			if (!Materials.hasMaterial(materialName)) {
				return false;
			}
		}
		return true;
	}
}
