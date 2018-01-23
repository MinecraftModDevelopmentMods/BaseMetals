package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class MekanismBase implements IIntegration {

	public static final String PLUGIN_MODID = "Mekanism";

	// ItemStackInput, FluidInput, GasInput
	private static final String INPUT = "input";

	// AdvancedMachineInput
	// Also input
	private static final String GAS_TYPE = "gasType";

	// ChemicalPairInput
	// private static final String LEFT_INPUT = "leftInput";
	// private static final String RIGHT_INPUT = "rightInput";

	// InfusionInput (Metallurgic Infuser)
	// Also input
	// private static final String INFUSE_TYPE = "infuseType";
	// private static final String INFUSE_AMOUNT = "infuseAmount";

	// PressurizedInput (Pressurized Reaction Chamber)
	private static final String ITEM_INPUT = "itemInput";
	private static final String FLUID_INPUT = "fluidInput";
	private static final String GAS_INPUT = "gasInput";

	// ItemStackOutput, FluidOutput, GasOutput
	private static final String OUTPUT = "output";

	// ChemicalPairOutput
	// private static final String LEFT_OUTPUT = "leftOutput";
	// private static final String RIGHT_OUTPUT = "rightOutput";

	// ChanceOutput
	// private static final String PRIMARY_OUTPUT = "primaryOutput";
	// private static final String SECONDARY_OUTPUT = "secondaryOutput";
	// private static final String SECONDARY_CHANCE = "secondaryChance";

	// PressurizedOutput (Pressurized Reaction Chamber)
	private static final String ITEM_OUTPUT = "itemOutput";
	private static final String GAS_OUTPUT = "gasOutput";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		/*
		for (Gas gas : GasRegistry.getRegisteredGasses()) {
			BaseMetals.logger.info("PEEKING FOR GASSES: %s", gas.getName());
		}
		*/
	}

	protected static void addGassesForMaterial(@Nonnull final String materialName) {
		addGassesForMaterial(Materials.getMaterialByName(materialName));
	}

	protected static void addGassesForMaterial(@Nonnull final MMDMaterial material) {
		final Gas gas1 = new Gas(material.getName(), material.getName() + "-Icon");
		gas1.setUnlocalizedName("gas" + material.getName());
		GasRegistry.register(gas1);

		final Gas gas2 = new Gas("clean" + material.getCapitalizedName(), material.getName() + "-CleanIcon");
		gas2.setUnlocalizedName("clean" + material.getName());
		GasRegistry.register(gas2);
	}

	protected static void addOreMultiplicationRecipes(@Nonnull final String materialName) {
		addOreMultiplicationRecipes(Materials.getMaterialByName(materialName));
	}

	protected static void addOreMultiplicationRecipes(@Nonnull final MMDMaterial material) {
		// Combiner 8 dust to 1 ore
		// Clump to dirty IC2: Macerator)
		final ItemStack clump = material.getItemStack(Names.CLUMP);
		final ItemStack powderDirty = material.getItemStack(Names.POWDER_DIRTY);
		final ItemStack ingot = material.getItemStack(Names.INGOT);
		final ItemStack powder = material.getItemStack(Names.POWDER);
		final ItemStack ore = material.getBlockItemStack(Names.ORE);
		final ItemStack shard = material.getItemStack(Names.SHARD);
		// TODO: check specifically for mek crystal type
		final ItemStack crystal = material.getItemStack(Names.CRYSTAL);

		if ((material.hasItem(Names.CLUMP)) && (material.hasItem(Names.POWDER_DIRTY))) {
			addCrusherRecipe(clump, powderDirty);
		}
		if ((material.hasItem(Names.INGOT)) && (material.hasItem(Names.POWDER))) {
			addCrusherRecipe(ingot, powder);
		}

		if (material.hasItem(Names.POWDER)) {
			if (material.hasBlock(Names.ORE)) {
				addEnrichmentChamberRecipe(ore, new ItemStack(powder.getItem(), 2));
			}
			if (material.hasItem(Names.POWDER_DIRTY)) {
				addEnrichmentChamberRecipe(powderDirty, powder);
			}
		}

		if (material.hasItem(Names.CLUMP)) {
			if (material.hasBlock(Names.ORE)) {
				addPurificationChamberRecipe(ore, new ItemStack(clump.getItem(), 3));
			}
			if (material.hasItem(Names.SHARD)) {
				addPurificationChamberRecipe(shard, clump);
			}
		}

		if (material.hasItem(Names.SHARD)) {
			if (material.hasBlock(Names.ORE)) {
				addChemicalInjectionChamberRecipe(ore, new ItemStack(shard.getItem(), 4));
			}
			if (material.hasItem(Names.CRYSTAL)) {
				addChemicalInjectionChamberRecipe(crystal, shard);
			}
		}

		if(material.hasItem(Names.CRYSTAL) && material.hasBlock(Names.ORE)) {
			// Crystallizer is 200mB for 1 crystal
			addChemicalCrystallizerRecipe("clean" + material.getCapitalizedName(), 200, crystal);
			addChemicalWasherRecipe(material.getName(), 1000, "clean" + material.getCapitalizedName());
			addChemicalDissolutionChamberRecipe(ore, material.getCapitalizedName());
		}
	}

	protected static void addMetallurgicInfuserRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "MetallurgicInfuserRecipe", recipeTag);
	}

	protected static void addCrusherRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "CrusherRecipe", recipeTag);
	}

	protected static void addEnrichmentChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "EnrichmentChamberRecipe", recipeTag);
	}

	protected static void addPurificationChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		// Oxygen
		final String inputGas = "oxygen";
		final int inputGasQty = 1000;
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PurificationChamberRecipe", recipeTag);
	}

	protected static void addChemicalInjectionChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		// Hydrogen Chloride
		final String inputGas = "hydrogenChloride";
		final int inputGasQty = 1000;
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInjectionChamberRecipe", recipeTag);
	}

	// 5x, Slurry to Crystal
	protected static void addChemicalCrystallizerRecipe(@Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem) {
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalCrystallizerRecipe", recipeTag);
	}

	// 5x, Slurry to Clean Slurry
	protected static void addChemicalWasherRecipe(@Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final String outputGas) {
		// Water
		final FluidStack inputFluid = FluidRegistry.getFluidStack("water", 1000);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), 1000).write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalWasherChamberRecipe", recipeTag);
	}

	// 5x, Ore to Slurry
	protected static void addChemicalDissolutionChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final String outputGas) {
		// Sulphuric Acid
		final String inputGas = "sulphuricAcid";
		final int inputGasQty = 1000;
		final NBTTagCompound recipeTag = new NBTTagCompound();
	
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), 1000).write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalDissolutionChamberRecipe", recipeTag);
	}

	protected static void addPRCRecipe(@Nonnull final ItemStack inputItem, @Nonnull final FluidStack inputFluid, @Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem, @Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(ITEM_INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(FLUID_INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_INPUT,	new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(ITEM_OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), outputGasQty).write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "PressurizedReactionChamberRecipe", recipeTag);
	}

	protected static void addOsmiumCompressorRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "OsmiumCompressorRecipe", recipeTag);
	}

	protected static void addCombinerRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "CombinerRecipe", recipeTag);
	}

	protected static void addChemicalOxidizerRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalOxidizerRecipe", recipeTag);
	}

	protected static void addChemicalInfuserRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInfuserRecipe", recipeTag);
	}

	protected static void addPrecisionSawmillRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PrecisionSawmillRecipe", recipeTag);
	}

	protected static void addElectrolyticSeparatorRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ElectrolyticSeparatorRecipe", recipeTag);
	}

	protected static void addThermalEvaporationRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ThermalEvaporationRecipe", recipeTag);
	}

	protected static void addSolarNeutronRecipe() {
		final NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "SolarNeutronRecipe", recipeTag);
	}
}
