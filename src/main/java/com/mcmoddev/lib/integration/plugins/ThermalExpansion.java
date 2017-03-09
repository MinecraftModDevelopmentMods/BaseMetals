package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

// Parts of this are based on code from older versions of CoFHLib

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
		FMLInterModComms.sendMessage("thermalexpansion", "FurnaceRecipe", msg);
	}
}
