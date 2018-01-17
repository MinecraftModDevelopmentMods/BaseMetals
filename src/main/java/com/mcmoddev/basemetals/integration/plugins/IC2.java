package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.IC2Base;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends IC2Base implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(IC2.PLUGIN_MODID)) {
			return;
		}

		MinecraftForge.EVENT_BUS.register(this);

		initDone = true;
	}

	@SubscribeEvent
	public void regCallback(RegistryEvent.Register<IRecipe> event) {
		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).equals(Materials.emptyMaterial))
				.forEach(materialName -> {
					registerVanillaRecipes(materialName);
					addMaceratorRecipes(materialName);
					addOreWashingPlantRecipes(materialName);
					addThermalCentrifugeRecipes(materialName);
					addMetalFormerRecipes(materialName);
					addCompressorRecipes(materialName);
					addForgeHammerRecipe(materialName);
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
	}
}
