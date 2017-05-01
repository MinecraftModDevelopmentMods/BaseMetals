package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;

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
		
		if (input.getItem() == null || output.getItem() == null ) {
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
		
		if (input.getItem() == null || output.getItem() == null ) {
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
		if (enabled) {
			MMDMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Ore -> Ingot default, according to TE source, is 2000
			 * dust -> Ingot default, according to same, is DEFAULT * 14 / 20 - at the 2000RF default, this is 1400
			 */
			final int ENERGY_ORE = 2000;
			final int ENERGY_DUST = 1400;
			ItemStack ore;
			if (mat.hasBlock(Names.ORE) && mat.getBlock(Names.ORE) != null) {
				ore = new ItemStack(mat.getBlock(Names.ORE), 1, 0);
			} else if( mat.hasItem(Names.BLEND) &&  mat.getItem(Names.BLEND) != null ) {
				ore = new ItemStack(mat.getItem(Names.BLEND), 1, 0);
			} else {
				return;
			}

			if ((!mat.hasItem(Names.INGOT)) || mat.getItem(Names.INGOT) == null) {
				return;
			}
			
			ItemStack ingot = new ItemStack( mat.getItem(Names.INGOT), 1, 0);
			ThermalExpansionHelper.addFurnaceRecipe(ENERGY_ORE, ore, ingot);
			if (Options.enableBasics) {
				if (mat.hasItem(Names.POWDER) && mat.getItem(Names.POWDER) != null) {
					ItemStack dust = new ItemStack(mat.getItem(Names.POWDER), 1, 0);
					ThermalExpansionHelper.addFurnaceRecipe(ENERGY_DUST, dust, ingot);
				}
			}
		}
	}

	public static void addCrucible(boolean enabled, String materialName) {
		if (enabled) {
			MMDMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Default power, according to TE source, is 8000
			 * This is used for Pyrotheum, Cryotheum, Aerotheum, Petrotheum and Redstone.
			 * Redstone Block is 72000
			 * Glowstone and Ender Pearl are 20000
			 * Glowstone block is 80000
			 * Cobblestone/Stone/Obsidian is 320000
			 */
			final int ENERGY = 8000;

			ItemStack ingot = new ItemStack(mat.getItem(Names.INGOT));
			FluidStack oreFluid = FluidRegistry.getFluidStack(mat.getName(), 288);
			FluidStack baseFluid = FluidRegistry.getFluidStack(mat.getName(), 144);

			if (mat.getBlock(Names.ORE) != null) {
				ItemStack ore = new ItemStack( mat.getBlock(Names.ORE) );
				ThermalExpansionHelper.addCrucibleRecipe(ENERGY, ore, oreFluid);
			}

			ThermalExpansionHelper.addCrucibleRecipe(ENERGY, ingot, baseFluid);

			if (Options.enableBasics && mat.getItem(Names.POWDER) != null) {
				ItemStack dust = new ItemStack( mat.getItem(Names.POWDER) );
				ThermalExpansionHelper.addCrucibleRecipe(ENERGY, dust, baseFluid);
			}

			addCrucibleExtra(Options.enablePlate, Item.getItemFromBlock(mat.getBlock(Names.PLATE)), FluidRegistry.getFluidStack(mat.getName(), 144), ENERGY);
			addCrucibleExtra(Options.enableBasics, mat.getItem(Names.NUGGET), FluidRegistry.getFluidStack(mat.getName(), 16), ENERGY);
		}
	}

	private static void addCrucibleExtra(boolean enabled, Item input, FluidStack output, int energy ) {
		if (enabled && input != null && output != null) {
			ItemStack inItems = new ItemStack( input );
			ThermalExpansionHelper.addCrucibleRecipe(energy, inItems, output);
		}
	}

	public static void addPlatePress(boolean enabled, String materialName) {
		if( enabled && Options.enablePlate ) {
			MMDMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());

			/*
			 * Compactors default is 4000RF per operation
			 */
			final int ENERGY = 4000;
			addCompactorPressRecipe(ENERGY, new ItemStack(mat.getItem(Names.INGOT)), new ItemStack(mat.getBlock(Names.PLATE)));
		}
	}

	public static void addPressStorage(boolean enabled, String materialName) {
		if( enabled ) {
			MMDMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			
			/*
			 * Compactors default is 4000RF per operation
			 */
			final int ENERGY = 4000;
			ItemStack ingots = new ItemStack(mat.getItem(Names.INGOT), 9);
			ItemStack nuggets = new ItemStack(mat.getItem(Names.NUGGET), 9);
			ItemStack block = new ItemStack(mat.getBlock(Names.BLOCK), 1);
			ItemStack ingot = new ItemStack(mat.getItem(Names.INGOT), 1);
			
			addCompactorStorageRecipe(ENERGY, ingots, block);
			addCompactorStorageRecipe(ENERGY, nuggets, ingot);
		}
	}
}
