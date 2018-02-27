package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.crafting.IRecipe;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID, initCallback="doHammerRecipes")
public class IC2 extends com.mcmoddev.lib.integration.plugins.IC2Base implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void mainInteraction(RegistryEvent.Register<IRecipe> event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					registerVanillaRecipes(materialName);
					addMaceratorRecipes(materialName);
					addOreWashingPlantRecipes(materialName);
					addThermalCentrifugeRecipes(materialName);
					addMetalFormerRecipes(materialName);
					addCompressorRecipes(materialName);
				});

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
			final String oreDictName = diamond.getCapitalizedName();
			if (diamond.hasItem(Names.POWDER)) {
				addMaceratorRecipe(Oredicts.ORE + oreDictName, diamond.getItemStack(Names.POWDER, 2));
			}
		}

		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			final MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
			final String oreDictName = emerald.getCapitalizedName();
			if (emerald.hasItem(Names.POWDER)) {
				addMaceratorRecipe(Oredicts.ORE + oreDictName, emerald.getItemStack(Names.POWDER, 2));
			}
		}

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void doHammerRecipes() {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC);
		materials.stream().filter(Materials::hasMaterial)
		.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
		.forEach(this::addForgeHammerRecipe);
	}
}
