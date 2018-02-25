package com.mcmoddev.lib.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EntityHelpers {
	private EntityHelpers() {
		// private constructor
	}
	
	public static NBTTagCompound writeToNBTItemStack(NBTTagCompound compound, ItemStack itemStack) {
		final NBTTagCompound itemStackCompound = new NBTTagCompound();
		itemStack.writeToNBT(itemStackCompound);
		compound.setTag("itemstack", itemStackCompound);
		return compound;
	}

	public static ItemStack readFromNBTItemStack(NBTTagCompound compound) {
		return new ItemStack(compound.getCompoundTag("itemstack"));
	}

}
