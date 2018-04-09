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

	public static final String PLUGIN_MODID = "mekanism";

	// ItemStackInput, FluidInput, GasInput
	private static final String INPUT = "input";

	// AdvancedMachineInput
	// Also input
	private static final String GAS_TYPE = "gasType";

	// ChemicalPairInput
	private static final String LEFT_INPUT = "leftInput";
	private static final String RIGHT_INPUT = "rightInput";

	// InfusionInput (Metallurgic Infuser)
	// Also input
	private static final String INFUSE_TYPE = "infuseType";
	private static final String INFUSE_AMOUNT = "infuseAmount";

	// PressurizedInput (Pressurized Reaction Chamber)
	private static final String ITEM_INPUT = "itemInput";
	private static final String FLUID_INPUT = "fluidInput";
	private static final String GAS_INPUT = "gasInput";

	// ItemStackOutput, FluidOutput, GasOutput
	private static final String OUTPUT = "output";

	// ChemicalPairOutput
	private static final String LEFT_OUTPUT = "leftOutput";
	private static final String RIGHT_OUTPUT = "rightOutput";

	// ChanceOutput
	private static final String PRIMARY_OUTPUT = "primaryOutput";
	private static final String SECONDARY_OUTPUT = "secondaryOutput";
	private static final String SECONDARY_CHANCE = "secondaryChance";

	// PressurizedOutput (Pressurized Reaction Chamber)
	private static final String ITEM_OUTPUT = "itemOutput";
	private static final String GAS_OUTPUT = "gasOutput";

	private static final String CLEAN = "clean";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	protected static void addGassesForMaterial(@Nonnull final String materialName) {
		if (!Materials.hasMaterial(materialName)
				|| Materials.getMaterialByName(materialName).isEmpty()) {
			return;
		}

		addGassesForMaterial(Materials.getMaterialByName(materialName));
	}

	protected static void addGassesForMaterial(@Nonnull final MMDMaterial material) {
		if (material.getFluid() == null) {
			return;
		}
		final Gas gas1 = new Gas(material.getName(), material.getName() + "-Icon");
		gas1.setUnlocalizedName("gas" + material.getCapitalizedName());
		GasRegistry.register(gas1);

		final Gas gas2 = new Gas(makeCleanGasName(material),
				material.getName() + "-CleanIcon");
		gas2.setUnlocalizedName("cleanGas" + material.getCapitalizedName());
		GasRegistry.register(gas2);
	}

	private static String makeCleanGasName(@Nonnull final MMDMaterial material) {
		return CLEAN + material.getName();
	}

	protected static void addOreMultiplicationRecipes(@Nonnull final String materialName) {
		addOreMultiplicationRecipes(Materials.getMaterialByName(materialName));
	}

	protected static void addOreMultiplicationRecipes(@Nonnull final MMDMaterial material) {
		final ItemStack clump = material.getItemStack(Names.CLUMP);
		final ItemStack powderDirty = material.getItemStack(Names.POWDER_DIRTY);
		final ItemStack ingot = material.getItemStack(Names.INGOT);
		final ItemStack powder = material.getItemStack(Names.POWDER);
		final ItemStack ore = material.getBlockItemStack(Names.ORE);
		final ItemStack shard = material.getItemStack(Names.SHARD);
		// TODO: check specifically for mek crystal type
		final ItemStack crystal = material.getItemStack(Names.CRYSTAL);
		final String cleanGas = makeCleanGasName(material);

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

		if (material.hasItem(Names.CRYSTAL) && material.hasBlock(Names.ORE)) {
			// Crystallizer is 200mB for 1 crystal
			addChemicalCrystallizerRecipe(CLEAN + material.getName(), 200, crystal);
			addChemicalWasherRecipe(material.getName(), 1000, cleanGas);
			addChemicalDissolutionChamberRecipe(ore, material.getName());
		}
	}

	protected static void addMetallurgicInfuserRecipe(@Nonnull final String infuse,
			@Nonnull final int amount, @Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setString(INFUSE_TYPE, infuse);
		recipeTag.setInteger(INFUSE_AMOUNT, amount);
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "MetallurgicInfuserRecipe", recipeTag);
	}

	protected static void addCrusherRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "CrusherRecipe", recipeTag);
	}

	protected static void addEnrichmentChamberRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "EnrichmentChamberRecipe", recipeTag);
	}

	protected static void addPurificationChamberRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}

		final GasStack inputGasStack = new GasStack(GasRegistry.getGas("oxygen"), 1000);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "PurificationChamberRecipe", recipeTag);
	}

	protected static void addChemicalInjectionChamberRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if ((inputItem.isEmpty()) || (outputItem.isEmpty())) {
			return;
		}

		final GasStack inputGasStack = new GasStack(GasRegistry.getGas("hydrogenChloride"), 1000);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInjectionChamberRecipe", recipeTag);
	}

	// 5x, Slurry to Crystal
	protected static void addChemicalCrystallizerRecipe(@Nonnull final String inputGas,
			@Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem) {
		if ((outputItem.isEmpty()) || (!GasRegistry.containsGas(inputGas))) {
			return;
		}

		final GasStack inputGasStack = new GasStack(GasRegistry.getGas(inputGas), inputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalCrystallizerRecipe", recipeTag);
	}

	// 5x, Slurry to "clean" Slurry
	protected static void addChemicalWasherRecipe(@Nonnull final String inputGas,
			@Nonnull final int inputGasQty, @Nonnull final String outputGas) {
		final FluidStack inputFluid = FluidRegistry.getFluidStack("water", 1000);
		final GasStack inputGasStack = new GasStack(GasRegistry.getGas(inputGas), inputGasQty);
		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), inputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalWasherChamberRecipe", recipeTag);
	}

	// 5x, Ore to Slurry
	protected static void addChemicalDissolutionChamberRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final String outputGas, @Nonnull final int outputQty) {
		if (inputItem.isEmpty()) {
			return;
		}

		final GasStack inputGasStack = new GasStack(GasRegistry.getGas("sulphuricAcid"), 1000);
		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), outputQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalDissolutionChamberRecipe", recipeTag);
	}

	protected static void addChemicalDissolutionChamberRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final String outputGas) {
		if ((inputItem.isEmpty()) || (!GasRegistry.containsGas(outputGas))) {
			return;
		}

		addChemicalDissolutionChamberRecipe(inputItem, outputGas, 1000);
	}

	protected static void addPRCRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final FluidStack inputFluid, @Nonnull final String inputGas,
			@Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem,
			@Nonnull final String outputGas, @Nonnull final int outputGasQty, final int extraEnergy,
			final int ticks) {
		if (inputItem.isEmpty()) {
			return;
		}

		final GasStack inputGasStack = new GasStack(GasRegistry.getGas(inputGas), inputGasQty);
		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), outputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		recipeTag.setTag(ITEM_INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(FLUID_INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_INPUT, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(ITEM_OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "PressurizedReactionChamberRecipe", recipeTag);
	}

	protected static void addPRCRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final FluidStack inputFluid, @Nonnull final String inputGas,
			@Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem,
			@Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		addPRCRecipe(inputItem, inputFluid, inputGas, inputGasQty, outputItem, outputGas,
				outputGasQty, 0, 60);
	}

	protected static void addOsmiumCompressorRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}

		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "OsmiumCompressorRecipe", recipeTag);
	}

	protected static void addCombinerRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}

		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "CombinerRecipe", recipeTag);
	}

	protected static void addChemicalOxidizerRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		if (inputItem.isEmpty()) {
			return;
		}

		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), outputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalOxidizerRecipe", recipeTag);
	}

	protected static void addChemicalInfuserRecipe(@Nonnull final String leftInputGas,
			@Nonnull final int leftInputGasQty, @Nonnull final String rightInputGas,
			@Nonnull final int rightInputGasQty, @Nonnull final String outputGas,
			@Nonnull final int outputGasQty) {
		final GasStack leftInputGasStack = new GasStack(GasRegistry.getGas(leftInputGas),
				leftInputGasQty);
		final GasStack rightInputGasStack = new GasStack(GasRegistry.getGas(rightInputGas),
				rightInputGasQty);
		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), outputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO
		recipeTag.setTag(LEFT_INPUT, leftInputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(RIGHT_INPUT, rightInputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInfuserRecipe", recipeTag);
	}

	protected static void addPrecisionSawmillRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack outputItem) {
		if (inputItem.isEmpty() || outputItem.isEmpty()) {
			return;
		}

		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "PrecisionSawmillRecipe", recipeTag);
	}

	protected static void addPrecisionSawmillRecipe(@Nonnull final ItemStack inputItem,
			@Nonnull final ItemStack primaryOutput, @Nonnull final ItemStack secondaryOutput,
			@Nonnull final double chance) {
		if (inputItem.isEmpty() || primaryOutput.isEmpty()) {
			return;
		}

		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(PRIMARY_OUTPUT, primaryOutput.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(SECONDARY_OUTPUT, secondaryOutput.writeToNBT(new NBTTagCompound()));
		recipeTag.setDouble(SECONDARY_CHANCE, chance);

		FMLInterModComms.sendMessage(PLUGIN_MODID, "PrecisionSawmillRecipe", recipeTag);
	}

	protected static void addElectrolyticSeparatorRecipe(@Nonnull final String inputFluidName,
			@Nonnull final int inputFluidQty, @Nonnull final String leftOutputGas,
			@Nonnull final int leftOutputGasQty, @Nonnull final String rightOutputGas,
			@Nonnull final int rightOutputGasQty, @Nonnull final int energy) {
		final FluidStack inputFluid = new FluidStack(FluidRegistry.getFluid(inputFluidName),
				inputFluidQty);
		final GasStack leftOutput = new GasStack(GasRegistry.getGas(leftOutputGas),
				leftOutputGasQty);
		final GasStack rightOutput = new GasStack(GasRegistry.getGas(rightOutputGas),
				rightOutputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - check
		recipeTag.setTag(INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setDouble("energyUsage", energy);
		recipeTag.setTag(LEFT_OUTPUT, leftOutput.write(new NBTTagCompound()));
		recipeTag.setTag(RIGHT_OUTPUT, rightOutput.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ElectrolyticSeparatorRecipe", recipeTag);
	}

	protected static void addThermalEvaporationRecipe(@Nonnull final String inputFluidName,
			@Nonnull final int inputFluidQty, @Nonnull final String outputFluidName,
			@Nonnull final int outputFluidQty) {
		final FluidStack inputFluid = new FluidStack(FluidRegistry.getFluid(inputFluidName),
				inputFluidQty);
		final FluidStack outputFluid = new FluidStack(FluidRegistry.getFluid(outputFluidName),
				outputFluidQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputFluid.writeToNBT(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "ThermalEvaporationRecipe", recipeTag);
	}

	protected static void addSolarNeutronRecipe(@Nonnull final String inputGas,
			@Nonnull final int inputGasQty, @Nonnull final String outputGas,
			@Nonnull final int outputGasQty) {
		final GasStack inputGasStack = new GasStack(GasRegistry.getGas(inputGas), inputGasQty);
		final GasStack outputGasStack = new GasStack(GasRegistry.getGas(outputGas), outputGasQty);
		final NBTTagCompound recipeTag = new NBTTagCompound();

		// TODO - Check
		recipeTag.setTag(GAS_INPUT, inputGasStack.write(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, outputGasStack.write(new NBTTagCompound()));

		FMLInterModComms.sendMessage(PLUGIN_MODID, "SolarNeutronRecipe", recipeTag);
	}

	protected static boolean gasExists(@Nonnull final String gasName) {
		return GasRegistry.containsGas(gasName);
	}
}
