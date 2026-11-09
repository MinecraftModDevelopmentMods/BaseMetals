package com.mcmoddev.lib.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EntityHelpers {

	private EntityHelpers() {
		// private constructor
	}

	/**
	 *
	 * @param compound
	 * @param itemStack
	 * @return
	 */
	public static NBTTagCompound writeToNBTItemStack(final NBTTagCompound compound,
			final ItemStack itemStack) {
		final NBTTagCompound itemStackCompound = new NBTTagCompound();
		itemStack.writeToNBT(itemStackCompound);
		compound.setTag("itemstack", itemStackCompound);
		return compound;
	}

	/**
	 *
	 * @param compound
	 * @return
	 */
	public static ItemStack readFromNBTItemStack(final NBTTagCompound compound) {
		return new ItemStack(compound.getCompoundTag("itemstack"));
	}

}
