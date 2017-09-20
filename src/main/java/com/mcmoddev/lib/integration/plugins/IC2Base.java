package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.Item;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IC2Base implements IIntegration {

	public static final String PLUGIN_MODID = "ic2";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	protected void registerVanillaRecipes(@Nonnull final String materialName) {
		registerVanillaRecipes(Materials.getMaterialByName(materialName));
	}

	protected void registerVanillaRecipes(@Nonnull final MMDMaterial material) {
		// With the big 1.12 change this is almost certainly going to only be smelting recipes
		final Item ingot = material.getItem(Names.INGOT);
		GameRegistry.addSmelting(material.getItem(Names.CRUSHED), new ItemStack(ingot, 1), 0);
		GameRegistry.addSmelting(material.getItem(Names.CRUSHED_PURIFIED), new ItemStack(ingot, 1), 0);
	}

	protected void addMaceratorRecipes(@Nonnull final String materialName) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		addMaceratorRecipes(material);
	}

	protected void addMaceratorRecipes(@Nonnull final MMDMaterial material) {
		String oreDictName = material.getCapitalizedName();
		IRecipeInput inputOre = Recipes.inputFactory.forOreDict(Oredicts.ORE + oreDictName);
		IRecipeInput inputDensePlate = Recipes.inputFactory.forOreDict(Oredicts.PLATE_DENSE + oreDictName);
		Recipes.macerator.addRecipe(inputOre, null, false, new ItemStack(material.getItem(Names.CRUSHED), 2));
		Recipes.macerator.addRecipe(inputDensePlate, null, false, new ItemStack(material.getItem(Names.POWDER), 8));
	}
	
	protected void addOreWashingPlantRecipes(@Nonnull final String materialName) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		addOreWashingPlantRecipes(material);
	}
	
	protected void addOreWashingPlantRecipes(@Nonnull final MMDMaterial material) {
		String oreDictName = material.getCapitalizedName();
		IRecipeInput inputOre = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED + oreDictName);
		Recipes.oreWashing.addRecipe(inputOre, null, false, new ItemStack(material.getItem(Names.CRUSHED_PURIFIED)), new ItemStack(material.getItem(Names.SMALLPOWDER), 2));
	}
	
	protected void addThermalCentrifugeRecipes(@Nonnull final String materialName) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		addThermalCentrifugeRecipes(material);
	}
	
	protected void addThermalCentrifugeRecipes(@Nonnull final MMDMaterial material) {
		String oreDictName = material.getCapitalizedName();
		IRecipeInput inputOreCP = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED_PURIFIED + oreDictName);
		IRecipeInput inputOreC = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED + oreDictName);
		Recipes.centrifuge.addRecipe(inputOreCP, null, false, new ItemStack(material.getItem(Names.POWDER)), new ItemStack(material.getItem(Names.SMALLPOWDER),2));
		Recipes.centrifuge.addRecipe(inputOreC, null, false, new ItemStack(material.getItem(Names.POWDER)), new ItemStack(material.getItem(Names.SMALLPOWDER)));
	}
	
	protected void addMetalFormerRecipes(@Nonnull final String materialName) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		addMetalFormerRecipes(material);
	}
	
	protected void addMetalFormerRecipes(@Nonnull final MMDMaterial material) {
		String oreDictName = material.getCapitalizedName();
		IRecipeInput inputIngot = Recipes.inputFactory.forOreDict(Oredicts.INGOT + oreDictName);
		ItemStack outputPlate = new ItemStack(material.getBlock(Names.PLATE));
		Recipes.metalformerRolling.addRecipe(inputIngot, null, false, outputPlate);
	}
}
