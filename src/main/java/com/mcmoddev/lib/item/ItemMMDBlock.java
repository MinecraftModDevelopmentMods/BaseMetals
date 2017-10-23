package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMMDBlock extends ItemBlock implements IMMDObject {
	private int burnTime = 0;
	private MMDMaterial mmdMaterial;
	
	public ItemMMDBlock(MMDMaterial material, Block block) {
		super(block);
		this.mmdMaterial = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}
	
	@Override
	public int getItemBurnTime(@Nonnull ItemStack itemStack) {
		return this.burnTime;
	}
}
