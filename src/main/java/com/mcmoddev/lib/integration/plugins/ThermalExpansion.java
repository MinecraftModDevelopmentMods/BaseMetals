package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

// Parts of this are based on code from older versions of CoFHLib
// Other parts are lifted from the "ThermalExpansionHelper" of the 1.7 versions
// of CoFHLib - they were efficient and did the integration in the simplest
// manner. Why they've been removed, I don't know.

public class ThermalExpansion implements IIntegration {

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
}
