package com.mcmoddev.lib.util;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackHelper {

	public static final ItemStack EMPTY = new ItemStack((Item)null);

	public boolean isEmpty(@Nullable final ItemStack itemStack) {
		if (itemStack == null) {
			return true;
		}
		final Item item = getItemRaw(itemStack);
		return itemStack == EMPTY ? true : (item != null && item != Item.getItemFromBlock(Blocks.AIR) ? (itemStack.stackSize <= 0 ? true : itemStack.getItemDamage() < -32768 || itemStack.getItemDamage() > 65535) : true);
	}

	public int getCount(@Nullable final ItemStack itemStack) {
		return ((this.isEmpty(itemStack)) || (itemStack == null)) ? 0 : itemStack.stackSize;
	}

	public void setCount(@Nullable final ItemStack itemStack, final int size) {
		if (itemStack != null) {
			itemStack.stackSize = size;
		}
	}

	public void grow(@Nullable final ItemStack itemStack, final int quantity) {
		if (itemStack != null) {
			this.setCount(itemStack, itemStack.stackSize + quantity);
		}
	}

	public void shrink(@Nullable final ItemStack itemStack, final int quantity) {
		this.grow(itemStack, -quantity);
	}

	/**
	 * Internal call to get the actual item, not the delegate.
	 * In all other methods, FML replaces calls to this.item with the item delegate.
	 */
	private Item getItemRaw(@Nullable final ItemStack itemStack) {
		if (itemStack != null) {
			return itemStack.getItem();
			// TODO: Either AT or Reflect this instead
			// return itemStack.item;
		}
		return null;
	}
}
