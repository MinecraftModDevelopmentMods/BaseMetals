package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IC2Base implements IIntegration {

	public static final String PLUGIN_MODID = "ic2";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	// This is broken and needs extensive work on our side of things to make it work
	// IC2 'Recipes.advRecipes' is <em><strong>null</strong></em> at this point
	protected void addForgeHammerRecipe(@Nonnull final String materialName) {
		addForgeHammerRecipe(Materials.getMaterialByName(materialName));
	}

	private void addForgeHammerRecipe(@Nonnull final MMDMaterial material) {
/*		final ItemStack hammer = IC2Items.getItem("forge_hammer");
		final ItemStack plate = material.getItemStack("ItemBlock_"+material.getName()+"_plate", 1);
		final ItemStack casing = material.getItemStack(Names.CASING, 1);

		Recipes.advRecipes.addShapelessRecipe(casing, hammer, plate); */
	}

	protected void registerVanillaRecipes(@Nonnull final String materialName) {
		registerVanillaRecipes(Materials.getMaterialByName(materialName));
	}

	protected void registerVanillaRecipes(@Nonnull final MMDMaterial material) {
//		final Item forgeHammer = new ItemStack(Item.getByNameOrId("ic2:forge_hammer"), 1).getItem();
//		final Item forgeHammer = IC2Items.getItem("forge_hammer").getItem(); // crashes

		final ItemStack ingot = material.getItemStack(Names.INGOT, 1);

//		if (forgeHammer != null) {
//			GameRegistry.addRecipe(new ShapelessOreRecipe(material.getItemStack(Names.CASING, 2), material.getItemStack(Names.PLATE), forgeHammer));
//		}

		// Move these to main recipe loop?
		GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED), ingot, 0);
		GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED_PURIFIED), ingot, 0);
	}

	protected void addMaceratorRecipes(@Nonnull final String materialName) {
		addMaceratorRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addMaceratorRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputOre = Recipes.inputFactory.forOreDict(Oredicts.ORE + oreDictName);
		final IRecipeInput inputDensePlate = Recipes.inputFactory.forOreDict(Oredicts.PLATE_DENSE + oreDictName);
		Recipes.macerator.addRecipe(inputOre, null, false, material.getItemStack(Names.CRUSHED, 2));
		Recipes.macerator.addRecipe(inputDensePlate, null, false, material.getItemStack(Names.POWDER, 8));
	}

	protected void addMaceratorRecipe(@Nonnull final String oreDict, @Nonnull ItemStack output) {
		final IRecipeInput input = Recipes.inputFactory.forOreDict(oreDict);
		Recipes.macerator.addRecipe(input, null, false, output);
	}

	protected void addOreWashingPlantRecipes(@Nonnull final String materialName) {
		addOreWashingPlantRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addOreWashingPlantRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputOre = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED + oreDictName);
		Recipes.oreWashing.addRecipe(inputOre, null, false, material.getItemStack(Names.CRUSHED_PURIFIED), material.getItemStack(Names.SMALLPOWDER, 2));
	}

	protected void addThermalCentrifugeRecipes(@Nonnull final String materialName) {
		addThermalCentrifugeRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addThermalCentrifugeRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputOreCP = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED_PURIFIED + oreDictName);
		final IRecipeInput inputOreC = Recipes.inputFactory.forOreDict(Oredicts.CRUSHED + oreDictName);
		Recipes.centrifuge.addRecipe(inputOreCP, null, false, material.getItemStack(Names.POWDER), material.getItemStack(Names.SMALLPOWDER, 2));
		Recipes.centrifuge.addRecipe(inputOreC, null, false, material.getItemStack(Names.POWDER), material.getItemStack(Names.SMALLPOWDER));
	}

	protected void addMetalFormerRecipes(@Nonnull final String materialName) {
		addMetalFormerRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addMetalFormerRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputIngot = Recipes.inputFactory.forOreDict(Oredicts.INGOT + oreDictName);
		final IRecipeInput inputPlate = Recipes.inputFactory.forOreDict(Oredicts.PLATE + oreDictName);
		final ItemStack outputPlate = material.getBlockItemStack(Names.PLATE);
		final ItemStack outputCasing = material.getItemStack(Names.CASING);
		Recipes.metalformerRolling.addRecipe(inputIngot, null, false, outputPlate);
		Recipes.metalformerRolling.addRecipe(inputPlate, null, false, outputCasing);
	}

	protected void addCompressorRecipes(@Nonnull final String materialName) {
		addCompressorRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addCompressorRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputIngot = Recipes.inputFactory.forOreDict(Oredicts.PLATE + oreDictName, 9);
		final ItemStack outputDensePlate = material.getItemStack(Names.DENSE_PLATE);
		Recipes.compressor.addRecipe(inputIngot, null, false, outputDensePlate);
	}
}
