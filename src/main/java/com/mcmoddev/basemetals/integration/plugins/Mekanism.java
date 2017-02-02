package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;

import cyano.basemetals.init.Materials;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.common.Loader;

@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.Mekanism implements IIntegration {

//	public static final String PLUGIN_MODID = "Mekanism";

//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_MEKANISM) {
			return;
		}

		MetalMaterial material;

		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;
			addOreMultiplicationRecipes(material);
/*			addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
			addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

			addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
			addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

			addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
			addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

			addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
			addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));*/
		}

		// Crystalizer
		// Clean Slurry to Crystal
		// Slurry to Clean Slurry

		//C Dissolution Chamber = 1 ore to Slurry

		// 9 completed, 8 remain
		// addOsmiumCompressorRecipe
		// addCombinerRecipe
		// addChemicalOxidizerRecipe
		// addChemicalInfuserRecipe
		// addPrecisionSawmillRecipe
		// addElectrolyticSeparatorRecipe
		// addThermalEvaporationRecipe
		// addSolarNeutronRecipe

		initDone = true;
	}
/*
	protected static void addMetallurgicInfuserRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		FMLInterModComms.sendMessage(PLUGIN_MODID, "MetallurgicInfuserRecipe", recipeTag);
	}

	// 3x, Ore + Oxygen = Clump *3, Shard + Oxygen = Clump
	// Clump to dirty IC2: Macerator)
	protected static void addCrusherRecipe(ItemStack inputItem, ItemStack outputItem) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "CrusherRecipe", recipeTag);
	}

	protected static void addEnrichmentChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "EnrichmentChamberRecipe", recipeTag);
	}

	protected static void addPurificationChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		// Oxygen
		String inputGas = "oxygen";
		int inputGasQty = 1000;

		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("gasType", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PurificationChamberRecipe", recipeTag);
	}

	protected static void addChemicalInjectionChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		// Hydrogen Chloride
		String inputGas = "hydrogenChloride";
		int inputGasQty = 1000;

		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("gasType", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInjectionChamberRecipe", recipeTag);
	}

	// 5x, Slurry to Crystal
	protected static void addChemicalCrystallizerRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		//		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		//		recipeTag.setTag("gasType", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		//		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalCrystallizerRecipe", recipeTag);

	}

	// 5x, Slurry to Clean Slurry
	protected static void addChemicalWasherRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		//		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		//		recipeTag.setTag("gasType", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		//		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalWasherChamberRecipe", recipeTag);

	}

	// 5x, Ore to Slurry
	protected static void addChemicalDissolutionChamberRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		//		recipeTag.setTag("input", inputItem.writeToNBT(new NBTTagCompound()));
		//		recipeTag.setTag("gasType", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		//		recipeTag.setTag("output", outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalDissolutionChamberRecipe", recipeTag);
	}

	protected static void addPRCRecipe(ItemStack inputItem, FluidStack inputFluid, String inputGas, int inputGasQty, ItemStack outputItem, String outputGas, int outputGasQty) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag("itemInput", inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("fluidInput", inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("gasInput", new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));

		recipeTag.setTag("itemOutput", outputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag("gasOutput", new GasStack(GasRegistry.getGas(outputGas), outputGasQty).write(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PressurizedReactionChamberRecipe", recipeTag);
	}
	*/
}
