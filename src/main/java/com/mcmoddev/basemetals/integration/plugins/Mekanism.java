package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.MekanismBase;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public final class Mekanism extends MekanismBase implements IIntegration {

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.BISMUTH, MaterialNames.COLDIRON,
				MaterialNames.PLATINUM, MaterialNames.NICKEL, MaterialNames.STARSTEEL,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial).filter(Mekanism::isMaterialNotEmpty)
				.forEach(materialName -> {
					addGassesForMaterial(materialName);
					addOreMultiplicationRecipes(materialName);
				});

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			this.addVanillaOreMultiplicationRecipes(MaterialNames.DIAMOND);
		}
		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			this.addVanillaOreMultiplicationRecipes(MaterialNames.EMERALD);
		}
	}

	private static boolean isMaterialNotEmpty(@Nonnull final String materialName) {
		return !Materials.getMaterialByName(materialName).isEmpty();
	}

	private void addVanillaOreMultiplicationRecipes(final String materialName) {
		if (Materials.hasMaterial(materialName)) {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			if (material.hasBlock(Names.ORE) && (material.hasItem(Names.INGOT))) {
				addCrusherRecipe(material.getBlockItemStack(Names.ORE),
						material.getItemStack(Names.INGOT, 2));
			}
			if (material.hasItem(Names.INGOT) && (material.hasItem(Names.POWDER))) {
				addCrusherRecipe(material.getItemStack(Names.INGOT),
						material.getItemStack(Names.POWDER));
			}
			if (material.hasBlock(Names.ORE) && (material.hasItem(Names.POWDER))) {
				addPurificationChamberRecipe(material.getBlockItemStack(Names.ORE),
						material.getItemStack(Names.POWDER, 2));
			}
		}
	}
}
