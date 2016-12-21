package cyano.basemetals.integration.plugins;

import cyano.basemetals.init.Blocks;
import cyano.basemetals.init.Items;
import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import cyano.basemetals.util.Config.Options;
import mekanism.api.gas.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

@BaseMetalsPlugin(Mekanism.PLUGIN_MODID)
public class Mekanism implements IIntegration {

	public static final String PLUGIN_MODID = "Mekanism";

	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_MEKANISM) {
			return;
		}

		for (Gas gas : GasRegistry.getRegisteredGasses()) {
			FMLLog.severe("BASEMETALS: PEEKING FOR GASSES: %s", gas.getName());
		}

		// Combiner 8 dust to 1 ore
		if (Options.ENABLE_ADAMANTINE) {
			addCrusherRecipe(new ItemStack(Items.adamantine_clump), new ItemStack(Items.adamantine_powder_dirty)); // Verified
			addCrusherRecipe(new ItemStack(Items.adamantine_ingot), new ItemStack(Items.adamantine_powder)); // Verified

			addEnrichmentChamberRecipe(new ItemStack(Blocks.adamantine_ore), new ItemStack(Items.adamantine_powder, 2)); // Verified
			addEnrichmentChamberRecipe(new ItemStack(Items.adamantine_powder_dirty), new ItemStack(Items.adamantine_powder)); // Verified

			addPurificationChamberRecipe(new ItemStack(Blocks.adamantine_ore), new ItemStack(Items.adamantine_clump, 3)); // Verified
			addPurificationChamberRecipe(new ItemStack(Items.adamantine_shard), new ItemStack(Items.adamantine_clump)); // Verified

			addChemicalInjectionChamberRecipe(new ItemStack(Blocks.adamantine_ore), new ItemStack(Items.adamantine_shard, 4)); // Verified
			addChemicalInjectionChamberRecipe(new ItemStack(Items.adamantine_crystal), new ItemStack(Items.adamantine_shard)); // Verified
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
}
