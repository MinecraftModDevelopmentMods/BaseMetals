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
import mekanism.api.infuse.InfuseType;
import mekanism.api.infuse.InfuseRegistry;
import mekanism.common.recipe.RecipeHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

// mekanism.common.recipe.RecipeHandler
//  -- addEnrichmentChamberRecipe
//  -- addOsmiumCompressorRecipe
//  -- etc... this is where all the machines get their processing recipes stored
// For generator fuel integration:
// mekanism.common.FuelHandler - only handles gasses
// 
public class MekanismBase implements IIntegration {

	public static final String PLUGIN_MODID = "mekanism";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
		
	}

	protected static void addGassesForMaterial(@Nonnull final String materialName) {
		addGassesForMaterial(Materials.getMaterialByName(materialName));
	}

	protected static void addGassesForMaterial(@Nonnull final MMDMaterial material) {
		final Gas gas1 = new Gas(material.getName(),material.getFluid().getStill().toString());
		gas1.setUnlocalizedName("gas" + material.getCapitalizedName());
		GasRegistry.register(gas1);

		final Gas gas2 = new Gas("clean"+material.getName(),material.getFluid().getStill().toString());
		gas2.setUnlocalizedName("cleanGas"+material.getCapitalizedName());
		GasRegistry.register(gas2);
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

		if ((clump != ItemStack.EMPTY) && (powderDirty != ItemStack.EMPTY)) {
			addCrusherRecipe(clump, powderDirty);
		}
		if ((ingot != ItemStack.EMPTY) && (powder != ItemStack.EMPTY)) {
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
			addChemicalCrystallizerRecipe("clean" + material.getName(), 200, crystal);
			addChemicalWasherRecipe(material.getName(), 1000, "clean" + material.getName());
			addChemicalDissolutionChamberRecipe(ore, material.getName());
		}
	}

	protected static void addMetallurgicInfuserRecipe(@Nonnull final String infuse, @Nonnull int amount, @Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem ) {
		final InfuseType infuseType = InfuseRegistry.get(infuse);
		RecipeHandler.addMetallurgicInfuserRecipe(infuseType, amount, inputItem, outputItem);
	}

	protected static void addCrusherRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addCrusherRecipe(inputItem, outputItem);
	}

	protected static void addEnrichmentChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addEnrichmentChamberRecipe(inputItem, outputItem);
	}

	protected static void addPurificationChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addPurificationChamberRecipe(inputItem, outputItem);
	}

	protected static void addChemicalInjectionChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		Gas inputGas = GasRegistry.getGas("hydrogenChloride");
		RecipeHandler.addChemicalInjectionChamberRecipe(inputItem, inputGas, outputItem);
	}

	// 5x, Slurry to Crystal
	protected static void addChemicalCrystallizerRecipe(@Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem) {
		final GasStack inputGasStack = new GasStack( GasRegistry.getGas(inputGas), inputGasQty );
		RecipeHandler.addChemicalCrystallizerRecipe(inputGasStack, outputItem);
	}

	// 5x, Slurry to "clean" Slurry
	protected static void addChemicalWasherRecipe(@Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final String outputGas) {
		final GasStack inputGasStack = new GasStack( GasRegistry.getGas(inputGas), inputGasQty );
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), inputGasQty );
		RecipeHandler.addChemicalWasherRecipe(inputGasStack, outputGasStack);
	}

	// 5x, Ore to Slurry
	protected static void addChemicalDissolutionChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final String outputGas, @Nonnull int outputQty) {
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), outputQty );
		RecipeHandler.addChemicalDissolutionChamberRecipe(inputItem, outputGasStack);
	}

	protected static void addChemicalDissolutionChamberRecipe(@Nonnull final ItemStack inputItem, @Nonnull final String outputGas) {
		addChemicalDissolutionChamberRecipe(inputItem, outputGas, 1000);
	}

	protected static void addPRCRecipe(@Nonnull final ItemStack inputItem, @Nonnull final FluidStack inputFluid, @Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem,  @Nonnull final String outputGas, @Nonnull final int outputGasQty, int extraEnergy, int ticks ) {
		final GasStack inputGasStack = new GasStack( GasRegistry.getGas(inputGas), inputGasQty );
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), outputGasQty );
		RecipeHandler.addPRCRecipe(inputItem, inputFluid, inputGasStack, outputItem, outputGasStack, extraEnergy, ticks);
	}

	protected static void addPRCRecipe(@Nonnull final ItemStack inputItem, @Nonnull final FluidStack inputFluid, @Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final ItemStack outputItem, @Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		addPRCRecipe(inputItem, inputFluid, inputGas, inputGasQty, outputItem, outputGas, outputGasQty, 0, 60 );
	}

	protected static void addOsmiumCompressorRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addOsmiumCompressorRecipe(inputItem, outputItem);
	}

	protected static void addCombinerRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addCombinerRecipe(inputItem, outputItem);
	}

	protected static void addChemicalOxidizerRecipe(@Nonnull final ItemStack inputItem, @Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), outputGasQty);
		RecipeHandler.addChemicalOxidizerRecipe(inputItem, outputGasStack);
	}

	protected static void addChemicalInfuserRecipe(@Nonnull final String leftInputGas, @Nonnull final int leftInputGasQty, @Nonnull final String rightInputGas, @Nonnull final int rightInputGasQty, @Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		final GasStack leftInputGasStack = new GasStack( GasRegistry.getGas(leftInputGas), leftInputGasQty);
		final GasStack rightInputGasStack = new GasStack( GasRegistry.getGas(rightInputGas), rightInputGasQty);
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), outputGasQty);
		RecipeHandler.addChemicalInfuserRecipe(leftInputGasStack, rightInputGasStack, outputGasStack);
	}

	protected static void addPrecisionSawmillRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack outputItem) {
		RecipeHandler.addPrecisionSawmillRecipe(inputItem, outputItem);
	}

	protected static void addPrecisionSawmillRecipe(@Nonnull final ItemStack inputItem, @Nonnull final ItemStack mainOutput, @Nonnull final ItemStack secondaryOutput, @Nonnull final double chance) {
		RecipeHandler.addPrecisionSawmillRecipe(inputItem, mainOutput, secondaryOutput, chance);
	}

	protected static void addElectrolyticSeparatorRecipe(@Nonnull final String inputFluidName, @Nonnull final int inputFluidQty, @Nonnull final String leftOutputGas, @Nonnull final int leftOutputGasQty, @Nonnull final String rightOutputGas, @Nonnull final int rightOutputGasQty, @Nonnull final int energy) {
		final FluidStack inputFluid = new FluidStack( FluidRegistry.getFluid(inputFluidName), inputFluidQty );
		final GasStack leftOutput = new GasStack( GasRegistry.getGas(leftOutputGas), leftOutputGasQty );
		final GasStack rightOutput = new GasStack( GasRegistry.getGas(rightOutputGas), rightOutputGasQty );
		RecipeHandler.addElectrolyticSeparatorRecipe(inputFluid, energy, leftOutput, rightOutput);
	}

	protected static void addThermalEvaporationRecipe(@Nonnull final String inputFluidName, @Nonnull final int inputFluidQty, @Nonnull final String outputFluidName, @Nonnull final int outputFluidQty) {
		final FluidStack inputFluid = new FluidStack( FluidRegistry.getFluid(inputFluidName), inputFluidQty );
		FluidStack outputFluid = new FluidStack( FluidRegistry.getFluid(outputFluidName), outputFluidQty );
		RecipeHandler.addThermalEvaporationRecipe(inputFluid, outputFluid);
	}

	protected static void addSolarNeutronRecipe(@Nonnull final String inputGas, @Nonnull final int inputGasQty, @Nonnull final String outputGas, @Nonnull final int outputGasQty) {
		final GasStack inputGasStack = new GasStack( GasRegistry.getGas(inputGas), inputGasQty);
		final GasStack outputGasStack = new GasStack( GasRegistry.getGas(outputGas), outputGasQty);
		RecipeHandler.addSolarNeutronRecipe(inputGasStack, outputGasStack);
	}

	protected static boolean gasExists(@Nonnull final String gasName) {
		return GasRegistry.containsGas(gasName);
	}
}
