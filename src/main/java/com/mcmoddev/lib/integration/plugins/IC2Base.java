package com.mcmoddev.lib.integration.plugins;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		this.addForgeHammerRecipe(Materials.getMaterialByName(materialName));
	}

	private void addForgeHammerRecipe(@Nonnull final MMDMaterial material) {
		final ItemStack plate = material.getItemStack("ItemBlock_" + material.getName() + "_plate",
				1);
		final ItemStack casing = material.getItemStack(Names.CASING, 2);

		if (plate.isEmpty()) {
			return;
		}

		final List<IRecipeInput> inputsPlate = Arrays.asList(
				Recipes.inputFactory.forOreDict("craftingToolForgeHammer"),
				Recipes.inputFactory.forOreDict(Oredicts.INGOT + material.getCapitalizedName()));
		final List<IRecipeInput> inputsCasing = Arrays.asList(
				Recipes.inputFactory.forOreDict("craftingToolForgeHammer"),
				Recipes.inputFactory.forOreDict(Oredicts.PLATE + material.getCapitalizedName()));

		Recipes.advRecipes.addShapelessRecipe(casing, inputsCasing.toArray());
		Recipes.advRecipes.addShapelessRecipe(plate, inputsPlate.toArray());
	}

	protected void registerVanillaRecipes(@Nonnull final String materialName) {
		this.registerVanillaRecipes(Materials.getMaterialByName(materialName));
	}

	protected void registerVanillaRecipes(@Nonnull final MMDMaterial material) {
		final ItemStack ingot = material.getItemStack(Names.INGOT, 1);
		GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED), ingot, 0);
		GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED_PURIFIED), ingot, 0);
	}

	protected void addMaceratorRecipes(@Nonnull final String materialName) {
		this.addMaceratorRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addMaceratorRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputOre = Recipes.inputFactory.forOreDict(Oredicts.ORE + oreDictName);
		final IRecipeInput inputDensePlate = Recipes.inputFactory
				.forOreDict(Oredicts.PLATE_DENSE + oreDictName);
		if (!material.getItemStack(Names.CRUSHED).isEmpty()) {
			Recipes.macerator.addRecipe(inputOre, null, false,
					material.getItemStack(Names.CRUSHED, 2));
		}
		if (!material.getItemStack(Names.POWDER).isEmpty()) {
			Recipes.macerator.addRecipe(inputDensePlate, null, false,
					material.getItemStack(Names.POWDER, 8));
		}
	}

	protected void addMaceratorRecipe(@Nonnull final String oreDict,
			@Nonnull final ItemStack output) {
		final IRecipeInput input = Recipes.inputFactory.forOreDict(oreDict);
		Recipes.macerator.addRecipe(input, null, false, output);
	}

	protected void addOreWashingPlantRecipes(@Nonnull final String materialName) {
		this.addOreWashingPlantRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addOreWashingPlantRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputOre = Recipes.inputFactory
				.forOreDict(Oredicts.CRUSHED + oreDictName);
		final NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("amount", 1000);
		if (!(material.getItemStack(Names.CRUSHED_PURIFIED).isEmpty())) {
			if (material.hasItem(Names.SMALLPOWDER)
					&& !material.getItemStack(Names.SMALLPOWDER).isEmpty()) {
				Recipes.oreWashing.addRecipe(inputOre, metadata, false,
						material.getItemStack(Names.CRUSHED_PURIFIED),
						material.getItemStack(Names.SMALLPOWDER, 2));
			} else {
				Recipes.oreWashing.addRecipe(inputOre, metadata, false,
						material.getItemStack(Names.CRUSHED_PURIFIED));
			}
		}
	}

	protected void addThermalCentrifugeRecipes(@Nonnull final String materialName) {
		this.addThermalCentrifugeRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addThermalCentrifugeRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final int temp = this.getMaterialTemp(material);
		final IRecipeInput inputOreCP = Recipes.inputFactory
				.forOreDict(Oredicts.CRUSHED_PURIFIED + oreDictName);
		final IRecipeInput inputOreC = Recipes.inputFactory
				.forOreDict(Oredicts.CRUSHED + oreDictName);
		final NBTTagCompound metadata = new NBTTagCompound();
		metadata.setInteger("minHeat", temp);
		if (!((material.getItemStack(Names.POWDER).isEmpty())
				|| (material.getItemStack(Names.SMALLPOWDER).isEmpty()))) {
			Recipes.centrifuge.addRecipe(inputOreCP, metadata, false,
					material.getItemStack(Names.POWDER),
					material.getItemStack(Names.SMALLPOWDER, 2));
			Recipes.centrifuge.addRecipe(inputOreC, metadata, false,
					material.getItemStack(Names.POWDER), material.getItemStack(Names.SMALLPOWDER));
		}
	}

	private int getMaterialTemp(final MMDMaterial material) {
		if (material.getFluid() != null) {
			return material.getFluid().getTemperature() / 2;
		}
		return 1000; // same as silver
	}

	protected void addMetalFormerRecipes(@Nonnull final String materialName) {
		this.addMetalFormerRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addMetalFormerRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputIngot = Recipes.inputFactory
				.forOreDict(Oredicts.INGOT + oreDictName);
		final IRecipeInput inputPlate = Recipes.inputFactory
				.forOreDict(Oredicts.PLATE + oreDictName);
		final IRecipeInput inputCasing = Recipes.inputFactory
				.forOreDict(Oredicts.CASING + oreDictName);
		final ItemStack outputPlate = material.getBlockItemStack(Names.PLATE, 1);
		final ItemStack outputCasing = material.getItemStack(Names.CASING, 2);
		final ItemStack outputFence = material.getBlockItemStack(Names.FENCE, 1);

		if (!(outputPlate.isEmpty())) {
			Recipes.metalformerRolling.addRecipe(inputIngot, null, false, outputPlate);
		} else {
			BaseMetals.logger.fatal(
					"Material %s has no plate (getBlockItemStack(Names.PLATE, 1).isEmpty()",
					material.getCapitalizedName());
		}

		if (!(outputCasing.isEmpty())) {
			Recipes.metalformerRolling.addRecipe(inputPlate, null, false, outputCasing);
		} else {
			BaseMetals.logger.fatal(
					"Material %s has no casing (getItemStack(Names.CASING, 2).isEmpty()",
					material.getCapitalizedName());
		}

		if ((!outputFence.isEmpty())) {
			Recipes.metalformerExtruding.addRecipe(inputCasing, null, false, outputFence);
		}
	}

	protected void addCompressorRecipes(@Nonnull final String materialName) {
		this.addCompressorRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addCompressorRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();
		final IRecipeInput inputIngot = Recipes.inputFactory
				.forOreDict(Oredicts.PLATE + oreDictName, 9);
		final ItemStack outputDensePlate = material.getItemStack(Names.DENSE_PLATE);
		if (!(outputDensePlate.isEmpty()) && ((material.hasItem(Names.PLATE))
				&& !(material.getItemStack(Names.PLATE).isEmpty()))) {
			Recipes.compressor.addRecipe(inputIngot, null, false, outputDensePlate);
		}
	}
}
