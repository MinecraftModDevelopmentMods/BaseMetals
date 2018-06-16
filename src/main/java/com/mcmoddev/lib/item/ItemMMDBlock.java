package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.material.IMMDBurnableObject;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMMDBlock extends ItemBlock implements IMMDObject, IMMDBurnableObject {

	private int burnTime = 0;
	private final MMDMaterial mmdMaterial;

	public ItemMMDBlock(final MMDMaterial material, final Block block) {
		super(block);
		this.mmdMaterial = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}

	@Override
	public void setBurnTime(final int burnTime) {
		this.burnTime = burnTime;
	}

	@Override
	public int getItemBurnTime(@Nonnull final ItemStack itemStack) {
		return this.burnTime;
	}
}
