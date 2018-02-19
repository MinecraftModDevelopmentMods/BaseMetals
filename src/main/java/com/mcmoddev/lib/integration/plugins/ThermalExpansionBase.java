package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

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
	private static final String INPUT = "input";
	private static final String OUTPUT = "output";
	private static final String ENERGY = "energy";

	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
	}

	protected static void addCompactorPressRecipe(@Nonnull final int energy, @Nonnull final ItemStack input, @Nonnull final ItemStack output) {
		if (input.getItem() == null || output.getItem() == null) {
			return;
		}

		final NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger(ENERGY, energy);
		toSend.setTag(INPUT, new NBTTagCompound());
		toSend.setTag(OUTPUT, new NBTTagCompound());
		input.writeToNBT(toSend.getCompoundTag(INPUT));
		output.writeToNBT(toSend.getCompoundTag(OUTPUT));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "addcompactorpressrecipe", toSend);
	}

	protected static void addCompactorStorageRecipe(@Nonnull final int energy, @Nonnull final ItemStack input, @Nonnull final ItemStack output) {

		if (input.getItem() == null || output.getItem() == null) {
			return;
		}

		final NBTTagCompound toSend = new NBTTagCompound();

		toSend.setInteger(ENERGY, energy);
		toSend.setTag(INPUT, new NBTTagCompound());
		toSend.setTag(OUTPUT, new NBTTagCompound());
		input.writeToNBT(toSend.getCompoundTag(INPUT));
		output.writeToNBT(toSend.getCompoundTag(OUTPUT));
		FMLInterModComms.sendMessage(PLUGIN_MODID, "addcompactorstoragerecipe", toSend);
	}

	protected static void addFurnace(@Nonnull final String materialName) {
		addFurnace(Materials.getMaterialByName(materialName));
	}

	protected static void addFurnace(@Nonnull final MMDMaterial material) {
		/*
		 * Ore -> Ingot default, according to TE source, is 2000
		 * dust -> Ingot default, according to same, is DEFAULT * 14 / 20 - at the 2000RF default, this is 1400
		 */
		final int ENERGY_ORE = 2000;
		final int ENERGY_DUST = 1400;
		ItemStack ore;
		if (material.hasBlock(Names.ORE) && material.getBlock(Names.ORE) != null) {
			ore = material.getBlockItemStack(Names.ORE, 1);
		} else if (material.hasItem(Names.BLEND) && material.getItem(Names.BLEND) != null) {
			ore = material.getItemStack(Names.BLEND, 1);
		} else {
			return;
		}

		if ((!material.hasItem(Names.INGOT)) || material.getItem(Names.INGOT) == null) {
			return;
		}

		if (material.hasItem(Names.INGOT)) {
			final ItemStack ingot = material.getItemStack(Names.INGOT, 1);
			ThermalExpansionHelper.addFurnaceRecipe(ENERGY_ORE, ore, ingot);
			if (material.hasItem(Names.POWDER) && material.getItem(Names.POWDER) != null) {
				final ItemStack dust = material.getItemStack(Names.POWDER, 1);
				ThermalExpansionHelper.addFurnaceRecipe(ENERGY_DUST, dust, ingot);
			}
		}
	}

	protected static void addCrucible(@Nonnull final String materialName) {
		addCrucible(Materials.getMaterialByName(materialName));
	}

	protected static void addCrucible(@Nonnull final MMDMaterial material) {
		/*
		 * Default power, according to TE source, is 8000
		 * This is used for Pyrotheum, Cryotheum, Aerotheum, Petrotheum and Redstone.
		 * Redstone Block is 72000
		 * Glowstone and Ender Pearl are 20000
		 * Glowstone block is 80000
		 * Cobblestone/Stone/Obsidian is 320000
		 */
		final int ENERGY_QTY = 8000;

		final String materialName = material.getName();
		final ItemStack ingot = material.getItemStack(Names.INGOT);
		final ItemStack dust = material.getItemStack(Names.POWDER);
		final FluidStack oreFluid = FluidRegistry.getFluidStack(materialName, 288);
		final FluidStack baseFluid = FluidRegistry.getFluidStack(materialName, 144);
		final FluidStack nuggetFluid = FluidRegistry.getFluidStack(materialName, 16);

		if ((material.hasBlock(Names.ORE)) && (material.getBlock(Names.ORE) != null)) {
			final ItemStack ore = material.getBlockItemStack(Names.ORE);
			ThermalExpansionHelper.addCrucibleRecipe(ENERGY_QTY, ore, oreFluid);
		}

		if (material.hasItem(Names.INGOT)) {
			ThermalExpansionHelper.addCrucibleRecipe(ENERGY_QTY, ingot, baseFluid);
		}

		if (material.hasItem(Names.POWDER)) {
			ThermalExpansionHelper.addCrucibleRecipe(ENERGY_QTY, dust, baseFluid);
		}

		// TODO: Can we getBlockItemStack instead?
		if (material.hasBlock(Names.PLATE)) {
			ThermalExpansionHelper.addCrucibleRecipe(ENERGY_QTY, new ItemStack(Item.getItemFromBlock(material.getBlock(Names.PLATE))), baseFluid);
		}

		if (material.hasItem(Names.NUGGET)) {
			ThermalExpansionHelper.addCrucibleRecipe(ENERGY_QTY, material.getItemStack(Names.NUGGET), nuggetFluid);
		}
	}

	protected static void addPlatePress(@Nonnull final String materialName) {
		addPlatePress(Materials.getMaterialByName(materialName));
	}

	protected static void addPlatePress(@Nonnull final MMDMaterial material) {
		if (material.hasItem(Names.PLATE) && material.hasItem(Names.INGOT)) {

			/*
			 * Compactors default is 4000RF per operation
			 */
			final int ENERGY_QTY = 4000;
			addCompactorPressRecipe(ENERGY_QTY, new ItemStack(material.getItem(Names.INGOT)), new ItemStack(material.getBlock(Names.PLATE)));
		}
	}

	protected static void addPressStorage(@Nonnull final String materialName) {
		addPressStorage(Materials.getMaterialByName(materialName));
	}

	protected static void addPressStorage(@Nonnull final MMDMaterial material) {
		/*
		 * Compactors default is 4000RF per operation
		 */
		final int ENERGY_QTY = 4000;
		final ItemStack ingotStack = material.getItemStack(Names.INGOT);
		final ItemStack ingotsStack = new ItemStack(ingotStack.getItem(), 9);
		final ItemStack nuggetsStack = material.getItemStack(Names.NUGGET, 9);
		final ItemStack blockStack = material.getBlockItemStack(Names.BLOCK, 1);

		addCompactorStorageRecipe(ENERGY_QTY, ingotsStack, blockStack);
		addCompactorStorageRecipe(ENERGY_QTY, nuggetsStack, ingotStack);
	}
}
