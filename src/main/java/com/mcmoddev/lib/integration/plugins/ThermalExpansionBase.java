package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;

import cofh.api.util.ThermalExpansionHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;

// Parts of this are based on code from older versions of CoFHLib
// Other parts are lifted from the "ThermalExpansionHelper" of the 1.7 versions
// of CoFHLib - they were efficient and did the integration in the simplest
// manner. Why they've been removed, I don't know.

public class ThermalExpansionBase implements IIntegration {

	public static final String PLUGIN_MODID = "thermalexpansion";
	private static boolean initDone = false;
	
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableThermalExpansion) {
			return;
		}

		initDone = true;
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
	
	public static void addFurnace(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Ore -> Ingot default, according to TE source, is 2000
			 * dust -> Ingot default, according to same, is DEFAULT * 14 / 20 - at the 2000RF default, this is 1400
			 */
			int ENERGY_ORE = 2000;
			int ENERGY_DUST = 1400;
			ItemStack ore = new ItemStack( mat.ore, 1 );
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			ThermalExpansionHelper.addFurnaceRecipe( ENERGY_ORE, ore, ingot );
			if( Options.enableBasics && mat.powder != null ) {
				ItemStack dust = new ItemStack( mat.powder, 1 );
				ThermalExpansionHelper.addFurnaceRecipe( ENERGY_DUST, dust, ingot );
			}
		}
	}
	
	/* CRUCIBLE */
	public static void addCrucibleRecipe(int energy, ItemStack input, FluidStack output) {

		if (input == null || output == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("input", new NBTTagCompound());
		toSend.setTag("output", new NBTTagCompound());

		input.writeToNBT(toSend.getCompoundTag("input"));
		output.writeToNBT(toSend.getCompoundTag("output"));

		FMLInterModComms.sendMessage("thermalexpansion", "AddCrucibleRecipe", toSend);
	}
	
	public static void addCrucible(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Default power, according to TE source, is 8000
			 * This is used for Pyrotheum, Cryotheum, Aerotheum, Petrotheum and Redstone.
			 * Redstone Block is 72000
			 * Glowstone and Ender Pearl are 20000
			 * Glowstone block is 80000
			 * Cobblestone/Stone/Obsidian is 320000
			 */
			int ENERGY = 8000;
			
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			FluidStack oreFluid = FluidRegistry.getFluidStack( mat.getName(), 288 );
			FluidStack baseFluid = FluidRegistry.getFluidStack( mat.getName(), 144 );
			
			if( mat.ore != null ) {
				ItemStack ore = new ItemStack( mat.ore, 1 );
				addCrucibleRecipe( ENERGY, ore, oreFluid );
			}
			
			addCrucibleRecipe( ENERGY, ingot, baseFluid );
			
			if( Options.enableBasics && mat.powder != null ) {
				ItemStack dust = new ItemStack( mat.powder, 1 );
				addCrucibleRecipe( ENERGY, dust, baseFluid );
			}
			
			addCrucibleExtra( Options.enablePlate, Item.getItemFromBlock(mat.plate), FluidRegistry.getFluidStack(mat.getName(), 144), ENERGY );
			addCrucibleExtra( Options.enableBasics, mat.nugget, FluidRegistry.getFluidStack(mat.getName(), 16), ENERGY );
		}
	}

	private static void addCrucibleExtra(boolean enabled, Item input, FluidStack output, int energy ) {
		if( enabled && input != null && output != null ) {
			ItemStack inItems = new ItemStack( input, 1 );
			addCrucibleRecipe( energy, inItems, output );
		}
	}

	public static void addPlatePress(boolean enabled, String materialName) {
		if( enabled && Options.enablePlate ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			
			/*
			 * Compactors default is 4000RF per operation
			 */
			int ENERGY = 4000;
			addCompactorPressRecipe( ENERGY, new ItemStack( mat.ingot, 1), new ItemStack( mat.plate, 1) );
		}
	}

	public static void addPressStorage(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			
			/*
			 * Compactors default is 4000RF per operation
			 */
			int ENERGY = 4000;
			ItemStack ingots = new ItemStack( mat.ingot, 9 );
			ItemStack nuggets = new ItemStack( mat.nugget, 9 );
			ItemStack block = new ItemStack( mat.block, 1 );
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			
			addCompactorStorageRecipe( ENERGY, ingots, block );
			addCompactorStorageRecipe( ENERGY, nuggets, ingot );
		}
	}
	
	/* SMELTER */
	public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput) {

		addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, null, 0);
	}

	public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput) {

		addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, 100);
	}

	public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {

		if (primaryInput == null || secondaryInput == null || primaryOutput == null) {
			return;
		}
		NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger("energy", energy);
		toSend.setTag("primaryInput", new NBTTagCompound());
		toSend.setTag("secondaryInput", new NBTTagCompound());
		toSend.setTag("primaryOutput", new NBTTagCompound());

		primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
		secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
		primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));

		if (secondaryOutput != null) {
			toSend.setTag("secondaryOutput", new NBTTagCompound());
			secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
			toSend.setInteger("secondaryChance", secondaryChance);
		}
		FMLInterModComms.sendMessage("thermalexpansion", "AddSmelterRecipe", toSend);
	}
}
