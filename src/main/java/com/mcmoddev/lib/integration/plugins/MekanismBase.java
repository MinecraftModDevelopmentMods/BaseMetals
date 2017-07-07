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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class MekanismBase implements IIntegration {

	public static final String PLUGIN_MODID = "mekanism";

	private static boolean initDone = false;

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
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		/*
		for (Gas gas : GasRegistry.getRegisteredGasses()) {
			BaseMetals.logger.info("BASEMETALS: PEEKING FOR GASSES: %s", gas.getName());
		}
		*/

		initDone = true;
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
		final Item clump = material.getItem(Names.CLUMP);
		final Item powderDirty = material.getItem(Names.POWDER_DIRTY);
		final Item ingot = material.getItem(Names.INGOT);
		final Item powder = material.getItem(Names.POWDER);
		final Block ore = material.getBlock(Names.ORE);
		final Item shard = material.getItem(Names.SHARD);
		// TODO: check specifically for mek crystal type
		final Item crystal = material.getItem(Names.CRYSTAL);

		if ((clump != null) && (powderDirty != null)) {
			addCrusherRecipe(new ItemStack(clump), new ItemStack(powderDirty));
		}
		if ((ingot != null) && (powder != null)) {
			addCrusherRecipe(new ItemStack(ingot), new ItemStack(powder));
		}

		if (powder != null) {
			if (ore != null) {
				addEnrichmentChamberRecipe(new ItemStack(ore), new ItemStack(powder, 2));
			}
			if (powderDirty != null) {
				addEnrichmentChamberRecipe(new ItemStack(powderDirty), new ItemStack(powder));
			}
		}

		if (clump != null) {
			if (ore != null) {
				addPurificationChamberRecipe(new ItemStack(ore), new ItemStack(clump, 3));
			}
			if (shard != null) {
				addPurificationChamberRecipe(new ItemStack(shard), new ItemStack(clump));
			}
		}

		if (shard != null) {
			if (ore != null) {
				addChemicalInjectionChamberRecipe(new ItemStack(ore), new ItemStack(shard, 4));
			}
			if (crystal != null) {
				addChemicalInjectionChamberRecipe(new ItemStack(crystal), new ItemStack(shard));
			}
		}

		/*
		addChemicalCrystallizerRecipe("clean" + material.getCapitalizedName(), 1000, new ItemStack(material.crystal));

		addChemicalWasherRecipe(material.getName(), 1000, "clean" + material.getCapitalizedName());

		addChemicalDissolutionChamberRecipe(new ItemStack(material.ore), material.getName());
		*/
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
