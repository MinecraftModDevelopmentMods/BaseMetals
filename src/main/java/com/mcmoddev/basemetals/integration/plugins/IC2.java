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

import net.minecraftforge.common.MinecraftForge;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID, initCallback = "doHammerRecipes")
public final class IC2 extends IC2Base implements IIntegration {

	private static final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
			MaterialNames.ANTIMONY, MaterialNames.BISMUTH, MaterialNames.COLDIRON,
			MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL,
			MaterialNames.ZINC);

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> {
					this.registerVanillaRecipes(materialName);
					this.addMaceratorRecipes(materialName);
					this.addOreWashingPlantRecipes(materialName);
					this.addThermalCentrifugeRecipes(materialName);
					this.addMetalFormerRecipes(materialName);
					this.addCompressorRecipes(materialName);
				});

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
			final String oreDictName = diamond.getCapitalizedName();
			if (diamond.hasItem(Names.POWDER)) {
				this.addMaceratorRecipe(Oredicts.ORE + oreDictName,
						diamond.getItemStack(Names.POWDER, 2));
			}
		}

		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			final MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
			final String oreDictName = emerald.getCapitalizedName();
			if (emerald.hasItem(Names.POWDER)) {
				this.addMaceratorRecipe(Oredicts.ORE + oreDictName,
						emerald.getItemStack(Names.POWDER, 2));
			}
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	/**
	 *
	 */
	public void doHammerRecipes() {
		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(this::addForgeHammerRecipe);
	}
}
