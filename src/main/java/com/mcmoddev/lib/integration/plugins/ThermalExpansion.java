package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.oredict.OreDictionary;

// Parts of this are based on code from older versions of CoFHLib
// Other parts are lifted from the "ThermalExpansionHelper" of the 1.7 versions
// of CoFHLib - they were efficient and did the integration in the simplest
// manner. Why they've been removed, I don't know.

public class ThermalExpansion implements IIntegration {

	public static final String PLUGIN_MODID = "thermalexpansion";
	public static boolean initDone = false;
	
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableThermalExpansion) {
			return;
		}

		initDone = true;
	}

	public static void addFurnaceRecipe( int energy, ItemStack input, ItemStack output ) {
		if( input == null || output == null ) {
			return;
		}
		
		NBTTagCompound msg = new NBTTagCompound();
		msg.setInteger("energy", energy);
		msg.setTag("input", new NBTTagCompound());
		msg.setTag("output", new NBTTagCompound());
		
		input.writeToNBT(msg.getCompoundTag("input"));
		output.writeToNBT(msg.getCompoundTag("output"));
		FMLInterModComms.sendMessage("thermalexpansion", "addfurnacerecipe", msg);
	}
	
	public static void addPulverizerRecipe( int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
		if( input == null || primaryOutput == null ) {
			return;
		}
		
		NBTTagCompound msg = new NBTTagCompound();
		msg.setInteger("energy", energy);
		msg.setTag("input", new NBTTagCompound());
		msg.setTag("primaryOutput", new NBTTagCompound());
		input.writeToNBT(msg.getCompoundTag("input"));
		primaryOutput.writeToNBT(msg.getCompoundTag("primaryOutput"));
		
		if( secondaryOutput != null ) {
			msg.setTag("secondaryOutput", new NBTTagCompound());
			msg.setInteger("secondaryChance", chance);
			secondaryOutput.writeToNBT(msg.getCompoundTag("secondaryOutput"));
		}
		
		FMLInterModComms.sendMessage("thermalexpansion", "addpulverizerrecipe", msg);
	}
	
	public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
		if( primaryInput == null || secondaryInput == null || primaryOutput == null ) {
			return;
		}
		
		NBTTagCompound msg = new NBTTagCompound();
		msg.setInteger("energy", energy);
		msg.setTag("primaryInput", new NBTTagCompound());
		msg.setTag("secondaryInput", new NBTTagCompound());
		msg.setTag("primaryOutput", new NBTTagCompound());
		primaryInput.writeToNBT(msg.getCompoundTag("primaryInput"));
		secondaryInput.writeToNBT(msg.getCompoundTag("secondaryInput"));
		primaryOutput.writeToNBT(msg.getCompoundTag("primaryOutput"));
		
		if( secondaryOutput != null ) {
			msg.setTag("secondaryOutput", new NBTTagCompound());
			msg.setInteger("secondaryChance", chance);
			secondaryOutput.writeToNBT(msg.getCompoundTag("secondaryOutput"));
		}

		FMLInterModComms.sendMessage("thermalexpansion", "addsmelterrecipe", msg);
	}
	
	public static void addCrucibleRecipe( int energy, ItemStack input, FluidStack output ) {
		if( input == null || output == null ) {
			return;
		}
		
		NBTTagCompound msg = new NBTTagCompound();
		msg.setInteger("energy", energy);
		msg.setTag("input", new NBTTagCompound());
		msg.setTag("output", new NBTTagCompound());
		
		input.writeToNBT(msg.getCompoundTag("input"));
		output.writeToNBT(msg.getCompoundTag("output"));
		FMLInterModComms.sendMessage("thermalexpansion", "addcruciblerecipe", msg);
	}

	public static void addSmelterPyrotheumRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
		ItemStack pyrotheum = OreDictionary.getOres("dustPyrotheum").get(0);
		addSmelterRecipe(energy, input, pyrotheum, primaryOutput, secondaryOutput, chance);
	}
	
	/* Transposer */
	public static void addTransposerFill(int energy, ItemStack input, ItemStack output, FluidStack fluid, boolean reversible) {

		if (input == null || output == null || fluid == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());
		toSend.setTag("fluid", new NBTTagCompound());

		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));
		toSend.setBoolean("reversible", reversible);
		fluid.writeToNBT(toSend.getCompoundTag("fluid"));

		FMLInterModComms.sendMessage("thermalexpansion", "addtransposerfillrecipe", toSend);
	}

	public static void addTransposerExtract(int energy, ItemStack input, ItemStack output, FluidStack fluid, int chance, boolean reversible) {

		if (input == null || output == null || fluid == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());
		toSend.setTag("fluid", new NBTTagCompound());

		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));
		toSend.setBoolean("reversible", reversible);
		toSend.setInteger("chance", chance);
		fluid.writeToNBT(toSend.getCompoundTag("fluid"));

		FMLInterModComms.sendMessage("thermalexpansion", "addtransposerextractrecipe", toSend);
	}

	public static void addCompactorPressRecipe(int energy, ItemStack input, ItemStack output) {
		if (input == null || output == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());
		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));
		FMLInterModComms.sendMessage("thermalexpansion", "addcompactorpressrecipe", toSend);
		
	}

	public static void addCompactorStorageRecipe(int energy, ItemStack input, ItemStack output) {
		if (input == null || output == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());
		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));
		FMLInterModComms.sendMessage("thermalexpansion", "addcompactorstoragerecipe", toSend);
		
	}

	public static void addInsolatorRecipe(int energy, ItemStack primaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int chance) {
		if( primaryInput == null || primaryOutput == null ) {
			return;
		}
		
		NBTTagCompound msg = new NBTTagCompound();
		msg.setInteger("energy", energy);
		msg.setTag("primaryInput", new NBTTagCompound());
		msg.setTag("primaryOutput", new NBTTagCompound());
		primaryInput.writeToNBT(msg.getCompoundTag("primaryInput"));
		primaryOutput.writeToNBT(msg.getCompoundTag("primaryOutput"));
		
		if( secondaryOutput != null ) {
			msg.setTag("secondaryOutput", new NBTTagCompound());
			msg.setInteger("secondaryChance", chance);
			secondaryOutput.writeToNBT(msg.getCompoundTag("secondaryOutput"));
		}

		FMLInterModComms.sendMessage("thermalexpansion", "addinsolatorrecipe", msg);
	}

	/* Magmatic */
	public static void addMagmaticFuel(String fluidName, int energy) {

		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setString("fluidName", fluidName);
		toSend.setInteger("energy", energy);

		FMLInterModComms.sendMessage("thermalexpansion", "addmagmaticfuel", toSend);
	}

	/* Compression */
	public static void addCompressionFuel(String fluidName, int energy) {

		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setString("fluidName", fluidName);
		toSend.setInteger("energy", energy);

		FMLInterModComms.sendMessage("thermalexpansion", "addcompressionfuel", toSend);
	}

	/* Reactant */
	public static void addReactantFuel(String fluidName, int energy) {

		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setString("fluidName", fluidName);
		toSend.setInteger("energy", energy);

		FMLInterModComms.sendMessage("thermalexpansion", "addreactantfuel", toSend);
	}

}
