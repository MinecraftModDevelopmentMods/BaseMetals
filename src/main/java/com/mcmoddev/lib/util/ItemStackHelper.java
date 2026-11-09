package com.mcmoddev.lib.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {

	public static final ItemStack EMPTY = ItemStack.EMPTY;

	public boolean isEmpty(@Nullable final ItemStack itemStack) {
		return itemStack == null || itemStack.isEmpty();
	}

	public int getCount(@Nullable final ItemStack itemStack) {
		return ((this.isEmpty(itemStack)) || (itemStack == null)) ? 0 : itemStack.getCount();
	}

	public void setCount(@Nullable final ItemStack itemStack, final int size) {
		if (itemStack != null) {
			itemStack.setCount(size);
		}
	}

	public void grow(@Nullable final ItemStack itemStack, final int quantity) {
		if (itemStack != null) {
			this.setCount(itemStack, itemStack.getCount() + quantity);
		}
	}

	public void shrink(@Nullable final ItemStack itemStack, final int quantity) {
		this.grow(itemStack, -quantity);
	}

}
