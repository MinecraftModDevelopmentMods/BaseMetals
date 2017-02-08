package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

//@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism implements IIntegration {

	public static final String PLUGIN_MODID = "Mekanism";

	private static boolean initDone = false;

	// ItemStackInput, FluidInput, GasInput
	private static final String INPUT = "input";

	// AdvancedMachineInput
	// Also input
	private static final String GAS_TYPE = "gasType";
	
	// ChemicalPairInput
	//private static final String LEFT_INPUT = "leftInput";
	//private static final String RIGHT_INPUT = "rightInput";

	// InfusionInput (Metallurgic Infuser)
	// Also input
	//private static final String INFUSE_TYPE = "infuseType";
	//private static final String INFUSE_AMOUNT = "infuseAmount";

	// PressurizedInput (Pressurized Reaction Chamber)
	private static final String ITEM_INPUT = "itemInput";
	private static final String FLUID_INPUT = "fluidInput";
	private static final String GAS_INPUT = "gasInput";

	// ItemStackOutput, FluidOutput, GasOutput
	private static final String OUTPUT = "output";

	// ChemicalPairOutput
	//private static final String LEFT_OUTPUT = "leftOutput";
	//private static final String RIGHT_OUTPUT = "rightOutput";

	// ChanceOutput
	//private static final String PRIMARY_OUTPUT = "primaryOutput";
	//private static final String SECONDARY_OUTPUT = "secondaryOutput";
	//private static final String SECONDARY_CHANCE = "secondaryChance";
	
	// PressurizedOutput (Pressurized Reaction Chamber)
	private static final String ITEM_OUTPUT = "itemOutput";
	private static final String GAS_OUTPUT = "gasOutput";

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableMekanism) {
			return;
		}
// *
		for (Gas gas : GasRegistry.getRegisteredGasses()) {
			BaseMetals.logger.info("BASEMETALS: PEEKING FOR GASSES: %s", gas.getName());
		}
// * /

		initDone = true;
	}

	protected static void addGassesForMaterial(MetalMaterial material) {
		final Gas gas1 = new Gas(material.getName(), material.getName() + "-Icon");
		gas1.setUnlocalizedName("gas" + material.getName());
		GasRegistry.register(gas1);

		final Gas gas2 = new Gas("clean" + material.getCapitalizedName(), material.getName() + "-CleanIcon");
		gas2.setUnlocalizedName("clean" + material.getName());
		GasRegistry.register(gas2);
	}

	protected static void addOreMultiplicationRecipes(MetalMaterial material) {
		// Combiner 8 dust to 1 ore
		// Clump to dirty IC2: Macerator)

		addCrusherRecipe(new ItemStack(material.clump), new ItemStack(material.powder_dirty));
		addCrusherRecipe(new ItemStack(material.ingot), new ItemStack(material.powder));

		addEnrichmentChamberRecipe(new ItemStack(material.ore), new ItemStack(material.powder, 2));
		addEnrichmentChamberRecipe(new ItemStack(material.powder_dirty), new ItemStack(material.powder));

		addPurificationChamberRecipe(new ItemStack(material.ore), new ItemStack(material.clump, 3));
		addPurificationChamberRecipe(new ItemStack(material.shard), new ItemStack(material.clump));

		addChemicalInjectionChamberRecipe(new ItemStack(material.ore), new ItemStack(material.shard, 4));
		addChemicalInjectionChamberRecipe(new ItemStack(material.crystal), new ItemStack(material.shard));

		/*
		addChemicalCrystallizerRecipe("clean" + material.getCapitalizedName(), 1000, new ItemStack(material.crystal));

		addChemicalWasherRecipe(material.getName(), 1000, "clean" + material.getCapitalizedName());

		addChemicalDissolutionChamberRecipe(new ItemStack(material.ore), material.getName());
		*/
	}

	protected static void addMetallurgicInfuserRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "MetallurgicInfuserRecipe", recipeTag);
	}

	protected static void addCrusherRecipe(ItemStack inputItem, ItemStack outputItem) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "CrusherRecipe", recipeTag);
	}

	protected static void addEnrichmentChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "EnrichmentChamberRecipe", recipeTag);
	}

	protected static void addPurificationChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		// Oxygen
		String inputGas = "oxygen";
		int inputGasQty = 1000;

		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PurificationChamberRecipe", recipeTag);
	}

	protected static void addChemicalInjectionChamberRecipe(ItemStack inputItem, ItemStack outputItem) {
		// Hydrogen Chloride
		String inputGas = "hydrogenChloride";
		int inputGasQty = 1000;

		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
		recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInjectionChamberRecipe", recipeTag);
	}

	// 5x, Slurry to Crystal
	protected static void addChemicalCrystallizerRecipe(String inputGas, int inputGasQty, ItemStack outputItem) {
		NBTTagCompound recipeTag = new NBTTagCompound();
				recipeTag.setTag(INPUT, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
				recipeTag.setTag(OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalCrystallizerRecipe", recipeTag);
	}

	// 5x, Slurry to Clean Slurry
	protected static void addChemicalWasherRecipe(String inputGas, int inputGasQty, String outputGas) {
		// Water
		FluidStack inputFluid = FluidRegistry.getFluidStack("water", 1000);

		NBTTagCompound recipeTag = new NBTTagCompound();
				recipeTag.setTag(INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
				recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
				recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), 1000).write(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalWasherChamberRecipe", recipeTag);
	}

	// 5x, Ore to Slurry
	protected static void addChemicalDissolutionChamberRecipe(ItemStack inputItem, String outputGas) {
		// Sulphuric Acid
		String inputGas = "sulphuricAcid";
		int inputGasQty = 1000;

		NBTTagCompound recipeTag = new NBTTagCompound();
				recipeTag.setTag(INPUT, inputItem.writeToNBT(new NBTTagCompound()));
				recipeTag.setTag(GAS_TYPE, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));
				recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), 1000).write(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalDissolutionChamberRecipe", recipeTag);
	}

	protected static void addPRCRecipe(ItemStack inputItem, FluidStack inputFluid, String inputGas, int inputGasQty, ItemStack outputItem, String outputGas, int outputGasQty) {
		NBTTagCompound recipeTag = new NBTTagCompound();
		recipeTag.setTag(ITEM_INPUT, inputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(FLUID_INPUT, inputFluid.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_INPUT, new GasStack(GasRegistry.getGas(inputGas), inputGasQty).write(new NBTTagCompound()));

		recipeTag.setTag(ITEM_OUTPUT, outputItem.writeToNBT(new NBTTagCompound()));
		recipeTag.setTag(GAS_OUTPUT, new GasStack(GasRegistry.getGas(outputGas), outputGasQty).write(new NBTTagCompound()));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PressurizedReactionChamberRecipe", recipeTag);
	}

	protected static void addOsmiumCompressorRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "OsmiumCompressorRecipe", recipeTag);
	}

	protected static void addCombinerRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "CombinerRecipe", recipeTag);
	}

	protected static void addChemicalOxidizerRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalOxidizerRecipe", recipeTag);
	}

	protected static void addChemicalInfuserRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ChemicalInfuserRecipe", recipeTag);
	}

	protected static void addPrecisionSawmillRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "PrecisionSawmillRecipe", recipeTag);
	}

	protected static void addElectrolyticSeparatorRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ElectrolyticSeparatorRecipe", recipeTag);
	}

	protected static void addThermalEvaporationRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "ThermalEvaporationRecipe", recipeTag);
	}

	protected static void addSolarNeutronRecipe() {
		NBTTagCompound recipeTag = new NBTTagCompound();
		// TODO
		FMLInterModComms.sendMessage(PLUGIN_MODID, "SolarNeutronRecipe", recipeTag);
	}
}
