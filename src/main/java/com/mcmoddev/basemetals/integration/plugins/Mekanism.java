package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.crafting.IRecipe;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(Mekanism.PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);

		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC };

		for (final String materialName : baseNames) {
			if (Materials.hasMaterial(materialName)) {
				addGassesForMaterial(materialName);
			}
		}

		initDone = true;
	}

	@SubscribeEvent
	public void regCallback(RegistryEvent.Register<IRecipe> event) {
		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC };

		for (final String materialName : baseNames) {
			if (Materials.hasMaterial(materialName)) {
				addOreMultiplicationRecipes(materialName);
			}
		}

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			addVanillaOreMultiplicationRecipes(MaterialNames.DIAMOND);
		}
		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			addVanillaOreMultiplicationRecipes(MaterialNames.EMERALD);
		}
	}

	private void addVanillaOreMultiplicationRecipes(String materialName) {
		if (Materials.hasMaterial(materialName)) {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			if (material.hasBlock(Names.ORE) && (material.hasItem(Names.INGOT))) {
				addCrusherRecipe(material.getBlockItemStack(Names.ORE), material.getItemStack(Names.INGOT, 2));
			}
			if (material.hasItem(Names.INGOT) && (material.hasItem(Names.POWDER))) {
				addCrusherRecipe(material.getItemStack(Names.INGOT), material.getItemStack(Names.POWDER));
			}
			if (material.hasBlock(Names.ORE) && (material.hasItem(Names.POWDER))) {
				addPurificationChamberRecipe(material.getBlockItemStack(Names.ORE), material.getItemStack(Names.POWDER, 2));
			}
		}
	}
}
